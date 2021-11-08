package edu.kit.exp.impl.continuousCompetition.client;

import javafx.scene.chart.Chart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by dschnurr on 08.10.14.
 */
public class ChartDisplayPanel extends JPanel {

    protected static final Logger log4j = LogManager.getLogger(ContinuousCompetitionFirmScreen.class.getName());

    boolean isDiscreteTreatment;
    boolean isTriopolyTreatment;
    boolean isQuadropolyTreatment;

    //number of hatches on axis and colors of graphs
    protected int t_hatch_number;
    protected int p_hatch_number;
    protected static final Color GRAPH_COLOR = Color.black;
    protected static final Stroke GRAPH_STROKE = new BasicStroke(1.2f);
    protected Stroke point_stroke;

    //time dimensions: time depicted on x-axis of chart
    protected int t_window_max;                                            //maximum time depicted on x-axis (in ms), default 300000
    protected int t_mid_window;                                            //point in time to start window sliding, default t_window_max/2
    protected int t_hatch;

    protected Color colorMarketP;
    protected Color colorFirmA;
    protected Color colorFirmB;
    protected Color colorFirmC;
    protected Color colorFirmD;


    //maximum profit depicted by y-axis of chart
    protected int p_value_max;

    //chart size: dimensions in pixels
    protected int width_px;
    protected int height_px;
    protected static final int PADX_LEFT_PX = 27;
    protected static final int PADY_BOTTOM_PX = 30;
    protected static final int PADY_TOP_PX = 5;

    protected int tDuration;                                                 //duration of one period (i.e., treatment)
    protected int timeStep;                                                  //update interval given by external scheduler
    protected int tStartOfLastWindow;                                        //point in time to stop window sliding


    FirmDescription myRole;
    protected java.util.List<Timestamp> tsList;


    public ChartDisplayPanel(int chartWidth, int chartHeight, FirmDescription myRole, int timeStep, int duration, Color colorFirmA,
                             Color colorFirmB, Color colorFirmC, Color colorMarketP, boolean isDiscreteTreatment, boolean isTriopolyTreatment) {

        // Constructor overloading: set colorFirmD as Gray and isQuadropolyTreatment as false
        this(chartWidth, chartHeight, myRole, timeStep, duration, colorFirmA, colorFirmB, colorFirmC, Color.GRAY, colorMarketP, isDiscreteTreatment, isTriopolyTreatment, false);
    }


    public ChartDisplayPanel(int chartWidth, int chartHeight, FirmDescription myRole, int timeStep, int duration, Color colorFirmA,
                             Color colorFirmB, Color colorFirmC, Color colorFirmD, Color colorMarketP, boolean isDiscreteTreatment, boolean isTriopolyTreatment, boolean isQuadropolyTreatment) {

        super(true);
        this.point_stroke = new BasicStroke(2.0f);
        this.tsList = new ArrayList<Timestamp>();

        width_px = chartWidth - PADX_LEFT_PX;
        height_px = chartHeight - PADY_TOP_PX;
        this.timeStep = timeStep;
        this.tDuration = duration;
        this.myRole = myRole;
        this.colorFirmA = colorFirmA;
        this.colorFirmB = colorFirmB;
        this.colorFirmC = colorFirmC;
        this.colorFirmD = colorFirmD;
        this.colorMarketP = colorMarketP;
        this.isDiscreteTreatment = isDiscreteTreatment;
        this.isTriopolyTreatment = isTriopolyTreatment;
        this.isQuadropolyTreatment = isQuadropolyTreatment;
        this.p_value_max  = 27;

        this.t_window_max = 300000;
        this.t_hatch_number = 10;
        this.p_hatch_number = 5;
        calcDiagramParameters();

    }

    public void setDiscreteTreatment(boolean discreteTreatment) {
        isDiscreteTreatment = discreteTreatment;
    }

    protected void calcDiagramParameters() {
        this.t_mid_window = (t_window_max /2);
        this.t_hatch = t_window_max / t_hatch_number;
        this.tStartOfLastWindow = tDuration- t_window_max + t_mid_window;
    }


    public void addNewTimestamp(Timestamp ts) {
        this.tsList.add(ts);
        log4j.trace("Received new timestamp: List now includes {} timestamps.", tsList.size());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(GRAPH_COLOR);
        g2.setStroke(GRAPH_STROKE);
        g.setFont(new Font(this.getFont().toString(), Font.PLAIN, 10));

        // Draw horizontal and vertical axis
        drawGraphLine(g2, 0, height_px, width_px, height_px);
        drawGraphLine(g2, 0, 0, 0, height_px);

        // Draw vertical hatches and labels
        int yHatch = height_px / p_hatch_number;
        for (int n=0; n<= p_hatch_number; n++) {

            // Draw hatches on vertical axis
            if (n <= p_hatch_number) {
                drawGraphLine(g2, -5, height_px-(n*yHatch), 5, height_px-(n*yHatch));
            } else {
                //drawGraphLine(g2, -5, height_px, 0, height_px);
            }



            // Draw value labels on vertical axis
            String hatch_labels = String.format("%02d", (p_hatch_number -n)*(p_value_max / p_hatch_number));

            //Adjust position of highest label (p_value_max)
            drawGraphString(hatch_labels, -27, n*yHatch+4, g2);


        }

        if (tsList.isEmpty()) {
            drawDefaultHatches(g2);
        } else {
            log4j.trace("Time according to last ts in tsList: {}", tsList.get(tsList.size()-1));
        }


        Timestamp ts_prev;
        // "Phase 1" (P1): Initial window starting at t = 0
        if (tsList.size() > 0 && (tsList.size()-1) < (t_mid_window /timeStep)) { // First phase: draw graph while window is still not sliding

            log4j.debug("P1: size of tsList {}", tsList.size());
            ts_prev = tsList.get(0);

            drawDefaultHatches(g2);

            Iterator<Timestamp> tsIterator = tsList.iterator();
            while(tsIterator.hasNext()) {
                Timestamp ts = tsIterator.next();
                int x = (ts.getTime()* width_px)/ t_window_max;
                int x_prev = (ts_prev.getTime()* width_px)/ t_window_max;

                drawValues(g2, x, x_prev, ts,ts_prev);

                ts_prev = ts;
            }

        } else {

            // "Phase 2" (P2): visible windows starts sliding as t_mid_window is reached
            if ( tsList.size() > 0 && (tsList.size()) >= (t_mid_window /timeStep) && (tsList.size()) <= (tStartOfLastWindow/timeStep) ) { //Second phase: draw graph while window is sliding

                log4j.debug("P2: size of tsList = {}", tsList.size());

                java.util.List<Timestamp> tsWindowList = tsList.subList(tsList.size()-(t_mid_window / timeStep)-1, tsList.size());
                ts_prev = tsWindowList.get(0);

                int deltaT = ts_prev.getTime();
                int tOffsetHatch = (deltaT) % t_hatch;
                int xOffsetHatch = ((tOffsetHatch* width_px)/ t_window_max);

                log4j.trace("P2: deltaT: {}, tOffsetHatch: {}, xOffesetHatch: {}", deltaT, tOffsetHatch, xOffsetHatch);
                log4j.trace("P2: Time of ts at starting index of tsWindowList: {}", tsList.get(tsList.size()-(t_mid_window / timeStep)-1).getTime());
                log4j.trace("P2: Time of ts at last index of tsWindowList: {}", tsList.get(tsList.size()-1).getTime());
                log4j.trace("P2: hatch calculation: {} + {} = {}", (t_hatch /timeStep), (deltaT/timeStep), (((t_hatch /timeStep)) + (deltaT/timeStep)));

                // Adjust hatches and labels of x-axis to sliding window
                for (int n = 1; n<= t_hatch_number; n++) {

                    int xHatch = ((n*width_px) / t_hatch_number)-xOffsetHatch;
                    drawGraphLine(g2, xHatch, height_px -5, xHatch, height_px +5);

                    g.setFont(new Font(this.getFont().toString(), Font.PLAIN, 10));
                    if (isDiscreteTreatment) {
                        String hatch_rounds = String.format("%02d", ((n * (t_hatch/timeStep)) + (deltaT/timeStep)) );
                        //drawGraphString(hatch_rounds, xHatch-4, height_px +20, g2);
                        g2.drawString(hatch_rounds, xHatch-4 + PADX_LEFT_PX, height_px+20);

                    } else {
                        String hatch_minutes = String.format("%02d", (((n*(t_hatch /1000))+(deltaT/1000))/60) );
                        String hatch_seconds = String.format("%02d", ((((n*(t_hatch /1000))%60) + ((((deltaT/1000)%60)/(t_hatch /1000))*(t_hatch /1000))))%60 );
                        g2.drawString(hatch_minutes+":"+hatch_seconds, xHatch-10+ PADX_LEFT_PX, height_px +20);
                    }
                }

                Iterator<Timestamp> tsWindowIterator = tsWindowList.iterator();
                while(tsWindowIterator.hasNext()) {
                    Timestamp ts = tsWindowIterator.next();

                    int x = ((ts.getTime()-deltaT)  * width_px)/ t_window_max;
                    int x_prev = ((ts_prev.getTime()-deltaT)* width_px)/ t_window_max;

                    drawValues(g2, x, x_prev, ts, ts_prev);
                    ts_prev = ts;
                }
            } else {

                // "Phase 3" (P3): sliding window stops as countdown approaches tDuration
                if (tsList.size() > 0 && (tsList.size()-1) > (t_mid_window /timeStep) && (tsList.size()) > (tStartOfLastWindow/timeStep) ) {

                    int tLengthOfLastWindow = t_window_max - (tDuration-(tsList.size())* timeStep);

                    log4j.debug("P3: size of tsList {}", tsList.size());
                    log4j.trace("P3: tLengthOfLastWindow: {}", tLengthOfLastWindow);


                    if (tLengthOfLastWindow <= t_window_max) {

                        java.util.List<Timestamp> tsWindowList = tsList.subList(tsList.size()-((tLengthOfLastWindow/ timeStep))-1, tsList.size());
                        ts_prev = tsWindowList.get(0);

                        int deltaT = ts_prev.getTime();
                        int tOffsetHatch = (deltaT) % t_hatch;
                        int xOffsetHatch = ((tOffsetHatch* width_px)/ t_window_max);

                        log4j.trace("P3: tsWindowList: start index: {} - last index: {}", tsList.size()-((tLengthOfLastWindow/ timeStep))-1, tsList.size());
                        log4j.trace("P3: Time of ts at starting index of tsWindowList: {}", tsList.get(tsList.size()-((tLengthOfLastWindow/ timeStep))).getTime());
                        log4j.trace("P3: Time of ts at last index of tsWindowList: {}", tsList.get(tsList.size()-1).getTime());
                        log4j.trace("P3: deltaT: {}, deltaT/timeStep {}", deltaT, (deltaT/timeStep));

                        // Adjust hatches and labels of x-axis to final window
                        for (int n = 1; n<= t_hatch_number; n++) {

                            int xHatch = ((n*width_px) / t_hatch_number)-xOffsetHatch;
                            drawGraphLine(g2, xHatch, height_px -5, xHatch, height_px +5);

                            g.setFont(new Font(this.getFont().toString(), Font.PLAIN, 10));
                            if (isDiscreteTreatment) {
                                String hatch_rounds = String.format("%02d", ((n*(t_hatch/timeStep)) + ((deltaT)/timeStep)) );
                                g2.drawString(hatch_rounds, xHatch-4 + PADX_LEFT_PX, height_px+20);

                            } else {
                                String hatch_minutes = String.format("%02d", (((n*(t_hatch /1000))+(deltaT/1000))/60) );
                                String hatch_seconds = String.format("%02d", ((((n*(t_hatch /1000))%60) + ((((deltaT/1000)%60)/(t_hatch /1000))*(t_hatch /1000))))%60 );
                                g2.drawString(hatch_minutes+":"+hatch_seconds, xHatch-10+ PADX_LEFT_PX, height_px +20);
                            }
                        }

                        Iterator<Timestamp> tsWindowIterator = tsWindowList.iterator();
                        while(tsWindowIterator.hasNext()) {
                            Timestamp ts = tsWindowIterator.next();

                            int x = ((ts.getTime() - deltaT) * width_px) / t_window_max;
                            int x_prev = ((ts_prev.getTime() - deltaT) * width_px) / t_window_max;

                            drawValues(g2, x, x_prev, ts, ts_prev);
                            ts_prev = ts;
                        }
                    }
                }
            }

        }
    }

    protected void drawDefaultHatches(Graphics2D g2) {

        for (int n = 0; n<= t_hatch_number; n++) {

            int xHatch = ((n*width_px) / t_hatch_number);
            drawGraphLine(g2, xHatch, height_px -5, xHatch, height_px +5);

            if (isDiscreteTreatment) {
                String hatch_rounds = String.format("%02d", (n*(t_hatch / timeStep)));
                drawGraphString(hatch_rounds, xHatch-4, height_px +20, g2);

            } else {
                String hatch_minutes = String.format("%02d", ((n*(t_hatch /1000))/60));
                String hatch_seconds = String.format("%02d", ((n*(t_hatch /1000))%60));
                drawGraphString(hatch_minutes+":"+hatch_seconds, xHatch-10, height_px +20, g2);
            }

        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width_px + PADX_LEFT_PX, height_px + PADY_BOTTOM_PX + PADY_TOP_PX);
    }

    protected void drawValues(Graphics2D g2, int x, int x_prev, Timestamp ts, Timestamp ts_prev)  {

    }

    protected void drawGraphLineWithPoints(Graphics2D g2, int x_prev, int y_prev, int x, int y) {
        drawGraphLine(g2, x_prev, y_prev, x, y);
        if (isDiscreteTreatment) {
            drawPoints(g2, x_prev, y_prev);
            drawPoints(g2, x, y);
        }
    }

    protected void drawPoints(Graphics2D g2, int x, int y) {
        g2.setStroke(point_stroke);
        g2.drawLine(x+ PADX_LEFT_PX, y+PADY_TOP_PX, x+ PADX_LEFT_PX, y+PADY_TOP_PX);
        g2.setStroke(GRAPH_STROKE);
    }

    protected void drawGraphLine(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.drawLine(x1+ PADX_LEFT_PX, y1+PADY_TOP_PX, x2+ PADX_LEFT_PX, y2+PADY_TOP_PX);
    }

    protected void drawGraphString(String str, int x, int y, Graphics2D g2) {
        g2.drawString(str,  x+ PADX_LEFT_PX, y+PADY_TOP_PX);
    }

    /*
    private static void createAndShowGui() {

        List<Timestamp> sampleTSList = new ArrayList<Timestamp>();

        for (int t = 0; t <= 100 ; t++) {
            Timestamp ts = new Timestamp(t*1000, 12, 15, 30, 35, 25);
            sampleTSList.add(ts);
            System.out.println(ts+"t: "+ts.getTime()+" aFirmA: "+ts.getaFirmA()+ " aFirmB(): "+ts.getaFirmB());
        }

        ProfitDisplayPanel mainPanel = new ProfitDisplayPanel(sampleTSList);

        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(255, 255, 255, 100));
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        //frame.setBackground(new Color(255, 255, 255, 100));
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }
    */
}
