/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;

import org.apache.maven.plugin.MojoFailureException;

/**
 * <p>Abstract AbstractFileIterator class.</p>
 *
 * @author yura
 */
public abstract class AbstractFileIterator {

  private static final String EXT = ".png";

  private final File outputDirectory;

  protected AbstractFileIterator(final File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * @return the outputDirectory
   */
  protected File getOutputDirectory() {
    return outputDirectory;
  }

  protected File getOutputDirectory(final String rel) {
    if (rel == null || rel.isEmpty()) {
      return getOutputDirectory();
    } else {
      return new File(getOutputDirectory(), rel);
    }
  }

  abstract void iterate(FileVisitor visitor) throws FilesIterationException, MojoFailureException;

  protected String replaceExtension(final String filename) {
    final int ext = filename.lastIndexOf('.');
    if (ext > 0) {
      return filename.substring(0, ext) + EXT;
    } else {
      return filename + EXT;
    }
  }

}
