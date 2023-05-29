package donghun.me.postservice.adapter.output.persistence.command;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.PostMapper;
import donghun.me.postservice.adapter.output.persistence.mapper.TagMapper;
import donghun.me.postservice.adapter.output.persistence.repository.PostRepository;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.application.port.output.CommandPostPort;
import donghun.me.postservice.domain.exception.PostErrorCode;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.domain.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static donghun.me.postservice.domain.exception.PostErrorCode.POST_NOT_FOUND;
import static donghun.me.postservice.domain.exception.PostErrorCode.TAG_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class PostCommandAdapter implements CommandPostPort {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final TagRepository tagRepository;

    @Override
    public Post save(Post post) {
        PostEntity postEntity = postMapper.toEntity(post);
        return postMapper.toDomainModel(postRepository.save(postEntity));
    }

    @Override
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public void update(Post post) {
        PostEntity postEntity = postRepository.findById(post.getId())
                                              .orElseThrow(() -> new PostException(POST_NOT_FOUND));
        postEntity.update(post);

        postEntity.tagClear();
        for (Tag tag : post.getTags()) {
            TagEntity tagEntity = tagRepository.findById(tag.getId())
                                               .orElseThrow(() -> new PostException(TAG_NOT_FOUND));
            postEntity.addTag(tagEntity);
        }
    }
}
