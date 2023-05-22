package donghun.me.postservice.fixture;

import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.domain.model.Tag;

import java.util.List;

public class PostFixture {
    public static Post.PostBuilder complete() {
        return Post.builder()
                .id(1L)
                .title("title")
                   .contents("### 헤더\n" +
                           "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.svg)\n" +
                           "본문입니다1.\n" +
                           "### 본문입니다2\n" +
                           "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.tiff)\n" +
                           "하단입니다.!")
                   .visible(true)
                   .thumbnail("test.png")
                   .summary("요약입니다.")
                .tags(
                        List.of(
                                Tag.builder().id(1L).name("Spring Boot").build(),
                                Tag.builder().id(2L).name("Spring Security").build(),
                                Tag.builder().id(3L).name("OAuth").build(),
                                Tag.builder().id(4L).name("MSA").build(),
                                Tag.builder().id(5L).name("Spring Framework").build()
                        )
                )
                ;
    }
}
