package dev.lone.itemsadder.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface BehaviorFlags {
   boolean acH() default true;

   boolean acI() default true;

   boolean acJ() default true;
}
