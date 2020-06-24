package org.apache.peeco.impl;

import org.apache.peeco.api.HttpMethod;
import org.apache.peeco.api.Matching;

public class AnnotationValues
{
    HttpMethod[] httpMethod;
    String url;
    Matching matching;

    public AnnotationValues(HttpMethod[] httpMethod, String url, Matching matching){
        this.httpMethod = httpMethod;
        this.url = url;
        this.matching = matching;
    }
}
