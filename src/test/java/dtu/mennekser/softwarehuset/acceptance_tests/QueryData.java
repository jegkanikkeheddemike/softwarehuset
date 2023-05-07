package dtu.mennekser.softwarehuset.acceptance_tests;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Project;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientQuery;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class QueryData {
    public QueryData(){}
    /**
     * @Author Thor
     */
    Project data;
    @When("a query is made")
    public void aQueryIsMade() {
        data = new ClientQuery<AppBackend,Project>(appBackend -> appBackend.findProject("jens"),Throwable::printStackTrace).fetch();

    }

    @Then("nothing is returned")
    public void nothingIsReturned() {
        Assertions.assertNull(data);
    }

    @Then("the data is returned")
    public void theDataIsReturned() {
        Assertions.assertNotNull(data);
    }

    @Then("relevant data is inserted into the server")
    public void relevantDataIsInsertedIntoTheServer() throws InterruptedException {
        new ClientTask<AppBackend>(appBackend -> appBackend.createEmployee("karsten"),Throwable::printStackTrace);
        Thread.sleep(100);
        LoginManager.attemptLogin("kars");
        Session session = LoginManager.getCurrentSession();
        new ClientTask<AppBackend>(appBackend -> appBackend.createProject("jens","",session,"1"),Throwable::printStackTrace);
    }
}
