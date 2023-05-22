package donghun.me.postservice.application.service;

import donghun.me.postservice.application.port.output.CommandTagPort;
import donghun.me.postservice.application.port.output.QueryTagPort;
import donghun.me.postservice.domain.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagFacadeService {

    private final QueryTagPort queryTagPort;
    private final CommandTagPort commandTagPort;

    @Transactional
    public List<Tag> createOrFind(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String tag : tags) {
            if(StringUtils.hasText(tag)) {
                if (!queryTagPort.isTagExist(tag)) {
                    // TODO: 동시성 문제 예상됨
                    Tag result = commandTagPort.save(tag);
                    tagList.add(result);
                } else {
                    Tag result = queryTagPort.findByName(tag);
                    tagList.add(result);
                }
            }
        }
        return tagList;
    }
}
