package dtu.mennekser.softwarehuset.backend.javadb.client.tests;

import dtu.mennekser.softwarehuset.backend.javadb.client.ClientTask;
import dtu.mennekser.softwarehuset.backend.tables.User;

import java.util.Scanner;

public class TaskTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        var uname = scanner.nextLine();
        var realName = scanner.nextLine();

        User myUser = new User(uname,realName);
        ClientTask.SubmitTask("koebstoffer.info",tables -> {
            tables.users.insert(myUser);
        },Throwable::printStackTrace);
    }
}
