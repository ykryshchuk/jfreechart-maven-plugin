/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.jfree.chart.axis.AxisLocation;

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

  /**
   * @return the axisLocation
   */
  public AxisLocation getAxisLocation() throws MojoExecutionException {
    if (location == null) {
      return null;
    } else {
      try {
        final Field axisLocationField = AxisLocation.class.getField(location);
        return (AxisLocation) axisLocationField.get(null);
      } catch (final NoSuchFieldException e) {
        throw new MojoExecutionException("Invalid axis location " + location, e);
      } catch (final IllegalAccessException e) {
        throw new MojoExecutionException("Could not get location", e);
      }
    }
  }

  public NumberFormat getNumberFormat() {
    return new DecimalFormat(format);
  }

}
