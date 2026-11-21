package dev.lone.itemsadder.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface NamedAnnotationDup2 {
   String value() default "";
}
