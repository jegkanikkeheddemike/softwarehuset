package dtu.mennekser.softwarehuset.acceptance_tests;

import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;
import dtu.mennekser.softwarehuset.backend.streamDB.SocketLayer;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientSettings;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class StartLocalServer {
    public StartLocalServer() {
    }
    /**
     * @Author Thor
     */
    @Given("the remote_location is set as localhost")
    public void theRemote_locationIsSetAsLocalhost() {
        ClientSettings.remoteLocation = "localhost";
        ClientSettings.port = new Random().nextInt(8000,15000);
    }

    @And("a server is running")
    public void aLocalServerIsRunning() {
        // Hvis der allerede kører en server fordi der LIGE er blevet kørt tests,
        // vil den her fejle med at lave en ny server
        // Den har brug for at sende det to gange på grund af en implementations detalje.
        try {
            new ClientTask<>(dataLayer -> dataLayer.shutdown = true, error -> {});
            Thread.sleep(100);
            new ClientTask<>(dataLayer -> dataLayer.shutdown = true, error -> {});
            Thread.sleep(100);
        } catch (Exception ignored){}

        new SocketLayer<>(false, new AppBackend()).start();
    }

    @Then("a connection can be made to the server")
    public void aConnectionCanBeMadeToTheServer() throws IOException {
        Socket connection = new Socket(ClientSettings.remoteLocation, ClientSettings.port);
        connection.close();
    }
}
