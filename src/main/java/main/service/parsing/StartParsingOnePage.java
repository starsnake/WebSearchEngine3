package main.service.parsing;

import main.config.ConnectConfig;
import main.model.Page;
import main.model.Site;
import main.service.IParsingPageService;
import main.tools.Tools;

import java.util.Date;
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
        long start = System.currentTimeMillis();
        System.out.println("Start parsing sites " + new Date());
        parsingPageService.deleteOnePage(site, siteUrl);
        System.out.println("Duration of deleting: " + Tools.getTime((System.currentTimeMillis() - start) / 1000));
        start = System.currentTimeMillis();
        Page page = parsingPageService.newPage(siteUrl, site);
        ParsingPage parsingPage = new ParsingPage(parsingPageService, page, connectConfig);
        new ForkJoinPool().invoke(parsingPage);
        System.out.println("Duration of processing site " + site.getUrl() + ": " + Tools.getTime((System.currentTimeMillis() - start) / 1000));
        System.out.println("Links - " + parsingPageService.countPageBySite(site));
    }
}
