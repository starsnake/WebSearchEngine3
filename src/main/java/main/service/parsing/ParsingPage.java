package main.service.parsing;

import main.config.ConnectConfig;
import main.model.Page;
import main.model.Site;
import main.service.IParsingPageService;
import main.service.morphology.RuMorphology;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class ParsingPage extends RecursiveAction {
    private final ConnectConfig connectConfig;
    private final IParsingPageService parsingPageService;
    private final Site site;
    private final String url;
    private static HashMap<String, Float> listField;

    public ParsingPage(IParsingPageService parsingPageService,
                       Page page,
                       ConnectConfig connectConfig) {
        if(ParsingPage.listField == null) {
            ParsingPage.listField = parsingPageService.getAllFields();
        }
        this.parsingPageService = parsingPageService;
        this.connectConfig = connectConfig;
        this.site = page.getSite();
        this.url = page.getPath();
    }

    @Override
    protected void compute() {
        if (url == null || parsingPageService.isSiteFailed(site)) {
            return;
        }
        Page page = savePage(parsingPageService.findPageByPathAndSite(url, site));
        if(page == null || page.getCode() != 200){
            return;
        }
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Document doc = Jsoup.parse(page.getContent());
        HashMap<String, Float> ranks = getRankLems(doc, listField);
        parsingPageService.saveParsing(page, site, ranks);
        Elements elements = doc.body().select("a[href]");
        doc.setBaseUri(site.getUrl());
        List<ParsingPage> tasks = new ArrayList<>();
        for (Element e : elements){
            String str = e.attr("abs:href").replaceAll("#.*$", "");
            if(!str.startsWith(site.getUrl())){ // || str.matches(getNegativePattern)){
                continue;
            }
            str = str.replaceFirst(site.getUrl(), "");
//            if(!parsingPageService.isExistingPage(site, str) && str.length() <= 500){
            Page nPage = parsingPageService.newPage(str, site);
            if(nPage != null){
                ParsingPage task = new ParsingPage(parsingPageService, //listSet, parsingPageService, site, str
                        nPage, connectConfig);
                tasks.add(task);
                task.fork();
            }
        }
        for (ParsingPage pp : tasks){
            pp.join();
        }
    }

    private Page savePage(Page page){
        if(page == null) {
            return null;
        }
        Connection con = getConnect();
        Document doc = null;
        int statusCode = 200;
        try {
            doc = con.get();
        }
        catch (UnsupportedMimeTypeException e){
            return null;
        }
        catch (HttpStatusException e){
            statusCode = e.getStatusCode();
        }
        catch (IOException e) {
            parsingPageService.saveErrorParsing(site,
                    "An IOException occurred in metod - connection.get() at link " + url + ". Error message: " + e.getMessage());
            System.out.println("IOException: get() - " + url + " error - " + e.getMessage());
            return null;
        }
        if(doc != null) {
            Connection.Response resp = doc.connection().response();
            if (!Objects.requireNonNull(resp.contentType()).startsWith("text")) {
                return null;
            }
        }
//        Page page = new Page(site, url);
        page.setCode(statusCode);
        if(statusCode != 200){
            page.setContent("");
        }
        else {
            page.setContent(Objects.requireNonNull(doc).html());
        }
        return parsingPageService.savePage(page);
    }

    private Connection getConnect(){
        return Jsoup.connect(site.getUrl()
                        .concat(url))
                .userAgent(connectConfig.getUseragent())
                .referrer(connectConfig.getReferrer());
    }

    private HashMap<String, Float> getRankLems(String text, float fLem){
        HashMap<String, Float> rankLems = new HashMap<>();
        HashMap<String, Integer> cntLems;
        try {
            cntLems = RuMorphology.getLem(text);
            Set<String> keys = cntLems.keySet();
            for(String str : keys){
                rankLems.put(str, cntLems.get(str) * fLem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rankLems;
    }

    private HashMap<String, Float> getRankLems(Document doc, HashMap<String, Float> listField){
        Set<String> keys = listField.keySet();
        HashMap<String, Float> rankLemmas = new HashMap<>();
        for(String str : keys){
            HashMap<String, Float> rankItems = getRankLems(doc.select(str).text(), listField.get(str));
            rankItems.forEach((key, value)->rankLemmas
                    .merge(key, value, Float::sum));
        }
        return rankLemmas;
    }
}
