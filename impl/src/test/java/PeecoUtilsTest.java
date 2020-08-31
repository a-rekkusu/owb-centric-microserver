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
import org.apache.peeco.api.Matching;
import org.apache.peeco.impl.PeecoUtils;
import org.junit.Assert;
import org.junit.Test;

public class PeecoUtilsTest
{
    @Test
    public void testGetMatchingHandlerWildcard()
    {
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello3/*", "/hello3/id=2342"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello/*", "/hello/world/?lang=en-en"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello/*", "/hello/?lang=de-de"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello3/world/*", "/hello3/world/?id=2342"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "/hello/world*", "/hello/world/?lang=de-de"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.WILDCARD, "*.jpg", "/hello/blub.jpg?lang=de-de"));
    }

    @Test
    public void testGetMatchingHandlerExact(){
        Assert.assertTrue(PeecoUtils.isMatching(Matching.EXACT, "/hello/", "/hello/?lang=de-de"));
        Assert.assertTrue(PeecoUtils.isMatching(Matching.EXACT, "/hello", "/hello?lang=de-de"));
        Assert.assertFalse(PeecoUtils.isMatching(Matching.EXACT, "/hello3", "/hello?lang=de-de"));
        Assert.assertFalse(PeecoUtils.isMatching(Matching.EXACT, "/hello3/", "/hello?lang=de-de"));
    }
}