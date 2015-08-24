/*
 * Copyright 2014 ptma@163.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joy.task;

import java.sql.Connection;

public class CloseConnectionTask extends Task {

    private Connection connection;

    public CloseConnectionTask(Connection connection){
        this.connection = connection;
    }

    @Override
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
