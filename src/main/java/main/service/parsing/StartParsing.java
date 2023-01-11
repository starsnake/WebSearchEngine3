package main.service.parsing;

import main.config.ConnectConfig;
import main.model.Page;
import main.model.Site;
import main.model.TypeSiteIndexingStatus;
import main.service.IParsingPageService;
import main.tools.Tools;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;

public class StartParsing implements Runnable {
    private final ConnectConfig connectConfig;
    private final IParsingPageService parsingPageService;

    public StartParsing(ConnectConfig connectConfig, IParsingPageService parsingPageService) {
        this.connectConfig = connectConfig;
        this.parsingPageService = parsingPageService;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        System.out.println("Start parsing sites " + new Date());
        parsingPageService.deleteAllSites();
        System.out.println("Duration of deleting: " + Tools.getTime((System.currentTimeMillis() - start) / 1000));
        parsingPageService.createSitesFromConfig();
        for (Site site : parsingPageService.getAllSites()) {
            start = System.currentTimeMillis();
            Page page = parsingPageService.newPage("/", site);
            ParsingPage parsingPage = new ParsingPage(parsingPageService, page, connectConfig);
            new ForkJoinPool().invoke(parsingPage);
            System.out.println("Duration of processing site " + site.getUrl() + ": " + Tools.getTime((System.currentTimeMillis() - start) / 1000));
            System.out.println("Links - " + parsingPageService.countPageBySite(site)); // + parsingPage.getListSet());
        }
        finishIndexing();
    }
    private void setStatusSites(TypeSiteIndexingStatus status) {
        for (Site site : parsingPageService.getAllSites()) {
            parsingPageService.setTypeIndexing(site, status);
        }
    }

    public void stopIndexing() {
        setStatusSites(TypeSiteIndexingStatus.FAILED);
    }
    public void finishIndexing() {
        if(parsingPageService.isIndexing()) {
            setStatusSites(TypeSiteIndexingStatus.INDEXED);
        }
    }

}
