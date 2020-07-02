package org.apache.peeco.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD})
public @interface HttpHandler
{
    String url();

    HttpMethod[] method();

    Matching matching();
}
