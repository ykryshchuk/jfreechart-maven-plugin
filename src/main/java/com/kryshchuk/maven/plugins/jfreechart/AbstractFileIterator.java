/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
abstract class AbstractFileIterator {

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

  abstract void iterate(FileSetVisitor visitor) throws MojoExecutionException, MojoFailureException;

  protected String replaceExtension(final String filename) {
    final int ext = filename.lastIndexOf('.');
    if (ext > 0) {
      return filename.substring(0, ext) + EXT;
    } else {
      return filename + EXT;
    }
  }

}
