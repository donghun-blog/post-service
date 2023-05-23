package donghun.me.postservice.adapter.input.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagResponse {
    private Long id;
    private String name;

    @Builder
    private TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
