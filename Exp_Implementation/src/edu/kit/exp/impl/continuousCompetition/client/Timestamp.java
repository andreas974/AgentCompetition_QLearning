package edu.kit.exp.impl.continuousCompetition.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dschnurr on 27.02.14.
 */
public class Timestamp {

    private boolean isDiscrete;
    private int period;
    private int time;
    private int aFirmA;
    private int aFirmB;
    private int pFirmA;
    private int pFirmB;
    private int pFirmC;
    private int pFirmD;

    private double pMarket;
    private double qMarket;

    private double qFirmA;
    private double qFirmB;
    private double qFirmC;
    private double qFirmD;

    private double profitFirmA;
    private double profitFirmB;
    private double profitFirmC;
    private double profitFirmD;


    public Timestamp(boolean isDiscrete) {
        this.isDiscrete = isDiscrete;
    }

    public Timestamp(int time) {
        this.time = time;
    }

    public Timestamp() {

    }

    // Getters

    public int getTime() {
        return time;
    }

    public int getaFirmA() {
        return aFirmA;
    }

    public int getaFirmB() {
        return aFirmB;
    }

    public int getpFirmA() {
        return pFirmA;
    }

    public int getpFirmB() {
        return pFirmB;
    }

    public int getpFirmC() {
        return pFirmC;
    }

    public int getpFirmD() {
        return pFirmD;
    }

    public double getqFirmA() {
        return qFirmA;
    }

    public double getqFirmB() {
        return qFirmB;
    }

    public double getqFirmC() {
        return qFirmC;
    }

    public double getqFirmD() {
        return qFirmD;
    }

    public double getpMarket() {
        return pMarket;
    }

    public double getqMarket() {
        return qMarket;
    }

    public double getProfitFirmA() {
        return profitFirmA;
    }

    public double getProfitFirmB() {
        return profitFirmB;
    }

    public double getProfitFirmC() {
        return profitFirmC;
    }

    public double getProfitFirmD() {
        return profitFirmD;
    }

    public boolean isDiscrete() {
        return isDiscrete;
    }

    public int getPeriod() {
        return period;
    }

    // Setters

    public void setTime(int time) {
        this.time = time;
    }

    public void setaFirmA(int aFirmA) {
        this.aFirmA = aFirmA;
    }

    public void setaFirmB(int aFirmB) {
        this.aFirmB = aFirmB;
    }

    public void setpFirmA(int pFirmA) {
        this.pFirmA = pFirmA;
    }

    public void setpFirmB(int pFirmB) {
        this.pFirmB = pFirmB;
    }

    public void setpFirmC(int pFirmC) {
        this.pFirmC = pFirmC;
    }

    public void setpFirmD(int pFirmD) {
        this.pFirmD = pFirmD;
    }

    public void setqFirmA(double qFirmA) {
        this.qFirmA = qFirmA;
    }

    public void setqFirmB(double qFirmB) {
        this.qFirmB = qFirmB;
    }

    public void setqFirmC(double qFirmC) {
        this.qFirmC = qFirmC;
    }

    public void setqFirmD(double qFirmD) {
        this.qFirmD = qFirmD;
    }

    public void setpMarket(double pMarket) {
        this.pMarket = pMarket;
    }

    public void setqMarket(double qMarket) {
        this.qMarket = qMarket;
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

    public void setDiscrete(boolean isDiscrete) {
        this.isDiscrete = isDiscrete;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
