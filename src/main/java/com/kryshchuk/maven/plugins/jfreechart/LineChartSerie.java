/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.awt.Color;
import java.lang.reflect.Field;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.jfree.data.xy.XYSeries;

/**
 * <p>LineChartSerie class.</p>
 *
 * @author yura
 */
public class LineChartSerie extends LabeledValues {

  private final XYSeries serie = new XYSeries("");

  @Parameter(required = false)
  private String color;

  private Color lineColor;

  private final Class<Color> colorClass = Color.class;

  @Override
  public void setLabel(final String label) {
    super.setLabel(label);
    serie.setKey(label);
  }

  /**
   * @param color
   *          the color to set
   * @throws MojoExecutionException
   */
  public void setColor(final String color) throws MojoExecutionException {
    this.color = color;
    try {
      final Field colorField = colorClass.getField(color);
      lineColor = (Color) colorField.get(null);
    } catch (final NoSuchFieldException e) {
      try {
        lineColor = Color.decode(color);
      } catch (final NumberFormatException e2) {
        throw new MojoExecutionException("Invalid color", e2);
      }
    } catch (final IllegalAccessException e) {
      throw new MojoExecutionException("Cannot convert color", e);
    }
  }

  /**
   * @return the serie
   */
  public XYSeries getSerie() {
    return serie;
  }

  /**
   * @return the lineColor
   */
  public Color getLineColor() {
    return lineColor;
  }

  /**
   * 
   */
  public void clear() {
    getSerie().clear();
  }

}
