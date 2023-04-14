package dtu.mennekser.softwarehusetas.backend.javadb.client.tests;

import dtu.mennekser.softwarehusetas.backend.data.Join;
import dtu.mennekser.softwarehusetas.backend.javadb.client.ClientSubscriber;
import dtu.mennekser.softwarehusetas.backend.javadb.client.ClientTask;
import dtu.mennekser.softwarehusetas.backend.tables.Task;
import dtu.mennekser.softwarehusetas.backend.tables.User;

public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        ClientTask.SubmitTask("koebstoffer.info", tables -> {
            var uid1 = tables.users.insert(new User("Obama1", "Barack1"));
            var uid2 = tables.users.insert(new User("Obama2", "Barack2"));
            tables.users.insert(new User("Obama3", "Barack3"));
            tables.users.insert(new User("Obama4", "Barack4"));

            tables.tasks.insert(new Task(uid1));
            tables.tasks.insert(new Task(uid1));
            tables.tasks.insert(new Task(uid2));
        }, Throwable::printStackTrace);

        ClientSubscriber<Join<Task, User>[]> subscriber = new ClientSubscriber<Join<Task, User>[]>("koebstoffer.info",
                tables -> {
                    var stream = tables.users.joinOnId(
                            tables.tasks.stream(),
                            task -> task.assigned
                    );
                    return stream.toList().toArray((Join<Task,User>[]) new Join[0]);
                }, data -> {
            System.out.println("All tasks, and their assigned user");
            for (var join : data) {
                System.out.println(join);
            }
        }, Throwable::printStackTrace);

        Thread.sleep(1000);
        subscriber.kill();

        ClientSubscriber<Join<User, Task[]>[]> subscriber2 = new ClientSubscriber<Join<User, Task[]>[]>("koebstoffer.info",
                tables -> {
                    var stream = tables.tasks.joinWhere(
                            tables.users.stream(),
                            (user, task) -> task.assigned.equals(user.Id()),
                            new Task[0]
                    );
                    return stream.toList().toArray((Join<User, Task[]>[]) new Join[0]);
                }, data -> {
            System.out.println("All users and their assign tasks as arrays");
            for (var join : data) {
                System.out.println(join);
            }
            System.exit(0);
        }, Throwable::printStackTrace);
    }
}