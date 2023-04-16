package dtu.mennekser.softwarehuset.app.networking;

import javafx.animation.KeyValue;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class GarbageCollector {
    private static final HashMap<Integer,DBSubscriber<?>> subscribers = new HashMap<>();
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

    public static int addSubscriber(DBSubscriber<?> subscriber) {
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
                            DBSubscriber<?> subscriber = subscribers.remove(id);
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
                if (value instanceof DBSubscriber<?> subscriber) {
                    subscriber.garbageTagged = true;
                }
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
