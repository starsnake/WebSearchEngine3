package main.controllers;

import main.model.*;
import main.model.search.SearchResult;
import main.response.Response;
import main.response.ResponseFalse;
import main.service.IParsingPageService;
import main.service.morphology.RuMorphology;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class SearchController {
    @Autowired
    private IParsingPageService parsingPageService;

    @GetMapping(value = "/search")
    public ResponseEntity<Response> search(@RequestParam(value = "site", defaultValue = "") String siteUrl,
                                           @RequestParam("query") String query,
                                           @RequestParam(value = "offset", defaultValue = "0") int offset,
                                           @RequestParam(value = "limit", defaultValue = "20") int limit) {
        if(query.equals("")){
            return ResponseEntity.badRequest().body(new ResponseFalse("Задан пустой поисковый запрос"));
        }
        List<String> cntLems = RuMorphology.getsearchLem(query);
        List<Lemma> lemmaList = parsingPageService.getLemma(siteUrl, cntLems);
        //Проверяем количество лем в запросе с количеством найденных лем в базе
//        if(cntLems.size() != lemmaList.size()){
//            System.out.println("По запросу " + query + " ничего не найдено. Скорректированный запрос: "
//                    + RuMorphology.getCorrectQuery(query, lemmaList)); // Отобразить уточненный запрос
//        }
        Set<String> lemmas = new HashSet<>();
        for(Lemma lemma : lemmaList){
            lemmas.add(lemma.getLemma());
        }
        List<String> list = lemmas.stream().toList();
        long start = System.currentTimeMillis();
        List<Search> searchList =parsingPageService.searchPage(list, list.size(), siteUrl, limit, offset);
//        System.out.println(System.currentTimeMillis() - start);
        SearchResult searchResult = new SearchResult(parsingPageService.countSearchPage(list, list.size(), siteUrl));
        for(Search search : searchList){
            Page page = parsingPageService.getPageById(search.getId());
            searchResult.addData(page.getSite().getUrl(),
                    page.getSite().getName(),
                    page.getPath(),
                    Jsoup.parse(page.getContent()).title(),
                    "Фрагмент текста," +
                            " в котором найдены" +
                            " совпадения, <b>выделенные" +
                            " жирным</b>, в формате HTML",
                    search.getSum_rank());
        }
        return ResponseEntity.ok().body(searchResult);
    }
//    HashMap<String,>
}
