package xyz.nullshade.nullcore.annotation;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class AnnotationProcessor {

    private final JavaPlugin javaPlugin;

    @Inject
    public AnnotationProcessor(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void register() {
        registerCommands();
        registerListeners();
    }


    private void registerCommands() {
        Reflections reflections = new Reflections("xyz.nullshade.nullcore");
        Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(Command.class);
        for (Class<?> commandClass : commandClasses) {
            try {
                Command commandAnnotation = commandClass.getAnnotation(Command.class);
                String commandName = commandAnnotation.name();
                CommandExecutor commandExecutor = (CommandExecutor) commandClass.getDeclaredConstructor().newInstance();
                javaPlugin.getCommand(commandName).setExecutor(commandExecutor);
                if (commandAnnotation.aliases().length > 0) {
                    javaPlugin.getCommand(commandName).setAliases(java.util.Arrays.asList(commandAnnotation.aliases()));
                }
                javaPlugin.getLogger().info("Command " + commandName + " wurde registriert!");
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                javaPlugin.getLogger().severe("Fehler beim Registrieren des Commands: " + commandClass.getName());
                e.printStackTrace();
            }
        }
    }

    private void registerListeners() {
        Reflections reflections = new Reflections("com.yourplugin.listeners");
        Set<Class<?>> listenerClasses = reflections.getTypesAnnotatedWith(Event.class);
        for (Class<?> listenerClass : listenerClasses) {
            try {
                Listener listener = (Listener) listenerClass.getDeclaredConstructor().newInstance();
                Bukkit.getPluginManager().registerEvents(listener, javaPlugin);
                javaPlugin.getLogger().info("Listener " + listenerClass.getSimpleName() + " wurde registriert!");
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                javaPlugin.getLogger().severe("Fehler beim Registrieren des Listeners: " + listenerClass.getName());
                e.printStackTrace();
            }
        }
    }

}
