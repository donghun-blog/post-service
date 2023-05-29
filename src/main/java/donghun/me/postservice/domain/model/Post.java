package donghun.me.postservice.domain.model;

import donghun.me.postservice.domain.dto.CreatePostDomainModelDto;
import donghun.me.postservice.domain.dto.UpdatePostDomainModelDto;
import donghun.me.postservice.domain.exception.PostErrorCode;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.domain.service.ContentsAnalyzeDomainService;
import donghun.me.postservice.domain.service.ImagePathGenerateDomainService;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static donghun.me.postservice.domain.exception.PostErrorCode.IMAGE_EXTENSION_NOT_SUPPORT;
import static org.springframework.util.StringUtils.hasText;

@Getter
public class Post extends AbstractBaseDomainModel {
    private final Long id;
    private String title;
    private String contents;
    private TagCollection tags = new TagCollection();
    private boolean visible;
    private String thumbnail;
    private String summary;


    @Builder
    private Post(Long id, String title, String contents, List<Tag> tags, boolean visible, String thumbnail, String summary,
                 LocalDateTime createdAt, LocalDateTime modifiedAt) {
        super(createdAt, modifiedAt);
        if (!hasText(title)) {
            throw new PostException(PostErrorCode.POST_TITLE_EMPTY);
        }

        if (!hasText(contents)) {
            throw new PostException(PostErrorCode.POST_CONTENTS_EMPTY);
        }

        if (hasText(thumbnail) && !ImageSupportType.isSupport(thumbnail)) {
            throw new PostException(IMAGE_EXTENSION_NOT_SUPPORT);
        }

        this.id = id;
        this.title = title;
        this.contents = contents;
        this.thumbnail = hasText(thumbnail) ? thumbnail : null;
        this.visible = visible;
        this.summary = hasText(summary) ? summary : null;

        for (Tag tag : tags) {
            this.tags.add(tag);
        }
    }

    public boolean isThumbnailEmpty() {
        return !hasText(thumbnail);
    }

    public List<Tag> getTags() {
        return tags.getTags();
    }

    public void update(UpdatePostDomainModelDto updateDomainModelDto) {

        if (!hasText(updateDomainModelDto.title())) {
            throw new PostException(PostErrorCode.POST_TITLE_EMPTY);
        }

        if (!hasText(updateDomainModelDto.contents())) {
            throw new PostException(PostErrorCode.POST_CONTENTS_EMPTY);
        }

        String thumbnail = null;
        if (hasText(updateDomainModelDto.thumbnail())) {
            thumbnail = ImagePathGenerateDomainService.generate(updateDomainModelDto.thumbnail());
        } else {
            thumbnail = ContentsAnalyzeDomainService.getThumbnail(updateDomainModelDto.contents());
        }


        if (hasText(thumbnail) && !ImageSupportType.isSupport(thumbnail)) {
            throw new PostException(IMAGE_EXTENSION_NOT_SUPPORT);
        }

        this.title = updateDomainModelDto.title();
        this.contents = updateDomainModelDto.contents();
        this.thumbnail = thumbnail;
        this.visible = updateDomainModelDto.visible();
        this.summary = updateDomainModelDto.summary();

        this.tags.clear();
        for (Tag tag : updateDomainModelDto.tags()) {
            this.tags.add(tag);
        }
    }

    public static class Factory {
        public static Post create(CreatePostDomainModelDto dto) {

            String thumbnail = null;
            if (!hasText(dto.thumbnail())) {
                thumbnail = ContentsAnalyzeDomainService.getThumbnail(dto.contents());
            } else {
                thumbnail = ImagePathGenerateDomainService.generate(dto.thumbnail());
            }

            return Post.builder()
                       .title(dto.title())
                       .contents(dto.contents())
                       .thumbnail(thumbnail)
                       .visible(dto.visible())
                       .summary(dto.summary())
                       .tags(dto.tags())
                       .build();
        }
    }
}
