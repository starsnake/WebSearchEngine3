package main.controllers;

import main.config.ConnectConfig;
import main.model.Site;
import main.config.SiteConfig;
import main.response.Response;
import main.response.ResponseFalse;
import main.response.ResponseTrue;
import main.service.*;
import main.service.parsing.StartParsing;
import main.service.parsing.StartParsingOnePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashMap;
import java.util.List;

@Controller
public class ManagementController {
    @Autowired
    private IParsingPageService parsingPageService;
    @Autowired
    private ConnectConfig connectConfig;
    @Autowired
    private SiteConfig siteConfig;

    private StartParsing startParsing;
    private StartParsingOnePage startParsingOnePage;

    @GetMapping("/startIndexing")
    public ResponseEntity<Response> startIndexing(){
        List<Site> sites = siteConfig.getSites();
        if(sites.isEmpty()){
            return ResponseEntity.badRequest().body(new ResponseFalse("Пустой список сайтов в файле application.yaml"));
        }
        if(parsingPageService.isIndexing()) {
            return ResponseEntity.badRequest().body(new ResponseFalse("Индексация уже запущена"));
        }
        startParsing = new StartParsing(connectConfig, parsingPageService);
        new Thread(startParsing).start();
        return ResponseEntity.ok().body(new ResponseTrue());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<Response> stopIndexing(){
        if(!parsingPageService.isIndexing()){
            return ResponseEntity.badRequest().body(new ResponseFalse("Индексация не запущена"));
        }
        startParsing.stopIndexing();
        return ResponseEntity.ok().body(new ResponseTrue());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<Response> indexPage(@RequestParam String url){
        HashMap<String, Object> response = new HashMap<String, Object>();
        if(parsingPageService.isIndexing()) {
            return ResponseEntity.badRequest().body(new ResponseFalse("Индексация уже запущена"));
        }
        for(Site site : siteConfig.getSites()){
            url = url.toLowerCase().replaceFirst("www.", "");
            if(url.contains(site.getUrl().replaceFirst("www.", ""))){
                startParsingOnePage = new StartParsingOnePage(site, url, connectConfig, parsingPageService);
                new Thread(startParsingOnePage).start();
                return ResponseEntity.ok().body(new ResponseTrue());
            }
        }
        return ResponseEntity.badRequest()
                .body(new ResponseFalse("Данная страница находится за пределами сайтов, указаных в конфигурационном файле."));
    }

}
