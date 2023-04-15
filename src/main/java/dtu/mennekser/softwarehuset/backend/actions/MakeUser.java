package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.javadb.client.ClientTask;

import java.util.Scanner;

public class MakeUser {
    public static void main(String[] args) {
        String username = new Scanner(System.in).nextLine();
        ClientTask.SubmitTask(
                database -> database.createEmployee(username),
                Throwable::printStackTrace
        );
    }
}
