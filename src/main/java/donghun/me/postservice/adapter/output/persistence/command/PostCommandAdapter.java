package donghun.me.postservice.adapter.output.persistence.command;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.PostMapper;
import donghun.me.postservice.adapter.output.persistence.repository.PostRepository;
import donghun.me.postservice.application.port.output.CommandPostPort;
import donghun.me.postservice.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommandAdapter implements CommandPostPort {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public Post save(Post post) {
        PostEntity postEntity = postMapper.toEntity(post);
        return postMapper.toDomainModel(postRepository.save(postEntity));
    }

    @Override
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }
}
