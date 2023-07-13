package com.fitmate.app.mating.service;

import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.service.MatingRegisterService;
import com.fitmate.app.mating.helper.MatingAppTestHelper;
import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.domain.mating.domain.entity.Mating;
import com.fitmate.domain.mating.domain.repository.MatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Import({MatingAppTestHelper.class, FileTestHelper.class})
public class MatingRegisterServiceTest {
    @InjectMocks
    private MatingRegisterService target;
    @Mock
    private MatingRepository matingRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private FileService fileService;

    private final MatingAppTestHelper matingAppTestHelper = new MatingAppTestHelper();
    private final AccountAppTestHelper accountAppTestHelper = new AccountAppTestHelper();

    @Test
    public void 메이트찾기글_등록 () throws Exception {
        // given
        MatingDto.Create request = matingAppTestHelper.getTestRequest();
        Mating mating = matingAppTestHelper.getTestMating();
        AccountDataDto.Response accountData = accountAppTestHelper.getTestAccountData();

        doReturn(mating).when(matingRepository).save(any(Mating.class));
        when(accountService.validateFindById(anyLong())).thenReturn(accountData);
        // when
        final MatingDto.Response result = target.register(request);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(request.getTitle());
        assertThat(result.getFitPlace()).isEqualTo(request.getFitPlace());
    }
}
