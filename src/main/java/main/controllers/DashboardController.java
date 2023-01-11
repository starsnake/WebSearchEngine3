package main.controllers;

import main.response.ResponseStatistics;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @Autowired
    private IParsingPageService parsingService;

    @GetMapping("/statistics")
    public ResponseEntity<ResponseStatistics> getStatistics() {
        return ResponseEntity.ok().body(new ResponseStatistics(parsingService));
    }

}
