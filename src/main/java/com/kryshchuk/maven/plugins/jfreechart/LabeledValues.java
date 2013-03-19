/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.math.BigDecimal;
import java.util.regex.Matcher;

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
   * The value. Can be in form <code>%1$d</code> for integer value in a pattern, <code>%1$f</code> as decimal number in
   * a pattern.
   */
  @Parameter(required = false)
  private String value;

  private boolean integerValue;

  private boolean decimalValue;

  private int groupIndex;

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label
   *          the label to set
   */
  protected void setLabel(final String label) {
    this.label = label;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(final String value) {
    this.value = value;
    if (value.startsWith("%")) {
      final int groupEnd = value.indexOf('$');
      groupIndex = Integer.parseInt(value.substring(1, (groupEnd > 0) ? groupEnd : value.length()));
      if (value.endsWith("$d")) {
        integerValue = true;
      } else if (value.endsWith("$f")) {
        decimalValue = true;
      }
    }
  }

  public Number getValue(final Matcher m) {
    if (integerValue) {
      return Integer.valueOf(m.group(groupIndex));
    }
    if (decimalValue) {
      return Double.valueOf(m.group(groupIndex));
    }
    if (groupIndex != 0) {
      return new BigDecimal(m.group(groupIndex));
    }
    throw new IllegalStateException("Cannot determine the value");
  }

}
