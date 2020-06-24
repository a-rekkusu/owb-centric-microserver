package org.apache.peeco.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HttpHandlerInfo
{
    Class clazz;
    Method method;
    Annotation annotation;

    HttpHandlerInfo(Class clazz, Method method, Annotation annotation){
        this.clazz = clazz;
        this.method = method;
        this.annotation = annotation;
    }
}
