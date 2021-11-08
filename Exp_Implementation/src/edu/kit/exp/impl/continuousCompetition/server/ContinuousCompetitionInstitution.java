package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.client.gui.screens.DefaultWaitingScreen;
import edu.kit.exp.impl.continuousCompetition.client.*;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.Trial;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.TrialManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by dschnurr on 05.03.14.
 */
public class ContinuousCompetitionInstitution extends Institution<ContinuousCompetitionEnvironment> {

    protected static final Logger log4j = LogManager.getLogger(ContinuousCompetitionInstitution.class.getName());

    protected Subject firmA;
    protected Subject firmB;
    protected Subject firmC;
    protected Subject firmD;

    protected boolean isDiscreteTreatment;
    protected boolean isTriopolyTreatment;
    protected boolean isCournotTreatment;
    protected boolean isQuadropolyTreatment;

    protected final double diffParam = (2.0/3.0);
    protected int duration;
    protected int updateTimeStep;

    protected int[] checkOfCountIds = new int[10000];
    protected int readyCount = 0;
    protected int countPracticeFinished = 0;
    protected int countPeriodFinished = 0;
    protected static int countRoundFinished = 0;
    protected static HashMap<String, ContinuousCompetitionParamObject> pendingMarketUpdatesAndRecipients = new HashMap<String, ContinuousCompetitionParamObject>();
    protected static Set<Subject> subjectsInSession = new HashSet<Subject>();
    protected static HashMap<String, double[]> subjectsPracticeFinished = new HashMap<String, double[]>();
    protected ContinuousCompetitionMarketDataCalculator marketDataCalculator;

    protected int serverTime;
    protected double aFirmA;
    protected double aFirmB;
    protected double aFirmC;
    protected double aFirmD;


    protected double oMarket;
    protected double aMarket;

    protected double oFirmA;
    protected double oFirmB;
    protected double oFirmC;
    protected double oFirmD;
    protected double profitFirmA;
    protected double profitFirmB;
    protected double profitFirmC;
    protected double profitFirmD;

    protected double balanceFirmA;
    protected double balanceFirmB;
    protected double balanceFirmC;
    protected double balanceFirmD;


    /**
     * Creates an Institution.
     *
     * @param environment   The environment the economic institution interacts with,
     *                      within the economic system.
     * @param memberships   A list of memberships.
     * @param messageSender
     * @param gameId        @see edu.kit.exp.server.jpa.entity.Membership
     */
    public ContinuousCompetitionInstitution(ContinuousCompetitionEnvironment environment, List<Membership> memberships, ServerMessageSender messageSender, String gameId) {
        super(environment, memberships, messageSender, gameId);

        setupTreatmentConditions();

        for (Membership membership : memberships) {
            subjectsInSession.add(membership.getSubject());
        }

        marketDataCalculator = new ContinuousCompetitionMarketDataCalculator(diffParam, isCournotTreatment, isTriopolyTreatment, isQuadropolyTreatment);

        log4j.info("Started ContinuousCompetitionInstitution for cohort {} with diffParam = {}", getSubjectGroup().getIdSubjectGroup(), diffParam);
    }

    protected void setupTreatmentConditions() {
        duration = 1800000;
        updateTimeStep = 200;
        isTriopolyTreatment = true;
        isCournotTreatment = false;
        isDiscreteTreatment = false;
        isQuadropolyTreatment = true;
    }

    @Override
    public void startPeriod() throws Exception {
        log4j.warn("startPeriod() - start of execution");

        // Assign subjects to firm roles
        log4j.trace("Reading memberships: STARTED");
        /* Identify and assign firmA, firmB, and firmC */

        for (Membership membership : memberships) {
            if (membership.getRole().equals(ContinuousCompetitionEnvironment.getROLE_FIRMA())) {
                firmA = membership.getSubject();
            } else {
                if (membership.getRole().equals(ContinuousCompetitionEnvironment.getROLE_FIRMB())) {
                    firmB = membership.getSubject();
                } else {
                    if (membership.getRole().equals(ContinuousCompetitionEnvironment.getROLE_FIRME())) {
                        firmC = membership.getSubject();
                    } else {
                        if (membership.getRole().equals(ContinuousCompetitionEnvironment.getROLE_FIRMD())) {
                            firmD = membership.getSubject();
                        }
                    // else invalid state
                    }
                }
            }
        }
        log4j.trace("Reading memberships: FINISHED");

        // Set-up initial market update with information regarding meta-data and market data
        // - Meta-data: practiceRound, subjectGroup, statusMessage, screenName, time
        // - Market data: actions, profits

        if (!getCurrentPeriod().getPractice() && !subjectsPracticeFinished.isEmpty()) {
            initRegularRoundActionData(firmA, firmB, firmC, firmD);
            log4j.info("Initialized regular round action data.");
        } else {
            initPracticeRoundActionData();
            log4j.info("Initialized practice round action data.");
        }

        // create market update with starting action and market values and set initialization parameters
        ContinuousCompetitionParamObject initMarketUpdate = calculateMarketDataAndCreateMarketUpdate();
        initMarketUpdate.setInitialUpdate(true);
        initMarketUpdate.setTriopolyTreatment(isTriopolyTreatment);
        initMarketUpdate.setQuadropolyTreatment(isQuadropolyTreatment);
        initMarketUpdate.setCournotTreatment(isCournotTreatment);
        initMarketUpdate.setDiscreteTreatment(isDiscreteTreatment);
        initMarketUpdate.setDuration(duration);
        initMarketUpdate.setUpdateTimeStep(updateTimeStep);
        initMarketUpdate.setSubjectGroup(subjectGroup);
        initMarketUpdate.setScreenName(this.getCurrentTreatment().getName());

        initMarketUpdate = initBalanceDataAndUpdateMarketUpdate(initMarketUpdate);

        initMarketUpdate.setDiffParam(diffParam);
        if (getCurrentPeriod().getPractice()) { initMarketUpdate.setPracticeRound(true); }
        if (isDiscreteTreatment) { initMarketUpdate.setGlobalTime(0); }

        log4j.info("Action variables before initial market update: aFirmA: {}, aFirmB: {}, aFirmC: {}, aFirmD: {}.", aFirmA, aFirmB, aFirmC, aFirmD);
        log4j.info("Profit variables before initial market update: profitFirmA: {}, profitFirmB: {}, profitFirmC: {}, profitFirmD: {}", profitFirmA, profitFirmB, profitFirmC, profitFirmD);
        log4j.info("Balance variables before initial market update: balanceFirmA: {}, balanceFirmB: {}, balanceFirmC: {}, balanceFirmD: {}", balanceFirmA, balanceFirmB, balanceFirmC, balanceFirmD);


        // TODO

        assignScreen(firmA, FirmDescription.FirmA, 0, initMarketUpdate);
        assignScreen(firmB, FirmDescription.FirmB, 1, initMarketUpdate);

        if (isQuadropolyTreatment) {

            assignScreen(firmC, FirmDescription.FirmC, 2, initMarketUpdate);
            assignScreen(firmD, FirmDescription.FirmD, 3, initMarketUpdate);
        } else {
            if (isTriopolyTreatment) {
                assignScreen(firmC, FirmDescription.FirmC, 2, initMarketUpdate);
                firmD = new Subject();
            } else {
                // DuopolyTreatment
                firmC = new Subject();
                firmD = new Subject();
            }
        }

        log4j.trace("startPeriod() - end of execution");
    }

    private void assignScreen(Subject firm, FirmDescription firmDescription, int roleCode, ContinuousCompetitionParamObject initMarketUpdate) throws Exception {
        if (firm.getIdClient().startsWith("agent")) {
            customizeMarketUpdateAndShowScreen(firm, initMarketUpdate, firmDescription, roleCode, AgentCoreWithGui.class);
        }  else {
            if (isQuadropolyTreatment) {
                customizeMarketUpdateAndShowScreen(firm, initMarketUpdate, firmDescription, roleCode, ContinuousCompetitionFirmScreenQuadropoly.class);
            } else {
                customizeMarketUpdateAndShowScreen(firm, initMarketUpdate, firmDescription, roleCode, ContinuousCompetitionFirmScreen.class);
            }
        }
    }


    // Initialize screens for firms and send customized marketUpdate (including subject and role information)
    // Screen in practice round and in regular round is send with unlimited time deadline
    private void customizeMarketUpdateAndShowScreen(Subject subject, ContinuousCompetitionParamObject marketUpdate, FirmDescription firmDescription, int roleCode, Class screenId) throws Exception {
        marketUpdate.setSubject(subject);
        marketUpdate.setRole(firmDescription);
        marketUpdate.setRoleCode(roleCode);
        showScreen(subject, screenId, marketUpdate);
    }


    @Override
    public void processMessage(ClientResponseMessage msg) throws Exception {
        log4j.debug("processMessage() - start of execution");

        String msgScreenId = msg.getScreenId();
        String senderClientId = msg.getClientId();


        if ( msgScreenId.equals(ContinuousCompetitionFirmScreen.class.getName()) || msgScreenId.equals(ContinuousCompetitionFirmScreenQuadropoly.class.getName()) || msgScreenId.equals(AgentCore.class.getName()) || msgScreenId.equals(AgentCoreWithGui.class.getName()) ) {
            log4j.trace("Institution received update from " + senderClientId);
            ContinuousCompetitionResponseObject actionUpdate = msg.getParameters();


            if (getCurrentPeriod().getPractice()) {
                //Practice round: collect messages that report end of practice round; end period after last message has been received
                processActionUpdatesInPracticeRound(actionUpdate, senderClientId);

            }  else {

                // Check whether action update represents ready message in regular experiment period
                if (actionUpdate.isClientReady()) {
                    // Client reported ready for initial market update
                    log4j.info("Received ready message from client" + senderClientId);
                    processReadyUpdate();

                } else {
                    //Check whether action update contains finished flag that signals end of regular experiment period
                    //End period after last finish message is received and save reported balances as payoffs
                    if (actionUpdate.isClientFinished()) {

                        int countId = actionUpdate.getCountId();
                        updateMarketActions(actionUpdate, senderClientId);
                        serverTime = actionUpdate.getLocalTime();
                        saveActionUpdateToDatabase(actionUpdate, senderClientId);

                        log4j.info("Received final actionUpdate with countId {} of cohort {}.", countId, getSubjectGroup().getIdSubjectGroup());


                        countPeriodFinished = countPeriodFinished + 1;
                        log4j.info("countPeriodFinished was updated to {}. MembershipCount is set to {}.", countPeriodFinished, memberships.size());

                        if (countPeriodFinished == memberships.size()) {
                            ContinuousCompetitionParamObject marketUpdate = calculateMarketDataAndCreateMarketUpdate();
                            marketUpdate = calculateBalanceDataAndAddToMarketUpdate(marketUpdate);
                            saveMarketDataToDatabase(countId);

                            log4j.info("Period finished: countPeriodFinished ({}) reached memberships size ({}). Ending period now.", countPeriodFinished, memberships.size());

                            savePayoffsToDatabase(firmA, FirmDescription.FirmA);
                            log4j.info("Payoffs: FirmA {} earned {} Euro", firmA.getIdClient(), firmA.getPayoff());

                            savePayoffsToDatabase(firmB, FirmDescription.FirmB);
                            log4j.info("Payoffs: FirmB {} earned {} Euro", firmB.getIdClient(), firmB.getPayoff());

                            if (isTriopolyTreatment || isQuadropolyTreatment) {
                                savePayoffsToDatabase(firmC, FirmDescription.FirmC);
                                log4j.info("Payoffs: FirmC {} earned {} Euro", firmC.getIdClient(), firmC.getPayoff());
                            }

                            if (isQuadropolyTreatment) {
                                savePayoffsToDatabase(firmD, FirmDescription.FirmD);
                                log4j.info("Payoffs: FirmD {} earned {} Euro", firmD.getIdClient(), firmD.getPayoff());
                            }

                            endPeriod();
                        }


                    } else {
                        // Regular action update: check countIds to verify that each client has reported action update
                        int countId = actionUpdate.getCountId();
                        updateMarketActions(actionUpdate, senderClientId);
                        serverTime = actionUpdate.getLocalTime();
                        saveActionUpdateToDatabase(actionUpdate, senderClientId);

                        checkOfCountIds[countId] = checkOfCountIds[countId] + 1;
                        log4j.info("Received actionUpdate is number {} of {} with countId {} of cohort {}.", checkOfCountIds[countId], getMemberships().size(), countId, getSubjectGroup().getIdSubjectGroup());

                        if (isDiscreteTreatment) {
                            countRoundFinished = countRoundFinished + 1;
                            log4j.info("Increased countRoundFinished to {} of {} subjects in session.", countRoundFinished, subjectsInSession.size());
                        }

                        if (checkOfCountIds[countId] == memberships.size()) {
                            ContinuousCompetitionParamObject marketUpdate = calculateMarketDataAndCreateMarketUpdate();
                            marketUpdate = calculateBalanceDataAndAddToMarketUpdate(marketUpdate);
                            saveMarketDataToDatabase(countId);

                            marketUpdate.setGlobalTime(actionUpdate.getLocalTime());
                            log4j.info("Created marketUpdate is set to global time: {} and to countId: {}.", marketUpdate.getGlobalTime(), countId);
                            marketUpdate.setCountId(countId);

                            if (isDiscreteTreatment) {
                                // Discrete treatment: synchronize transmission of market updates
                                // - store all market updates until last subject in session has submitted a action update
                                // - send all market updates simultaneously after last action udpate is received

                                pendingMarketUpdatesAndRecipients.put(firmA.getIdClient(), marketUpdate);
                                pendingMarketUpdatesAndRecipients.put(firmB.getIdClient(), marketUpdate);
                                pendingMarketUpdatesAndRecipients.put(firmC.getIdClient(), marketUpdate);
                                pendingMarketUpdatesAndRecipients.put(firmD.getIdClient(), marketUpdate);
                                log4j.debug("Stored marketUpdate with countId {} for designated recipient {}.", countId, firmA.getIdClient());
                                log4j.debug("Stored marketUpdate with countId {} for designated recipient {}.", countId, firmB.getIdClient());
                                log4j.debug("Stored marketUpdate with countId {} for designated recipient {}.", countId, firmC.getIdClient());
                                log4j.debug("Stored marketUpdate with countId {} for designated recipient {}.", countId, firmD.getIdClient());
                                log4j.debug("countRoundFinished: {}, subjectsInSession.size(): {}, pendingMarketUpdatesAndRecipients: {}", countRoundFinished, subjectsInSession.size(), pendingMarketUpdatesAndRecipients.size());

                                if ( countRoundFinished == subjectsInSession.size()) {

                                    for (Subject recipient : subjectsInSession) {
                                        ContinuousCompetitionParamObject mu = pendingMarketUpdatesAndRecipients.get(recipient.getIdClient());
                                        updateParamObject(recipient, mu);
                                        log4j.debug("Sent marketUpdate with countId {} to recipient: {}", mu.getCountId(), recipient.getIdClient());
                                    }
                                    log4j.info("Sent respective marketUpdate to each participant of session.");

                                    countRoundFinished = 0;
                                    pendingMarketUpdatesAndRecipients = new HashMap<String, ContinuousCompetitionParamObject>();
                                    log4j.debug("Initialized new HashMap pendingMarketUpdatesAndRecipients and reset countRoundFinished to {}.", countRoundFinished);

                                }

                            } else {
                                for (Membership membership : memberships) {
                                    String clientId = membership.getSubject().getIdClient();
                                    updateParamObject(clientId, marketUpdate);
                                }
                            }

                            log4j.info("processMessage() - end of execution - processed actionUpdate with countId " + actionUpdate.getCountId());
                        }
                    }

                }
            }
        }
    }


    private void processActionUpdatesInPracticeRound(ContinuousCompetitionResponseObject actionUpdate, String senderClientId) throws Exception {
        if (actionUpdate.isPracticeRoundFinished()) {
            countPracticeFinished = countPracticeFinished +1;
            log4j.info("countPracticeFinished was updated to {} (client {}). MembershipCount is set to {}.", countPracticeFinished, senderClientId, memberships.size());

            if (actionUpdate.getRoleCode() == 0) {
                subjectsPracticeFinished.put(senderClientId, new double[]{actionUpdate.getaFirmA()} );
            } else {
                if (actionUpdate.getRoleCode() == 1) {
                    subjectsPracticeFinished.put(senderClientId, new double[]{actionUpdate.getaFirmB()} );
                } else {
                    if (actionUpdate.getRoleCode() == 2) {
                        subjectsPracticeFinished.put(senderClientId, new double[]{actionUpdate.getaFirmC()});
                    } else {
                        subjectsPracticeFinished.put(senderClientId, new double[]{actionUpdate.getaFirmD()});
                    }
                }
            }

            showScreen(senderClientId, DefaultWaitingScreen.class, new DefaultWaitingScreen.ParamObject());

            if (countPracticeFinished ==  memberships.size()) {
                Thread.sleep(4000);
                endPeriod();
            }

        }
    }

    private void processReadyUpdate() throws Exception {

        readyCount = readyCount + 1;

        if (readyCount == memberships.size()) {
            // If initial ready message of all clients is received, send start signal to clients
            log4j.info("All clients reported ready stated. Sending start signal");
            ContinuousCompetitionParamObject initMarketUpdate = new ContinuousCompetitionParamObject();
            initMarketUpdate.setStartFlag(true);
            updateMarketValuesOfMarketUpdate(initMarketUpdate); //TODO redundant

            for (Membership membership : memberships) {
                String clientId = membership.getSubject().getIdClient();
                updateParamObject(clientId, initMarketUpdate);
            }
            readyCount = 0;
        }


    }


    private void savePayoffsToDatabase(Subject subject, FirmDescription firmDescription) {
        Trial trial = new Trial();
        trial.setSubjectGroup(this.getSubjectGroup());
        trial.setSubject(subject);
        trial.setServerTime(new Date().getTime());                  //Todo implement correct date format
        trial.setScreenName(this.getCurrentTreatment().getName());  //Todo why save treatment?
        trial.setEvent("SRV_SAVE_PAYOFF");
        trial.setValue(subject.getIdClient()+ "," + firmDescription + "," + subject.getPayoff()
        );

        //trial.setSubject(humanBidder);

        try {
            TrialManagement.getInstance().createNewTrial(trial);
        } catch (StructureManagementException e1) {
            e1.printStackTrace();
        }
    }

    private void saveMarketDataToDatabase(int countId) {


        Trial trial = new Trial();
        trial.setSubjectGroup(this.getSubjectGroup());
        trial.setServerTime(new Date().getTime());                  //Todo implement correct date format
        trial.setScreenName(this.getCurrentTreatment().getName());  //Todo why save treatment?
        trial.setEvent("SRV_CALC_MARKET_UPDATE");
        trial.setValue(serverTime+ "," + countId + ",0," +
                        aFirmA + "," + aFirmB + "," + aFirmC + "," + aFirmD +  "," + aMarket + "," +
                        oFirmA + "," + oFirmB + "," + oFirmC + "," + oFirmD +  "," + oMarket + "," +
                        profitFirmA + "," + profitFirmB + "," + profitFirmC + "," + profitFirmD
        );

        //trial.setSubject(humanBidder);

        try {
            TrialManagement.getInstance().createNewTrial(trial);
        } catch (StructureManagementException e1) {
            e1.printStackTrace();
        }
    }

    private void saveActionUpdateToDatabase(ContinuousCompetitionResponseObject pu, String senderClientId) {
        Trial trial = new Trial();
        trial.setSubjectGroup(this.getSubjectGroup());
        trial.setServerTime(new Date().getTime());                  //Todo implement correct date format
        trial.setScreenName(this.getCurrentTreatment().getName());  //Todo why save treatment?

        for(Membership membership : memberships) {                  //Todo: more efficient way to retrieve subject based on clientId?
            Subject subject = membership.getSubject();
            if (subject.getIdClient().equals(senderClientId)) {
                trial.setSubject(subject);
            }
        }

        trial.setEvent("SRV_RCV_PRICE_UPDATE");
        trial.setValue(serverTime + "," + pu.getCountId() + "," + pu.getRoleCode()+ "," +
                        aFirmA + "," + aFirmB + "," + aFirmC + "," + aFirmD
        );

        try {
            TrialManagement.getInstance().createNewTrial(trial);
        } catch (StructureManagementException e1) {
            e1.printStackTrace();
        }
    }


    @Override
    protected void endPeriod() throws Exception {
        this.finished = true;
        log4j.warn("endPeriod()");
    }

    private void updateMarketActions(ContinuousCompetitionResponseObject actionUpdate, String senderClientId) {
        log4j.trace("updateMarketActions() - start of execution");

        if ( senderClientId.equals(firmA.getIdClient()) ) {
            aFirmA = actionUpdate.getaFirmA();
            log4j.info("Receiving actionUpdate with countId {} from client {}: Set aFirmA = {}", actionUpdate.getCountId(), senderClientId, aFirmA);
        }

        if  ( senderClientId.equals(firmB.getIdClient()) ) {
            aFirmB = actionUpdate.getaFirmB();
            log4j.info("Receiving actionUpdate with countId {} from client {}: Set aFirmB = {}", actionUpdate.getCountId(), senderClientId, aFirmB);
        }

        if ( senderClientId.equals(firmC.getIdClient()) ) {
            aFirmC = actionUpdate.getaFirmC();
            log4j.info("Receiving actionUpdate with countId {} from client {}: Set aFirmC = {}", actionUpdate.getCountId(), senderClientId, aFirmC);
        }

        if ( senderClientId.equals(firmD.getIdClient()) ) {
            aFirmD = actionUpdate.getaFirmD();
            log4j.info("Receiving actionUpdate with countId {} from client {}: Set aFirmD = {}", actionUpdate.getCountId(), senderClientId, aFirmD);
        }

        log4j.trace("updateMarketActions() - end of execution");
    }

    private void initPracticeRoundActionData() {
        aFirmA = 0.0;
        aFirmB = 0.0;
        aFirmC = 0.0;
        aFirmD = 0.0;
    }

    private void initRegularRoundActionData(Subject firmA, Subject firmB, Subject firmC, Subject firmD) {
        aFirmA = subjectsPracticeFinished.get(firmA.getIdClient())[0];
        aFirmB = subjectsPracticeFinished.get(firmB.getIdClient())[0];

        if (isTriopolyTreatment || isQuadropolyTreatment) {
            aFirmC = subjectsPracticeFinished.get(firmC.getIdClient())[0];
        }

        if (isQuadropolyTreatment) {
            aFirmD = subjectsPracticeFinished.get(firmD.getIdClient())[0];
        }
    }

    private ContinuousCompetitionParamObject calculateMarketDataAndCreateMarketUpdate() {

        ContinuousCompetitionParamObject marketData = marketDataCalculator.calculateMarketData(aFirmA, aFirmB, aFirmC, aFirmD);

        aMarket = marketData.getaMarket();
        oMarket = marketData.getoMarket();
        oFirmA = marketData.getoFirmA();
        oFirmB = marketData.getoFirmB();
        oFirmC = marketData.getoFirmC();
        oFirmD = marketData.getoFirmD();
        profitFirmA = marketData.getProfitFirmA();
        profitFirmB = marketData.getProfitFirmB();
        profitFirmC = marketData.getProfitFirmC();
        profitFirmD = marketData.getProfitFirmD();

        log4j.info("After calculation of market data: actions - aFirmA: {}, aFirmB: {}, aFirmC: {}, aFirmD: {}, aMarket: {}.", aFirmA, aFirmB, aFirmC, aFirmD, aMarket);
        log4j.info("After calculation of market data: outputs - oFirmA: {}, oFirmB: {}, oFirmC: {}, oFirmD: {}.", oFirmA, oFirmB, oFirmC, oFirmD);
        log4j.info("After calculation of market data: profits - profitFirmA: {}, profitFirmB: {}, profitFirmC: {}, profitFirmD: {}", profitFirmA, profitFirmB, profitFirmC, profitFirmD);


        return marketData;

    }

    private ContinuousCompetitionParamObject calculateBalanceDataAndAddToMarketUpdate(ContinuousCompetitionParamObject marketUpdate) {

        if (isDiscreteTreatment) {
            balanceFirmA = balanceFirmA + (profitFirmA/100.0);
            balanceFirmB = balanceFirmB + (profitFirmB/100.0);
            balanceFirmC = balanceFirmC + (profitFirmC/100.0);
            balanceFirmD = balanceFirmD + (profitFirmD/100.0);

        } else {
            log4j.debug("Ex-ante profits: firmA: {}, firmB: {}, firmC: {}, firmD: {}.", profitFirmA, profitFirmB, profitFirmC, profitFirmD);
            log4j.debug("Ex-ante profit increments: firmA: {}, firmB: {}, firmC: {}, firmD: {}. ", (profitFirmA / (duration / updateTimeStep)), (profitFirmB / (duration / updateTimeStep)), (profitFirmC / (duration / updateTimeStep)), (profitFirmD / (duration / updateTimeStep)));

            balanceFirmA = balanceFirmA + (profitFirmA / (30.0 * (1000.0/updateTimeStep) * 100.0));
            balanceFirmB = balanceFirmB + (profitFirmB / (30.0 * (1000.0/updateTimeStep) * 100.0));
            balanceFirmC = balanceFirmC + (profitFirmC / (30.0 * (1000.0/updateTimeStep) * 100.0));
            balanceFirmD = balanceFirmD + (profitFirmD / (30.0 * (1000.0/updateTimeStep) * 100.0));
        }

        firmA.setPayoff(balanceFirmA);
        firmB.setPayoff(balanceFirmB);
        if (isTriopolyTreatment || isQuadropolyTreatment) {firmC.setPayoff(balanceFirmC);}
        if (isQuadropolyTreatment) {firmD.setPayoff(balanceFirmD);}

        marketUpdate.setBalanceFirmA(balanceFirmA);
        marketUpdate.setBalanceFirmB(balanceFirmB);
        marketUpdate.setBalanceFirmC(balanceFirmC);
        marketUpdate.setBalanceFirmD(balanceFirmD);

        log4j.info("After calculation of market data: payoffs - balanceFirmA: {}, balanceFirmB: {}, balanceFirmC: {}, balanceFirmD: {}", balanceFirmA, balanceFirmB, balanceFirmC, balanceFirmD);

        return marketUpdate;
    }


    private ContinuousCompetitionParamObject initBalanceDataAndUpdateMarketUpdate(ContinuousCompetitionParamObject marketUpdate){
        balanceFirmA = 0.0;
        balanceFirmB = 0.0;
        balanceFirmC = 0.0;
        balanceFirmD = 0.0;

        marketUpdate.setBalanceFirmA(balanceFirmA);
        marketUpdate.setBalanceFirmB(balanceFirmB);
        marketUpdate.setBalanceFirmC(balanceFirmC);
        marketUpdate.setBalanceFirmD(balanceFirmD);

        return marketUpdate;
    }


    private ContinuousCompetitionParamObject updateMarketValuesOfMarketUpdate(ContinuousCompetitionParamObject marketUpdate) {
        marketUpdate.setaFirmA(aFirmA);
        marketUpdate.setaFirmB(aFirmB);
        marketUpdate.setaFirmC(aFirmC);
        marketUpdate.setaFirmD(aFirmD);
        marketUpdate.setaMarket(aMarket);
        marketUpdate.setoMarket(oMarket);
        marketUpdate.setoFirmA(oFirmA);
        marketUpdate.setoFirmB(oFirmB);
        marketUpdate.setoFirmC(oFirmC);
        marketUpdate.setoFirmD(oFirmD);
        marketUpdate.setProfitFirmA(profitFirmA);
        marketUpdate.setProfitFirmB(profitFirmB);
        marketUpdate.setProfitFirmC(profitFirmC);
        marketUpdate.setProfitFirmD(profitFirmD);

        log4j.trace("calculateMarketData() - end of execution");
        return marketUpdate;
    }


    /* Helper class to store (action, output) pairs */
    class FirmRecord {
        double p;
        double q;

        public FirmRecord (double q, double p) {
            this.q = q;
            this.p = p;
        }
    }

}
