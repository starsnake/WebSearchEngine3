package main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundSite extends RuntimeException{
    public NotFoundSite(int id) {
        super("Site with id=" + id + " not found");
    }

    public NotFoundSite(String url) {
        super("Site with url =" + url + " not found");
    }
}
