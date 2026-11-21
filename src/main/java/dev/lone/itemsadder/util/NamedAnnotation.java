package dev.lone.itemsadder.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface NamedAnnotation {
   String value() default "";
}
