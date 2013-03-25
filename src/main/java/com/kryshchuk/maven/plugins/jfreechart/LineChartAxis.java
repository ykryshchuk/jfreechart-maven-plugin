/*
 * JFreeChart Maven Plugin
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
 * along with this program.  If not, see <http://www.gnu.org/licenses />.
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;

/**
 * LineChartAxis class.
 * 
 * @author yura
 */
public class LineChartAxis extends LabeledValues {

  /**
   * Axis location. See possible values in {@link AxisLocation}.
   * 
   * @see AxisLocation
   */
  @Parameter(required = false)
  private String location;

  private AxisLocation axisLocation;

  @Parameter(required = false)
  private String format;

  private boolean sequence;

  /**
   * @param location
   *          the location to set
   */
  public void setLocation(final String location) throws MojoExecutionException {
    this.location = location;
    if (location != null) {
      try {
        final Field axisLocationField = AxisLocation.class.getField(location);
        axisLocation = (AxisLocation) axisLocationField.get(null);
      } catch (final NoSuchFieldException | IllegalAccessException e) {
        throw new MojoExecutionException("Invalid axis location " + location, e);
      }
    }
  }

  /**
   * @return the axisLocation
   */
  public AxisLocation getAxisLocation() {
    return axisLocation;
  }

  @Override
  public void setValue(final String value) {
    super.setValue(value);
    if ("$#".equals(value)) {
      sequence = true;
    } else {
      sequence = false;
    }
  }

  /**
   * @return the sequence
   */
  public boolean isSequence() {
    return sequence;
  }

  public void setupAxis(final NumberAxis axis) {
    if (format != null) {
      axis.setNumberFormatOverride(new DecimalFormat(format));
    }
    axis.setAutoRangeIncludesZero(false);
    // axis.setStandardTickUnits(new StandardTickUnitSource());
  }

  @Override
  public String toString() {
    final StringBuilder str = new StringBuilder("Line Chart Axis: ");
    str.append(super.toString());
    if (location != null) {
      str.append("location=").append(location).append(" ");
    }
    if (format != null) {
      str.append("format=").append(format).append(" ");
    }
    return str.toString();
  }
}
