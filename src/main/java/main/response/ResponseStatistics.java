package main.response;

import lombok.Data;
import main.model.statistics.Statistics;
import main.service.IParsingPageService;
import main.service.StatisticsService;
@Data
public class ResponseStatistics {
    private boolean result = true;
    private Statistics statistics;
    public ResponseStatistics(IParsingPageService parsingService) {
        StatisticsService statisticsService = new StatisticsService(parsingService);
        statistics = statisticsService.getStatistics();
    }
}
