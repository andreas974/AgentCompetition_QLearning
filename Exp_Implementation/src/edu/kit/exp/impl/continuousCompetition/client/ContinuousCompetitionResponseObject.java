package edu.kit.exp.impl.continuousCompetition.client;

import edu.kit.exp.client.gui.screens.Screen;

/**
 * Created by dschnurr on 05.03.14.
 */
public class ContinuousCompetitionResponseObject extends Screen.ResponseObject {

    private static final double COEFFICIENT_PRICES = 1;

    private String statusMsg;
    private int localTime;
    private int countId;
    private boolean clientReady = false;
    private boolean clientFinished = false;
    private int roleCode;
    private boolean practiceRoundFinished = false;

    private double aFirmA;
    private double aFirmB;
    private double aFirmC;
    private double aFirmD;

    private double balanceFirmA;
    private double balanceFirmB;
    private double balanceFirmC;
    private double balanceFirmD;

    // Setter methods

    public void setPracticeRoundFinished(boolean practiceRoundFinished) {
        this.practiceRoundFinished = practiceRoundFinished;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public void setLocalTime(int localTime) {
        this.localTime = localTime;
    }

    public void setRoleCode(int roleCode) {
        this.roleCode = roleCode;
    }

    public void setaFirmA(int aFirmA) {
        this.aFirmA = ((double) aFirmA) / COEFFICIENT_PRICES;
    }

    public void setaFirmB(int aFirmB) {
        this.aFirmB = ((double) aFirmB) / COEFFICIENT_PRICES;
    }

    public void setaFirmC(int aFirmC) {
        this.aFirmC = ((double) aFirmC) / COEFFICIENT_PRICES;
    }

    public void setaFirmD(int aFirmD) {
        this.aFirmD = ((double) aFirmD) / COEFFICIENT_PRICES;
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

    public void setClientFinished(boolean clientFinished) {
        this.clientFinished = clientFinished;
    }


    // Getter methods

    public boolean isPracticeRoundFinished() {
        return practiceRoundFinished;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public int getLocalTime() {
        return localTime;
    }

    public int getRoleCode() {
        return roleCode;
    }

    public double getaFirmA() {
        return aFirmA;
    }

    public double getaFirmB() {
        return aFirmB;
    }

    public double getaFirmC() {
        return aFirmC;
    }

    public double getaFirmD() {
        return aFirmD;
    }

    public int getCountId() {
        return countId;
    }

    public void setCountId(int countId) {
        this.countId = countId;
    }

    public boolean isClientReady() {
        return clientReady;
    }

    public void setClientReady(boolean clientReady) {
        this.clientReady = clientReady;
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

    public boolean isClientFinished() {
        return clientFinished;
    }
}
