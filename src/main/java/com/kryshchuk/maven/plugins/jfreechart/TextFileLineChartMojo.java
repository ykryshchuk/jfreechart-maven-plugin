/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 * Generates a line chart from a data in a plain text file. The data in a text file should follow the regular expression
 * pattern. Every pattern may provide the format for several chart data series.
 * 
 * @author yura
 * @since ${developmentVersion}
 * 
 */
@Mojo(name = "text-file-line-chart", defaultPhase = LifecyclePhase.PRE_SITE, threadSafe = true)
public class TextFileLineChartMojo extends AbstractMojo {

  /**
   * Width of the produced chart.
   */
  @Parameter(defaultValue = "800", property = "chart.width", required = false)
  private Integer width;

  /**
   * Height of the produced chart.
   */
  @Parameter(defaultValue = "600", property = "chart.height", required = false)
  private Integer height;

  /**
   * Chart datafile.
   */
  @Parameter(property = "chart.data", required = false)
  private File data;

  /**
   * Directory where chart(s) images will be stored.
   */
  @Parameter(defaultValue = "${project.reporting.outputDirectory}/chart", property = "chart.outputDirectory", required = false)
  private File outputDirectory;

  /**
   * Chart title.
   */
  @Parameter(property = "chart.title", required = false)
  private String title;

  /**
   * Input filesets.
   */
  @Parameter(required = false)
  private List<FileSet> filesets;

  @Parameter(required = true)
  private List<LineChartDataset> datasets;

  /**
   * {@inheritDoc}
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    // Check whether some data has been provided
    if (data == null && filesets == null) {
      throw new MojoFailureException("No data source specified for the chart");
    }
    if (data != null) {
      final SingleFileIterator i = new SingleFileIterator(data, outputDirectory);
      i.iterate(new DefaultFileSetVisitor());
    } else {
      getLog().warn("Not yet implemented");
    }
  }

  private void readDatasets(final File inputFile) throws MojoFailureException {
    getLog().debug("Reading data file " + inputFile);
    try {
      final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      try {
        String line;
        while ((line = reader.readLine()) != null) {
          for (final LineChartDataset ds : datasets) {
            final Matcher m = ds.getMatcher(line);
            if (m.matches()) {
              final Number label = ds.getAxisX().getValue(m);
              for (final LineChartSerie serie : ds.getSeries()) {
                serie.getSerie().add(label, serie.getValue(m));
              }
            }
          }
        }
      } finally {
        reader.close();
      }
    } catch (final IOException e) {
      throw new MojoFailureException("Failed to read data file", e);
    }
  }

  private class DefaultFileSetVisitor implements FileSetVisitor {

    public void visit(final File inputFile, final File outputFile) throws MojoExecutionException, MojoFailureException {
      if (!inputFile.isFile()) {
        throw new MojoExecutionException("Input file does not exist " + inputFile);
      }
      if (outputFile.isFile() && outputFile.lastModified() > inputFile.lastModified()) {
        getLog().debug("Chart " + outputFile + " is up to date");
      } else {
        readDatasets(inputFile);
        getLog().debug("Creating chart");

        final XYPlot plot = new XYPlot();
        final JFreeChart chart = new JFreeChart(title, plot);
        // final JFreeChart chart = ChartFactory.createXYLineChart(title, ds0.getAxisX().getLabel(), ds0.getAxisY()
        // .getLabel(), ds0.getSeriesCollection(), PlotOrientation.VERTICAL, true, false, false);

        for (int i = 0; i < datasets.size(); i++) {
          final LineChartDataset ds = datasets.get(i);
          getLog().debug("Adding dataset " + i);
          final XYItemRenderer renderer = new StandardXYItemRenderer();
          final NumberAxis domainAxis = new NumberAxis(ds.getAxisX().getLabel());
          domainAxis.setAutoRangeIncludesZero(false);
          plot.setDomainAxis(i, domainAxis);
          final NumberAxis rangeAxis = new NumberAxis(ds.getAxisY().getLabel());
          for (int s = 0; s < ds.getSeries().size(); s++) {
            final Color lineColor = ds.getSeries().get(s).getLineColor();
            if (lineColor != null) {
              renderer.setSeriesPaint(s, lineColor);
            }
          }
          rangeAxis.setAutoRangeIncludesZero(false);
          plot.setRangeAxis(i, rangeAxis);
          plot.setRenderer(i, renderer);
          // xyPlot.setRangeAxisLocation(i, AxisLocation.TOP_OR_LEFT);
          plot.setDataset(i, ds.getSeriesCollection());
          plot.mapDatasetToRangeAxis(i, i);
        }

        if (!outputFile.getParentFile().isDirectory()) {
          if (!outputFile.getParentFile().mkdirs()) {
            throw new MojoExecutionException("Failed to create chart directory");
          } else {
            getLog().debug("Created chart directory");
          }
        }
        try {
          ChartUtilities.saveChartAsPNG(outputFile, chart, width, height);
          getLog().info("Chart saved " + outputFile);
        } catch (final IOException e) {
          throw new MojoFailureException("Could not store chart image", e);
        }
      }
    }
  }

}
