package dtu.mennekser.softwarehuset.backend.tables;

import dtu.mennekser.softwarehuset.backend.data.TableData;

import java.util.Date;

public class Log extends TableData<Log> {
    final LogLevel logLevel;
    final Date date;
    final String message;

    public Log(LogLevel logLevel, String message) {
        this.logLevel = logLevel;
        this.message = message;
        this.date = new Date();
    }
    public enum LogLevel {
        INFO,
        WARNING,
        ERROR,
        FATAL
    }

    @Override
    public String toString() {
        return date + ": " + logLevel + " " + message;
    }
}
