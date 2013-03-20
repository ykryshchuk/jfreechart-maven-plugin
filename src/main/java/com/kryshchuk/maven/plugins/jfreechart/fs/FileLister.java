/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * FileLister class.
 * </p>
 * 
 * @author yura
 */
public class FileLister extends PathFilter implements FileFilter {

  private final String relativePath;

  /**
   * 
   * @param pathFilter
   */
  public FileLister(final PathFilter pathFilter) {
    if (pathFilter != null) {
      setIncludes(pathFilter.getIncludes());
      setExcludes(pathFilter.getExcludes());
    }
    relativePath = null;
  }

  private FileLister(final String relativePath) {
    this.relativePath = relativePath;
  }

  public boolean accept(final File file) {
    if (!isIncludesAny()) {
      if (getIncludes() != null) {
        if (!match(file, getIncludes(), false)) {
          return false;
        }
      }
    }
    if (isExcludesAny()) {
      return false;
    }
    if (getExcludes() != null) {
      if (match(file, getExcludes(), true)) {
        return false;
      }
    }
    return true;
  }

  protected static boolean match(final File file, final List<String> paths, final boolean firstLevel) {
    for (final String path : paths) {
      if (match(file, path, firstLevel)) {
        return true;
      }
    }
    return false;
  }

  protected static boolean match(final File file, final String path, final boolean firstLevel) {
    final String fname = file.getName();
    if (fname.equals(path) || "*".equals(path) || "**".equals(path)) {
      return true;
    } else {
      final int pathSeparator = path.indexOf('/');
      if (pathSeparator > 0) {
        if (!firstLevel && file.isDirectory()) {
          return match(file, path.substring(0, pathSeparator), true);
        } else {
          return false;
        }
      } else {
        final String pathRegexp = path.replace(".", "\\.").replace('?', '.').replace("*", ".*");
        return Pattern.matches(pathRegexp, fname);
      }
    }
  }

  /**
   * @return the relativePath
   */
  public String getRelativePath() {
    return relativePath;
  }

  /**
   * @param file
   * @return
   */
  public FileLister getFileLister(final File file) {
    final String fname = file.getName();
    final FileLister fileLister = new FileLister(getNestedRelativePath(fname));
    if (getIncludes() != null) {
      fileLister.setIncludes(expand(getIncludes(), fname));
    }
    if (getExcludes() != null) {
      fileLister.setExcludes(expand(getExcludes(), fname));
    }
    return fileLister;
  }

  protected static List<String> expand(final List<String> paths, final String fname) {
    final List<String> expanded = new ArrayList<String>();
    for (final String inc : paths) {
      if (inc.startsWith("**")) {
        expanded.add(inc);
      }
      final int firstSeparator = inc.indexOf('/');
      if (firstSeparator > 0 && (firstSeparator < inc.length() - 1)) {
        final String firstPath = inc.substring(0, firstSeparator);
        final String remainingPath = inc.substring(firstSeparator + 1);
        if ("**".equals(firstPath) || "*".equals(firstPath) || fname.equals(firstPath)) {
          expanded.add(remainingPath);
        }
      }
    }
    return expanded;
  }

  protected String getNestedRelativePath(final String fname) {
    if (relativePath == null) {
      return fname;
    } else {
      return relativePath + "/" + fname;
    }
  }

}
