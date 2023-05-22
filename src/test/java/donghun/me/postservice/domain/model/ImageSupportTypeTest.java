package donghun.me.postservice.domain.model;

import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ImageSupportTypeTest extends AbstractDefaultTest {

    @DisplayName("지원하는 이미지 확장자인 경우 true를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "test.png",
            "test.jpg",
            "test.jpeg",
            "test.gif"
    })
    void isSupportTrue(String path) {
        // given

        // when
        boolean result = ImageSupportType.isSupport(path);

        // then
        assertTrue(result);
    }

    @DisplayName("지원하지 않는 이미지 확장자인 경우 false를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "test.svg",
            "test.bmp",
            "test.tiff"
    })
    void isSupportFalse(String path) {
        // given

        // when
        boolean result = ImageSupportType.isSupport(path);

        // then
        assertFalse(result);
    }

}