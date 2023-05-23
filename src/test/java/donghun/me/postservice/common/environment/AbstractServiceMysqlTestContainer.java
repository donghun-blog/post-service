package donghun.me.postservice.common.environment;

import com.amazonaws.services.s3.AmazonS3Client;
import donghun.me.postservice.adapter.output.s3.config.S3Config;
import donghun.me.postservice.adapter.output.s3.config.S3Properties;
import donghun.me.postservice.common.environment.base.AbstractIntegrationTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test-container")
@Transactional
public abstract class AbstractServiceMysqlTestContainer extends AbstractIntegrationTest {
    @Container
    protected static final MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:8.0.30")
            .withUsername("test")
            .withPassword("test");

    @MockBean
    protected S3Properties s3Properties;

    @MockBean
    protected S3Config s3Config;

    @MockBean
    protected AmazonS3Client amazonS3Client;
}
