/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;

/**
 * <p>FileVisitor interface.</p>
 *
 * @author yura
 */
public interface FileVisitor {

  void visit(File inputFile, File outputFile) throws VisitorException;

}
