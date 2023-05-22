package donghun.me.postservice.domain.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.hasText;

public class ContentsAnalyzeDomainService {
    public static String getThumbnail(String contents) {
        if(!hasText(contents)) {
            return null;
        }

        String pattern = "!\\[[^\\]]*\\]\\(([^)]+)\\)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(contents);

        if (matcher.find()) {
            String group = matcher.group(1);
            return group.substring(group.lastIndexOf("/") + 1);
        }

        return null;
    }
}
