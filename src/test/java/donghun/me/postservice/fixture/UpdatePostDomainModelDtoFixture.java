package donghun.me.postservice.fixture;

import donghun.me.postservice.domain.dto.UpdatePostDomainModelDto;
import donghun.me.postservice.domain.model.Tag;

import java.util.List;

public class UpdatePostDomainModelDtoFixture {

    public static UpdatePostDomainModelDto.UpdatePostDomainModelDtoBuilder complete() {
        return UpdatePostDomainModelDto.builder()
                .title("title")
                .contents("### 헤더\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.png)\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.gif)\n" +
                        "하단입니다.!")
                .thumbnail("thumbnail.png")
                .visible(true)
                .summary("summary")
                .tags(
                        List.of(
                                Tag.builder().id(1L).name("태그1").build(),
                                Tag.builder().id(2L).name("태그2").build(),
                                Tag.builder().id(3L).name("태그3").build()
                        )
                );
    }
}
