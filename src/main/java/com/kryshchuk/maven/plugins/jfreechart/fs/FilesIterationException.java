/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

/**
 * <p>FilesIterationException class.</p>
 *
 * @author yura
 */
public class FilesIterationException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public FilesIterationException(final String message) {
    super(message);
  }

  public FilesIterationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
