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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

public class Response
{
    private InputStream output;
    private Map<String, List<String>> headers;

    public Response()
    {
        this.output = new ByteArrayInputStream(new byte[0]);
        this.headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public Response(InputStream output)
    {
        this.output = output;
        this.headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public Map<String, List<String>> headers()
    {
        return headers;
    }

    public void addHeader(String name, String value)
    {
        headers().computeIfAbsent(name, k -> new ArrayList<>())
                .add(value);
    }

    public InputStream output()
    {
        return output;
    }

    public void setOutput(InputStream stream)
    {
        this.output = stream;
    }

    public void setOutput(String output)
    {
        setOutput(new ByteArrayInputStream(output.getBytes()));
    }
}