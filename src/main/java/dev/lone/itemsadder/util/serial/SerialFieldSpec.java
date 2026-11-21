package dev.lone.itemsadder.util.serial;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerialFieldSpec {
   int fieldNumber();

   String name() default "";

   int afx() default 0;
}
