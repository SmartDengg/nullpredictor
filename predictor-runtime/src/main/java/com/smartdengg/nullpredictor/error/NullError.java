package com.smartdengg.nullpredictor.error;

/**
 * 创建时间:  2017/04/04 15:02 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class NullError extends InnerError {

  public NullError(String message, Throwable cause, StackTraceElement[] stackTrace) {
    super(message, cause, stackTrace);
  }
}
