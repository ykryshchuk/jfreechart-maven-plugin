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
public interface FileSetVisitor {

  void visit(File inputFile, File outputFile) throws FileIterationException, MojoFailureException;

}
