package com.smartdengg.nullpredictor.error;

/**
 * 创建时间:  2017/04/04 18:46 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

class InnerError extends Error {

  private final StackTraceElement[] stackTrace;

  public InnerError(String message, Throwable cause, StackTraceElement[] stackTrace) {
    super(message, cause);
    this.stackTrace = stackTrace;
    this.fillInStackTrace();
  }

  @Override public synchronized Throwable fillInStackTrace() {
    if (stackTrace != null) setStackTrace(stackTrace);
    return this;
  }
}
