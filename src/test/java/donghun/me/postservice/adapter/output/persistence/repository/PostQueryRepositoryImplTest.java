package donghun.me.postservice.adapter.output.persistence.repository;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.common.environment.AbstractDataAccessMysqlTestContainer;
import donghun.me.postservice.fixture.PostEntityFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class PostQueryRepositoryImplTest extends AbstractDataAccessMysqlTestContainer {

    @Autowired
    private PostQueryRepository postQueryRepository;
    @Autowired
    private PostRepository postRepository;


    @DisplayName("게시글이 존재하면 true를 반환한다.")
    @Test
    void isExistReturnTrue() {
        // given
        PostEntity postEntity = PostEntityFixture.complete()
                                                 .build();
        postRepository.save(postEntity);

        // when
        boolean result = postQueryRepository.isExist(postEntity.getId());

        // then
        assertTrue(result);
    }

    @DisplayName("게시글이 존재하면 false를 반환한다.")
    @Test
    void isExistReturnFalse() {
        // given

        // when
        boolean result = postQueryRepository.isExist(1L);

        // then
        assertFalse(result);
    }

}