package donghun.me.postservice.domain.model;

import donghun.me.postservice.domain.exception.PostErrorCode;
import donghun.me.postservice.domain.exception.PostException;
import lombok.Builder;
import lombok.Getter;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

@Getter
public class Tag {
    private final Long id;
    private final String name;

    @Builder
    private Tag(Long id, String name) {

        if (isNull(id)) {
            throw new PostException(PostErrorCode.TAG_NOT_VALID);
        }

        if (!hasText(name)) {
            throw new PostException(PostErrorCode.TAG_NOT_VALID);
        }

        this.id = id;
        this.name = name;
    }

    public static class Factory {
        public static Tag create(Long id, String name) {
            return Tag.builder()
                      .id(id)
                      .name(name)
                      .build();
        }
    }
}
