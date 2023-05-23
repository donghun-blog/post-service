package donghun.me.postservice.adapter.output.persistence.mapper;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.domain.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final TagMapper tagMapper;

    public Post toDomainModel(PostEntity postEntity) {
        return Post.builder()
                   .id(postEntity.getId())
                   .title(postEntity.getTitle())
                   .contents(postEntity.getContents())
                   .visible(postEntity.isVisible())
                   .thumbnail(postEntity.getThumbnail())
                   .summary(postEntity.getSummary())
                   .tags(
                           postEntity.getPostTags()
                                     .stream()
                                     .map(te -> tagMapper.toDomainModel(te.getTag()))
                                     .collect(Collectors.toList())
                   )
                   .build();
    }

    public PostEntity toEntity(Post post) {
        PostEntity postEntity = PostEntity.builder()
                                          .id(post.getId())
                                          .title(post.getTitle())
                                          .contents(post.getContents())
                                          .visible(post.isVisible())
                                          .thumbnail(post.getThumbnail())
                                          .summary(post.getSummary())
                                          .build();

        for (Tag tag : post.getTags()) {
            postEntity.addTag(tagMapper.toEntity(tag));
        }
        return postEntity;
    }
}
