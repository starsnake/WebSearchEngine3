package main.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("connect")
@Getter
@Setter
public class ConnectConfig {
    private String useragent;
    private String referrer;
}
