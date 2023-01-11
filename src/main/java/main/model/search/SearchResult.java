package main.model.search;

import main.response.Response;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
public class SearchResult implements Response{
    private boolean result;
    private int count;
    private List<Data> data;

    public SearchResult() {
        this.result = true;
        data = new ArrayList<>();
    }

    public SearchResult(int count) {
        this();
        this.count = count;
    }

    public void addData(String site,
                        String siteName,
                        String uri,
                        String title,
                        String snippet,
                        float relevance){
        data.add(new Data(site, siteName, uri, title, snippet, relevance));
    }
}
