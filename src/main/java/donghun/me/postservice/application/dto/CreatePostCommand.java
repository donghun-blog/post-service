package donghun.me.postservice.application.dto;

import donghun.me.postservice.domain.dto.CreatePostDomainModelDto;
import donghun.me.postservice.domain.model.Tag;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

public record CreatePostCommand(
        String title,
        String contents,
        List<String> tags,
        boolean visible,
        MultipartFile thumbnail,
        String summary
) {

    @Builder
    public CreatePostCommand {
    }

    public CreatePostDomainModelDto toDomainModelDto(List<Tag> tags) {
        return new CreatePostDomainModelDto(
                title(),
                contents(),
                isNull(thumbnail) ? null : thumbnail().getOriginalFilename(),
                visible(),
                summary(),
                tags
        );
    }
}
