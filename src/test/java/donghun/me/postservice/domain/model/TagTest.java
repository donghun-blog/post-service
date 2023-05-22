package donghun.me.postservice.domain.model;

import donghun.me.postservice.common.EmptyParameters;
import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import donghun.me.postservice.domain.exception.PostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static donghun.me.postservice.domain.exception.PostErrorCode.TAG_NOT_VALID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TagTest extends AbstractDefaultTest {

    @DisplayName("태그 생성 시 id가 null인 경우 예외를 반환한다.")
    @Test
    void createTagNullIdException() {
        // given

        // when & then
        assertThatThrownBy(() -> Tag.Factory.create(null, "태그"))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", TAG_NOT_VALID);
    }

    @DisplayName("태그 생성 시 태그명이 empty인 경우 예외를 반환한다.")
    @EmptyParameters
    void createTagNameEmptyException(String emptyName) {
        // given

        // when & then
        assertThatThrownBy(() -> Tag.Factory.create(1L, emptyName))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", TAG_NOT_VALID);
    }

}