package com.fitmate.usecase.mate;

import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.error.results.NotMatchErrorResult;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
import com.fitmate.port.in.mate.usecase.MateApplyUseCasePort;
import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.chat.enums.MessageType;
import com.fitmate.domain.chat.message.ChatMessage;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.chat.LoadChatPort;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import com.fitmate.usecase.UseCase;
import com.fitmate.usecase.mate.event.MateApproveEvent;
import com.fitmate.usecase.mate.event.dto.MateApproveEventDto;
import com.fitmate.usecase.mate.event.MateRequestEvent;
import com.fitmate.usecase.mate.event.dto.MateRequestEventDto;
import com.fitmate.usecase.mate.mapper.MateUseCaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
@Transactional
public class MateApplyUseCase implements MateApplyUseCasePort {

    private final LoadMatePort loadMatePort;
    private final LoadMateRequestPort loadMateRequestPort;
    private final LoadAccountPort loadAccountPort;
    private final LoadChatPort loadChatPort;
    private final MateUseCaseMapper mateUseCaseMapper;
    private final ApplicationEventPublisher eventPublisher;
    private static final String DEFAULT_ENTER_MESSAGE = "님이 채팅방에 참여하였습니다.";

    @Override
    @Transactional(readOnly = true)
    public MateQuestionResponse readQuestion(Long mateId) {
        return loadMateRequestPort.loadMateQuestion(mateId);
    }

    @Override
    public void applyMate(MateApplyCommand mateApplyCommand) {
        Loaded<Mate> loadedMate = loadMatePort.loadMate(new MateId(mateApplyCommand.getMateId()));
        Long applierId = mateApplyCommand.getApplierId();
        if(applierId.equals(loadedMate.get().getWriterId()))
            throw new NotMatchException(NotMatchErrorResult.CANNOT_APPLY_WRITER);

        ApproveStatus approveStatus = saveNewMateRequest(mateApplyCommand, loadedMate.get().getGatherType());
        loadedMate.update(mate -> {
            if(approveStatus == ApproveStatus.APPROVE)
                mate.addApprovedAccountId(applierId);
            else
                mate.addWaitingAccountId(applierId);
        });

        if(approveStatus == ApproveStatus.APPROVE) {
            addToChatRoom(mateApplyCommand.getMateId(), applierId);
        }

        publishMateRequestEvent(loadedMate.get(), applierId);
    }

    private ApproveStatus saveNewMateRequest(MateApplyCommand mateApplyCommand, GatherType gatherType) {
        loadMateRequestPort.isDuplicateMateRequest(mateApplyCommand.getMateId(), mateApplyCommand.getApplierId());
        ApproveStatus approveStatus;
        if(gatherType == GatherType.FAST)
            approveStatus = ApproveStatus.APPROVE;
        else
            approveStatus = ApproveStatus.WAIT;
        MateApply mateApply = mateUseCaseMapper.commandToDomain(mateApplyCommand, approveStatus);
        loadMateRequestPort.saveMateRequestEntity(mateApply);

        return approveStatus;
    }

    private void publishMateRequestEvent(Mate mate, Long applierId) {
        MateRequestEventDto eventDto = new MateRequestEventDto(
                mate.getTitle(),
                mate.getId().getValue(),
                applierId
        );
        MateRequestEvent event = new MateRequestEvent(eventDto);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void approveMate(MateApproveCommand mateApproveCommand) {
        Long mateId = mateApproveCommand.getMateId();
        Long applierId = mateApproveCommand.getApplierId();

        Loaded<Mate> loadedMate = loadMatePort.loadMate(new MateId(mateId));
        if(!mateApproveCommand.getAccountId().equals(loadedMate.get().getWriterId()))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_WRITER_ID);

        Loaded<MateApply> loadedMateApply = loadMateRequestPort.loadMateApply(mateId, applierId);
        loadedMateApply.update(MateApply::changeToApprove);

        loadedMate.update(mate -> mate.approve(applierId));

        addToChatRoom(mateId, applierId);

        publishMateApproveEvent(loadedMate.get(), applierId);
    }

    @Override
    public void cancelMateApply(Long mateId, Long applierId, String cancelReason) {
        Loaded<Mate> loadedMate = loadMatePort.loadMate(new MateId(mateId));
        loadedMate.update(mate -> mate.cancelApply(applierId));

        Loaded<MateApply> loadedMateApply = loadMateRequestPort.loadMateApply(mateId, applierId);
        loadedMateApply.update(mateApply -> mateApply.cancel(cancelReason, LocalDateTime.now()));
    }

    private void addToChatRoom(Long mateId, Long accountId) {
        ChatRoom chatRoom = loadChatPort.loadChatRoomByMateId(mateId);
        chatRoom.addJoinAccountId(accountId);
        String roomId = loadChatPort.saveChatRoom(chatRoom);

        Account account = loadAccountPort.loadAccountEntity(new AccountId(accountId));
        String nickName = account.getProfileInfo().getNickName();
        String enterMessage = nickName + DEFAULT_ENTER_MESSAGE;
        ChatMessage chatMessage = ChatMessage.withoutId(roomId, enterMessage, accountId, nickName, MessageType.ENTER, null);
        loadChatPort.saveChatMessage(chatMessage);
    }

    private void publishMateApproveEvent(Mate mate, Long applierId) {
        MateApproveEventDto eventDto = new MateApproveEventDto(
                mate.getTitle(),
                mate.getId().getValue(),
                applierId
        );
        MateApproveEvent event = new MateApproveEvent(eventDto);
        eventPublisher.publishEvent(event);
    }
}
