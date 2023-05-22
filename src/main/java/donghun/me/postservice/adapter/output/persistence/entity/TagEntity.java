package donghun.me.postservice.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "tag",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "name" })
        }
)
public class TagEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder
    private TagEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
