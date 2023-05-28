package donghun.me.postservice.application.service;

import donghun.me.postservice.adapter.output.s3.config.S3Properties;
import donghun.me.postservice.application.dto.PostDetailDto;
import donghun.me.postservice.application.dto.PostDto;
import donghun.me.postservice.application.port.input.PostQueryUseCase;
import donghun.me.postservice.application.port.output.QueryPostPort;
import donghun.me.postservice.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService implements PostQueryUseCase {

    private final QueryPostPort queryPostPort;
    private final S3Properties s3Properties;

    @Override
    public PostDetailDto findById(Long postId) {
        Post post = queryPostPort.findById(postId);
        return PostDetailDto.of(post, s3Properties.getAbsolutePath());
    }

    @Override
    public Page<PostDto> getPage(Pageable pageable) {
        Page<Post> posts = queryPostPort.getPage(pageable);
        return posts.map(p -> PostDto.of(p, s3Properties.getAbsolutePath()));
    }
}
