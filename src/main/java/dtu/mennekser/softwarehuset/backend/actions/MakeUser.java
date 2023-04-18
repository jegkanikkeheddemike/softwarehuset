package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;

import java.util.Scanner;

public class MakeUser {
    public static void main(String[] args) {
        String username = new Scanner(System.in).nextLine();
        new ClientTask<Database>(
                database -> database.createEmployee(username),
                Throwable::printStackTrace
        );
    }
}
