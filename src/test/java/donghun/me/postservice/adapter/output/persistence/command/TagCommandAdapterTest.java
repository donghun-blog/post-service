package donghun.me.postservice.adapter.output.persistence.command;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.TagMapper;
import donghun.me.postservice.adapter.output.persistence.query.TagQueryAdapter;
import donghun.me.postservice.adapter.output.persistence.repository.TagQueryRepository;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.common.environment.AbstractDataAccessMysqlTestContainer;
import donghun.me.postservice.domain.model.Tag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TagCommandAdapterTest extends AbstractDataAccessMysqlTestContainer {
    @TestConfiguration
    static class TagQueryAdapterTestConfiguration {

        @Autowired
        private TagRepository tagRepository;

        @Bean
        TagMapper tagMapper() {
            return new TagMapper();
        }

        @Bean
        TagCommandAdapter tagQueryAdapter() {
            return new TagCommandAdapter(tagRepository, tagMapper());
        }
    }

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagCommandAdapter tagCommandAdapter;

    @DisplayName("태그 저장 후 태그 도메인을 반환한다.")
    @Test
    void save() {
        // given
        final String insertTagName = "Spring Security";

        // when
        Tag result = tagCommandAdapter.save(insertTagName);

        // then
        assertThat(result)
                  .isNotNull()
                  .extracting("name")
                  .isEqualTo(insertTagName);
        assertThat(result.getId()).isNotNull();
    }

    @DisplayName("태그 저장 시 중복된 태그명이 등록된 경우 예외를 반환한다.")
    @Test
    void saveDuplicateTagNameException() {
        // given
        final String insertTagName = "Spring Security";
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name(insertTagName)
                                        .build();

        tagRepository.save(tagEntity1);

        // when & then
        assertThatThrownBy(() -> tagCommandAdapter.save(insertTagName))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}