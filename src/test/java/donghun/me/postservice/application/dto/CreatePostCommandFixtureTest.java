package donghun.me.postservice.application.dto;

import donghun.me.postservice.domain.dto.CreatePostDomainModelDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

class CreatePostCommandFixtureTest {

    @DisplayName("썸네일 이미지가 없는 경우 null을 반환한다.")
    @Test
    void toDomainModelDtoEmptyThumbnail() {
        // given
        CreatePostCommand command = new CreatePostCommand(
                "title",
                "contents",
                List.of(
                        "태그1",
                        "태그2"
                ),
                true,
                null,
                "summary"
        );
    
        // when
        CreatePostDomainModelDto domainModelDto = command.toDomainModelDto(null);

        // then
        assertThat(domainModelDto.thumbnail())
                  .isNull();
    }

    @DisplayName("썸네일 이미지가 있는 경우 filename을 반환한다.")
    @Test
    void toDomainModelDto() {
        // given
        final String originalFileName = "test.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("profile",
                originalFileName,
                MULTIPART_FORM_DATA_VALUE,
                "test.png".getBytes());

        CreatePostCommand command = new CreatePostCommand(
                "title",
                "contents",
                List.of(
                        "태그1",
                        "태그2"
                ),
                true,
                mockMultipartFile,
                "summary"
        );

        // when
        CreatePostDomainModelDto domainModelDto = command.toDomainModelDto(null);

        // then
        assertThat(domainModelDto.thumbnail())
                .isNotNull()
                .isEqualTo(originalFileName);
    }
}