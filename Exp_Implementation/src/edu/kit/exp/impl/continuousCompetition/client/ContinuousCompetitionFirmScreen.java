package edu.kit.exp.impl.continuousCompetition.client;

import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.impl.continuousCompetition.server.ContinuousCompetitionMarketDataCalculator;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dschnurr on 06.03.14.
 */
public class ContinuousCompetitionFirmScreen extends Screen {

    private static final Logger log4j = LogManager.getLogger(ContinuousCompetitionFirmScreen.class.getName());

    boolean isDiscreteTreatment = false;
    boolean isCournotTreatment;
    boolean isTriopolyTreatment;
    protected double diffParam;
    protected ContinuousCompetitionMarketDataCalculator marketDataCalculator;

    JFrame frmOpt;
    protected JPanel backgroundPanel;
    protected int widthPanel = 1240;
    protected int widthLeftSide = 850;
    protected int widthRightSide = widthPanel-widthLeftSide-20;
    protected int widthRightColumn = widthRightSide / 2;

    protected FirmDescription myRole;
    protected int myRoleCode;
    protected int localTime = 0;

    protected List<Timestamp> tsList;
    protected int timeStep;
    protected int duration;
    protected int countId = 0;
    protected boolean practiceRound;
    protected boolean periodFinished = false;
    protected int maxRoundNumber;

    protected int pFirmA;
    protected int pFirmB;
    protected int pFirmC;
    protected double qFirmA;
    protected double qFirmB;
    protected double qFirmC;

    protected double pMarket;
    protected double qMarket;

    protected double balanceFirmA;
    protected double balanceFirmB;
    protected double balanceFirmC;
    protected double profitFirmA;
    protected double profitFirmB;
    protected double profitFirmC;
    protected boolean firmAOutOfMarket = false;
    protected boolean firmBOutOfMarket = false;
    protected boolean FirmCOutOfMarket = false;

    protected ContinuousCompetitionParamObject latestMarketUpdate;

    protected JSlider pFirmASlider;
    protected JSlider pFirmBSlider;
    protected JSlider pFirmCSlider;
    protected int sliderMin = 0;
    protected int sliderMax = 100;
    protected int sliderValue = 1;

    protected JPanel profitDisplayPanel;
    protected JPanel profitDescriptionDisplayPanel;
    protected ProfitChartDisplayPanel profitChartDisplayPanel;
    protected JPanel profitTextDisplayPanel;

    protected JPanel priceDisplayPanel;
    protected JPanel priceDescriptionDisplayPanel;
    protected ChartDisplayPanel priceChartDisplayPanel;
    protected JPanel priceTextDisplayPanel;

    protected JLabel pFirmADescriptionLabel;
    protected JLabel pFirmAValueLabel;
    protected JLabel pFirmAUnitLabel;

    protected JLabel pFirmBDescriptionLabel;
    protected JLabel pFirmBValueLabel;
    protected JLabel pFirmBUnitLabel;

    protected JLabel pFirmCDescriptionLabel;
    protected JLabel pFirmCValueLabel;
    protected JLabel pFirmCUnitLabel;

    protected JLabel pMarketDescriptionLabel;
    protected JLabel pMarketValueLabel;
    protected JLabel pMarketUnitLabel;

    protected JLabel pFirmADescriptionDisplayLabel;
    protected JLabel pFirmBDescriptionDisplayLabel;
    protected JLabel pFirmCDescriptionDisplayLabel;
    protected JLabel pMarketDescriptionDisplayLabel;

    protected JPanel quantityDisplayPanel;
    protected JPanel quantityDescriptionDisplayPanel;
    protected ChartDisplayPanel quantityChartDisplayPanel;
    protected JPanel quantityTextDisplayPanel;

    protected JLabel qFirmADescriptionLabel;
    protected JLabel qFirmAValueLabel;
    protected JLabel qFirmAUnitLabel;

    protected JLabel qFirmBDescriptionLabel;
    protected JLabel qFirmBValueLabel;
    protected JLabel qFirmBUnitLabel;

    protected JLabel qFirmCDescriptionLabel;
    protected JLabel qFirmCValueLabel;
    protected JLabel qFirmCUnitLabel;

    protected JLabel qMarketDescriptionLabel;
    protected JLabel qMarketValueLabel;
    protected JLabel qMarketUnitLabel;

    protected JLabel qFirmADescriptionDisplayLabel;
    protected JLabel qFirmBDescriptionDisplayLabel;
    protected JLabel qFirmCDescriptionDisplayLabel;
    protected JLabel qMarketDescriptionDisplayLabel;

    protected JPanel headerPanel;
    protected JPanel retailPricingPanel;
    protected JPanel retailPricingTextDisplayPanel;
    protected JPanel retailPricingFirmAPanel;
    protected JPanel retailPricingFirmBPanel;
    protected JPanel retailPricingFirmCPanel;

    protected JButton readyButton;
    protected JButton restoreButton;

    protected JLabel headerLabel;
    protected JLabel counterLabel;
    protected JLabel headerFirmLabel;
    protected JLabel profitFirmADescriptionChartDisplayLabel;
    protected JLabel profitFirmBDescriptionChartDisplayLabel;
    protected JLabel profitFirmCDescriptionChartDisplayLabel;

    protected JLabel balanceDescriptionLabel;
    protected JLabel balanceValueLabel;
    protected JLabel balanceUnitLabel;


    protected JLabel profitFirmADescriptionLabel;
    protected JLabel profitFirmBDescriptionLabel;
    protected JLabel profitFirmCDescriptionLabel;

    protected JLabel profitFirmAUnitLabel;
    protected JLabel profitFirmBUnitLabel;
    protected JLabel profitFirmCUnitLabel;

    protected JLabel profitFirmAValueLabel;
    protected JLabel profitFirmBValueLabel;
    protected JLabel profitFirmCValueLabel;

    protected JLabel firmALabel;
    protected JLabel firmBLabel;
    protected JLabel FirmCLabel;
    protected JLabel unitLabel;

    protected JLabel pFirmALabel;
    protected JLabel pFirmBLabel;
    protected JLabel pFirmCLabel;

    protected JLabel firmAOutOfMarketLabel;
    protected JLabel firmBOutOfMarketLabel;
    protected JLabel FirmCOutOfMarketLabel;

    protected JTextField pFirmATextField;
    protected JTextField pFirmBTextField;
    protected JTextField pFirmCTextField;

    protected JPanel waitingPanel;
    protected JLabel waitingMessageLabel;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    final DecimalFormat df = new DecimalFormat("00");

    protected final float  FONT_SIZE_PROFIT = 16.0f;

    protected static final Color COLOR_MARKET = new Color(160, 160, 160, 255);

    protected static final Color COLOR_FIRM_A = new Color(30, 30, 204, 200);
    protected static final Color COLOR_FIRM_B = new Color(31, 119, 80);
    protected static final Color COLOR_FIRM_E = new Color(204, 98, 26, 190);

    protected static final Color COLOR_LIGHT_FIRM_A = new Color(144, 174, 210);
    protected static final Color COLOR_LIGHT_FIRM_B = new Color(111, 174, 142);
    protected static final Color COLOR_LIGHT_FIRM_E = new Color(253, 206, 137);

    SubjectGroup subjectGroup;
    Subject subject;
    String screenName;



    /**
     * Creates an instance of screen.
     *
     * @param gameId     ID of the running game, whose institution triggered that
     *                   screen to be shown at the client.
     * @param parameter  A List of all parameters used in this screen. i.e. text.
     * @param screenId   The global screen id has to be given for a complete trial
     *                   entry at server side.
     * @param showUpTime
     */
    public ContinuousCompetitionFirmScreen(String gameId, ContinuousCompetitionParamObject parameter, String screenId, Long showUpTime) {
        super(gameId, parameter, screenId, showUpTime);
        log4j.trace("Start constructor ContinuousCompetitionFirmScreen()");

        this.isTriopolyTreatment = parameter.isTriopolyTreatment();
        this.isCournotTreatment = parameter.isCournotTreatment();
        this.diffParam = parameter.getDiffParam();

        // time management
        this.isDiscreteTreatment = parameter.isDiscreteTreatment();
        this.practiceRound = parameter.isPracticeRound();
        this.duration = parameter.getDuration(); //30 minutes: 1800*1000
        this.localTime = 0;

        // discrete treatment: timestep denotes the seconds per discrete round (default: 15000ms)
        // continuous treatment: timestep denotes the minimum time period (default: 500ms) and minimum clock cycle
        this.timeStep = parameter.getUpdateTimeStep();

        if (isDiscreteTreatment) {
            this.maxRoundNumber = duration / timeStep;
        }

        this.tsList = new ArrayList<Timestamp>();

        // identity and role initialization
        myRole = parameter.getRole();
        myRoleCode = parameter.getRoleCode();           // roles are codified as follows: (1) firmA, (2) firmB, (3) FirmC
        subjectGroup =  parameter.getSubjectGroup();
        subject = parameter.getSubject();
        screenName =  parameter.getScreenName();

        marketDataCalculator = new ContinuousCompetitionMarketDataCalculator(diffParam, isCournotTreatment, isTriopolyTreatment);

        log4j.info("Started ContinuousCompetitionFirmScreen for client {} in cohort {} with diffParam = {}, discreteTreatment = {}, practiceRound = {}, myRole = {}", subject.getIdClient(), subjectGroup.getIdSubjectGroup(), diffParam, isDiscreteTreatment, practiceRound, myRole);

        initPanel();                                    // default screen size: 1280, 1024
        initWaitingPanel();

        if (isCournotTreatment) { setupCournotPanels();}
        if (!isTriopolyTreatment) { setupDuopolyPanels();}

        updateStateOfLocalVariables(parameter);
        latestMarketUpdate = parameter;
        initValues();

        log4j.warn("Local state of profit variables after initial market update: aFirmA: profitFirmA: {}, profitFirmB: {}, profitFirmC: {}", profitFirmA, profitFirmB, profitFirmC);

        if (practiceRound || isDiscreteTreatment) {
            initDiscreteActionListeners();
        } else {
            initContinuousActionListeners();

            log4j.info(myRole + ": sending ready message to server.");
            ContinuousCompetitionResponseObject readyUpdate = new ContinuousCompetitionResponseObject();
            readyUpdate.setClientReady(true);
            sendResponse(readyUpdate);
        }

        log4j.trace("End constructor ContinuousCompetitionFirmScreen()");
    }


    // INPUT PROCESSING
    //
    // process incoming market update from server: update market data variables, gui elements and chart display panels
    // * in discrete setting: re-activate sliders; activate background panel and disable waiting panel

    @Override
    public void processParamObjectUpdate(IScreenParamObject paramObject) {
        log4j.debug("processParamObjectUpdate(): start of execution");
        if (paramObject instanceof ContinuousCompetitionParamObject) {

            // Convert parameter Object in market update and keep track of latest market update
            ContinuousCompetitionParamObject marketUpdate = (ContinuousCompetitionParamObject) paramObject;
            latestMarketUpdate = marketUpdate;

            log4j.info("Recipient {} received MarketUpdate with countId {} and globalTime {} in cohort {}", subject.getIdClient(), marketUpdate.getCountId(), marketUpdate.getGlobalTime(), subjectGroup.getIdSubjectGroup());
            log4j.info("Updating local market variables: pFirmA: {} pFirmB: {}, pFirmC: {}, pMarket: {}", marketUpdate.getaFirmA(), marketUpdate.getaFirmB(), marketUpdate.getaFirmC(), marketUpdate.getaMarket());

            updateStateOfLocalVariables(marketUpdate);      // update market variables and gui elements according to market update sent by Institution

            updateBalanceDataAndGUI(marketUpdate);          // update aggregated profit variables and personal gui element

            updateCounterLabel();                           // update gui time / round gui element

            updateTimestampListAndChartDisplayPanels();     // create new timestamp based on market variables and repaint ChartDisplayPanels

            if (!isDiscreteTreatment && marketUpdate.getStartFlag()) {
                log4j.info("Recipient {} (role: {}) in cohort {} also received start flag - starting timer.", subject.getIdClient(), myRole, subjectGroup.getIdSubjectGroup());
                initTimer();
            }

            if (isDiscreteTreatment) {
                pFirmASlider.setEnabled(true);
                pFirmBSlider.setEnabled(true);
                pFirmCSlider.setEnabled(true);
                readyButton.setVisible(true);
                waitingPanel.setVisible(false);
                backgroundPanel.setVisible(true);
            }

        } else {
            log4j.fatal("Error: paramObject is not instance of UpstreamCompetitionParamObject");
        }
        log4j.trace("processParamObjectUpdate(): end of execution");
    }


    // OUTPUT PROCESSING
    //
    // send price update that includes only own price decision

    private void sendPriceUpdate() {
        log4j.debug("sendPriceUpdate(): start of execution");
        ContinuousCompetitionResponseObject priceUpdate = new ContinuousCompetitionResponseObject();
        priceUpdate.setCountId(countId);
        priceUpdate.setLocalTime(localTime);
        priceUpdate.setRoleCode(myRoleCode);

        if (practiceRound) { priceUpdate.setPracticeRoundFinished(true); }
        if (periodFinished) { priceUpdate.setClientFinished(true); }

        if (myRole == FirmDescription.FirmA) {
            priceUpdate.setaFirmA(pFirmA);
            log4j.info("Firm A: Sending priceUpdate with countId {} and time {} in round {} - pFirmA: {}.", countId, localTime, (localTime/timeStep), pFirmA);
        } else {
            if (myRole == FirmDescription.FirmB) {
                priceUpdate.setaFirmB(pFirmB);
                priceUpdate.setBalanceFirmB(balanceFirmB);
                log4j.info("Firm B: Sending priceUpdate with countId {} and time {} in round {} - pFirmB: {}.", countId, localTime, (localTime/timeStep), pFirmB);
            } else {
                //myRole == FirmDescription.FirmC
                priceUpdate.setaFirmC(pFirmC);
                priceUpdate.setBalanceFirmC(balanceFirmC);
                log4j.info("Firm E: Sending priceUpdate with countId {} and time {} in round {} - pFirmC: {}.", countId, localTime, (localTime/timeStep), pFirmC);
            }
        }
        log4j.info("Last state of local prices (sliders) in round {} before price update: pFirmA {}, pFirmB {}, pFirmC {}.", (localTime/timeStep), pFirmA, pFirmB, pFirmC);
        sendResponse(priceUpdate);

        log4j.debug("sendPriceUpdate(): end of execution - countId: {}", priceUpdate.getCountId());
    }

    // initial setting of balance variables and price labels

    private void initValues() {
        pFirmAValueLabel.setText(String.valueOf(df.format(pFirmA)));
        pFirmBValueLabel.setText(String.valueOf(df.format(pFirmB)));
        pFirmCValueLabel.setText(String.valueOf(df.format(pFirmC)));
    }

    // action listeners activated in discrete and practice setting - slider input triggers instant calculation of market data

    public void initDiscreteActionListeners() {

        pFirmASlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!pFirmASlider.getValueIsAdjusting()) {
                    pFirmA = pFirmASlider.getValue();
                    pFirmATextField.setText(String.valueOf(pFirmA));
                    pFirmAValueLabel.setText(String.valueOf(df.format(pFirmA)));
                    log4j.trace("SliderChangeListener: " + pFirmA);
                    calculateDiscreteRoundMarketData();
                }
            }
        });

        pFirmATextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pFirmA = Integer.parseInt(pFirmATextField.getText());
                pFirmASlider.setValue(pFirmA);
                pFirmAValueLabel.setText(String.valueOf(df.format(pFirmA)));
                log4j.trace("TextFieldChangeListener: " + pFirmA);
            }
        });

        pFirmBSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!pFirmBSlider.getValueIsAdjusting()) {
                    pFirmB = pFirmBSlider.getValue();
                    pFirmBTextField.setText(String.valueOf(pFirmB));
                    pFirmBValueLabel.setText(String.valueOf(df.format(pFirmB)));
                    calculateDiscreteRoundMarketData();
                }
            }
        });

        pFirmBTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pFirmB = Integer.parseInt(pFirmBTextField.getText());
                pFirmBSlider.setValue(pFirmB);
                pFirmBValueLabel.setText(String.valueOf(df.format(pFirmB)));
            }
        });

        pFirmCSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!pFirmCSlider.getValueIsAdjusting()) {
                    pFirmC = pFirmCSlider.getValue();
                    pFirmCTextField.setText(String.valueOf(pFirmC));
                    pFirmCValueLabel.setText(String.valueOf(df.format(pFirmC)));
                    calculateDiscreteRoundMarketData();
                }
            }
        });

        pFirmCTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pFirmC = Integer.parseInt(pFirmCTextField.getText());
                pFirmCSlider.setValue(pFirmC);
                pFirmCValueLabel.setText(String.valueOf(df.format(pFirmC)));
            }
        });


        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int response = 0;
                if (practiceRound) {
                    if (frmOpt == null) {
                        frmOpt = new JFrame();
                    }
                    frmOpt.setVisible(true);
                    frmOpt.setLocation((backgroundPanel.getWidth() / 2), (backgroundPanel.getHeight() / 2) -200);
                    frmOpt.setAlwaysOnTop(true);
                    response = JOptionPane.showConfirmDialog(frmOpt, "Wollen Sie Ihre Enscheidung endgültig treffen?", "Entscheidung treffen", JOptionPane.YES_NO_OPTION);

                }

                if (response == JOptionPane.YES_OPTION || isDiscreteTreatment) {
                    if (isDiscreteTreatment && !practiceRound) {

                        backgroundPanel.setVisible(false);
                        waitingPanel.setVisible(true);
                        pFirmASlider.setEnabled(false);
                        pFirmBSlider.setEnabled(false);
                        pFirmCSlider.setEnabled(false);
                        countId = countId + 1;
                        int currentRound = convertTimeToCurrentRound();
                        if (currentRound < maxRoundNumber) {
                            waitingMessageLabel.setText("<html><body><h1>Bitte warten Sie...</h1><br>Nächste Periode: " + (currentRound + 1) + " von " + maxRoundNumber + "</body></html>");
                        } else {
                            waitingMessageLabel.setText("<html><body><h1>Bitte warten Sie...</h1></body></html>");
                        }

                        if (localTime < (duration - timeStep)) {
                            localTime = localTime + timeStep;
                            log4j.info("After confirmation of prices, current local time set to {} and current round to {}.", localTime, localTime/timeStep );
                        } else {
                            periodFinished = true;
                            localTime = localTime + timeStep;
                            log4j.warn("Stopped period at {} in round {}. Set periodFinished to {}", localTime, localTime/timeStep, periodFinished);
                        }

                        readyButton.setVisible(false);
                    }
                    sendPriceUpdate();
                }

                if (practiceRound) {
                    frmOpt.dispose();
                }
            }

        });



        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log4j.warn("Restore results of last round: "+ latestMarketUpdate);
                updateStateOfLocalVariables(latestMarketUpdate);
                if (myRole == FirmDescription.FirmA) {
                    pFirmA = latestMarketUpdate.getaFirmA();
                    pFirmASlider.setValue(pFirmA);
                    pFirmATextField.setText(String.valueOf(pFirmA));
                } else {
                    if (myRole == FirmDescription.FirmB) {
                        pFirmB = latestMarketUpdate.getaFirmB();
                        pFirmBSlider.setValue(pFirmB);
                        pFirmBTextField.setText(String.valueOf(pFirmB));

                    } else {
                        //myRole == FirmDescription.FirmC
                        pFirmC = latestMarketUpdate.getaFirmC();
                        pFirmCSlider.setValue(pFirmC);
                        pFirmCTextField.setText(String.valueOf(pFirmC));
                    }
                }
            }
        });

    }

    public void calculateDiscreteRoundMarketData() {

        ContinuousCompetitionParamObject practiceMarketUpdate = marketDataCalculator.calculateMarketData(pFirmA, pFirmB, pFirmC);
        practiceMarketUpdate.setGlobalTime(localTime);

        updateStateOfLocalVariables(practiceMarketUpdate);
    }

    // action listeners activated in continuous setting - price update is triggered by timer

    public void initContinuousActionListeners() {

        if (myRole == FirmDescription.FirmA){
            // Activate ActionListeners for Firm A
            pFirmASlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!pFirmASlider.getValueIsAdjusting()) {
                        pFirmA = pFirmASlider.getValue();
                        pFirmATextField.setText(String.valueOf(pFirmA));
                        pFirmAValueLabel.setText(String.valueOf(pFirmA));
                        log4j.trace("SliderChangeListener: " + pFirmA);
                    }
                }
            });

            pFirmATextField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pFirmA = Integer.parseInt(pFirmATextField.getText());
                    pFirmASlider.setValue(pFirmA);
                    pFirmAValueLabel.setText(String.valueOf(pFirmA));
                    log4j.trace("TextFieldChangeListener: " + pFirmA);
                }
            });
        } else {
            // Activate ActionListeners for Firm B
            if (myRole == FirmDescription.FirmB) {

                pFirmBSlider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (!pFirmBSlider.getValueIsAdjusting()) {
                            pFirmB = pFirmBSlider.getValue();
                            pFirmBTextField.setText(String.valueOf(pFirmB));
                            pFirmBValueLabel.setText(String.valueOf(pFirmB));
                        }
                    }
                });

                pFirmBTextField.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        pFirmB = Integer.parseInt(pFirmBTextField.getText());
                        pFirmBSlider.setValue(pFirmB);
                        pFirmBValueLabel.setText(String.valueOf(pFirmB));
                    }
                });
            } else {
                // Activate ActionListeners for Firm E
                pFirmCSlider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (!pFirmCSlider.getValueIsAdjusting()) {
                            pFirmC = pFirmCSlider.getValue();
                            pFirmCTextField.setText(String.valueOf(pFirmC));
                            pFirmCValueLabel.setText(String.valueOf(pFirmC));
                        }
                    }
                });

                pFirmCTextField.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        pFirmC = Integer.parseInt(pFirmCTextField.getText());
                        pFirmCSlider.setValue(pFirmC);
                        pFirmCValueLabel.setText(String.valueOf(pFirmC));
                    }
                });
            }
        }
    }

    // timer activated in continuous setting - regular update of prices according to chosen timestep

    public void initTimer() {
        localTime = 0;

        int delay = timeStep;

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                sendPriceUpdate();
                countId = countId +1;

                if (localTime < (duration-timeStep)) {
                    localTime = localTime + timeStep;
                    log4j.info("Timer: Advanced local time from {} to {}. Duration is set to: {}", localTime-timeStep, localTime, duration);
                } else {
                    ((Timer) evt.getSource()).stop();
                    periodFinished = true;
                    sendPriceUpdate();
                    log4j.warn("Stopped timer at {}. Set periodFinished to {}", localTime, periodFinished);
                }
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    // update methods, called during input processing

    private void updateStateOfLocalVariables(ContinuousCompetitionParamObject marketUpdate) {

        if  (marketUpdate.isInitialUpdate()) {
            pFirmA = marketUpdate.getaFirmA();
            pFirmB = marketUpdate.getaFirmB();
            pFirmC = marketUpdate.getaFirmC();
        } else {
            if (!practiceRound) {
                if (myRole == FirmDescription.FirmA) {
                    pFirmB = marketUpdate.getaFirmB();
                    pFirmC = marketUpdate.getaFirmC();
                } else {
                    if (myRole == FirmDescription.FirmB) {
                        pFirmA = marketUpdate.getaFirmA();
                        pFirmC = marketUpdate.getaFirmC();
                    } else {
                        pFirmA = marketUpdate.getaFirmA();
                        pFirmB = marketUpdate.getaFirmB();
                    }
                }
            }
        }

        pMarket = marketUpdate.getaMarket();
        qMarket = marketUpdate.getoMarket();

        qFirmA = marketUpdate.getoFirmA();
        qFirmB = marketUpdate.getoFirmB();
        qFirmC = marketUpdate.getoFirmC();

        if (!isCournotTreatment) {
            firmAOutOfMarket = marketUpdate.getoFirmA() <= 0.0;
            firmBOutOfMarket = marketUpdate.getoFirmB() <= 0.0;
            FirmCOutOfMarket = marketUpdate.getoFirmC() <= 0.0;
        }

        profitFirmA = marketUpdate.getProfitFirmA();
        profitFirmB = marketUpdate.getProfitFirmB();
        profitFirmC = marketUpdate.getProfitFirmC();


        updateGUItoParameters(marketUpdate.isInitialUpdate());
    }

    private void updateGUItoParameters(boolean isInitialUpdate) {
        log4j.trace("updateGUItoParameters(): start of execution");
        DecimalFormat df = new DecimalFormat("00.00");
        DecimalFormat sdf = new DecimalFormat("00");


        if (isInitialUpdate) {
            pFirmASlider.setValue(pFirmA);
            pFirmATextField.setText(String.valueOf(pFirmA));
            pFirmBSlider.setValue(pFirmB);
            pFirmBTextField.setText(String.valueOf(pFirmB));
            pFirmCSlider.setValue(pFirmC);
            pFirmCTextField.setText(String.valueOf(pFirmC));

        } else {
            if (!practiceRound) {
                if (myRole == FirmDescription.FirmA) {
                    pFirmBSlider.setValue(pFirmB);
                    pFirmBTextField.setText(String.valueOf(pFirmB));
                    pFirmBValueLabel.setText(String.valueOf(sdf.format(pFirmB)));
                    pFirmCSlider.setValue(pFirmC);
                    pFirmCTextField.setText(String.valueOf(pFirmC));
                    pFirmCValueLabel.setText(String.valueOf(sdf.format(pFirmC)));

                } else {
                    if (myRole == FirmDescription.FirmB) {
                        pFirmASlider.setValue(pFirmA);
                        pFirmATextField.setText(String.valueOf(pFirmA));
                        pFirmAValueLabel.setText(String.valueOf(sdf.format(pFirmA)));
                        pFirmCSlider.setValue(pFirmC);
                        pFirmCTextField.setText(String.valueOf(pFirmC));
                        pFirmCValueLabel.setText(String.valueOf(sdf.format(pFirmC)));

                    } else {
                        //myRole == FirmDescription.FirmC
                        pFirmASlider.setValue(pFirmA);
                        pFirmATextField.setText(String.valueOf(pFirmA));
                        pFirmAValueLabel.setText(String.valueOf(sdf.format(pFirmA)));
                        pFirmBSlider.setValue(pFirmB);
                        pFirmBTextField.setText(String.valueOf(pFirmB));
                        pFirmBValueLabel.setText(String.valueOf(sdf.format(pFirmB)));
                    }
                }
            }
        }

        firmAOutOfMarketLabel.setVisible(firmAOutOfMarket);
        firmBOutOfMarketLabel.setVisible(firmBOutOfMarket);
        FirmCOutOfMarketLabel.setVisible(FirmCOutOfMarket);

        qFirmAValueLabel.setText(String.valueOf(sdf.format(qFirmA)));
        qFirmBValueLabel.setText(String.valueOf(sdf.format(qFirmB)));
        qFirmCValueLabel.setText(String.valueOf(sdf.format(qFirmC)));

        pMarketValueLabel.setText(String.valueOf(sdf.format(pMarket)));
        qMarketValueLabel.setText(String.valueOf(sdf.format(qMarket)));

        profitFirmAValueLabel.setText(String.valueOf(df.format(profitFirmA)));
        profitFirmBValueLabel.setText(String.valueOf(df.format(profitFirmB)));
        profitFirmCValueLabel.setText(String.valueOf(df.format(profitFirmC)));

        log4j.trace("updateGUItoParameters(): end of execution");
    }

    private void updateTimestampListAndChartDisplayPanels() {
        Timestamp ts = new Timestamp();
        updateTimestamp(ts);
        tsList.add(ts);

        profitChartDisplayPanel.addNewTimestamp(ts);
        profitChartDisplayPanel.repaint();

        priceChartDisplayPanel.addNewTimestamp(ts);
        priceChartDisplayPanel.repaint();

        quantityChartDisplayPanel.addNewTimestamp(ts);
        quantityChartDisplayPanel.repaint();
    }

    private void updateTimestamp(Timestamp ts){

        if (isDiscreteTreatment) {
            ts.setDiscrete(true);
        } else {
            ts.setDiscrete(false);
        }
        ts.setTime(localTime);
        ts.setpFirmA(pFirmA);
        ts.setpFirmB(pFirmB);
        ts.setpFirmC(pFirmC);
        ts.setqFirmA(qFirmA);
        ts.setqFirmB(qFirmB);
        ts.setqFirmC(qFirmC);
        ts.setpMarket(pMarket);
        ts.setqMarket(qMarket);
        ts.setProfitFirmA(profitFirmA);
        ts.setProfitFirmB(profitFirmB);
        ts.setProfitFirmC(profitFirmC);
    }

    private void updateBalanceDataAndGUI(ContinuousCompetitionParamObject marketUpdate) {
        DecimalFormat df = new DecimalFormat("00.00");

        balanceFirmA = marketUpdate.getBalanceFirmA();
        balanceFirmB = marketUpdate.getBalanceFirmB();
        balanceFirmC = marketUpdate.getBalanceFirmC();


        // update GUI
        if (myRole == FirmDescription.FirmA) {
            balanceValueLabel.setText(String.valueOf(df.format(balanceFirmA)));

        } else {
            if (myRole == FirmDescription.FirmB) {
                balanceValueLabel.setText(String.valueOf(df.format(balanceFirmB)));

            } else {
                //myRole == FirmDescription.FirmC
                balanceValueLabel.setText(String.valueOf(df.format(balanceFirmC)));
            }
        }
    }

    private void updateCounterLabel() {
        if (isDiscreteTreatment) {
            String roundNumberString = String.format("%02d", convertTimeToCurrentRound());
            String maxRoundNumberString = String.format("%02d", maxRoundNumber);
            counterLabel.setText("Aktuelle Periode: "+roundNumberString + " von " + maxRoundNumberString);
        } else {
            String currentMinutes = String.format("%02d", ((localTime)/(1000*60)));
            String currentSeconds = String.format("%02d", ((localTime/1000)%60));

            String maxMinutes = String.format("%02d", ((duration)/(1000*60)));
            String maxSeconds = String.format("%02d", ((duration/1000)%60));

            counterLabel.setText("Dauer: "+currentMinutes+ ":" +currentSeconds+ " von " +maxMinutes+ ":" +maxSeconds);
        }
    }




    // create and setup gui panels

    private void initPanel() {

        backgroundPanel = new JPanel(new GridBagLayout());
        //backgroundPanel.setPreferredSize(panelSize);
        setPanelSize(backgroundPanel, 1280, 980);
        //backgroundPanel.setBackground(Color.RED);
        GridBagConstraints c = new GridBagConstraints();

        createDefaultLabels();

        headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setPanelSize(headerPanel, widthPanel, 30);
        createHeaderPanel();
        c.anchor = GridBagConstraints.PAGE_START;
        c.ipadx = 20;
        c.ipady = 5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.weighty = 0.1;
        backgroundPanel.add(headerPanel, c);

        profitDisplayPanel = new JPanel(new GridBagLayout());
        profitDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Gewinnhistorie"));
        setPanelSize(profitDisplayPanel, widthLeftSide, 190); //previously, display height was 250 to include profitTextDisplayPanel
        createProfitDisplayPanel();
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0,0,0,0);
        c.ipadx = 20;
        c.ipady = 10;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weighty = 0.1;
        backgroundPanel.add(profitDisplayPanel, c);

        profitTextDisplayPanel = new JPanel(new GridBagLayout());
        profitTextDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Momentaner Gewinn"));
        setPanelSize(profitTextDisplayPanel, widthRightColumn, 190);
        createProfitTextDisplayPanel();
        c.ipadx = 20;
        c.ipady = 10;
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        backgroundPanel.add(profitTextDisplayPanel, c);

        quantityDisplayPanel = new JPanel(new GridBagLayout());
        quantityDisplayPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Mengenhistorie"));
        setPanelSize(quantityDisplayPanel, widthLeftSide, 190);
        createQuantityDisplayPanel();
        c.insets = new Insets(5,0,0,0);
        c.ipadx = 20;
        c.ipady = 10;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        backgroundPanel.add(quantityDisplayPanel, c);

        quantityTextDisplayPanel = new JPanel(new GridBagLayout());
        quantityTextDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Momentane Mengen"));
        createQuantityTextDisplayPanel();
        setPanelSize(quantityTextDisplayPanel, widthRightSide, 190);
        c.ipadx = 20;
        c.ipady = 10;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(quantityTextDisplayPanel, c);

        priceDisplayPanel = new JPanel(new GridBagLayout());
        priceDisplayPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Preishistorie"));
        setPanelSize(priceDisplayPanel, widthLeftSide, 190);
        createPriceDisplayPanel();
        c.insets = new Insets(5,0,0,0);
        c.ipadx = 20;
        c.ipady = 10;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        backgroundPanel.add(priceDisplayPanel, c);

        priceTextDisplayPanel = new JPanel(new GridBagLayout());
        priceTextDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Momentane Preise"));
        setPanelSize(priceTextDisplayPanel, widthRightSide, 190);
        createPriceTextDisplayPanel();
        c.insets = new Insets(5,0,0,0);
        c.ipadx = 20;
        c.ipady = 10;
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(priceTextDisplayPanel, c);

        retailPricingPanel = new JPanel(new GridBagLayout());
        retailPricingPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Festlegen der Preise"));
        setPanelSize(retailPricingPanel, widthLeftSide, 270);
        createRetailPricingPanel();
        c.insets = new Insets(5,0,0,0);
        c.ipadx = 20;
        c.ipady = 10;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        backgroundPanel.add(retailPricingPanel, c);


        retailPricingTextDisplayPanel = new JPanel(new GridBagLayout());
        retailPricingTextDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Werte wiederherstellen"));
        setPanelSize(retailPricingTextDisplayPanel, widthRightSide, 270);
        createRetailPricingTextDisplayPanel();
        c.ipadx = 20;
        c.ipady = 10;
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(retailPricingTextDisplayPanel, c);
        if (!isDiscreteTreatment) { retailPricingTextDisplayPanel.setVisible(false); }

        this.add(backgroundPanel);
    }

    private void initWaitingPanel() {
        waitingPanel = new JPanel();

        setPanelSize(waitingPanel, 1280, 980);
        waitingPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        waitingPanel.setBackground(Color.WHITE);
        waitingPanel.setLayout(new BorderLayout(0, 0));

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BorderLayout(0, 0));
        waitingMessageLabel = new JLabel("<html><body><h1>Bitte warten Sie...</h1><br>Nächste Periode: " + (convertTimeToCurrentRound() +1) + " von " + maxRoundNumber +"</body></html>");
        waitingMessageLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        infoPanel.add(waitingMessageLabel);
        waitingPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(0, 0));
        JLabel lblLogo = new JLabel("");
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/edu/kit/exp/common/resources/kit_logo.png")));
        panel.add(lblLogo, BorderLayout.EAST);
        waitingPanel.add(panel, BorderLayout.NORTH);

        waitingPanel.setVisible(false);
        this.add(waitingPanel);

    }

    protected void setupCournotPanels() {
        priceDisplayPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Mengenhistorie"));
        priceTextDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Momentane Mengen"));

        quantityDisplayPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Preishistorie"));
        quantityTextDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Momentane Preise"));

        retailPricingPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Festlegen der Mengen"));
        pFirmALabel.setText("Menge Firma A:");
        pFirmBLabel.setText("Menge Firma B:");
        pFirmCLabel.setText("Menge Firma C:");
    }

    private void setupDuopolyPanels() {
        retailPricingFirmCPanel.setVisible(false);

        profitFirmCDescriptionChartDisplayLabel.setVisible(false);
        profitFirmCDescriptionLabel.setVisible(false);
        profitFirmCUnitLabel.setVisible(false);
        profitFirmCValueLabel.setVisible(false);

        pFirmCDescriptionDisplayLabel.setVisible(false);
        pFirmCDescriptionLabel.setVisible(false);
        pFirmCValueLabel.setVisible(false);
        pFirmCUnitLabel.setVisible(false);

        qFirmCDescriptionDisplayLabel.setVisible(false);
        qFirmCDescriptionLabel.setVisible(false);
        qFirmCValueLabel.setVisible(false);
        qFirmCUnitLabel.setVisible(false);
    }

    private void createDefaultLabels() {
        firmALabel = new JLabel();
        firmALabel.setText(FirmDescription.FirmA.toString()+":");
        firmALabel.setForeground(COLOR_FIRM_A);

        firmBLabel = new JLabel();
        firmBLabel.setText(FirmDescription.FirmB.toString()+":");
        firmBLabel.setForeground(COLOR_FIRM_B);

        FirmCLabel = new JLabel();
        FirmCLabel.setText(FirmDescription.FirmC.toString()+":");
        FirmCLabel.setForeground(COLOR_FIRM_E);

        unitLabel = new JLabel();
        unitLabel.setText("Euro");
    }

    private void createHeaderPanel() {
        Color ownFirmColor = identifyUsersLabelColor();
        GridBagConstraints c = new GridBagConstraints();

        headerLabel = new JLabel("Ihre Rolle:");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0,5,0,0);
        headerPanel.add(headerLabel, c);

        headerFirmLabel = createNewRightSideDescriptionLabel(ownFirmColor);
        headerFirmLabel.setText(String.valueOf(myRole));
        headerFirmLabel.setFont(headerFirmLabel.getFont().deriveFont(14.0f));
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0,5,0,5);
        headerPanel.add(headerFirmLabel, c);

        // Ready button for in practice round
        if (practiceRound || isDiscreteTreatment) {

            String buttonText;
            buttonText = "Entscheidung treffen";

            readyButton = new JButton(buttonText);
            readyButton.setBackground(Color.red);
            c.anchor = GridBagConstraints.LINE_END;
            c.weightx = 1;
            c.gridx = 2;
            c.gridy = 0;
            headerPanel.add(readyButton, c);
        }

        balanceDescriptionLabel = new JLabel("Aktueller Kontostand:");
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 1;
        c.gridx = 3;
        c.gridy = 0;
        headerPanel.add(balanceDescriptionLabel, c);

        balanceValueLabel = new JLabel("00,00");
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0;
        c.gridx = 4;
        c.gridy = 0;
        headerPanel.add(balanceValueLabel, c);

        balanceUnitLabel = createNewRegularUnitLabel();
        c.gridx = 5;
        c.gridy = 0;
        headerPanel.add(balanceUnitLabel, c);

        counterLabel = new JLabel();
        counterLabel.setBackground(Color.red);
        updateCounterLabel();
        c.gridx = 6;
        c.gridy = 0;
        headerPanel.add(counterLabel, c);
    }

    private void createProfitDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();

        profitDescriptionDisplayPanel = new JPanel(new GridBagLayout());
        setPanelSize(profitDescriptionDisplayPanel, widthLeftSide, 20);
        createProfitDescriptionDisplayPanel();
        c.gridx = 0;
        c.gridy = 0;
        profitDisplayPanel.add(profitDescriptionDisplayPanel, c);

        profitChartDisplayPanel = new ProfitChartDisplayPanel(widthLeftSide, 125, myRole, timeStep, duration,
                COLOR_FIRM_A, COLOR_FIRM_B, COLOR_FIRM_E, COLOR_MARKET, isDiscreteTreatment, isTriopolyTreatment);
        setPanelSize(profitChartDisplayPanel, widthLeftSide, 230);
        //profitChartDisplayPanel.setBackground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 1;
        profitDisplayPanel.add(profitChartDisplayPanel, c);
    }

    private void createProfitDescriptionDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();

        profitFirmADescriptionChartDisplayLabel = cloneLabel(firmALabel);
        profitFirmADescriptionChartDisplayLabel.setText("- Firma A");
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 1;
        profitDescriptionDisplayPanel.add(profitFirmADescriptionChartDisplayLabel, c);

        profitFirmBDescriptionChartDisplayLabel = cloneLabel(firmBLabel);
        profitFirmBDescriptionChartDisplayLabel.setText("- Firma B");
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0;
        profitDescriptionDisplayPanel.add(profitFirmBDescriptionChartDisplayLabel, c);

        profitFirmCDescriptionChartDisplayLabel = cloneLabel(FirmCLabel);
        profitFirmCDescriptionChartDisplayLabel.setText("- Firma C");
        c.gridx = 3;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        profitDescriptionDisplayPanel.add(profitFirmCDescriptionChartDisplayLabel, c);
    }

    private void createProfitTextDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();
        int pady = 25;

        profitFirmADescriptionLabel = createNewFirmALabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 0;
        profitTextDisplayPanel.add(profitFirmADescriptionLabel, c);

        profitFirmAValueLabel = createNewRightSideValueLabel(COLOR_FIRM_A);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 0;
        profitTextDisplayPanel.add(profitFirmAValueLabel, c);

        profitFirmAUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_A);
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 0;
        profitTextDisplayPanel.add(profitFirmAUnitLabel, c);


        profitFirmBDescriptionLabel = createNewFirmBLabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 1;
        profitTextDisplayPanel.add(profitFirmBDescriptionLabel, c);

        profitFirmBValueLabel = createNewRightSideValueLabel(COLOR_FIRM_B);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 1;
        profitTextDisplayPanel.add(profitFirmBValueLabel, c);

        profitFirmBUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_B);
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 1;
        profitTextDisplayPanel.add(profitFirmBUnitLabel, c);


        profitFirmCDescriptionLabel = createNewFirmCLabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 2;
        profitTextDisplayPanel.add(profitFirmCDescriptionLabel, c);

        profitFirmCValueLabel = createNewRightSideValueLabel(COLOR_FIRM_E);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 2;
        profitTextDisplayPanel.add(profitFirmCValueLabel, c);

        profitFirmCUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_E);
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 2;
        profitTextDisplayPanel.add(profitFirmCUnitLabel, c);
    }

    private void createPriceDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();

        priceDescriptionDisplayPanel = new JPanel(new GridBagLayout());
        setPanelSize(priceDescriptionDisplayPanel, widthLeftSide, 20);
        createPriceDescriptionDisplayPanel();
        c.gridx = 0;
        c.gridy = 1;
        priceDisplayPanel.add(priceDescriptionDisplayPanel, c);

        priceChartDisplayPanel = new PriceChartDisplayPanel(widthLeftSide, 125, myRole, timeStep, duration,
                COLOR_FIRM_A, COLOR_FIRM_B, COLOR_FIRM_E, COLOR_MARKET, isDiscreteTreatment, isTriopolyTreatment);



        setPanelSize(priceChartDisplayPanel, widthLeftSide, 150);
        //priceChartDisplayPanel.setBackground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 2;
        priceDisplayPanel.add(priceChartDisplayPanel, c);
    }

    private void createPriceDescriptionDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();

        pFirmADescriptionDisplayLabel = createNewFirmADescriptionLabel();
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 1;
        priceDescriptionDisplayPanel.add(pFirmADescriptionDisplayLabel, c);

        pFirmBDescriptionDisplayLabel = createNewFirmBDescriptionLabel();
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0;
        priceDescriptionDisplayPanel.add(pFirmBDescriptionDisplayLabel, c);

        pFirmCDescriptionDisplayLabel = createNewFirmCDescriptionLabel();
        c.gridx = 3;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        priceDescriptionDisplayPanel.add(pFirmCDescriptionDisplayLabel, c);

        pMarketDescriptionDisplayLabel = new JLabel();
        pMarketDescriptionDisplayLabel.setText("- Durchschnitt");
        pMarketDescriptionDisplayLabel.setForeground(COLOR_MARKET);
        c.gridx = 4;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        priceDescriptionDisplayPanel.add(pMarketDescriptionDisplayLabel, c);
    }

    private void createPriceTextDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();
        int pady = 13;

        pFirmADescriptionLabel = createNewFirmALabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 0;
        priceTextDisplayPanel.add(pFirmADescriptionLabel, c);

        pFirmAValueLabel = createNewRightSideValueLabel(COLOR_FIRM_A);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 0;
        priceTextDisplayPanel.add(pFirmAValueLabel, c);

        pFirmAUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_A);
        pFirmAUnitLabel.setText("");
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 0;
        priceTextDisplayPanel.add(pFirmAUnitLabel, c);


        pFirmBDescriptionLabel = createNewFirmBLabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 1;
        priceTextDisplayPanel.add(pFirmBDescriptionLabel, c);

        pFirmBValueLabel = createNewRightSideValueLabel(COLOR_FIRM_B);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 1;
        priceTextDisplayPanel.add(pFirmBValueLabel, c);

        pFirmBUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_B);
        pFirmBUnitLabel.setText("");
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 1;
        priceTextDisplayPanel.add(pFirmBUnitLabel, c);


        pFirmCDescriptionLabel = createNewFirmCLabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 2;
        priceTextDisplayPanel.add(pFirmCDescriptionLabel, c);

        pFirmCValueLabel = createNewRightSideValueLabel(COLOR_FIRM_E);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 2;
        priceTextDisplayPanel.add(pFirmCValueLabel, c);

        pFirmCUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_E);
        pFirmCUnitLabel.setText("");
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 2;
        priceTextDisplayPanel.add(pFirmCUnitLabel, c);


        pMarketDescriptionLabel = createNewRightSideDescriptionLabel(COLOR_MARKET);
        pMarketDescriptionLabel.setText("Durchschnitt:");
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 3;
        priceTextDisplayPanel.add(pMarketDescriptionLabel, c);

        pMarketValueLabel = createNewRightSideValueLabel(COLOR_MARKET);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 3;
        priceTextDisplayPanel.add(pMarketValueLabel, c);

        pMarketUnitLabel = createNewRightSideUnitLabel(COLOR_MARKET);
        pMarketUnitLabel.setText("");
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 3;
        priceTextDisplayPanel.add(pMarketUnitLabel, c);

    }

    private void createQuantityDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();

        quantityDescriptionDisplayPanel = new JPanel(new GridBagLayout());
        setPanelSize(quantityDescriptionDisplayPanel, widthLeftSide, 20);
        createQuantityDescriptionDisplayPanel();
        c.gridx = 0;
        c.gridy = 0;
        quantityDisplayPanel.add(quantityDescriptionDisplayPanel, c);

        quantityChartDisplayPanel = new QuantityChartDisplayPanel(widthLeftSide, 125, myRole, timeStep, duration,
                COLOR_FIRM_A, COLOR_FIRM_B, COLOR_FIRM_E, COLOR_MARKET, isDiscreteTreatment, isTriopolyTreatment);

        setPanelSize(quantityChartDisplayPanel, widthLeftSide, 150);
        c.gridx = 0;
        c.gridy = 1;
        quantityDisplayPanel.add(quantityChartDisplayPanel, c);
    }

    private void createQuantityDescriptionDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();

        qFirmADescriptionDisplayLabel = cloneLabel(firmALabel);
        qFirmADescriptionDisplayLabel.setText("- Firma A");
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 1;
        quantityDescriptionDisplayPanel.add(qFirmADescriptionDisplayLabel, c);

        qFirmBDescriptionDisplayLabel = cloneLabel(firmBLabel);
        qFirmBDescriptionDisplayLabel.setText("- Firma B");
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0;
        quantityDescriptionDisplayPanel.add(qFirmBDescriptionDisplayLabel, c);

        qFirmCDescriptionDisplayLabel = cloneLabel(FirmCLabel);
        qFirmCDescriptionDisplayLabel.setText("- Firma C");
        c.gridx = 3;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        quantityDescriptionDisplayPanel.add(qFirmCDescriptionDisplayLabel, c);

        qMarketDescriptionDisplayLabel = new JLabel();
        qMarketDescriptionDisplayLabel.setText("- Durchschnitt");
        qMarketDescriptionDisplayLabel.setForeground(COLOR_MARKET);
        c.gridx = 4;
        c.gridy = 0;
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        quantityDescriptionDisplayPanel.add(qMarketDescriptionDisplayLabel, c);
        qMarketDescriptionDisplayLabel.setVisible(false);

    }

    private void createQuantityTextDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();
        int pady = 13;

        qFirmADescriptionLabel = createNewFirmALabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 0;
        quantityTextDisplayPanel.add(qFirmADescriptionLabel, c);

        qFirmAValueLabel = createNewRightSideValueLabel(COLOR_FIRM_A);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 0;
        quantityTextDisplayPanel.add(qFirmAValueLabel, c);

        qFirmAUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_A);
        qFirmAUnitLabel.setText("");
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 0;
        quantityTextDisplayPanel.add(qFirmAUnitLabel, c);


        qFirmBDescriptionLabel = createNewFirmBLabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 1;
        quantityTextDisplayPanel.add(qFirmBDescriptionLabel, c);

        qFirmBValueLabel = createNewRightSideValueLabel(COLOR_FIRM_B);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 1;
        quantityTextDisplayPanel.add(qFirmBValueLabel, c);

        qFirmBUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_B);
        qFirmBUnitLabel.setText("");
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 1;
        quantityTextDisplayPanel.add(qFirmBUnitLabel, c);


        qFirmCDescriptionLabel = createNewFirmCLabel();
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 2;
        quantityTextDisplayPanel.add(qFirmCDescriptionLabel, c);

        qFirmCValueLabel = createNewRightSideValueLabel(COLOR_FIRM_E);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 2;
        quantityTextDisplayPanel.add(qFirmCValueLabel, c);

        qFirmCUnitLabel = createNewRightSideUnitLabel(COLOR_FIRM_E);
        qFirmCUnitLabel.setText("");
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 2;
        quantityTextDisplayPanel.add(qFirmCUnitLabel, c);


        qMarketDescriptionLabel = createNewRightSideDescriptionLabel(COLOR_MARKET);
        qMarketDescriptionLabel.setText("Durchschnitt");
        c = gbcDescriptionColumn(c, pady);
        c.gridx = 0;
        c.gridy = 3;
        quantityTextDisplayPanel.add(qMarketDescriptionLabel, c);

        qMarketValueLabel = createNewRightSideValueLabel(COLOR_MARKET);
        c = gbcValueColumn(c, pady);
        c.gridx = 1;
        c.gridy = 3;
        quantityTextDisplayPanel.add(qMarketValueLabel, c);

        qMarketUnitLabel = createNewRightSideUnitLabel(COLOR_MARKET);
        qMarketUnitLabel.setText("");
        c = gbcUnitColumn(c, pady);
        c.gridx = 2;
        c.gridy = 3;
        quantityTextDisplayPanel.add(qMarketUnitLabel, c);

        qMarketDescriptionLabel.setVisible(false);
        qMarketValueLabel.setVisible(false);
        qMarketUnitLabel.setVisible(false);
    }


    private void createRetailPricingPanel() {
        GridBagConstraints c = new GridBagConstraints();

        retailPricingFirmAPanel = new JPanel(new GridBagLayout());
        retailPricingFirmAPanel.setBackground(COLOR_LIGHT_FIRM_A);
        retailPricingFirmAPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        createRetailPricingFirmAPanel();
        c.insets = new Insets(5,0,0,0);
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 20;
        retailPricingPanel.add(retailPricingFirmAPanel, c);

        retailPricingFirmBPanel = new JPanel(new GridBagLayout());
        retailPricingFirmBPanel.setBackground(COLOR_LIGHT_FIRM_B);
        retailPricingFirmBPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        createRetailPricingFirmBPanel();
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 20;
        retailPricingPanel.add(retailPricingFirmBPanel, c);

        retailPricingFirmCPanel = new JPanel(new GridBagLayout());
        retailPricingFirmCPanel.setBackground(COLOR_LIGHT_FIRM_E);
        retailPricingFirmCPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        createRetailPricingFirmCPanel();
        c.gridx = 0;
        c.gridy = 3;
        c.ipadx = 20;
        retailPricingPanel.add(retailPricingFirmCPanel, c);

    }

    private void createRetailPricingTextDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();

        restoreButton = new JButton("Letztes Periodenergebnis anzeigen");
        setComponentSize(restoreButton, 250, 30);
        c.gridx = 0;
        c.gridy = 0;
        retailPricingTextDisplayPanel.add(restoreButton, c);


    }

    private void createRetailPricingFirmAPanel() {
        GridBagConstraints c = new GridBagConstraints();

        pFirmALabel = new JLabel();
        pFirmALabel.setText("Preis Firma A:");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,0,0,0);
        retailPricingFirmAPanel.add(pFirmALabel, c);

        firmAOutOfMarketLabel = createNewNoDemandLabel();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,10,0,0);
        retailPricingFirmAPanel.add(firmAOutOfMarketLabel, c);


        pFirmATextField = new JTextField();
        if (myRole == FirmDescription.FirmA || practiceRound || isDiscreteTreatment) {
            setInActiveTextField(pFirmATextField);
        } else {
            setInActiveTextField(pFirmATextField);
        }
        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        retailPricingFirmAPanel.add(pFirmATextField, c);

        pFirmASlider = new JSlider();
        pFirmASlider.setBackground(retailPricingFirmAPanel.getBackground());
        if (myRole == FirmDescription.FirmA || practiceRound || isDiscreteTreatment) {
            createActiveSlider(pFirmASlider);
        } else {
            createInactiveSlider(pFirmASlider);
        }
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,0,0);
        retailPricingFirmAPanel.add(pFirmASlider, c);
    }

    private void createRetailPricingFirmBPanel() {
        GridBagConstraints c = new GridBagConstraints();

        pFirmBLabel = new JLabel();
        pFirmBLabel.setText("Preis Firma B:");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,0,0,0);
        retailPricingFirmBPanel.add(pFirmBLabel, c);

        firmBOutOfMarketLabel = createNewNoDemandLabel();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,10,0,0);
        retailPricingFirmBPanel.add(firmBOutOfMarketLabel, c);

        pFirmBTextField = new JTextField();
        if (myRole == FirmDescription.FirmB || practiceRound || isDiscreteTreatment) {
            setInActiveTextField(pFirmBTextField);
        } else {
            setInActiveTextField(pFirmBTextField);
        }
        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        retailPricingFirmBPanel.add(pFirmBTextField, c);

        pFirmBSlider = new JSlider();
        pFirmBSlider.setBackground(retailPricingFirmBPanel.getBackground());
        if (myRole == FirmDescription.FirmB || practiceRound || isDiscreteTreatment) {
            createActiveSlider(pFirmBSlider);
        } else {
            createInactiveSlider(pFirmBSlider);
        }
        c.insets = new Insets(5,0,0,0);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        retailPricingFirmBPanel.add(pFirmBSlider, c);
    }

    private void createRetailPricingFirmCPanel() {
        GridBagConstraints c = new GridBagConstraints();

        pFirmCLabel = new JLabel();
        pFirmCLabel.setText("Preis Firma C:");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,0,0,0);
        retailPricingFirmCPanel.add(pFirmCLabel, c);

        FirmCOutOfMarketLabel = createNewNoDemandLabel();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,10,0,0);
        retailPricingFirmCPanel.add(FirmCOutOfMarketLabel, c);

        pFirmCTextField = new JTextField();
        if (myRole == FirmDescription.FirmC || practiceRound || isDiscreteTreatment) {
            setInActiveTextField(pFirmCTextField);
        } else {
            setInActiveTextField(pFirmCTextField);
        }
        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        retailPricingFirmCPanel.add(pFirmCTextField, c);

        pFirmCSlider = new JSlider();
        pFirmCSlider.setBackground(retailPricingFirmCPanel.getBackground());
        if (myRole == FirmDescription.FirmC || practiceRound || isDiscreteTreatment) {
            createActiveSlider(pFirmCSlider);
        } else {
            createInactiveSlider(pFirmCSlider);
        }
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        retailPricingFirmCPanel.add(pFirmCSlider, c);
    }


    private void setCurrentProfitTextField(JTextField tf) {
        tf.setMinimumSize(new Dimension(60, 20));
        tf.setMaximumSize(new Dimension(60, 20));
        tf.setPreferredSize(new Dimension(60, 20));
        tf.setEnabled(false);
        tf.setForeground(Color.BLACK);

    }

    private void setActiveTextField(JTextField tf) {
        tf.setMinimumSize(new Dimension(100, 20));
        tf.setMaximumSize(new Dimension(100, 20));
        tf.setPreferredSize(new Dimension(100, 20));
        tf.setHorizontalAlignment(JTextField.CENTER);
    }

    private void setInActiveTextField(JTextField tf) {
        setActiveTextField(tf);
        tf.setEnabled(false);
    }


    private void createActiveSlider(JSlider slider) {
        slider.setMinimum(sliderMin);
        slider.setMaximum(sliderMax);
        slider.setValue(sliderValue);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setPaintLabels(true);

        slider.setMinimumSize(new Dimension(800, 50));
        slider.setMaximumSize(new Dimension(800, 50));
        slider.setPreferredSize(new Dimension(800, 50));
    }

    private void createInactiveSlider(JSlider slider) {
        createActiveSlider(slider);
        slider.setEnabled(false);
        Color color = slider.getBackground();
        //slider.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
    }

    private JLabel cloneLabel(JLabel orgLabel) {
        JLabel newLabel = new JLabel();
        newLabel.setText(orgLabel.getText());
        newLabel.setForeground(orgLabel.getForeground());
        return newLabel;
    }


    private JLabel createNewFirmADescriptionLabel() {
        JLabel label = new JLabel();
        label.setForeground(COLOR_FIRM_A);
        label.setText("- Firma A");
        return label;
    }

    private JLabel createNewFirmBDescriptionLabel() {
        JLabel label = new JLabel();
        label.setForeground(COLOR_FIRM_B);
        label.setText("- Firma B");
        return label;
    }

    private JLabel createNewFirmCDescriptionLabel() {
        JLabel label = new JLabel();
        label.setForeground(COLOR_FIRM_E);
        label.setText("- Firma C");
        return label;
    }

    private JLabel createNewFirmALabel() {
        JLabel label = createNewRightSideDescriptionLabel(COLOR_FIRM_A);
        label.setText("Firma A");
        return label;
    }

    private JLabel createNewFirmBLabel() {
        JLabel label = createNewRightSideDescriptionLabel(COLOR_FIRM_B);
        label.setText("Firma B");
        return label;
    }

    private JLabel createNewFirmCLabel() {
        JLabel label = createNewRightSideDescriptionLabel(COLOR_FIRM_E);
        label.setText("Firma C");
        return label;
    }

    private JLabel createNewRightSideValueLabel(Color color) {
        JLabel label = createNewRightSideDescriptionLabel(color);
        label.setFont(label.getFont().deriveFont(FONT_SIZE_PROFIT));
        return label;
    }

    private JLabel createNewRightSideDescriptionLabel(Color color) {
        JLabel label = new JLabel();
        label.setForeground(color);
        return label;
    }

    private JLabel createNewRightSideUnitLabel(Color color) {
        JLabel label = createNewRegularUnitLabel(color);
        label.setFont(label.getFont().deriveFont(FONT_SIZE_PROFIT));
        return label;
    }

    private JLabel createNewRegularUnitLabel(Color color) {
        JLabel label = new JLabel();
        label.setText("Cent");
        label.setForeground(color);
        return label;
    }

    private JLabel createNewNoDemandLabel() {
        JLabel label = new JLabel();
        label.setVisible(false);
        label.setForeground(Color.RED);
        //label.setFont(label.getFont().deriveFont(14.0f));
        label.setText("Keine Nachfrage");
        label.setFont(new Font(label.getFont().getName(),Font.ITALIC,label.getFont().getSize()));
        return label;
    }



    private JLabel createNewRegularUnitLabel() {
        JLabel label = new JLabel();
        label.setText("Euro");
        return label;
    }

    private void updateFontOfUnitLabel(JLabel label, Color color ) {
        label.setFont(label.getFont().deriveFont(FONT_SIZE_PROFIT));
        label.setForeground(color);
    }

    private void lockSlider(JSlider slider) {
        slider.setEnabled(false);
    }

    private void unlockSlider(JSlider slider) {
        slider.setEnabled(true);
    }

    private void setPanelSize(JPanel panel, int width, int height) {
        panel.setPreferredSize(new Dimension(width, height));
        panel.setMaximumSize(new Dimension(width, height));
        panel.setMinimumSize(new Dimension(width, height));
    }

    private void setComponentSize(JComponent comp, int width, int height) {
        comp.setPreferredSize(new Dimension(width, height));
        comp.setMaximumSize(new Dimension(width, height));
        comp.setMinimumSize(new Dimension(width, height));
    }

    private Color identifyUsersLabelColor() {
        if (myRole.equals(FirmDescription.FirmA)) {
            return COLOR_FIRM_A;
        } else {
            if (myRole.equals(FirmDescription.FirmB)) {
                return COLOR_FIRM_B;
            } else {
                return COLOR_FIRM_E;
            }
        }
    }

    private JLabel identifyUsersFirmLabel() {
        if (myRole.equals(FirmDescription.FirmA)) {
            return firmALabel;
        } else {
            if (myRole.equals(FirmDescription.FirmB)) {
                return firmBLabel;
            } else {
                return FirmCLabel;
            }
        }
    }

    private GridBagConstraints gbcDescriptionColumn(GridBagConstraints c, int pady) {
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0,20,0,0);
        c.weightx = 1;
        c.ipady = pady;
        return c;
    }

    private GridBagConstraints gbcValueColumn(GridBagConstraints c, int pady) {
        c.insets = new Insets(0,10,0,10);
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0;
        c.ipady = pady;
        return c;
    }

    private GridBagConstraints gbcUnitColumn(GridBagConstraints c, int pady) {
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0,0,0,20);
        c.weightx = 0;
        c.ipady = pady;
        return c;
    }

    private int convertTimeToCurrentRound() {
        return ((localTime / timeStep)+1);
    }





}

