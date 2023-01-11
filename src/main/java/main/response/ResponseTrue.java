package main.response;

import lombok.Data;
@Data
public class ResponseTrue implements Response{
    private boolean result = true;
}
