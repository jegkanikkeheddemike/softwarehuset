package dtu.mennekser.softwarehuset.acceptance_tests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.SocketLayer;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientSettings;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.net.Socket;

public class StartLocalServer {
    public StartLocalServer() {}

    @Given("the remote_location is set as localhost")
    public void theRemote_locationIsSetAsLocalhost() {
        ClientSettings.remoteLocation = "localhost";
    }

    @And("a server is running")
    public void aLocalServerIsRunning() {
        //Tjek om der allerede er en server
        try {
            //Hvis der allerede er en server skal den ikke forbinde
            Socket s = new Socket(ClientSettings.remoteLocation,7009);
            s.close();
        } catch (Exception e) {
            new SocketLayer<>(false, new AppBackend()).start();
        }


    }

    @Then("a connection can be made to the server")
    public void aConnectionCanBeMadeToTheServer() throws IOException {
        Socket connection = new Socket(ClientSettings.remoteLocation, 7009);
        connection.close();
    }
}
