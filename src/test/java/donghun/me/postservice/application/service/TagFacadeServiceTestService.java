package donghun.me.postservice.application.service;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.common.environment.AbstractServiceMysqlTestContainer;
import donghun.me.postservice.domain.model.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TagFacadeServiceTestService extends AbstractServiceMysqlTestContainer {

    @Autowired
    private TagFacadeService tagFacadeService;
    @Autowired
    private TagRepository tagRepository;

    @DisplayName("태그 생성 또는 검색에서 빈 리스트가 넘어온 경우 빈 리스트를 반환한다.")
    @Test
    void createOrFindEmptyListReturnEmpty() {
        // given
        List<String> tags = Collections.emptyList();

        // when
        List<Tag> result = tagFacadeService.createOrFind(tags);

        // then
        assertThat(result).hasSize(0)
                          .isEmpty();
    }

    @DisplayName("태그 생성 또는 검색에서 태그 테이블에 태그 정보가 없는 경우 태그를 추가한다.")
    @Test
    void createOrFindCreate() {
        // given
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name("Spring Boot")
                                        .build();
        TagEntity tagEntity2 = TagEntity.builder()
                                        .name("Spring Security")
                                        .build();
        TagEntity tagEntity3 = TagEntity.builder()
                                        .name("Transaction")
                                        .build();
        tagRepository.saveAll(List.of(tagEntity1, tagEntity2, tagEntity3));

        List<String> insertTags = List.of("Spring Boot", "Transaction", "OAuth", "MSA");

        // when
        List<Tag> result = tagFacadeService.createOrFind(insertTags);

        // then
        assertThat(result).isNotEmpty()
                          .hasSize(4)
                          .extracting("name")
                          .contains("Spring Boot", "Transaction", "OAuth", "MSA");

        List<TagEntity> all = tagRepository.findAll();
        assertThat(all).isNotEmpty()
                       .hasSize(5)
                       .extracting("name")
                       .contains("Spring Boot", "Spring Security", "Transaction", "OAuth", "MSA");
    }

    @DisplayName("태그 생성 또는 검색에서 태그 테이블에 태그 정보가 동일한 경우 태그를 반환한다.")
    @Test
    void createOrFind() {
        // given
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name("Spring Boot")
                                        .build();
        TagEntity tagEntity2 = TagEntity.builder()
                                        .name("Spring Security")
                                        .build();
        TagEntity tagEntity3 = TagEntity.builder()
                                        .name("Transaction")
                                        .build();
        tagRepository.saveAll(List.of(tagEntity1, tagEntity2, tagEntity3));

        List<String> insertTags = List.of("Spring Boot", "Spring Security", "Transaction");

        // when
        List<Tag> result = tagFacadeService.createOrFind(insertTags);

        // then
        assertThat(result).isNotEmpty()
                          .hasSize(3)
                          .extracting("name")
                          .contains("Spring Boot", "Spring Security", "Transaction");

        List<TagEntity> all = tagRepository.findAll();
        assertThat(all).isNotEmpty()
                       .hasSize(3)
                       .extracting("name")
                       .contains("Spring Boot", "Spring Security", "Transaction");
    }
}