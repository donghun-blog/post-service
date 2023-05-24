package donghun.me.postservice.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post")
public class PostEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String contents;
    private boolean visible;
    private String thumbnail;
    private String summary;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTagEntity> postTags = new ArrayList<>();

    @Builder
    private PostEntity(Long id, String title, String contents, boolean visible, String thumbnail, String summary, List<PostTagEntity> postTags) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.visible = visible;
        this.thumbnail = thumbnail;
        this.summary = summary;
        
    }

    public void addTag(TagEntity tagEntity) {
        PostTagEntity postTagEntity = createPostTagEntity(tagEntity);

        postTags.add(postTagEntity);
    }

    private PostTagEntity createPostTagEntity(TagEntity tagEntity) {
        return PostTagEntity.builder()
                            .post(this)
                            .tag(tagEntity)
                            .build();
    }

}
