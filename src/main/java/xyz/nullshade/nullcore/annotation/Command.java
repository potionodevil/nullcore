package xyz.nullshade.nullcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();
    String desc() default "";
    String[] aliases() default {};
    String usage() default "";
    String permission() default "";

}
