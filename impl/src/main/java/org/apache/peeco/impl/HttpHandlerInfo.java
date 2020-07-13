package org.apache.peeco.impl;

import java.lang.reflect.Method;
import org.apache.peeco.api.HttpHandler;

public class HttpHandlerInfo
{
    Class clazz;
    Object bean;
    Method method;
    HttpHandler annotation;

    HttpHandlerInfo(Class clazz, Method method, HttpHandler annotation)
    {
        this.clazz = clazz;
        this.method = method;
        this.annotation = annotation;
    }
}
