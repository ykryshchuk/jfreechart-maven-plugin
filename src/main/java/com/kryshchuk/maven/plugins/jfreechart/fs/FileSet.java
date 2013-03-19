/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;

import org.apache.maven.plugins.annotations.Parameter;


/**
 * @author yura
 * @since ${developmentVersion}
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

}
