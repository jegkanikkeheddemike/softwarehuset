package dtu.mennekser.softwarehuset.backend.tables;

import dtu.mennekser.softwarehuset.backend.data.TableData;
import dtu.mennekser.softwarehuset.backend.data.TableID;

public final class User extends TableData<User> {
    public String username;
    public String realName;

    public User(String username, String realName) {
        this.username = username;
        this.realName = realName;
    }
}
