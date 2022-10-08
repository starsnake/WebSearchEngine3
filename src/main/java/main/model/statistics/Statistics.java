package main.model.statistics;

import lombok.*;
import main.model.TypeSiteIndexingStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Statistics {
    Total total;
    List<Detailed> detailed;

    public Statistics() {
        total = new Total();
        detailed = new ArrayList<Detailed>();
    }

    public void addDetailed(String url,
                            String name,
                            TypeSiteIndexingStatus status,
                            Date statusTime,
                            String error,
                            long pages,
                            long lemmas){
        detailed.add(new Detailed(url, name, status, statusTime, error, pages, lemmas));
    }
}
