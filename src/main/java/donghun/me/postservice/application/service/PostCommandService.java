package donghun.me.postservice.application.service;

import donghun.me.postservice.application.dto.CreatePostCommand;
import donghun.me.postservice.application.port.input.PostCommandUseCase;
import donghun.me.postservice.application.port.output.CommandPostPort;
import donghun.me.postservice.application.port.output.QueryPostPort;
import donghun.me.postservice.application.port.output.UploadImagePort;
import donghun.me.postservice.domain.dto.CreatePostDomainModelDto;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.domain.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static donghun.me.postservice.domain.exception.PostErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostCommandService implements PostCommandUseCase {
    private final TagFacadeService tagFacadeService;
    private final CommandPostPort commandPostPort;
    private final QueryPostPort queryPostPort;
    private final UploadImagePort uploadImagePort;

    @Override
    @Transactional
    public Long createPost(CreatePostCommand command) {
        // 태그 조회
        List<Tag> tags = getTags(command.tags());

        CreatePostDomainModelDto domainModelDto = command.toDomainModelDto(tags);

        Post post = Post.Factory.create(domainModelDto);

        if(!post.isThumbnailEmpty() && !Objects.isNull(command.thumbnail())) {
            uploadImagePort.upload(post.getThumbnail(), command.thumbnail());
        }

        return commandPostPort.save(post).getId();
    }

    @Override
    public void deletePost(Long postId) {
        if(!queryPostPort.isPostExist(postId)) {
            throw new PostException(POST_NOT_FOUND);
        }
        commandPostPort.delete(postId);
    }

    private List<Tag> getTags(List<String> insertTag) {
        List<Tag> tags = Collections.emptyList();
        if (!insertTag.isEmpty()) {
            tags = tagFacadeService.createOrFind(insertTag);
        }
        return tags;
    }
}
