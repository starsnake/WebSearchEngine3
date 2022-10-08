package main.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.model.TypeSiteIndexingStatus;

import java.util.Date;

@AllArgsConstructor
@Data
public class Detailed{
    String url;
    String name;
    TypeSiteIndexingStatus status;
    Date statusTime;
    String error;
    long pages;
    long lemmas;
}
