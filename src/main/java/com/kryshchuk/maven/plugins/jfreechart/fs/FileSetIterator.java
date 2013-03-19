/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;
import java.io.FileFilter;

import org.apache.maven.plugin.MojoFailureException;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class FileSetIterator extends AbstractFileIterator {

  private final FileSet fileset;

  public FileSetIterator(final FileSet fileset, final File outputDirectory) {
    super(outputDirectory);
    this.fileset = fileset;
  }

  @Override
  public void iterate(final FileSetVisitor visitor) throws FileIterationException, MojoFailureException {
    iterate(fileset.getDirectory(), new FileLister(fileset), visitor);
  }

  private void iterate(final File dir, final FileLister fileLister, final FileSetVisitor visitor)
      throws FileIterationException, MojoFailureException {
    final File outDir = getOutputDirectory(fileLister.getRelativePath());
    final FileFilter filter = null;
    final File[] list = dir.listFiles(filter);
    for (final File file : list) {
      if (file.isDirectory()) {
        final FileLister nestedFileLister = fileLister.getFileLister(file);
        iterate(file, nestedFileLister, visitor);
      } else {
        final File outputFile = new File(outDir, replaceExtension(file.getName()));
        visitor.visit(file, outputFile);
      }
    }
  }

}
