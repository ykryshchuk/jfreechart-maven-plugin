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
  private LineChartAxis axisDomain;

  @Parameter(required = true)
  private LineChartAxis axisRange;

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
  public LineChartAxis getDomainAxis() {
    return axisDomain;
  }

  /**
   * @return the axisY
   */
  public LineChartAxis getRangeAxis() {
    return axisRange;
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

  public void clear() {
    for (final LineChartSerie serie : getSeries()) {
      serie.clear();
    }
    getDomainAxis().clear();
    getRangeAxis().clear();
  }

}
