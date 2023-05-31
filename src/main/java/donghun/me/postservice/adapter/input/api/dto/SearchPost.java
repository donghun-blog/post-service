package donghun.me.postservice.adapter.input.api.dto;

import donghun.me.postservice.application.dto.SearchCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPost {
    private String keyword;
    public SearchCondition toCondition() {
        return new SearchCondition(keyword);
    }
}
