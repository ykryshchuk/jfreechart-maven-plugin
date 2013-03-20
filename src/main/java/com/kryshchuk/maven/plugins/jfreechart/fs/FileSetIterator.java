/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;

import org.apache.maven.plugin.MojoFailureException;

/**
 * @author yura
 * 
 */
public class FileSetIterator extends AbstractFileIterator {

  private final FileSet fileset;

  public FileSetIterator(final FileSet fileset, final File outputDirectory) {
    super(outputDirectory);
    this.fileset = fileset;
  }

  @Override
  public void iterate(final FileVisitor visitor) throws FilesIterationException, MojoFailureException {
    iterate(fileset.getDirectory(), new FileLister(fileset), visitor);
  }

  private void iterate(final File dir, final FileLister fileLister, final FileVisitor visitor)
      throws FilesIterationException, MojoFailureException {
    final File outDir = getOutputDirectory(fileLister.getRelativePath());
    final File[] list = dir.listFiles(fileLister);
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
