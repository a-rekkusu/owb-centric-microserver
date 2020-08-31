/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.peeco.api;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Request
{
    private HttpMethod httpMethod;
    private String url;
    private InputStream inputStream;
    private Map<String, List<String>> headers;
    private Map<String, List<String>> bodyParameters;
    private Map<String, List<String>> queryParameters;

    public Request(HttpMethod httpMethod, String url, InputStream stream)
    {
        this.httpMethod = httpMethod;
        this.url = url;
        this.inputStream = stream;
        this.headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.bodyParameters = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.queryParameters = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public HttpMethod httpMethod()
    {
        return httpMethod;
    }

    public String url()
    {
        return url;
    }

    public InputStream inputStream()
    {
        return inputStream;
    }

    public Map<String, List<String>> headers()
    {
        return headers;
    }

    public Map<String, List<String>> parameters()
    {
        Map<String, List<String>> parameters = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        parameters.putAll(queryParameters());

        bodyParameters().forEach((s, strings) ->
                parameters.merge(s, strings, (strings1, strings2) ->
                {
                    strings1.addAll(strings2);
                    return strings1;
                }));

        return parameters;
    }

    public Map<String, List<String>> bodyParameters()
    {
        return bodyParameters;
    }

    public Map<String, List<String>> queryParameters()
    {
        return queryParameters;
    }
}