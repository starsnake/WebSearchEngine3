package main.model.search;

import lombok.AllArgsConstructor;

@lombok.Data
@AllArgsConstructor
public class Data {
    private String site;
    private String siteName;
    private String uri;
    private String title;
    private String snippet;
    private float relevance;
}
