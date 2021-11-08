package edu.kit.exp.impl.continuousCompetition.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dschnurr on 27.02.14.
 */
public class QuantityChartDisplayPanel extends ChartDisplayPanel {


    public QuantityChartDisplayPanel(int chartWidth, int chartHeight, FirmDescription myRole, int timeStep, int duration,
                                     Color colorFirmA, Color colorFirmB, Color colorFirmC, Color colorMarketP, boolean isDiscreteTreatment, boolean isTriopolyTreatment) {

        this(chartWidth, chartHeight, myRole, timeStep, duration, colorFirmA, colorFirmB, colorFirmC, Color.GRAY, colorMarketP, isDiscreteTreatment, isTriopolyTreatment, false);
    }


    public QuantityChartDisplayPanel(int chartWidth, int chartHeight, FirmDescription myRole, int timeStep, int duration,
                                  Color colorFirmA, Color colorFirmB, Color colorFirmC, Color colorFirmD, Color colorMarketP, boolean isDiscreteTreatment, boolean isTriopolyTreatment, boolean isQuadropolyTreatmen) {

        super(chartWidth, chartHeight, myRole, timeStep, duration, colorFirmA, colorFirmB, colorFirmC, colorFirmD, colorMarketP, isDiscreteTreatment, isTriopolyTreatment, isQuadropolyTreatmen);
        this.p_value_max = 100;
        this.t_hatch_number = 20;
        this.p_hatch_number = 4;
        calcDiagramParameters();
    }

    protected void drawValues(Graphics2D g2, int x, int x_prev, Timestamp ts, Timestamp ts_prev)  {
        int y;
        int y_prev;

        // Draw average retail price
        /*g2.setPaint(colorMarketP);
        y = height_px - (( (int) ts.getqMarket()* height_px) / p_value_max);
        y_prev = height_px - (( (int) ts_prev.getqMarket() * height_px) / p_value_max);
        drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);*/

        // Draw retail price of own firm
        g2.setPaint(colorFirmA);
        y = height_px - (((int) ts.getqFirmA() * height_px) / p_value_max);
        y_prev = height_px - (((int) ts_prev.getqFirmA() * height_px) / p_value_max);
        drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);

        g2.setPaint(colorFirmB);
        y = height_px - (((int) ts.getqFirmB() * height_px) / p_value_max);
        y_prev = height_px - (((int) ts_prev.getqFirmB() * height_px) / p_value_max);
        drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);

        if (isTriopolyTreatment) {
            g2.setPaint(colorFirmC);
            y = height_px - (((int) ts.getqFirmC() * height_px) / p_value_max);
            y_prev = height_px - (((int) ts_prev.getqFirmC() * height_px) / p_value_max);
            drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);
        }

        if (isQuadropolyTreatment) {
            g2.setPaint(colorFirmD);
            y = height_px - (((int) ts.getqFirmD() * height_px) / p_value_max);
            y_prev = height_px - (((int) ts_prev.getqFirmD() * height_px) / p_value_max);
            drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);
        }

    }

}
