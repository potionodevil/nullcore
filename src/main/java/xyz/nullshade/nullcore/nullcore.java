package xyz.nullshade.nullcore;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.sisu.launch.InjectedTestCase;
import xyz.nullshade.nullcore.annotation.AnnotationProcessor;
import xyz.nullshade.nullcore.sql.provider.ConnectionProvider;

public class nullcore extends JavaPlugin {
    private Injector injector;
    @Inject
    private AnnotationProcessor annotationProcessor;
    private ConnectionProvider provider;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.injector = CoreModule.createInjector(this);
        provider = injector.getInstance(ConnectionProvider.class);
        annotationProcessor.register();
    }

    @Override
    public void onDisable() {
        if (provider != null) {
            provider.closeAll();
        }
        getLogger().info("NullCore wurde deaktiviert!");
    }

    public Injector injector() {
        return injector;
    }
}
