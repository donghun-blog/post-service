package donghun.me.postservice.fixture;

import donghun.me.postservice.application.dto.CreatePostCommand;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

public class CreatePostCommandFixture {
    public static String getFirstImage() {
        return "ad051fad-f6f0-4f0e-86e2-fec065e76e40.gif";
    }
    public static CreatePostCommand.CreatePostCommandBuilder complete() {

        final String originalFileName = "test.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("profile",
                originalFileName,
                MULTIPART_FORM_DATA_VALUE,
                "test.png".getBytes());

        return CreatePostCommand.builder()
                .title("제목입니다.")
                .contents("### 헤더\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.gif)\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.png)\n" +
                        "하단입니다.!")
                .visible(true)
                .summary("요약입니다!")
                .tags(List.of(
                        "Spring Boot",
                        "ELK",
                        "Spring Security",
                        "Spring Data Jpa",
                        "JUnit5"
                ))
                .thumbnail(mockMultipartFile)
                ;
    }
}
