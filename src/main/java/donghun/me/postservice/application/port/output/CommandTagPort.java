package donghun.me.postservice.application.port.output;

import donghun.me.postservice.domain.model.Tag;

public interface CommandTagPort {
    Tag save(String name);
}
