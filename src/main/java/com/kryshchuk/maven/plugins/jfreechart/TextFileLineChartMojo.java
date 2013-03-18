/**
 * 
 */
package com.kryshchuk.maven.plugins.jfreechart;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
  private int width;

  /**
   * Height of the produced chart.
   */
  @Parameter(defaultValue = "600", property = "chart.height", required = false)
  private int height;

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
   * The regular expression for data pattern.
   */
  @Parameter(property = "chart.data.regexp", required = true)
  private String regexp;

  /**
   * Input filesets.
   */
  @Parameter(required = false)
  private List<FileSet> filesets;

  @Parameter(required = true)
  private List<ChartDataset> datasets;

  private Pattern dataRecordPattern;

  /**
   * {@inheritDoc}
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    // Check whether some data has been provided
    if (data == null && filesets == null) {
      throw new MojoFailureException("No data source specified for the chart");
    }
    try {
      dataRecordPattern = Pattern.compile(regexp);
    } catch (final PatternSyntaxException e) {
      throw new MojoExecutionException("Invalid data record pattern", e);
    }
    if (data != null) {
      final SingleFileIterator i = new SingleFileIterator(data, outputDirectory);
      i.iterate(new DefaultFileSetVisitor());
    } else {
      getLog().warn("Not yet implemented");
    }
  }

  private void readDatasets(final File inputFile, final XYSeriesCollection[] seriesCollections)
      throws MojoFailureException {
    getLog().debug("Reading data file " + inputFile);
    try {
      final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      try {
        String line;
        while ((line = reader.readLine()) != null) {
          final Matcher m = dataRecordPattern.matcher(line);
          if (m.matches()) {
            for (int i = 0; i < seriesCollections.length; i++) {
              final XYSeriesCollection dataset = seriesCollections[i];
              final Number label = new BigDecimal(m.group(datasets.get(i).getAxisX().getValueGroup()));
              for (int j = 0; j < dataset.getSeriesCount(); j++) {
                final XYSeries serie = dataset.getSeries(j);
                final BigDecimal value = new BigDecimal(m.group(datasets.get(i).getSeries().get(j).getValueGroup()));
                serie.add(label, value);
              }
            }
          } else {
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
        getLog().debug("Setting up datasets");
        final XYSeriesCollection[] seriesCollections = new XYSeriesCollection[datasets.size()];
        for (int i = 0; i < datasets.size(); i++) {
          final XYSeriesCollection dataset = new XYSeriesCollection();
          for (final LabeledValues labeledSerie : datasets.get(i).getSeries()) {
            final XYSeries serie = new XYSeries(labeledSerie.getLabel());
            dataset.addSeries(serie);
          }
          seriesCollections[i] = dataset;
        }
        readDatasets(inputFile, seriesCollections);

        final ChartDataset ds1 = datasets.get(0);
        getLog().debug("Creating chart");
        final JFreeChart chart = ChartFactory.createXYLineChart(title, ds1.getAxisX().getLabel(), ds1.getAxisY()
            .getLabel(), seriesCollections[0], PlotOrientation.VERTICAL, true, false, false);
        final XYPlot xyPlot = chart.getXYPlot();

        for (int i = 0; i < datasets.size(); i++) {
          getLog().debug("Adding dataset " + i);
          final NumberAxis rangeAxis = new NumberAxis(datasets.get(i).getAxisY().getLabel());
          final XYItemRenderer renderer = new StandardXYItemRenderer();
          renderer.setSeriesPaint(0, Color.GREEN);
          rangeAxis.setAutoRangeIncludesZero(false);
          xyPlot.setRenderer(i, renderer);
          xyPlot.setRangeAxis(i, rangeAxis);
          xyPlot.setRangeAxisLocation(i, AxisLocation.TOP_OR_LEFT);
          xyPlot.setDataset(i, seriesCollections[i]);
          xyPlot.mapDatasetToRangeAxis(i, i);
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
