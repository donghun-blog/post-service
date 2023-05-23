package donghun.me.postservice.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class AbstractBaseDomainModel {
    protected final LocalDateTime createdAt;
    protected final LocalDateTime modifiedAt;

    public AbstractBaseDomainModel(LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
