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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugins.annotations.Parameter;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * LineChartDataset class.
 * 
 * @author yura
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

  private int sequence;

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

  public Number getDomainValue(final LineChartAxis defaultDomainAxis, final Matcher m) {
    final LineChartAxis domainAxis = getDomainAxis() == null ? defaultDomainAxis : getDomainAxis();
    if (domainAxis.isSequence()) {
      return Integer.valueOf(++sequence);
    } else {
      return domainAxis.getValue(m);
    }
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
    sequence = 0;
  }

  @Override
  public String toString() {
    final StringBuilder str = new StringBuilder("Line Chart Dataset: ");
    str.append("regexp={").append(regexp).append("} ");
    return str.toString();
  }

}
