/*
 * Maven JFreeChart Plugin.
 * Copyright (C) 2013  Yuriy Kryshchuk
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.math.BigDecimal;
import java.util.regex.Matcher;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * <p>
 * LabeledValues class.
 * </p>
 * 
 * @author yura
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

  @Override
  public String toString() {
    final StringBuilder str = new StringBuilder();
    str.append("label=").append(label).append(" ");
    if (value != null) {
      str.append("value=").append(value).append(" ");
    }
    return str.toString();
  }

}
