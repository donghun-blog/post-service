package donghun.me.postservice.fixture;

import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

public class MockMultipartFileFixture {
    public static MockMultipartFile complete() {
        return new MockMultipartFile(
                "thumbnail",
                "test.png",
                MULTIPART_FORM_DATA_VALUE,
                "test.png".getBytes());
    }
}
