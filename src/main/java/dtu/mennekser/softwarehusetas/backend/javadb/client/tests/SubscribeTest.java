package dtu.mennekser.softwarehusetas.backend.javadb.client.tests;

import dtu.mennekser.softwarehusetas.backend.javadb.client.ClientSubscriber;
import dtu.mennekser.softwarehusetas.backend.tables.User;

public class SubscribeTest {
    public static void main(String[] args) throws InterruptedException {
        ClientSubscriber<User[]> usersSubscriber = new ClientSubscriber<>("koebstoffer.info", tables -> {
            return tables.users.stream().toList().toArray(new User[0]);
        }, users -> {
            System.out.println("Updated users:");
            for (User user : users) {
                System.out.println(user);
            }
        },error -> {
            System.out.println("Failed to receive users: " + error.getMessage());
        });
    }
}
