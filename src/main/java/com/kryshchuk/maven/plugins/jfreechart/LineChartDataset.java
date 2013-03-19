/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugins.annotations.Parameter;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class LineChartDataset {

  /**
   * The regular expression for data pattern.
   */
  @Parameter(required = true)
  private String regexp;

  private Pattern pattern;

  @Parameter(required = true)
  private LineChartAxis axisX;

  @Parameter(required = true)
  private LineChartAxis axisY;

  @Parameter(required = true)
  private List<LineChartSerie> series;

  private final XYSeriesCollection seriesCollection = new XYSeriesCollection();

  /**
   * @param regexp
   *          the regexp to set
   */
  public void setRegexp(final String regexp) {
    this.regexp = regexp;
    pattern = Pattern.compile(regexp);
  }

  public Matcher getMatcher(final String line) {
    return pattern.matcher(line);
  }

  /**
   * @return the axisX
   */
  public LineChartAxis getAxisX() {
    return axisX;
  }

  /**
   * @return the axisY
   */
  public LineChartAxis getAxisY() {
    return axisY;
  }

  /**
   * @param series
   *          the series to set
   */
  public void setSeries(final List<LineChartSerie> series) {
    this.series = series;
    for (final LineChartSerie serie : series) {
      seriesCollection.addSeries(serie.getSerie());
    }
  }

  /**
   * @return the series
   */
  public List<LineChartSerie> getSeries() {
    return series;
  }

  /**
   * @return the seriesCollection
   */
  public XYSeriesCollection getSeriesCollection() {
    return seriesCollection;
  }

}
