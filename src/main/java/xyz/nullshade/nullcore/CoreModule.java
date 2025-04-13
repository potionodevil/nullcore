package xyz.nullshade.nullcore;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nullshade.nullcore.annotation.AnnotationProcessor;
import xyz.nullshade.nullcore.sql.DatabaseModule;

public class CoreModule extends AbstractModule {

    private final JavaPlugin javaPlugin;

    public CoreModule(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(javaPlugin);
        bind(AnnotationProcessor.class).asEagerSingleton();

        install(new DatabaseModule());
    }

    public static Injector createInjector(JavaPlugin plugin) {
        Injector injector = Guice.createInjector(new CoreModule(plugin));
        AnnotationProcessor annotationProcessor = injector.getInstance(AnnotationProcessor.class);
        annotationProcessor.register();
        return injector;
    }
}
