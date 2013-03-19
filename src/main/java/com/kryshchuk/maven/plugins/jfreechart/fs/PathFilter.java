/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public abstract class PathFilter {

  @Parameter(required = false)
  private List<String> includes;

  @Parameter(required = false)
  private List<String> excludes;

  private boolean includesAny;

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

  /**
   * @param includes
   *          the includes to set
   */
  protected void setIncludes(final List<String> includes) {
    this.includes = includes;
    for (final String inc : includes) {
      if ("**".equals(inc) || "*".equals(inc)) {
        includesAny = true;
        break;
      }
    }
  }

  /**
   * @param excludes
   *          the excludes to set
   */
  protected void setExcludes(final List<String> excludes) {
    this.excludes = excludes;
  }

  /**
   * @return the includesAny
   */
  protected boolean isIncludesAny() {
    return includesAny;
  }

}
