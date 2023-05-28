package donghun.me.postservice.application.service;

import donghun.me.postservice.application.port.output.UploadImagePort;
import donghun.me.postservice.common.environment.AbstractServiceMysqlTestContainer;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.fixture.MockMultipartFileFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import static donghun.me.postservice.domain.exception.PostErrorCode.IMAGE_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

class ImageUploadServiceTest extends AbstractServiceMysqlTestContainer {

    @MockBean
    private UploadImagePort uploadImagePort;

    @Autowired
    private ImageUploadService imageUploadService;

    @DisplayName("이미지 업로드시 이미지가 없는 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void uploadImageEmptyException(MockMultipartFile empty) {
        // given

        // when & then
        assertThatThrownBy(() -> imageUploadService.upload(empty))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", IMAGE_EMPTY);
    }

    @DisplayName("이미지 업로드 후 경로를 반환한다.")
    @Test
    void upload() {
        // given
        MockMultipartFile mockMultipartFile = MockMultipartFileFixture.complete();
        final String path = "test/avatar";
        willDoNothing().given(uploadImagePort)
                       .upload(anyString(), any());
        given(s3Properties.getAbsolutePath())
                .willReturn(path);

        // when
        String result = imageUploadService.upload(mockMultipartFile);

        // then
        assertThat(result)
                .isNotBlank();
    }
}