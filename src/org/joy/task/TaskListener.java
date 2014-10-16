package org.joy.task;

public interface TaskListener {
  /**
   * Called when the observed task has finished.
   */
  public void taskFinished();

  /**
   * Called when the observed task reports its status.
   */
  public void taskStatus(Object obj);

  /**
   * Called when the observed task reports its result.
   */
  public void taskResult(Object obj);

  /**
   * Called when the observed task throws an exception.
   */
  public void taskError(Exception e);
}
