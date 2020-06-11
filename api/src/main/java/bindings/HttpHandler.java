package bindings;

import enums.Matching;
import enums.Method;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target({TYPE})
public @interface HttpHandler
{
    String url();
    Method[] method();
    Matching[] matching();
}
