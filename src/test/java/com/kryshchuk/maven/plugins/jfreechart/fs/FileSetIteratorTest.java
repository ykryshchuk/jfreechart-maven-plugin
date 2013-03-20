/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.annotations.Test;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class FileSetIteratorTest {

  @Test
  public void runScenarioOther() throws IOException, MojoFailureException, FilesIterationException {
    // setup dir
    final File dir = File.createTempFile("jfcmp-", "-test");
    Assert.assertTrue(dir.delete());
    Assert.assertTrue(dir.mkdirs());
    Assert.assertTrue(new File(dir, "data1.txt").createNewFile());
    Assert.assertTrue(new File(dir, "data2.txt").createNewFile());
    final File subdir = new File(dir, "subdir");
    Assert.assertTrue(subdir.mkdirs());
    Assert.assertTrue(new File(subdir, "file1.log").createNewFile());
    Assert.assertTrue(new File(subdir, "file2.log").createNewFile());
    Assert.assertTrue(new File(subdir, "other.log").createNewFile());
    // setup fileset
    final FileSet fileset = new FileSet();
    fileset.setDirectory(dir);
    fileset.setIncludes(Arrays.asList("subdir/*.log"));
    fileset.setExcludes(Arrays.asList("*/other.*"));
    // iterate
    final FileSetIterator iterator = new FileSetIterator(fileset, dir);
    final FileVisitor visitor = Mockito.mock(FileVisitor.class);
    iterator.iterate(visitor);
    Mockito.verify(visitor).visit(Matchers.eq(new File(subdir, "file1.log")), Matchers.any(File.class));
    Mockito.verify(visitor).visit(Matchers.eq(new File(subdir, "file2.log")), Matchers.any(File.class));
    Mockito.verifyNoMoreInteractions(visitor);
  }

}
