/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author yura
 * @since ${developmentVersion}
 * 
 */
public class ChartDataset {

  @Parameter(required = true)
  private LabeledValues axisX;

  @Parameter(required = true)
  private LabeledValues axisY;

  @Parameter(required = true)
  private List<LabeledValues> series;

  /**
   * @return the axisX
   */
  public LabeledValues getAxisX() {
    return axisX;
  }

  /**
   * @return the axisY
   */
  public LabeledValues getAxisY() {
    return axisY;
  }

  /**
   * @return the series
   */
  public List<LabeledValues> getSeries() {
    return series;
  }

}
