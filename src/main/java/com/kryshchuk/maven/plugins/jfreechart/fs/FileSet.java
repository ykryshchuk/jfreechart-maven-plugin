/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author yura
 * 
 */
public class FileSet extends PathFilter {

  /**
   * Directory containing input data files for chart(s).
   */
  @Parameter(defaultValue = "${chart.directory}", required = true)
  private File directory;

  /**
   * @return the directory
   */
  public File getDirectory() {
    return directory;
  }

  /**
   * @param directory
   *          the directory to set
   */
  protected void setDirectory(final File directory) {
    this.directory = directory;
  }

  @Override
  public String toString() {
    final StringBuilder str = new StringBuilder("File set: ");
    str.append("directory=").append(directory).append(" ");
    str.append(super.toString());
    return str.toString();
  }

}
