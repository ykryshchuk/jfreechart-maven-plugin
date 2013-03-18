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
interface FileSetVisitor {

  void visit(File inputFile, File outputFile) throws MojoExecutionException, MojoFailureException;

}
