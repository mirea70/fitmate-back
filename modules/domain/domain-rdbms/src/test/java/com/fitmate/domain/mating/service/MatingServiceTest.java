package com.fitmate.domain.mating.service;

import com.fitmate.domain.mating.domain.entity.Mating;
import com.fitmate.domain.mating.helper.MatingDomainTestHelper;
import com.fitmate.domain.mating.domain.repository.MatingRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MatingServiceTest {
    @InjectMocks
    private MatingService target;
    @Mock
    private MatingRepository matingRepository;

    private final MatingDomainTestHelper matingDomainTestHelper = new MatingDomainTestHelper();

    @Test
    public void 메이팅조회ID로_실패_존재X () throws Exception {
        // given
        Long matingId = 1L;
        doReturn(Optional.empty()).when(matingRepository).findById(anyLong());
        // when
        final NotFoundException result = assertThrows(NotFoundException.class,
                () -> target.validateFindById(matingId));
        // then
        assertThat(result.getErrorResult()).isEqualTo(NotFoundErrorResult.NOT_FOUND_MATING_DATA);
    }

    @Test
    public void 메이팅조회ID로_성공 () throws Exception {
        // given
        Mating mating = matingDomainTestHelper.getTestMating();
        Long matingId = 1L;
        doReturn(Optional.of(mating)).when(matingRepository).findById(anyLong());
        // when
        final Mating result = target.validateFindById(matingId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(mating.getTitle());
        assertThat(result.getMateAt()).isEqualTo(mating.getMateAt());
    }
}
