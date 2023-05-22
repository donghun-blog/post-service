package donghun.me.postservice.adapter.output.persistence.mapper;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import donghun.me.postservice.domain.model.Tag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagMapperTest extends AbstractDefaultTest {

    @DisplayName("태그 엔티티에서 태그 도메인 모델로 변환 후 값을 확인한다.")
    @Test
    void toDomainModel() {
        // given
        TagMapper tagMapper = new TagMapper();
        final Long tagId = 1L;
        final String tagName = "태그1";
        TagEntity tagEntity = TagEntity.builder()
                                       .id(tagId)
                                       .name(tagName)
                                       .build();

        // when
        Tag tag = tagMapper.toDomainModel(tagEntity);

        // then
        Assertions.assertThat(tag)
                  .isNotNull()
                  .extracting("id", "name")
                  .contains(tagId, tagName);
    }

    @DisplayName("태그 도메인에서 태그 엔티티로 변환 후 값을 확인한다.")
    @Test
    void toEntity() {
        // given
        TagMapper tagMapper = new TagMapper();
        final Long tagId = 1L;
        final String tagName = "태그1";
        Tag tag = Tag.builder()
                     .id(tagId)
                     .name(tagName)
                     .build();

        // when
        TagEntity entity = tagMapper.toEntity(tag);

        // then
        Assertions.assertThat(entity)
                  .isNotNull()
                  .extracting("id", "name")
                  .contains(tagId, tagName);
    }
}