package main.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class  DefaultController {
    @GetMapping("${webinterface}")
    public String index(){
        return "Index";
    }
}
