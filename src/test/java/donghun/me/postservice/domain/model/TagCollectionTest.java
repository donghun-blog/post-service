package donghun.me.postservice.domain.model;

import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import donghun.me.postservice.domain.exception.PostException;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static donghun.me.postservice.domain.exception.PostErrorCode.POST_TAG_DUPLICATE;
import static donghun.me.postservice.domain.exception.PostErrorCode.TAG_MAXIMUM_OVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TagCollectionTest extends AbstractDefaultTest {

    @DisplayName("태그 컬렉션에 태그 추가 시 중복된 태그명이 등록되는 경우 예외를 반환한다.")
    @Test
    void addDuplicateNameException() {
        // given
        Tag tag1 = createTag(1L, "태그1");
        Tag tag2 = createTag(2L, "태그2");
        Tag duplicateTag = createTag(3L, "태그1");

        TagCollection tagCollection = new TagCollection();

        tagCollection.add(tag1);
        tagCollection.add(tag2);

        // when & then
        assertThatThrownBy(() -> tagCollection.add(duplicateTag))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_TAG_DUPLICATE);
    }

    @DisplayName("태그 컬렉션에 태그 추가 시 중복된 태그Id가 등록되는 경우 예외를 반환한다.")
    @Test
    void addDuplicateIdException() {
        // given
        TagCollection tagCollection = new TagCollection();
        Tag tag1 = createTag(1L, "태그1");
        Tag tag2 = createTag(2L, "태그2");
        Tag duplicateTag = createTag(1L, "태그5");


        tagCollection.add(tag1);
        tagCollection.add(tag2);

        // when & then
        assertThatThrownBy(() -> tagCollection.add(duplicateTag))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_TAG_DUPLICATE);
    }

    @DisplayName("태그 컬렉션에 태그 추가 시 최대 개수 초과한 경우 예외를 반환한다.")
    @Test
    void addSizeOverException() {
        // given
        TagCollection tagCollection = new TagCollection();
        for (int i = 0; i < TagCollection.MAX_TAG_SIZE; i++) {
            tagCollection.add(createTag((long) (i + 1), "태그" + i));
        }

        final Tag insertTag = createTag((long) (TagCollection.MAX_TAG_SIZE + 1), "초과 태그명");

        // when & then
        assertThatThrownBy(() -> tagCollection.add(insertTag))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", TAG_MAXIMUM_OVER);
    }

    @DisplayName("태그 추가 후 값을 확인한다.")
    @Test
    void add() {
        // given
        TagCollection tagCollection = new TagCollection();
        Tag tag1 = createTag(1L, "태그1");
        Tag tag2 = createTag(2L, "태그2");
        Tag tag3 = createTag(3L, "태그3");

        tagCollection.add(tag1);
        tagCollection.add(tag2);

        // when
        tagCollection.add(tag3);


        // then
        assertFalse(tagCollection.isEmpty());
        assertThat(tagCollection.getTags()).isNotNull()
                                           .hasSize(3)
                                           .extracting("id", "name")
                                           .containsExactlyInAnyOrder(
                                                   tuple(tag1.getId(), tag1.getName()),
                                                   tuple(tag2.getId(), tag2.getName()),
                                                   tuple(tag3.getId(), tag3.getName())
                                           );
    }

    private static Tag createTag(Long id, String name) {
        return Tag.builder()
                  .id(id)
                  .name(name)
                  .build();
    }

}