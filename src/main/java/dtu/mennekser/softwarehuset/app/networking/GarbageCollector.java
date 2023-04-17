package dtu.mennekser.softwarehuset.app.networking;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Thor
 */

public final class GarbageCollector {

    //Hvorfor mon der er en garbage collector klasse? Svaret er fordi java's indbygget ikke er god nok
    // så jeg (Thor) lavede vores egen.

    //Der er brug for den fordi der ligger subscribers i nogle javafx elementer, men når de elementer
    //bliver fjernet, skal der også fjernes alle de subscribers de bruger, hvilket ikke altid bliver
    //gjort lige godt.

    //Den måde garbage colleteren virker er at den har en liste af alle eksiterende subscribers,
    //En gang i mellem finder den alle javafx vinduer. Den rekurvist finder så alle javafx elementerne
    // som er i brug af vinduerne. Når den støder på et element som er et vi har skrevet (fordi
    // dens klasse starter med "dtu.mennekser") så bliver der ved brug af reflektion loopet over alle
    // dens variabler (felter), og hvis feltet er en instans af en DBSubscriber bliver den tagget.

    //Når den er færdig med det, dræber den alle de subscribers som ikke blev tagget.

    private static final HashMap<Integer, DataListener<?>> subscribers = new HashMap<>();
    private static GarbageCollector instance;
    public static void init() {
        if (instance == null) {
            instance = new GarbageCollector();
            new Thread(instance::run).start();
        } else {
            throw new RuntimeException("Garbage collector already running");
        }
    }

    static AtomicInteger nextID = new AtomicInteger(0);

    public static int addSubscriber(DataListener<?> subscriber) {
        int id = nextID.addAndGet(1);
        synchronized (subscribers) {
            subscribers.put(id,subscriber);
        }
        return id;
    }

    private void run() {
        while (true) {
            try {
                Thread.sleep(5000);

                Platform.runLater(() -> {
                    List<Window> windows = Window.getWindows();

                    for (Window window : windows) {
                        tagObject(window);
                        Scene scene = window.getScene();
                        tagObject(scene);
                        Pane root = (Pane) scene.getRoot();
                        recursiveTag(root);
                    }

                    synchronized (subscribers) {
                        ArrayList<Integer> toBeCollected = new ArrayList<>();

                        subscribers.forEach((id,subscriber) -> {
                            //System.out.println(id + ": " + subscriber.garbageTagged);
                            if (!subscriber.garbageTagged) {
                                toBeCollected.add(id);
                            }
                        });
                        for (Integer id : toBeCollected) {
                            DataListener<?> subscriber = subscribers.remove(id);
                            subscriber.kill();
                        }

                        subscribers.forEach((id,subscriber) -> {
                            subscriber.garbageTagged = false;
                        });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void recursiveTag(Pane pane) {
        //Loop over children first
        for (Object child : pane.getChildren()) {
            tagObject(child);

            if (child instanceof Pane childPane) {
                recursiveTag(childPane);
            }
        }
    }

    private static void tagObject(Object object) {
        if (object == null) {
            return;
        }

        if (!object.getClass().getName().startsWith("dtu")) {
            return;
        }
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value instanceof DataListener<?> subscriber) {
                    subscriber.garbageTagged = true;
                }
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
