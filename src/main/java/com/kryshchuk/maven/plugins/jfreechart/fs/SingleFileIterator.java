/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;

import org.apache.maven.plugin.MojoFailureException;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class SingleFileIterator extends AbstractFileIterator {

  private final File file;

  public SingleFileIterator(final File file, final File outputDirectory) {
    super(outputDirectory);
    this.file = file;
  }

  @Override
  public void iterate(final FileVisitor visitor) throws FilesIterationException, MojoFailureException {
    final File outputFile = new File(getOutputDirectory(), replaceExtension(file.getName()));
    visitor.visit(file, outputFile);
  }

}
