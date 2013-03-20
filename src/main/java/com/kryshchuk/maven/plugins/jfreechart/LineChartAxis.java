/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.regex.Matcher;

import org.apache.maven.plugins.annotations.Parameter;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;

import com.kryshchuk.maven.plugins.jfreechart.fs.VisitorException;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class LineChartAxis extends LabeledValues {

  /**
   * Axis location. See possible values in {@link AxisLocation}.
   * 
   * @see AxisLocation
   */
  @Parameter(required = false)
  private String location;

  @Parameter(required = false)
  private String format;

  private int sequence;

  /**
   * @return the axisLocation
   */
  public AxisLocation getAxisLocation() throws VisitorException {
    if (location == null) {
      return null;
    } else {
      try {
        final Field axisLocationField = AxisLocation.class.getField(location);
        return (AxisLocation) axisLocationField.get(null);
      } catch (final NoSuchFieldException e) {
        throw new VisitorException("Invalid axis location " + location, e);
      } catch (final IllegalAccessException e) {
        throw new VisitorException("Could not get location", e);
      }
    }
  }

  @Override
  public void setValue(final String value) {
    super.setValue(value);
    if ("$#".equals(value)) {
      sequence = 0;
    } else {
      sequence = -1;
    }
  }

  @Override
  public Number getValue(final Matcher m) {
    if (sequence >= 0) {
      return Integer.valueOf(++sequence);
    } else {
      return super.getValue(m);
    }
  }

  public void setupAxis(final NumberAxis axis) {
    if (format != null) {
      axis.setNumberFormatOverride(new DecimalFormat(format));
    }
    axis.setAutoRangeIncludesZero(false);
  }

  /**
   * 
   */
  public void clear() {
    if (sequence >= 0) {
      sequence = 0;
    }
  }

  /*
   * (non-Javadoc)
   * 
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
