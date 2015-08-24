package org.joy.task;

import java.util.ArrayList;
import java.util.List;

public abstract class Task implements Runnable {

    private boolean            finished  = false;
    private Object             status    = null;
    private Object             result    = null;
    private Exception          error     = null;
    private boolean            cancelled = false;
    private List<TaskListener> listeners = new ArrayList<TaskListener>();

    public synchronized void addTaskListener(TaskListener listener) {
        List<TaskListener> newListeners = new ArrayList<TaskListener>(listeners);
        newListeners.add(listener);
        listeners = newListeners;
    }

    public synchronized void removeTaskListener(TaskListener listener) {
        List<TaskListener> newListeners = new ArrayList<TaskListener>(listeners);
        newListeners.remove(listener);
        listeners = newListeners;
    }

    protected synchronized void reportFinished() {
        finished = true;
        for (TaskListener listener : listeners) {
            listener.taskFinished();
        }
    }

    protected synchronized void reportStatus(Object obj) {
        status = obj;
        for (TaskListener listener : listeners) {
            listener.taskStatus(obj);
        }
    }

    protected synchronized void reportResult(Object obj) {
        result = obj;
        for (TaskListener listener : listeners){
            listener.taskResult(obj);
        }
    }

    protected synchronized void reportError(Exception e) {
        error = e;
        for (TaskListener listener : listeners){
            listener.taskError(e);
        }
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public synchronized Object getStatus() {
        return status;
    }

    public synchronized boolean hasResult() {
        return result != null;
    }

    public synchronized Object getResult() {
        return result;
    }

    public synchronized boolean hasError() {
        return error != null;
    }

    public synchronized Exception getError() {
        return error;
    }

    public synchronized void cancel() {
        cancelled = true;
    }

    public synchronized boolean isCancelled() {
        return cancelled;
    }
}
