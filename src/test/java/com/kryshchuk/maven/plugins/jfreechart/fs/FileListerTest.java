/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class FileListerTest {

  @Test
  public void verifyConstructorWithPathFilter() {
    final PathFilter pathFilter = Mockito.mock(PathFilter.class);
    Mockito.doReturn(Arrays.asList("a", "b", "c")).when(pathFilter).getIncludes();
    Mockito.doReturn(Arrays.asList("1", "2", "3")).when(pathFilter).getExcludes();
    final FileLister fileLister = new FileLister(pathFilter);
    Assert.assertNull(fileLister.getRelativePath(), "Default relative path shall be null");
    Assert.assertEquals(fileLister.getIncludes(), Arrays.asList("a", "b", "c"), "Wrong includes");
    Assert.assertEquals(fileLister.getExcludes(), Arrays.asList("1", "2", "3"), "Wrong excludes");
  }

  @Test
  public void verifyNestedRelativePath() {
    final FileLister fileLister = new FileLister(null);
    final String path1 = fileLister.getNestedRelativePath("sub1");
    Assert.assertEquals(path1, "sub1");
  }

  @Test
  public void verify2ndNestedRelativePath() {
    final FileLister fileLister = new FileLister(null).getFileLister(new File("subdir"));
    final String path1 = fileLister.getNestedRelativePath("sub1");
    Assert.assertEquals(path1, "subdir/sub1");
  }

  @Test
  public void verifyExpand() {
    final List<String> list = Arrays.asList("**/file.txt", "*/*/log", "dir1/*", "dir2/*");
    Assert.assertEquals(FileLister.expand(list, "dir"), Arrays.asList("**/file.txt", "file.txt", "*/log"));
    Assert.assertEquals(FileLister.expand(list, "dir1"), Arrays.asList("**/file.txt", "file.txt", "*/log", "*"));
    Assert.assertEquals(FileLister.expand(list, "dir2"), Arrays.asList("**/file.txt", "file.txt", "*/log", "*"));
  }

  @Test
  public void verifyNestedExpand() {
    final List<String> initial = Arrays.asList("**", "**/file.txt", "*/*/log", "dir1/*", "dir2/*");
    final List<String> expanded = FileLister.expand(initial, "dir1");
    Assert.assertEquals(FileLister.expand(expanded, "sub"), Arrays.asList("**", "**/file.txt", "file.txt", "log"));
  }

}
