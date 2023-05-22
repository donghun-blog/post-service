package donghun.me.postservice.domain.service;

import donghun.me.postservice.common.EmptyParameters;
import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ContentsAnalyzeDomainServiceTest extends AbstractDefaultTest {

    @DisplayName("게시글 본문 분석 시 본문이 없는 경우 null을 반환한다.")
    @EmptyParameters
    void getThumbnailEmptyContents(String emptyContents) {
        // given

        // when
        String firstImage = ContentsAnalyzeDomainService.getThumbnail(emptyContents);

        // then
        assertThat(firstImage).isNull();
    }

    @DisplayName("게시글 본문 분석 후 첫번째 이미지를 추출하여 반환한다.")
    @Test
    void getThumbnail() {
        // given

        String firstImage = "![alt text](https://winter-blog-bucket.s3.ap-northeast-2.amazonaws.com/post/bec85c6d-3185-462c-b81d-ace7d795b7dd.png)";
        String relativePath = "bec85c6d-3185-462c-b81d-ace7d795b7dd.png";
        String contents = "### 본문입니다.\n" +
                firstImage + "'\n" +
                "abcdefg\n" +
                "### 제목입니다.\n" +
                "![alt text](이미지2.jpeg)\n" +
                "---\n" +
                "가나다라마바사";

        // when
        String result = ContentsAnalyzeDomainService.getThumbnail(contents);

        // then
        assertThat(result).isNotBlank()
                          .isEqualTo(relativePath);
    }

}