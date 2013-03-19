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
class FileSetIterator extends AbstractFileIterator {

  private final FileSet fileset;

  protected FileSetIterator(final FileSet fileset, final File outputDirectory) {
    super(outputDirectory);
    this.fileset = fileset;
  }

  @Override
  void iterate(final FileSetVisitor visitor) throws MojoExecutionException, MojoFailureException {
    // final File outputFile = new File(getOutputDirectory(), replaceExtension(file.getName()));
    // visitor.visit(file, outputFile);
  }

}
