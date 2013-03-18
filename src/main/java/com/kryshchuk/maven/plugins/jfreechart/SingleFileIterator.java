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
class SingleFileIterator extends AbstractFileIterator {

  private final File file;

  protected SingleFileIterator(final File file, final File outputDirectory) {
    super(outputDirectory);
    this.file = file;
  }

  @Override
  void iterate(final FileSetVisitor visitor) throws MojoExecutionException, MojoFailureException {
    final File outputFile = new File(getOutputDirectory(), replaceExtension(file.getName()));
    visitor.visit(file, outputFile);
  }

}
