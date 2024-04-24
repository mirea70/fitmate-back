package com.fitmate.usecase.chat;

import com.fitmate.domain.account.aggregate.Account;
import com.fitmate.domain.account.vo.AccountId;
import com.fitmate.domain.chat.aggregate.ChatMessage;
import com.fitmate.domain.chat.aggregate.ChatRoom;
import com.fitmate.domain.error.exceptions.DuplicatedException;
import com.fitmate.domain.error.results.DuplicatedErrorResult;
import com.fitmate.domain.mate.aggregate.Mate;
import com.fitmate.domain.mate.vo.MateId;
import com.fitmate.port.in.chat.dto.ChatMessageCommand;
import com.fitmate.port.in.chat.dto.ChatRoomCreateDmCommand;
import com.fitmate.port.in.chat.dto.ChatRoomCreateGroupCommand;
import com.fitmate.port.in.chat.usecase.ChatUseCasePort;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.chat.LoadChatPort;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import com.fitmate.port.out.chat.dto.ChatRoomListItemResponse;
import com.fitmate.port.out.chat.dto.ChatRoomSimpleResponse;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.usecase.UseCase;
import com.fitmate.usecase.chat.mapper.ChatUseCaseMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@UseCase
@RequiredArgsConstructor
public class ChatUseCase implements ChatUseCasePort {

    private final LoadChatPort loadChatPort;
    private final LoadMatePort loadMatePort;
    private final LoadAccountPort loadAccountPort;
    private final ChatUseCaseMapper chatUseCaseMapper;
    private static final String DEFAULT_ENTER_MESSAGE = "님이 채팅방에 참여하였습니다.";

    @Override
    public ChatMessageResponse enterChatRoom(ChatMessageCommand chatMessageCommand) {
        String roomId = chatMessageCommand.getRoomId();
        Long senderId = chatMessageCommand.getSenderId();

        ChatRoom chatRoom = loadChatPort.loadChatRoom(roomId);
        chatRoom.addJoinAccountId(senderId);
        loadChatPort.saveChatRoom(chatRoom);

        return chatUseCaseMapper.commandToResponse(chatMessageCommand, DEFAULT_ENTER_MESSAGE);
    }

    @Override
    public void saveChatMessage(ChatMessageCommand chatMessageCommand) {
        ChatMessage chatMessage = chatUseCaseMapper.commandToDomain(chatMessageCommand);
        loadChatPort.saveChatMessage(chatMessage);
    }

    @Override
    public ChatRoomSimpleResponse createGroupChatRoom(ChatRoomCreateGroupCommand command) {
        Mate mate = loadMatePort.loadMateEntity(new MateId(command.getMateId()));
        checkDuplicateChatRoom(mate.getId());
        ChatRoom chatRoom = chatUseCaseMapper.commandToDomain(mate.getTitle(), mate.getId().getValue());
        chatRoom.addJoinAccountId(command.getAccountId());
        String roomId = loadChatPort.saveChatRoom(chatRoom);

        return new ChatRoomSimpleResponse(roomId);
    }

    private void checkDuplicateChatRoom(MateId id) {
        if(loadChatPort.existChatRoom(id))
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_CHAT_ROOM_ABOUT_MATE);
    }

    @Override
    public ChatRoomSimpleResponse createDmChatRoom(ChatRoomCreateDmCommand command) {
        Long fromId = command.getFromAccountId();
        Long toId = command.getToAccountId();
        Account fromAccount = loadAccountPort.loadAccountEntity(new AccountId(fromId));
        Account toAccount = loadAccountPort.loadAccountEntity(new AccountId(toId));
        checkDuplicateChatRoom(fromId, toId);

        String fromNickName = fromAccount.getProfileInfo().getNickName();
        ChatRoom chatRoom = ChatRoom.createByName(fromNickName);
        chatRoom.addJoinAccountId(fromId);
        chatRoom.addJoinAccountId(toId);
        String roomId = loadChatPort.saveChatRoom(chatRoom);

        return new ChatRoomSimpleResponse(roomId);
    }

    private void checkDuplicateChatRoom(Long fromAccountId, Long toAccountId) {
        if(loadChatPort.existChatRoom(Set.of(fromAccountId, toAccountId)))
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_CHAT_ROOM_BETWEEN_ACCOUNT);
    }

    @Override
    public List<ChatMessageResponse> getMessages(String roomId) {
        return loadChatPort.getMessagesByRoomId(roomId);
    }

    @Override
    public List<ChatRoomListItemResponse> getMyChatRooms(Long accountId) {
        return loadChatPort.getMyChatRooms(accountId);
    }
}
