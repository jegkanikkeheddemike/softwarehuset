package dtu.mennekser.softwarehuset.backend.javadb.client.tests;

import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import dtu.mennekser.softwarehuset.backend.tables.Employee;

public class SubscribeTest {
    public static void main(String[] args) throws InterruptedException {
        ClientSubscriber<Employee[]> usersSubscriber = new ClientSubscriber<>("koebstoffer.info", tables -> {
            return tables.users.stream().toList().toArray(new Employee[0]);
        }, users -> {
            System.out.println("Updated users:");
            for (Employee user : users) {
                System.out.println(user);
            }
        },error -> {
            System.out.println("Failed to receive users: " + error.getMessage());
        });
    }
}
