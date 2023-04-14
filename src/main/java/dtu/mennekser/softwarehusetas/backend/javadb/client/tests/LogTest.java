package dtu.mennekser.softwarehusetas.backend.javadb.client.tests;

import dtu.mennekser.softwarehusetas.backend.javadb.client.ClientSubscriber;
import dtu.mennekser.softwarehusetas.backend.tables.Log;

public class LogTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Stuff");
        ClientSubscriber<Log[]> logSubscriber = new ClientSubscriber<>("koebstoffer.info", tables -> {
            return tables.logs.stream().toList().toArray(new Log[0]);
        }, logs -> {
            for (Log log : logs) {
                System.out.println(log);
            }
        }, Throwable::printStackTrace);

        //Thread.sleep(1000);

        //logSubscriber.kill();
    }
}
