/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class FileIterationException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public FileIterationException(final String message) {
    super(message);
  }

  public FileIterationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
