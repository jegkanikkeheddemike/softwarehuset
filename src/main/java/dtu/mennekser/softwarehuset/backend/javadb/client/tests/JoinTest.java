package dtu.mennekser.softwarehuset.backend.javadb.client.tests;

import dtu.mennekser.softwarehuset.backend.data.Join;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientTask;
import dtu.mennekser.softwarehuset.backend.tables.Task;
import dtu.mennekser.softwarehuset.backend.tables.Employee;

public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        ClientTask.SubmitTask("koebstoffer.info", tables -> {
            var uid1 = tables.users.insert(new Employee("Obama1", "Barack1"));
            var uid2 = tables.users.insert(new Employee("Obama2", "Barack2"));
            tables.users.insert(new Employee("Obama3", "Barack3"));
            tables.users.insert(new Employee("Obama4", "Barack4"));

            tables.tasks.insert(new Task(uid1));
            tables.tasks.insert(new Task(uid1));
            tables.tasks.insert(new Task(uid2));
        }, Throwable::printStackTrace);

        ClientSubscriber<Join<Task, Employee>[]> subscriber = new ClientSubscriber<Join<Task, Employee>[]>("koebstoffer.info",
                tables -> {
                    var stream = tables.users.joinOnId(
                            tables.tasks.stream(),
                            task -> task.assigned
                    );
                    return stream.toList().toArray((Join<Task, Employee>[]) new Join[0]);
                }, data -> {
            System.out.println("All tasks, and their assigned user");
            for (var join : data) {
                System.out.println(join);
            }
        }, Throwable::printStackTrace);

        Thread.sleep(1000);
        subscriber.kill();

        ClientSubscriber<Join<Employee, Task[]>[]> subscriber2 = new ClientSubscriber<Join<Employee, Task[]>[]>("koebstoffer.info",
                tables -> {
                    var stream = tables.tasks.joinWhere(
                            tables.users.stream(),
                            (user, task) -> task.assigned.equals(user.Id()),
                            new Task[0]
                    );
                    return stream.toList().toArray((Join<Employee, Task[]>[]) new Join[0]);
                }, data -> {
            System.out.println("All users and their assign tasks as arrays");
            for (var join : data) {
                System.out.println(join);
            }
            System.exit(0);
        }, Throwable::printStackTrace);
    }
}