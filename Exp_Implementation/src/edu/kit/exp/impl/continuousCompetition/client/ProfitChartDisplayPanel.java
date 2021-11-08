package edu.kit.exp.impl.continuousCompetition.client;

import java.awt.*;

/**
 * Created by dschnurr on 27.02.14.
 */
public class ProfitChartDisplayPanel extends ChartDisplayPanel {


    public ProfitChartDisplayPanel(int chartWidth, int chartHeight, FirmDescription myRole, int timeStep, int duration,
                                   Color colorFirmA, Color colorFirmB, Color colorFirmC, Color colorMarketP, boolean isDiscreteTreatment, boolean isTriopolyTreatment) {
        this(chartWidth, chartHeight, myRole, timeStep, duration, colorFirmA, colorFirmB, colorFirmC, Color.GRAY, colorMarketP, isDiscreteTreatment, isTriopolyTreatment, false);
    }


    public ProfitChartDisplayPanel(int chartWidth, int chartHeight, FirmDescription myRole, int timeStep, int duration,
                                   Color colorFirmA, Color colorFirmB, Color colorFirmC, Color colorFirmD, Color colorMarketP, boolean isDiscreteTreatment, boolean isTriopolyTreatment, boolean isQuadropolyTreatment) {

        super(chartWidth, chartHeight, myRole, timeStep, duration, colorFirmA, colorFirmB, colorFirmC, colorFirmD,colorMarketP, isDiscreteTreatment, isTriopolyTreatment, isQuadropolyTreatment);
        this.p_value_max = 100;
        this.t_hatch_number = 20;
        this.p_hatch_number = 4;
        this.point_stroke = new BasicStroke(3.0f);
        calcDiagramParameters();
        log4j.info("Initialized CartDisplayPanel: timestep:  {}, tduration: {}, t_mid_window: {}, t_hatch: {}, t_startLastWindow: {}", timeStep, this.tDuration, this.t_mid_window, this.t_hatch, this.tStartOfLastWindow);
    }

    @Override
    protected void drawValues(Graphics2D g2, int x, int x_prev, Timestamp ts, Timestamp ts_prev)  {
        int y;
        int y_prev;

        g2.setPaint(colorFirmA);
        y = height_px - (( (int) ts.getProfitFirmA() * height_px) / p_value_max);
        y_prev = height_px - (( (int) ts_prev.getProfitFirmA() * height_px) / p_value_max);
        drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);

        g2.setPaint(colorFirmB);
        y = height_px - (( (int) ts.getProfitFirmB() * height_px) / p_value_max);
        y_prev = height_px - (( (int) ts_prev.getProfitFirmB() * height_px) / p_value_max);
        drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);

        if (isTriopolyTreatment || isQuadropolyTreatment) {
            g2.setPaint(colorFirmC);
            y = height_px - (( (int) ts.getProfitFirmC() * height_px) / p_value_max);
            y_prev = height_px - (( (int) ts_prev.getProfitFirmC() * height_px) / p_value_max);
            drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);
        }

        if (isQuadropolyTreatment) {
            g2.setPaint(colorFirmD);
            y = height_px - (( (int) ts.getProfitFirmD() * height_px) / p_value_max);
            y_prev = height_px - (( (int) ts_prev.getProfitFirmD() * height_px) / p_value_max);
            drawGraphLineWithPoints(g2, x_prev, y_prev, x, y);
        }

    }

}
