package donghun.me.postservice.domain.model;

import donghun.me.postservice.domain.exception.PostErrorCode;
import donghun.me.postservice.domain.exception.PostException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TagCollection {
    public static final Integer MAX_TAG_SIZE = 5;
    private final List<Tag> tags = new ArrayList<>();

    public void add(Tag tag) {
        tags.stream()
            .filter(t -> t.getName()
                          .equals(tag.getName()) || t.getId()
                                                     .equals(tag.getId()))
            .findFirst()
            .ifPresent(t -> {
                throw new PostException(PostErrorCode.POST_TAG_DUPLICATE);
            });

        if (tags.size() >= MAX_TAG_SIZE) {
            throw new PostException(PostErrorCode.TAG_MAXIMUM_OVER);
        }

        this.tags.add(tag);
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    public Integer size() {
        return tags.size();
    }

    public void clear() {
        tags.clear();
    }
}
