package donghun.me.postservice.adapter.output.persistence.command;

import com.netflix.discovery.converters.Auto;
import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.PostMapper;
import donghun.me.postservice.adapter.output.persistence.mapper.TagMapper;
import donghun.me.postservice.adapter.output.persistence.repository.PostRepository;
import donghun.me.postservice.adapter.output.persistence.repository.PostTagRepository;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.common.environment.AbstractServiceMysqlTestContainer;
import donghun.me.postservice.domain.dto.UpdatePostDomainModelDto;
import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.domain.model.Tag;
import donghun.me.postservice.fixture.PostEntityFixture;
import donghun.me.postservice.fixture.PostFixture;
import donghun.me.postservice.fixture.UpdatePostDomainModelDtoFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class PostCommandAdapterTest extends AbstractServiceMysqlTestContainer {

    @Autowired
    private PostCommandAdapter postCommandAdapter;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private PostMapper postMapper;
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
                                                 .id(null)
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

    @DisplayName("게시글 엔티티를 업데이트 후 값을 확인한다.")
    @Test
    void update() {
        // given
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name("Spring Boot")
                                        .build();

        TagEntity tagEntity2 = TagEntity.builder()
                                        .name("Spring Security")
                                        .build();

        TagEntity updateTagEntity1 = TagEntity.builder()
                                        .name("Update Spring Boot")
                                        .build();

        TagEntity updateTagEntity2 = TagEntity.builder()
                                        .name("Update Spring Security")
                                        .build();

        tagRepository.save(tagEntity1);
        tagRepository.save(tagEntity2);

        tagRepository.save(updateTagEntity1);
        tagRepository.save(updateTagEntity2);

        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();

        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);

        postRepository.save(postEntity);

        final String updateTitle = "update Title";
        final String updateContents = "update Contents";
        final String updateThumbnail = "updateThumbnail.png";
        final boolean updateVisible = false;
        final String updateSummary = "updateSummary";

        List<Tag> updateTags = List.of(
                Tag.builder()
                   .id(updateTagEntity1.getId())
                   .name(updateTagEntity1.getName())
                   .build(),
                Tag.builder()
                   .id(updateTagEntity2.getId())
                   .name(updateTagEntity2.getName())
                   .build()
        );

        Post updatePost = postMapper.toDomainModel(postEntity);

        UpdatePostDomainModelDto updatePostDomainModelDto = UpdatePostDomainModelDtoFixture.complete()
                                                                                           .title(updateTitle)
                                                                                           .contents(updateContents)
                                                                                           .thumbnail(updateThumbnail)
                                                                                           .visible(updateVisible)
                                                                                           .summary(updateSummary)
                                                                                           .tags(updateTags)
                                                                                           .build();

        updatePost.update(updatePostDomainModelDto);

        // when
        postCommandAdapter.update(updatePost);

        // then
        postRepository.flush();
        System.out.println("-----");
        PostEntity findPostEntity = postRepository.findById(postEntity.getId())
                                               .orElseThrow();

        assertThat(findPostEntity)
                .isNotNull()
                .extracting("title", "contents", "visible", "summary")
                .contains(
                        updateTitle,
                        updateContents,
                        updateVisible,
                        updateSummary
                );

        assertThat(findPostEntity.getThumbnail())
                .isNotBlank();

        assertThat(findPostEntity.getPostTags())
                .isNotNull()
                .hasSize(2);

        assertThat(findPostEntity.getPostTags()
                                 .get(0)
                                 .getTag())
                .isNotNull()
                .extracting("id", "name")
                .contains(updateTags.get(0)
                                    .getId(), updateTags.get(0)
                                                        .getName());

        assertThat(findPostEntity.getPostTags()
                                 .get(1)
                                 .getTag())
                .isNotNull()
                .extracting("id", "name")
                .contains(updateTags.get(1)
                                    .getId(), updateTags.get(1)
                                                        .getName());
    }
}