package donghun.me.postservice.application.port.output;

import donghun.me.postservice.domain.model.Post;

public interface CommandPostPort {
    Post save(Post post);
}
