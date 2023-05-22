package donghun.me.postservice.common.environment;


import com.fasterxml.jackson.databind.ObjectMapper;
import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;


public abstract class AbstractPresentationTest extends AbstractDefaultTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
}
