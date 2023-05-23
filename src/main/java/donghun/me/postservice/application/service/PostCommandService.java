package donghun.me.postservice.application.service;

import donghun.me.postservice.application.dto.CreatePostCommand;
import donghun.me.postservice.application.port.input.PostCommandUseCase;
import donghun.me.postservice.application.port.output.CommandPostPort;
import donghun.me.postservice.application.port.output.UploadImagePort;
import donghun.me.postservice.domain.dto.CreatePostDomainModelDto;
import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.domain.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommandService implements PostCommandUseCase {
    private final TagFacadeService tagFacadeService;
    private final CommandPostPort commandPostPort;
    private final UploadImagePort uploadImagePort;

    @Override
    @Transactional
    public Long createPost(CreatePostCommand command) {
        // 태그 조회
        List<Tag> tags = getTags(command.tags());

        CreatePostDomainModelDto domainModelDto = command.toDomainModelDto(tags);

        Post post = Post.Factory.create(domainModelDto);

        if(!post.isThumbnailEmpty()) {
            uploadImagePort.upload(post.getThumbnail(), command.thumbnail());
        }

        return commandPostPort.save(post).getId();
    }

    private List<Tag> getTags(List<String> insertTag) {
        List<Tag> tags = Collections.emptyList();
        if (!insertTag.isEmpty()) {
            tags = tagFacadeService.createOrFind(insertTag);
        }
        return tags;
    }
}
