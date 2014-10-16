package org.joy.task;

import java.sql.Connection;

public class CloseConnectionTask extends Task {

    private Connection connection;

    public CloseConnectionTask(Connection connection){
        this.connection = connection;
    }

    public void run() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            reportError(e);
        } finally {
            reportFinished();
        }
    }
}
