package xyz.nullshade.nullcore.annotation;

import org.bukkit.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
    boolean ignoreCancelled() default false;
    EventPriority priority() default EventPriority.NORMAL;
}
