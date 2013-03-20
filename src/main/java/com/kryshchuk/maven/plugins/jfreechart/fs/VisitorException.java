/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import org.apache.maven.plugin.MojoFailureException;

/**
 * @author yura
 * 
 */
public class VisitorException extends MojoFailureException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param message
   */
  public VisitorException(final String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public VisitorException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
