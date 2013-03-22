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
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import com.kryshchuk.maven.plugins.filevisitor.AbstractFileVisitor;
import com.kryshchuk.maven.plugins.filevisitor.FileMapper;
import com.kryshchuk.maven.plugins.filevisitor.FileSet;
import com.kryshchuk.maven.plugins.filevisitor.FileSetIterator;
import com.kryshchuk.maven.plugins.filevisitor.ReplaceExtensionFileMapper;
import com.kryshchuk.maven.plugins.filevisitor.SingleFileIterator;
import com.kryshchuk.maven.plugins.filevisitor.VisitorException;

/**
 * Generates a line chart from a data in a plain text file. The data in a text file should follow the regular expression
 * pattern. Every pattern may provide the format for several chart data series.
 * 
 * @author yura
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

  @Parameter(required = false)
  private LineChartAxis axisDomain;

  @Parameter(required = false)
  private LineChartAxis axisRange;

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
    try {
      if (data != null) {
        final FileMapper fileMapper = new ReplaceExtensionFileMapper(data.getParentFile(), outputDirectory, "png");
        final SingleFileIterator i = new SingleFileIterator(data, fileMapper);
        i.iterate(new DefaultFileSetVisitor());
      } else {
        final DefaultFileSetVisitor visitor = new DefaultFileSetVisitor();
        for (final FileSet fs : filesets) {
          final FileMapper fileMapper = new ReplaceExtensionFileMapper(fs.getDirectory(), outputDirectory, "png");
          final FileSetIterator i = new FileSetIterator(fs, fileMapper);
          i.iterate(visitor);
        }
      }
    } catch (final VisitorException e) {
      throw new MojoExecutionException("Could not iterate files", e);
    }
  }

  private void readDatasets(final File inputFile) throws IOException {
    getLog().debug("Reading data file " + inputFile);
    for (final LineChartDataset ds : datasets) {
      ds.clear();
    }
    final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        for (final LineChartDataset ds : datasets) {
          final Matcher m = ds.getMatcher(line);
          if (m.matches()) {
            final Number label = ds.getDomainValue(axisDomain, m);
            for (final LineChartSerie serie : ds.getSeries()) {
              serie.getSerie().add(label, serie.getValue(m));
            }
          }
        }
      }
    } finally {
      reader.close();
    }
  }

  private class DefaultFileSetVisitor extends AbstractFileVisitor {

    @Override
    protected boolean shouldOverwrite(final File inputFile, final File outputFile) {
      return false;
    }

    @Override
    protected void handleFile(final File inputFile, final File outputFile) throws VisitorException {
      final long startTime = System.nanoTime();
      try {
        readDatasets(inputFile);
      } catch (final IOException e) {
        throw new VisitorException("Failed to read data file", e);
      }
      final long readTime = System.nanoTime();
      getLog().debug("Datasets read from " + inputFile + " in " + (readTime - startTime) + " ns");

      final XYPlot plot = new XYPlot();
      final JFreeChart chart = new JFreeChart(String.format(title, inputFile.getName()), plot);

      if (axisDomain != null) {
        getLog().debug("Configuring default domain axis");
        final NumberAxis domainAxis = new NumberAxis(axisDomain.getLabel());
        plot.setDomainAxis(domainAxis);
        final AxisLocation axisXLocation = axisDomain.getAxisLocation();
        if (axisXLocation != null) {
          plot.setDomainAxisLocation(axisXLocation);
        }
        axisDomain.setupAxis(domainAxis);
      }

      if (axisRange != null) {
        getLog().debug("Configuring default range axis");
        final NumberAxis rangeAxis = new NumberAxis(axisRange.getLabel());
        plot.setRangeAxis(rangeAxis);
        final AxisLocation axisYLocation = axisRange.getAxisLocation();
        if (axisYLocation != null) {
          plot.setRangeAxisLocation(axisYLocation);
        }
        axisRange.setupAxis(rangeAxis);
      }

      for (int d = 0; d < datasets.size(); d++) {
        final LineChartDataset ds = datasets.get(d);
        // Domain axis
        if (ds.getDomainAxis() != null) {
          getLog().debug("Configuring domain axis");
          final NumberAxis domainAxis = new NumberAxis(ds.getDomainAxis().getLabel());
          plot.setDomainAxis(d, domainAxis);
          final AxisLocation axisXLocation = ds.getDomainAxis().getAxisLocation();
          if (axisXLocation != null) {
            plot.setDomainAxisLocation(d, axisXLocation);
          }
          ds.getDomainAxis().setupAxis(domainAxis);
        }
        // Range axis
        if (ds.getRangeAxis() != null) {
          getLog().debug("Configuring range axis");
          final NumberAxis rangeAxis = new NumberAxis(ds.getRangeAxis().getLabel());
          plot.setRangeAxis(d, rangeAxis);
          final AxisLocation axisYLocation = ds.getRangeAxis().getAxisLocation();
          if (axisYLocation != null) {
            plot.setRangeAxisLocation(d, axisYLocation);
          }
          ds.getRangeAxis().setupAxis(rangeAxis);
        }
        // Renderer
        final XYItemRenderer renderer = new StandardXYItemRenderer();
        for (int s = 0; s < ds.getSeries().size(); s++) {
          final Color lineColor = ds.getSeries().get(s).getLineColor();
          if (lineColor != null) {
            renderer.setSeriesPaint(s, lineColor);
          }
        }
        plot.setRenderer(d, renderer);
        getLog().debug("Completing dataset " + d);
        plot.setDataset(d, ds.getSeriesCollection());
        if (ds.getDomainAxis() != null) {
          plot.mapDatasetToDomainAxis(d, d);
        }
        if (ds.getRangeAxis() != null) {
          plot.mapDatasetToRangeAxis(d, d);
        }
      }

      verifyParentDirectory(outputFile);
      try {
        ChartUtilities.saveChartAsPNG(outputFile, chart, width, height);
        final long execTime = System.nanoTime() - startTime;
        getLog().info("Chart generated within " + execTime + " ns, saved to " + outputFile);
      } catch (final IOException e) {
        throw new VisitorException("Could not store chart image", e);
      }
    }

  }

}
