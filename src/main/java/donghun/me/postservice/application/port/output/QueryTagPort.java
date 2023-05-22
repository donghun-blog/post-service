package donghun.me.postservice.application.port.output;

import donghun.me.postservice.domain.model.Tag;

public interface QueryTagPort {
    boolean isTagExist(String tag);

    Tag findByName(String name);
}
