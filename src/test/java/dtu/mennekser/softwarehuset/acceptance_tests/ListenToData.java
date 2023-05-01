package dtu.mennekser.softwarehuset.acceptance_tests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientListener;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ListenToData {
    public ListenToData() {
    }

    ClientListener<AppBackend, ArrayList<Employee>> listener;
    LinkedBlockingQueue<ArrayList<Employee>> queue = new LinkedBlockingQueue<>();

    ArrayList<Employee> oldData;
    ArrayList<Employee> newData;

    @And("a client is listening to data")
    public void aClientIsListeningToData() throws InterruptedException {
        listener = new ClientListener<>(
                AppBackend::getEmployees,
                employees -> {
                    try {
                        queue.put(employees);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, Throwable::printStackTrace
        );
        oldData = queue.take();
    }

    @When("data is inserted into to server")
    public void dataIsInsertedIntoToServer() {
        new ClientTask<AppBackend>(dataLayer -> dataLayer.createEmployee("jens karsten"), Throwable::printStackTrace);
    }

    @Then("the client receives the data")
    public void theClientReceivesTheData() throws InterruptedException {
        newData = queue.take();
    }

    @And("the old data is not the same as the new data")
    public void theOldDataIsNotTheSameAsTheNewData() {
        Assertions.assertNotEquals(oldData,newData);
    }
}
