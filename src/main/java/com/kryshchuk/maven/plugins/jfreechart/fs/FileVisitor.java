/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart.fs;

import java.io.File;

/**
 * @author yura
 * 
 */
public interface FileVisitor {

  void visit(File inputFile, File outputFile) throws VisitorException;

}
