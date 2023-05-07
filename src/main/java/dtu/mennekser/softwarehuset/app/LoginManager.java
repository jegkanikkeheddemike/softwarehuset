package dtu.mennekser.softwarehuset.app;

import dtu.mennekser.softwarehuset.app.networking.OnceQuery;
import dtu.mennekser.softwarehuset.backend.schema.Session;
/**
 * @Author Tobias
 */
public class LoginManager {
    private static Session currentSession;

    public static void attemptLogin(String username) {
        assert currentSession == null;
        currentSession = new OnceQuery<>(backend -> backend.attemptLogin(username)).fetch();
    }

    public static Session getCurrentSession() {
        if (currentSession == null) {
            throw new RuntimeException("Not logged in");
        } else {
            return currentSession.cloneSession();
        }
    }
    public static void logout() {
        assert currentSession != null;
        currentSession = null;
    }
    private LoginManager() {
        throw new RuntimeException("An instance of LoginManager cannot be created");
    }
}
