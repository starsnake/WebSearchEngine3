package main.config;

import lombok.Getter;
import lombok.Setter;
import main.model.Site;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties
@Getter
@Setter
public class SiteConfig {
    private List<Site> sites;
}
