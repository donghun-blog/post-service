package donghun.me.postservice.fixture;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.PostTagEntity;

import java.util.List;

public class PostEntityFixture {
    public static PostEntity.PostEntityBuilder complete() {
        return PostEntity.builder()
                .id(1L)
                .title("제목입니다.")
                .contents("### 헤더\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.svg)\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.tiff)\n" +
                        "하단입니다.!")
                .visible(true)
                .thumbnail("test.png")
                .summary("요약입니다.")
                ;
    }
}
