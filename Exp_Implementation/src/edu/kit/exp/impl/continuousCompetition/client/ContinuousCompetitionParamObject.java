package edu.kit.exp.impl.continuousCompetition.client;

import edu.kit.exp.client.gui.screens.Screen.ParamObject;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * Created by dschnurr on 04.03.14.
 */
public class ContinuousCompetitionParamObject extends ParamObject {

    private SubjectGroup subjectGroup;
    private Subject subject;
    private String screenName;
    private double diffParam;
    private boolean practiceRound = false;
    private boolean initialUpdate = false;
    private boolean isDiscreteTreatment = false;
    private boolean isTriopolyTreatment = true;
    private boolean isCournotTreatment;
    private boolean isQuadropolyTreatment;
    private int duration;
    private int globalTime;
    private int updateTimeStep;

    private final double COEFFICIENT_PRICES = 1;
    private final double COEFFICIENT_PROFITS = 1;


    private String statusMsg;

    private Boolean startFlag = false;
    private FirmDescription role;
    int roleCode;
    int countId;

    private double aFirmA;
    private double aFirmB;
    private double aFirmC;
    private double aFirmD;

    private double aMarket;
    private double oMarket;

    private double oFirmA;
    private double oFirmB;
    private double oFirmC;
    private double oFirmD;

    private double profitFirmA;
    private double profitFirmB;
    private double profitFirmC;
    private double profitFirmD;

    private double balanceFirmA;
    private double balanceFirmB;
    private double balanceFirmC;
    private double balanceFirmD;


    public void setDiffParam(double diffParam) {
        this.diffParam = diffParam;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setUpdateTimeStep(int updateTimeStep) {
        this.updateTimeStep = updateTimeStep;
    }

    public void setPracticeRound(boolean practiceRound) {
        this.practiceRound = practiceRound;
    }

    public void setInitialUpdate(boolean initialUpdate) {
        this.initialUpdate = initialUpdate;
    }

    public void setSubjectGroup(SubjectGroup subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setRole(FirmDescription role) {
        this.role = role;
    }

    public void setRoleCode(int roleCode) {
        this.roleCode = roleCode;
    }

    public void setDiscreteTreatment(boolean isDiscreteTreatment) {
        this.isDiscreteTreatment = isDiscreteTreatment;
    }

    public void setTriopolyTreatment(boolean isDuopolyTreatment) {
        this.isTriopolyTreatment = isDuopolyTreatment;
    }

    public void setQuadropolyTreatment(boolean isQuadropolyTreatment) {
        this.isQuadropolyTreatment = isQuadropolyTreatment;
    }

    public void setCournotTreatment(boolean isCournotTreatment) {
        this.isCournotTreatment = isCournotTreatment;
    }

    public void setaFirmA(double aFirmA) {
        this.aFirmA = aFirmA;
    }

    public void setaFirmB(double aFirmB) {
        this.aFirmB = aFirmB;
    }

    public void setaFirmC(double aFirmC) {
        this.aFirmC = aFirmC;
    }

    public void setaFirmD(double aFirmD) {
        this.aFirmD = aFirmD;
    }

    public void setaMarket(double aMarket) {
        this.aMarket = aMarket;
    }

    public void setoMarket(double oMarket) {
        this.oMarket = oMarket;
    }

    public void setProfitFirmA(double profitFirmA) {
        this.profitFirmA = profitFirmA;
    }

    public void setProfitFirmB(double profitFirmB) {
        this.profitFirmB = profitFirmB;
    }

    public void setProfitFirmC(double profitFirmC) {
        this.profitFirmC = profitFirmC;
    }

    public void setProfitFirmD(double profitFirmD) {
        this.profitFirmD = profitFirmD;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public void setGlobalTime(int globalTime) {
        this.globalTime = globalTime;
    }

    public void setoFirmA(double oFirmA) {
        this.oFirmA = oFirmA;
    }

    public void setoFirmB(double oFirmB) {
        this.oFirmB = oFirmB;
    }

    public void setoFirmC(double oFirmC) {
        this.oFirmC = oFirmC;
    }

    public void setoFirmD(double oFirmD) {
        this.oFirmD = oFirmD;
    }

    public void setBalanceFirmA(double balanceFirmA) {
        this.balanceFirmA = balanceFirmA;
    }

    public void setBalanceFirmB(double balanceFirmB) {
        this.balanceFirmB = balanceFirmB;
    }

    public void setBalanceFirmC(double balanceFirmC) {
        this.balanceFirmC = balanceFirmC;
    }

    public void setBalanceFirmD(double balanceFirmD) {
        this.balanceFirmD = balanceFirmD;
    }



    public boolean isInitialUpdate() {
        return initialUpdate;
    }

    public int getDuration() {
        return duration;
    }

    public int getUpdateTimeStep() {
        return updateTimeStep;
    }

    public boolean isPracticeRound() {
        return practiceRound;
    }

    public boolean isDiscreteTreatment() {
        return isDiscreteTreatment;
    }

    public boolean isTriopolyTreatment() {
        return isTriopolyTreatment;
    }

    public boolean isQuadropolyTreatment() {
        return isQuadropolyTreatment;
    }

    public boolean isCournotTreatment() {
        return isCournotTreatment;
    }

    public double getDiffParam() {
        return diffParam;
    }

    public SubjectGroup getSubjectGroup() {
        return subjectGroup;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getScreenName() {
        return screenName;
    }

    public FirmDescription getRole() {
        return role;
    }

    public int getRoleCode() {
        return roleCode;
    }

    public int getGlobalTime() {
        return globalTime;
    }

    public int getaFirmA() {
        return (int) Math.round(aFirmA *COEFFICIENT_PRICES);
    }

    public int getaFirmB() {
        return (int) Math.round(aFirmB *COEFFICIENT_PRICES);
    }

    public int getaFirmC() {
        return (int) Math.round(aFirmC *COEFFICIENT_PRICES);
    }

    public int getaFirmD() {
        return (int) Math.round(aFirmD *COEFFICIENT_PRICES);
    }

    public double getaMarket() {
        return (aMarket *COEFFICIENT_PRICES);
    }

    public double getoMarket() {
        return oMarket;
    }

    public double getProfitFirmA() {
        return (profitFirmA*COEFFICIENT_PROFITS);
    }

    public double getProfitFirmB() {
        return (profitFirmB*COEFFICIENT_PROFITS);
    }

    public double getProfitFirmC() {
        return (profitFirmC*COEFFICIENT_PROFITS);
    }

    public double getProfitFirmD() {
        return (profitFirmD*COEFFICIENT_PROFITS);
    }

    public Boolean getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(Boolean startFlag) {
        this.startFlag = startFlag;
    }

    public int getCountId() {
        return countId;
    }

    public void setCountId(int countId) {
        this.countId = countId;
    }

    public double getoFirmA() {
        return oFirmA;
    }

    public double getoFirmB() {
        return oFirmB;
    }

    public double getoFirmC() {
        return oFirmC;
    }

    public double getoFirmD() {
        return oFirmD;
    }

    public double getBalanceFirmA() {
        return balanceFirmA;
    }

    public double getBalanceFirmB() {
        return balanceFirmB;
    }

    public double getBalanceFirmC() {
        return balanceFirmC;
    }

    public double getBalanceFirmD() {
        return balanceFirmD;
    }
}
