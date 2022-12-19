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
    private final Site site;

    public StartParsingOnePage(Site site, String siteUrl, ConnectConfig connectConfig, IParsingPageService parsingPageService) {
        this.site = parsingPageService.getSite(site.getUrl().replaceFirst("www.", ""));
        this.siteUrl = siteUrl.replaceFirst(this.site.getUrl(), "");
        this.connectConfig = connectConfig;
        this.parsingPageService = parsingPageService;
    }

    @Override
    public void run() {
        parsingPageService.deleteOnePage(site, siteUrl);
        if(site == null){
            return;
        }
//        Site newSite = new Site(siteUrl, site.getName());
//        parsingPageService.deleteSite(site);
//        newSite.setStatus(TypeSiteIndexingStatus.INDEXING);
//        newSite = parsingPageService.saveSite(newSite);
//
//        ParsingPage parsingPage = new ParsingPage(parsingPageService, newSite, connectConfig);
//        new ForkJoinPool().invoke(parsingPage);
//
//        if(!parsingPageService.isSiteFailed(site)) {
//            newSite.setStatus(TypeSiteIndexingStatus.INDEXED);
//            parsingPageService.saveSite(newSite);
//        }
    }
}
