package main.service.parsing;

import main.config.ConnectConfig;
import main.model.Site;
import main.model.TypeSiteIndexingStatus;
import main.service.IParsingPageService;

import java.util.concurrent.ForkJoinPool;

public class StartParsingOnePage implements Runnable{
    private final IParsingPageService parsingPageService;
    private final ConnectConfig connectConfig;
    private final String siteUrl;

    public StartParsingOnePage(String siteUrl, ConnectConfig connectConfig, IParsingPageService parsingPageService) {
        this.siteUrl = siteUrl;
        this.connectConfig = connectConfig;
        this.parsingPageService = parsingPageService;
    }

    @Override
    public void run() {
        Site site = parsingPageService.getSite(siteUrl);
        if(site == null){
            return;
        }
        Site newSite = new Site(siteUrl, site.getName());
        parsingPageService.deleteSite(site);
        newSite.setStatus(TypeSiteIndexingStatus.INDEXING);
        newSite = parsingPageService.saveSite(newSite);

        ParsingPage parsingPage = new ParsingPage(parsingPageService, newSite , connectConfig);
        new ForkJoinPool().invoke(parsingPage);

        if(!parsingPageService.isSiteFailed(site)) {
            newSite.setStatus(TypeSiteIndexingStatus.INDEXED);
            parsingPageService.saveSite(newSite);
        }
    }
}
