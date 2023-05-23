package donghun.me.postservice.adapter.output.persistence.query;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.repository.PostQueryRepository;
import donghun.me.postservice.adapter.output.persistence.repository.PostRepository;
import donghun.me.postservice.common.environment.AbstractDataAccessMysqlTestContainer;
import donghun.me.postservice.fixture.PostEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class PostQueryAdapterTest extends AbstractDataAccessMysqlTestContainer {

    @TestConfiguration
    static class PostQueryAdapterTestConfiguration {
        @Autowired
        private PostQueryRepository postQueryRepository;

        @Bean
        PostQueryAdapter postQueryAdapter() {
            return new PostQueryAdapter(postQueryRepository);
        }
    }

    @Autowired
    private PostQueryAdapter postQueryAdapter;
    @Autowired
    private PostRepository postRepository;

    @DisplayName("게시글이 존재하는 경우 true를 반환한다.")
    @Test
    void isPostExistReturnTrue() {
        // given
        PostEntity postEntity = PostEntityFixture.complete()
                                                 .build();
        postRepository.save(postEntity);

        // when
        boolean result = postQueryAdapter.isPostExist(postEntity.getId());

        // then
        assertTrue(result);
    }

    @DisplayName("게시글이 존재하지 않는 경우 false를 반환한다.")
    @Test
    void isPostNotExistReturnFalse() {
        // given

        // when
        boolean result = postQueryAdapter.isPostExist(1L);

        // then
        assertFalse(result);
    }
}