package main.response;

import lombok.Data;

@Data
public class ResponseFalse implements Response {
    private boolean result;
    private String error;

    public ResponseFalse(String error) {
        this.result = false;
        this.error = error;
    }
}
