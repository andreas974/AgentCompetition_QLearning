package edu.kit.exp.impl.continuousCompetition.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

/**
 * Created by dschnurr on 27.02.14.
 */
public class PriceChartDisplayPanel extends ChartDisplayPanel {

    public PriceChartDisplayPanel(int chartWidth, int chartHeight, FirmDescription myRole, int timeStep, int duration, Color colorFirmA,
                                  Color colorFirmB, Color colorFirmC, Color colorMarketP, boolean isDiscreteTreatment, boolean isTriopolyTreatment) {

        this(chartWidth, chartHeight, myRole, timeStep, duration, colorFirmA, colorFirmB, colorFirmC, Color.GRAY, colorMarketP, isDiscreteTreatment, isTriopolyTreatment, false);
    }


    public PriceChartDisplayPanel(int chartWidth, int chartHeight, FirmDescription myRole, int timeStep, int duration, Color colorFirmA,
                                  Color colorFirmB, Color colorFirmC, Color colorFirmD, Color colorMarketP, boolean isDiscreteTreatment, boolean isTriopolyTreatment, boolean isQuadropolyTreatment) {

        super(chartWidth, chartHeight, myRole, timeStep, duration, colorFirmA, colorFirmB, colorFirmC, colorFirmD, colorMarketP, isDiscreteTreatment, isTriopolyTreatment, isQuadropolyTreatment);
        this.p_value_max = 100;
        this.t_hatch_number = 20;
        this.p_hatch_number = 4;
        calcDiagramParameters();
    }

    protected void drawValues(Graphics2D g2, int x, int x_prev, Timestamp ts, Timestamp ts_prev)  {
        int y;
        int y_prev;

        // Draw average retail price
        g2.setPaint(colorMarketP);
        y = height_px - (( (int) ts.getpMarket()* height_px) / p_value_max);
        y_prev = height_px - (( (int) ts_prev.getpMarket() * height_px) / p_value_max);
        //System.out.println("Profit FirmA"+ts.getProfitFirmA());
        drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);

        // Draw retail prices of each firm
        g2.setPaint(colorFirmA);
        y = height_px - (((int) ts.getpFirmA() * height_px) / p_value_max);
        y_prev = height_px - (((int) ts_prev.getpFirmA() * height_px) / p_value_max);
        drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);

        g2.setPaint(colorFirmB);
        y = height_px - (((int) ts.getpFirmB() * height_px) / p_value_max);
        y_prev = height_px - (((int) ts_prev.getpFirmB() * height_px) / p_value_max);
        drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);

        if (isTriopolyTreatment || isQuadropolyTreatment) {
            g2.setPaint(colorFirmC);
            y = height_px - (((int) ts.getpFirmC() * height_px) / p_value_max);
            y_prev = height_px - (((int) ts_prev.getpFirmC() * height_px) / p_value_max);
            drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);
        }

        if (isQuadropolyTreatment) {
            g2.setPaint(colorFirmD);
            y = height_px - (((int) ts.getpFirmD() * height_px) / p_value_max);
            y_prev = height_px - (((int) ts_prev.getpFirmD() * height_px) / p_value_max);
            drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);
        }

    }
}
