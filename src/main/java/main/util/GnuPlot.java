package main.util;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.ArrayDataSet;
import com.panayotis.gnuplot.dataset.DataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.utils.Debug;
import main.GAStatistic;
import main.util.gnuplot.terminal.SVGTerminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author created by Jens Plümer on 30.11.17
 */
public class GnuPlot {

    private static Logger log = LoggerFactory.getLogger(GnuPlot.class);

    private Collection<DataSetPlot> dataSetPlots = new ArrayList<>();
    private StringBuffer bf;
    private String filePath;
    private long curTime;

    public GnuPlot(GAStatistic stats, long curTime, String filePath) {

        this.filePath = filePath;
        this.curTime = curTime;
        addPlot(stats.getBestFitnessValues(), "BEST");
        addPlot(stats.getWorstFitnessValues(), "WORST");
        addPlot(stats.getAvgFitnessValues(), "AVG");

    }


    private void addPlot(List<Double> values, String title) {

        if (values.size() > 0) {
            double[][] doubles = new double[values.size()][1];

            bf = new StringBuffer();
            bf.append("gen " + title);
            bf.append("\n");
            for (int i = 0; i < values.size(); i++) {
                doubles[i][0] = values.get(i);
                bf.append(i);
                bf.append(' ');
                bf.append(values.get(i));
                bf.append(' ');
                bf.append("\n");
            }


            try (BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(
                    filePath + title + "-" + curTime + ".data")))) {
                bwr.write(bf.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataSetPlot dataSetPlot = new DataSetPlot();
            dataSetPlot.setDataSet(new ArrayDataSet(doubles));
            dataSetPlot.setPlotStyle(new PlotStyle(Style.LINES));
            dataSetPlot.setTitle(title);
            dataSetPlots.add(dataSetPlot);

        }

    }

    public void show() {

        JavaPlot p = new JavaPlot();

        SVGTerminal svg = new SVGTerminal();
        p.setPersist(true);
        p.setTerminal(svg);
        p.setTitle("HEARTHSTONE META DECK EVOLUTION");
        dataSetPlots.stream().forEach(e -> p.addPlot(e));
        p.plot();

//        log.info("gnu=" +svg.getTextOutput());// get svg output

        try {
            JFrame f = new JFrame();
            f.getContentPane().add(svg.getPanel());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        } catch (ClassNotFoundException ex) {
            System.err.println("Error: Library SVGSalamander not properly installed?");
        }
    }

    public BufferedImage getPlot() {

        return null;

    }

}
