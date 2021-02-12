package org.zhjj370.plot;

import com.orsonpdf.PDFDocument;
import com.orsonpdf.PDFGraphics2D;
import com.orsonpdf.Page;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPlot {

    private List<Double> averageFitnessList = new ArrayList<>();
    private double min = 0;
    private double max = 0;
    /**
     * Creates a line dataset.
     *
     * @return The dataset.
     */
    private XYDataset createUnitDatasetForLine(List<Double> unitData) {
        XYSeries series1 = new XYSeries("Fitness");
        for(int i = 0;i< unitData.size();i++){
            series1.add(i,unitData.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);

        return dataset;
    }

    /**
     * Creates a line chart.
     *
     * @param dataset  a dataset for the line chart.
     *
     * @return a chart.
     */
    private JFreeChart createLineChart(XYDataset dataset,int time, String title, String xName,String yName) {
        JFreeChart chart = ChartFactory.createXYLineChart(title, xName, yName, dataset);
        chart.setTitle(new TextTitle(title,new Font("Dialog",Font.BOLD,13)));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setLowerMargin(0.2);
        xAxis.setUpperMargin(0.2);
        xAxis.setRange(0,time+10) ;
        xAxis.setLabelFont(new Font("Dialog", Font.BOLD, 10));
        xAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 8));
        plot.setDomainAxis(xAxis);
        //xAxis.setStandardTickUnits(createStandardDateTickUnits());

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setLowerMargin(0.2);
        yAxis.setUpperMargin(0.2);
        double a = (max-min)/10;
        yAxis.setRange(min-a,max+a);
        yAxis.setLabelFont(new Font("Dialog", Font.BOLD, 10));
        yAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 8));
        plot.setRangeAxis(yAxis);

        return chart;
    }


    /**
     * plot average fitness
     * @param averageFitnessList
     * @param time
     * @throws IOException
     */
    public void plotAverageFitness(List<Double> averageFitnessList, int time,double min,double max) throws IOException {
        this.averageFitnessList = averageFitnessList;
        this.max = max;
        this.min = min;
        JFreeChart chart = createLineChart(createUnitDatasetForLine(this.averageFitnessList),time,
                "Line chart for fitness","Number of iterations","Fitness");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/averageFitness.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());

        //The following program is for my thesis which is used to produce pdf images for latex
        PDFDocument pdfDoc = new PDFDocument();
        pdfDoc.setTitle("AverageFitness-chart");
        pdfDoc.setAuthor("zhjj370@nuaa.edu.cn");
        Page page = pdfDoc.createPage(new Rectangle(500, 300));
        PDFGraphics2D g2pdf = page.getGraphics2D();
        chart.draw(g2pdf, new Rectangle(0, 0, 500, 300));
        pdfDoc.writeToFile(new File("output/pdf/averageFitness.pdf"));

    }

}
