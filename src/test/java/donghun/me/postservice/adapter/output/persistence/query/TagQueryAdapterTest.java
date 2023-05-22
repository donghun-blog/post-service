package donghun.me.postservice.adapter.output.persistence.query;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.TagMapper;
import donghun.me.postservice.adapter.output.persistence.repository.TagQueryRepository;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.common.environment.AbstractDataAccessMysqlTestContainer;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.domain.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static donghun.me.postservice.domain.exception.PostErrorCode.TAG_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TagQueryAdapterTest extends AbstractDataAccessMysqlTestContainer {

    @TestConfiguration
    static class TagQueryAdapterTestConfiguration {

        @Autowired
        private TagQueryRepository tagQueryRepository;

        @Bean
        TagMapper tagMapper() {
            return new TagMapper();
        }

        @Bean
        TagQueryAdapter tagQueryAdapter() {
            return new TagQueryAdapter(tagQueryRepository, tagMapper());
        }
    }

    @Autowired
    private TagQueryAdapter tagQueryAdapter;
    @Autowired
    private TagRepository tagRepository;

    @DisplayName("태그가 존재하는 경우 true를 반환한다.")
    @Test
    void isTagExistReturnTrue() {
        // given
        final String tagName = "Spring Boot";
        TagEntity tagEntity = TagEntity.builder()
                                       .name(tagName)
                                       .build();

        tagRepository.save(tagEntity);

        // when
        boolean result = tagQueryAdapter.isTagExist(tagName);

        // then
        assertTrue(result);
    }

    @DisplayName("태그가 존재하지 않는 경우 false를 반환한다.")
    @Test
    void isTagExistReturnFalse() {
        // given
        final String tagName = "Spring Boot";

        // when
        boolean result = tagQueryAdapter.isTagExist(tagName);

        // then
        assertFalse(result);
    }

    @DisplayName("태그 조회 시 태그가 없는 경우 예외를 반환한다.")
    @Test
    void findByNameNotExistException() {
        // given
        final String tagName = "Spring Boot";

        // when & then
        assertThatThrownBy(() -> tagQueryAdapter.findByName(tagName))
                                       .isInstanceOf(PostException.class)
                                       .hasFieldOrPropertyWithValue("errorCode", TAG_NOT_FOUND);
    }


    @DisplayName("태그 조회 시 태그가 있는 경우 태그 도메인을 반환한다.")
    @Test
    void findByName() {
        // given
        final String tagName = "Spring Boot";
        TagEntity tagEntity = TagEntity.builder()
                                       .name(tagName)
                                       .build();

        tagRepository.save(tagEntity);

        // when
        Tag result = tagQueryAdapter.findByName(tagName);

        // then
        assertThat(result).isNotNull()
                          .extracting("id", "name")
                          .contains(tagEntity.getId(), tagName);

    }
}