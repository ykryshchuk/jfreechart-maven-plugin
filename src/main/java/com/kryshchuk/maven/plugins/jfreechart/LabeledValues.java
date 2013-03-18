/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class LabeledValues {

  @Parameter(required = true)
  private String label;

  /**
   * The group index of the value in matched (by regexp) record.
   */
  @Parameter(required = false)
  private int valueGroup;

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @return the valueGroup
   */
  public int getValueGroup() {
    return valueGroup;
  }

}
