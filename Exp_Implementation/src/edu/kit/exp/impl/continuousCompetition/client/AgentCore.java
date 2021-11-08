package edu.kit.exp.impl.continuousCompetition.client;

import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.impl.continuousCompetition.server.ContinuousCompetitionMarketDataCalculator;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dschnurr on 18.03.15.
 */
public class AgentCore extends Screen {

    private static final Logger log4j = LogManager.getLogger(AgentCore.class.getName());

    protected ContinuousCompetitionMarketDataCalculator marketDataCalculator;
    protected AgentStrategy agentStrategy;

    protected boolean isDiscreteTreatment;
    protected boolean isTriopolyTreatment;
    protected boolean isQuadropolyTreatment;
    protected boolean isCournotTreatment;
    protected boolean practiceRound;

    protected SubjectGroup subjectGroup;
    protected Subject subject;
    protected String screenName;
    protected FirmDescription myRole;
    protected int myRoleCode;

    protected int localTime ;
    protected int timeStep;
    protected int duration;
    protected int maxRoundNumber;
    protected int countId;
    protected boolean lastPeriod;

    protected ContinuousCompetitionParamObject latestMarketUpdate;

    protected int action;
    protected double currentBalance;


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
    public AgentCore(String gameId, ContinuousCompetitionParamObject parameter, String screenId, Long showUpTime) {
        super(gameId, parameter, screenId, showUpTime);

        subjectGroup =  parameter.getSubjectGroup();
        subject = parameter.getSubject();

        this.isCournotTreatment = parameter.isCournotTreatment();
        this.isDiscreteTreatment = parameter.isDiscreteTreatment();
        this.isTriopolyTreatment = parameter.isTriopolyTreatment();
        this.isQuadropolyTreatment = parameter.isQuadropolyTreatment();
        this.practiceRound = parameter.isPracticeRound();
        this.duration = parameter.getDuration();
        this.localTime = 0;
        this.timeStep = parameter.getUpdateTimeStep();
        this.myRole = parameter.getRole();
        this.myRoleCode = parameter.getRoleCode();

        if (isDiscreteTreatment) {
            this.maxRoundNumber = duration / timeStep;
        }

        marketDataCalculator = new ContinuousCompetitionMarketDataCalculator(isCournotTreatment);
        agentStrategy = new AgentStrategy(this);

        log4j.info("Started AgentInterface for client {} in cohort {} with discreteTreatment = {}, practiceRound = {}", subject.getIdClient(), subjectGroup.getIdSubjectGroup(), isDiscreteTreatment, practiceRound);

        latestMarketUpdate = parameter;

        log4j.info("Initial price values: aFirmA: {}, aFirmB: {}, aFirmC: {}, aFirmD: {}", parameter.getaFirmA(), parameter.getProfitFirmB(), parameter.getaFirmC(), parameter.getaFirmD());


        agentStrategy.init((ContinuousCompetitionParamObject) parameter);
        if (!isDiscreteTreatment && !practiceRound) {
                log4j.info("Client {} sending ready message to server.", subject.getIdClient());
                ContinuousCompetitionResponseObject readyUpdate = new ContinuousCompetitionResponseObject();
                readyUpdate.setClientReady(true);
                sendResponse(readyUpdate);
        }
    }

    public void updateAction(int updatedAction) {
        log4j.info("updateAction({})", updatedAction);
        this.action = updatedAction;

        if (isDiscreteTreatment || practiceRound) {
            log4j.info("call sendActionUpdate()");
            sendActionUpdate();
        }
    }

    @Override
    public void processParamObjectUpdate(IScreenParamObject paramObject) {
        // Convert parameter Object in market update and keep track of latest market update
        ContinuousCompetitionParamObject marketUpdate = (ContinuousCompetitionParamObject) paramObject;
        latestMarketUpdate = marketUpdate;

        if (!isDiscreteTreatment && marketUpdate.getStartFlag()) {
            log4j.info("Recipient {} (role: {}) in cohort {} received start flag - starting timer.", subject.getIdClient(), myRole, subjectGroup.getIdSubjectGroup());
            initTimer();
        }

        agentStrategy.processMarketUpdate(marketUpdate);
    }

    private void sendActionUpdate() {
        log4j.debug("sendPriceUpdate(): start of execution - countId: {}"+countId);
        ContinuousCompetitionResponseObject actionUpdate = new ContinuousCompetitionResponseObject();
        actionUpdate.setCountId(countId);
        actionUpdate.setLocalTime(localTime);
        actionUpdate.setRoleCode(myRoleCode);

        if (practiceRound) { actionUpdate.setPracticeRoundFinished(true); }
        if (lastPeriod) { actionUpdate.setClientFinished(true); }

        if (myRole == FirmDescription.FirmA) {
            actionUpdate.setaFirmA(action);
            actionUpdate.setBalanceFirmA(currentBalance);
            log4j.info("Firm A: Sending actionUpdate with countId {} and time {} in round {} - pFirmA: {}.", countId, localTime, (localTime/timeStep), action);
        } else {
            if (myRole == FirmDescription.FirmB) {
                actionUpdate.setaFirmB(action);
                actionUpdate.setBalanceFirmB(currentBalance);
                log4j.info("Firm B: Sending actionUpdate with countId {} and time {} in round {} - pFirmB: {}.", countId, localTime, (localTime/timeStep), action);
            } else {
                if (myRole == FirmDescription.FirmC) {
                    actionUpdate.setaFirmC(action);
                    actionUpdate.setBalanceFirmC(currentBalance);
                    log4j.info("Firm C: Sending actionUpdate with countId {} and time {} in round {} - pFirmC: {}.", countId, localTime, (localTime/timeStep), action);
                } else {
                    //myRole == FirmDescription.FirmD
                    actionUpdate.setaFirmD(action);
                    actionUpdate.setBalanceFirmD(currentBalance);
                    log4j.info("Firm D: Sending actionUpdate with countId {} and time {} in round {} - pFirmD: {}.", countId, localTime, (localTime/timeStep), action);
                }
            }
        }

        sendResponse(actionUpdate);

        log4j.debug("sendPriceUpdate(): end of execution - countId: {}", actionUpdate.getCountId());
    }

    public void initTimer() {
        localTime = 0;
        int delay = timeStep;

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                sendActionUpdate();
                countId = countId +1;

                if (localTime < (duration-timeStep)) {
                    localTime = localTime + timeStep;
                    log4j.info("Timer: Advanced local time from {} to {}. Duration is set to: {}", localTime-timeStep, localTime, duration);
                } else {
                    ((Timer) evt.getSource()).stop();
                    lastPeriod = true;
                    sendActionUpdate();
                    log4j.warn("Stopped timer at {}. Set lastPeriod to {}", localTime, lastPeriod);
                }
            }
        };
        new Timer(delay, taskPerformer).start();
    }


}
