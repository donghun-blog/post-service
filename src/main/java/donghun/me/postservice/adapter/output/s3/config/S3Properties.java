package donghun.me.postservice.adapter.output.s3.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloud.aws.s3")
@Data
public class S3Properties {

    private String bucket;
    private String prefix;
    private String baseUrl;

    public String getAbsolutePath() {
        return baseUrl + prefix;
    }
}
