/**
 * 
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
      } catch (final NoSuchFieldException e) {
        throw new MojoExecutionException("Invalid axis location " + location, e);
      } catch (final IllegalAccessException e) {
        throw new MojoExecutionException("Could not get location", e);
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

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
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
