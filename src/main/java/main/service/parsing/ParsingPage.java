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
    private final ConcurrentSkipListSet<String> listSet;
    private final ConnectConfig connectConfig;
    private final IParsingPageService parsingPageService;
    private final Site site;
    private final String url;
    private static HashMap<String, Float> listField;

    public ParsingPage(ConcurrentSkipListSet<String> listSet,
                       IParsingPageService parsingPageService,
                       Site site, String url,
                       ConnectConfig connectConfig) {
        this.listSet = listSet;
        this.parsingPageService = parsingPageService;
        this.connectConfig = connectConfig;
        this.site = site;
        this.url = url;
    }

    public ParsingPage(IParsingPageService parsingPageService,
                       Site site,
                       ConnectConfig connectConfig) {
        this.parsingPageService = parsingPageService;
        this.connectConfig = connectConfig;
        ParsingPage.listField = parsingPageService.getAllFields();
        this.url = "/";
        this.site = site;
        this.listSet = new ConcurrentSkipListSet<>();
        listSet.add("/");
    }

    @Override
    protected void compute() {
        if (url == null || parsingPageService.isSiteFailed(site)) {
            return;
        }
        Page page = savePage();
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
//            if(!parsingPageService.isExistingPage(site, str)){
//                ParsingPage task = new ParsingPage(listSet, parsingPageService,
//                        site, str, connectConfig);
//                tasks.add(task);
//                task.fork();
//            }
//
            if (listSet.add(str)) {
//                ParsingPage task = new ParsingPage(listSet, parsingPageService,
//                        site, str, connectConfig);
                        tasks.add(new ParsingPage(listSet, parsingPageService,
                                site, str, connectConfig));
//                        task.fork();
            }
        }
        invokeAll(tasks);
        for (ParsingPage pp : tasks){
            pp.join();
        }
    }

    public int getListSet(){
        return listSet.size();
    }
    private Page savePage(){
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
        Page page = new Page(site, url);
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
