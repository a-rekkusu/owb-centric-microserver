package api;

import lombok.Getter;
import lombok.Setter;

public class Header
{
    @Getter
    @Setter
    private int statusCode;
    private String characterEncoding;
    private String contentType;
    private int contentLength;
}