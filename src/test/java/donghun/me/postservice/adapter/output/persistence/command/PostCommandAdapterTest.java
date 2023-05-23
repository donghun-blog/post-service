package donghun.me.postservice.adapter.output.persistence.command;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.TagMapper;
import donghun.me.postservice.adapter.output.persistence.repository.PostRepository;
import donghun.me.postservice.adapter.output.persistence.repository.PostTagRepository;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.common.environment.AbstractMysqlTestContainer;
import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.domain.model.Tag;
import donghun.me.postservice.fixture.PostEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class PostCommandAdapterTest extends AbstractMysqlTestContainer {

    @Autowired
    private PostCommandAdapter postCommandAdapter;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private PostTagRepository postTagRepository;

    @DisplayName("게시글 도메인 생성 후 게시글 id를 확인한다.")
    @Test
    void save() {
        // given
        final String title = "title";
        final String contents = "contents";
        final boolean visible = true;
        final String thumbnail = "123.png";
        final String summary = "summary";
        final List<Tag> tags = new ArrayList<>();
        TagEntity tagEntity = TagEntity.builder()
                                       .name("Spring Boot")
                                       .build();

        tagRepository.save(tagEntity);

        tags.add(tagMapper.toDomainModel(tagEntity));

        Post post = Post.builder()
                        .title(title)
                        .contents(contents)
                        .visible(visible)
                        .thumbnail(thumbnail)
                        .summary(summary)
                        .tags(tags)
                        .build();

        // when
        Post savedPost = postCommandAdapter.save(post);

        // then
        List<PostEntity> postEntities = postRepository.findAll();
        assertThat(postEntities)
                .hasSize(1);
        assertThat(savedPost)
                .isNotNull()
                .extracting("id", "title", "contents", "visible", "thumbnail", "summary")
                .contains(postEntities.get(0)
                                      .getId(), title, contents, visible, thumbnail, summary);
        assertThat(savedPost.getTags())
                .isNotEmpty()
                .hasSize(1)
                .extracting("id", "name")
                .contains(tuple(tagEntity.getId(), "Spring Boot"));
    }

    @DisplayName("게시글이 존재하는 경우 삭제한 다음 확인한다.")
    @Test
    void delete() {
        // given
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name("Spring Boot")
                                        .build();

        TagEntity tagEntity2 = TagEntity.builder()
                                        .name("Spring Security")
                                        .build();

        tagRepository.save(tagEntity1);
        tagRepository.save(tagEntity2);

        PostEntity postEntity = PostEntityFixture.complete()
                                                 .build();

        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);

        postRepository.save(postEntity);

        // when
        postCommandAdapter.delete(postEntity.getId());

        // then
        assertThat(tagRepository.findAll())
                .hasSize(2);
        assertThat(postTagRepository.findAll())
                .hasSize(0);
        assertThat(postRepository.findAll())
                .hasSize(0);
    }
}