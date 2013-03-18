/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.io.File;
import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class FileSet {

  /**
   * Directory containing input data files for chart(s).
   */
  @Parameter(defaultValue = "${chart.directory}", required = true)
  private File directory;

  @Parameter(required = false)
  private List<String> includes;

  @Parameter(required = false)
  private List<String> excludes;

  /**
   * @return the directory
   */
  public File getDirectory() {
    return directory;
  }

  /**
   * @return the includes
   */
  public List<String> getIncludes() {
    return includes;
  }

  /**
   * @return the excludes
   */
  public List<String> getExcludes() {
    return excludes;
  }

}
