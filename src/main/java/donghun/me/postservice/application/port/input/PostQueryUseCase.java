package donghun.me.postservice.application.port.input;

import donghun.me.postservice.application.dto.PostDetailDto;
import donghun.me.postservice.application.dto.PostDto;
import donghun.me.postservice.application.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryUseCase {
    PostDetailDto findById(Long postId);
    Page<PostDto> getPage(Pageable pageable, SearchCondition searchCond);
}
