package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.javadb.client.ClientTask;

import java.util.Scanner;

public class MakeUser {
    public static void main(String[] args) {
        String username = new Scanner(System.in).nextLine();
        ClientTask task = new ClientTask(
                "koebstoffer.info",
                database -> database.createEmployee(username),
                Throwable::printStackTrace
        );
    }
}
