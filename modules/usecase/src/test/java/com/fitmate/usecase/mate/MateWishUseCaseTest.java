package com.fitmate.usecase.mate;

import com.fitmate.domain.mate.wish.MateWish;
import com.fitmate.port.out.mate.LoadMateWishPort;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("MateWishUseCase 단위 테스트")
class MateWishUseCaseTest {

    @InjectMocks
    private MateWishUseCase mateWishUseCase;

    @Mock
    private LoadMateWishPort loadMateWishPort;

    @Test
    @DisplayName("찜이 없으면 저장하고 true 반환")
    void toggleWish_add() {
        given(loadMateWishPort.existsWish(1L, 10L)).willReturn(false);

        boolean result = mateWishUseCase.toggleWish(1L, 10L);

        assertThat(result).isTrue();
        then(loadMateWishPort).should().saveWish(any(MateWish.class));
        then(loadMateWishPort).should(never()).deleteWish(1L, 10L);
    }

    @Test
    @DisplayName("찜이 있으면 삭제하고 false 반환")
    void toggleWish_remove() {
        given(loadMateWishPort.existsWish(1L, 10L)).willReturn(true);

        boolean result = mateWishUseCase.toggleWish(1L, 10L);

        assertThat(result).isFalse();
        then(loadMateWishPort).should().deleteWish(1L, 10L);
        then(loadMateWishPort).should(never()).saveWish(any());
    }

    @Test
    @DisplayName("내 찜 목록 조회 위임")
    void getMyWishMates() {
        given(loadMateWishPort.getWishedMates(1L)).willReturn(List.of());

        List<MateSimpleResponse> result = mateWishUseCase.getMyWishMates(1L);

        assertThat(result).isEmpty();
        then(loadMateWishPort).should().getWishedMates(1L);
    }
}
