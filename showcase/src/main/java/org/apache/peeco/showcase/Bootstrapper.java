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
package org.apache.peeco.showcase;

import org.apache.webbeans.service.ClassLoaderProxyService;
import org.apache.webbeans.spi.DefiningClassService;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class Bootstrapper
{
    public static void main(String[] args) throws Exception
    {
        try (final SeContainer container = SeContainerInitializer.newInstance()
                .addProperty(DefiningClassService.class.getName(), ClassLoaderProxyService.LoadFirst.class.getName())
                .addProperty("org.apache.webbeans.proxy.useStaticNames", "true")
                .initialize())
        {
            HelloWorldHandler handler = container.select(HelloWorldHandler.class).get();
            System.out.println(handler);
        }
    }
}
