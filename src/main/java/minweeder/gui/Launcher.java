package minweeder.gui;

import javafx.application.Application;

/**
 * A launcher class whose only job is to invoke {@link Main} via
 * {@link Application#launch}, rather than launching {@link Main} directly.
 * This works around a classpath issue that occurs when a JavaFX
 * {@code Application} subclass is used as the entry point of a shadow/fat jar.
 */
public class Launcher {
    /**
     * Starts the JavaFX application.
     *
     * @param args unused command-line arguments.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
