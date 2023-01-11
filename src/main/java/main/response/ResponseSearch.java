package main.response;

import lombok.Data;
import main.model.statistics.Statistics;
@Data
public class ResponseSearch implements Response{
    private boolean result = true;
    private Statistics statistics;

}
