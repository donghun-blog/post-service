package donghun.me.postservice.adapter.output.persistence.repository;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.common.environment.AbstractDataAccessMysqlTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagQueryRepositoryImplTest extends AbstractDataAccessMysqlTestContainer {

    @Autowired
    private TagQueryRepository tagQueryRepository;
    @Autowired
    private TagRepository tagRepository;


    @DisplayName("태그 조회 시 태그가 존재하지 않는 경우 false를 반환한다.")
    @Test
    void isTagExistReturnFalse() {
        // given
        final String insertTagName = "MSA";

        // when
        boolean result = tagQueryRepository.isTagExist(insertTagName);

        // then
        assertFalse(result);
    }

    @DisplayName("태그 조회 시 태그가 존재하는 경우 true를 반환한다.")
    @Test
    void isTagExistReturnTrue() {
        // given
        final String insertTagName = "MSA";
        TagEntity tagEntity = TagEntity.builder()
                                       .name(insertTagName)
                                       .build();

        tagRepository.save(tagEntity);

        // when
        boolean result = tagQueryRepository.isTagExist(insertTagName);

        // then
        assertTrue(result);
    }

    @DisplayName("태그 조회 시 태그가 존재하지 않는 경우 null을 반환한다.")
    @Test
    void findByNameReturnNull() {
        // given
        final String findTagName = "트랜잭션";

        // when
        Optional<TagEntity> result = tagQueryRepository.findByName(findTagName);

        // then
        assertTrue(result.isEmpty());
    }

    @DisplayName("태그 조회 시 태그가 존재하는 경우 태그 엔티티를 반환한다.")
    @Test
    void findByName() {
        // given
        final String findTagName = "트랜잭션";
        TagEntity tagEntity = TagEntity.builder()
                                       .name(findTagName)
                                       .build();

        tagRepository.save(tagEntity);

        // when
        Optional<TagEntity> result = tagQueryRepository.findByName(findTagName);

        // then
        assertTrue(result.isPresent());
        assertThat(result.get())
                .isNotNull()
                .extracting("id", "name")
                .contains(tagEntity.getId(), findTagName);
    }
}