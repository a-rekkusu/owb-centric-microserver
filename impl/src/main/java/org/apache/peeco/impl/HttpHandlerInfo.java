package org.apache.peeco.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HttpHandlerInfo
{
    Class clazz;
    Method method;
    Annotation annotation;
    AnnotationValues values;

    HttpHandlerInfo(Class clazz, Method method, Annotation annotation, AnnotationValues values){
        this.clazz = clazz;
        this.method = method;
        this.annotation = annotation;
        this.values = values;
    }
}
