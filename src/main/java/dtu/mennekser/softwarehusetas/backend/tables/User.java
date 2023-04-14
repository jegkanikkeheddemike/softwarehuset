package dtu.mennekser.softwarehusetas.backend.tables;

import dtu.mennekser.softwarehusetas.backend.data.TableData;
import dtu.mennekser.softwarehusetas.backend.data.TableID;

public final class User extends TableData<User> {
    public String username;
    public String realName;

    public User(String username, String realName) {
        this.username = username;
        this.realName = realName;
    }
}
