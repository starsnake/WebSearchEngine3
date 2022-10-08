package main.service;

import main.model.Site;
import main.model.statistics.Detailed;
import main.model.statistics.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService implements IStatisticsService {

    private final IParsingPageService parsingService;
    @Autowired
    public StatisticsService(IParsingPageService parsingService) {
        this.parsingService = parsingService;
    }

    @Override
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();
        statistics.getTotal().setSites((int)(parsingService.countSites()));
        statistics.getTotal().setPages((int)(parsingService.countPage()));
        statistics.getTotal().setLemmas((int)(parsingService.countLemma()));
        statistics.getTotal().setIndexing(parsingService.isIndexing());
        List<Site> siteList = parsingService.getAllSites();
        for(Site site : siteList){
            statistics.addDetailed(site.getUrl(),
                    site.getName(),
                    site.getStatus(),
                    site.getStatusTime(),
                    site.getLastError(),
                    parsingService.countPageBySite(site),
                    parsingService.countLemmaBySite(site));
        }
        return statistics;
    }
}
