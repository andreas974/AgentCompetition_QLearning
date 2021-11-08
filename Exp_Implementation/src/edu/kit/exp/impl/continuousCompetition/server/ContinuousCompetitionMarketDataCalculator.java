package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.impl.continuousCompetition.client.ContinuousCompetitionParamObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Created by dschnurr on 02.10.14.
 */
public class ContinuousCompetitionMarketDataCalculator {

    private static final Logger log4j = LogManager.getLogger(ContinuousCompetitionProfitCalculator.class.getName());
    private boolean isTriopolyTreatment;
    private boolean isCournotTreatment;
    private boolean isQuadropolyTreatment;
    private static double theta;    //theta
    private static double a = 100;            //market size
    private static double b = 1.0;            //price elasticity
    private static double coeff_profit_tri_cournot  = (1.5625 / 60.0);
    private static double coeff_profit_tri_betrand  = (2.3625 / 60.0);
    private static double coeff_profit_duo_bertrand = (1.2500 / 60.0);
    private static double coeff_profit_duo_cournot = (1.0 / 60.0);
    private static double coeff_profit_quadro_cournot = (2.25 / 60.0);
    private static double coeff_profit_quadro_bertrand = ((16875/4375) / 60.0);

    private static double coeff_q_duo_cournot = (60.0/100.0);
    private static double coeff_q_trio_cournot = (300/700.0);
    private static double coeff_q_quadro_cournot = (1/3.0);


    public ContinuousCompetitionMarketDataCalculator(boolean isCournotTreatment) {
        this((2.0/3.0), isCournotTreatment, false, false);
    }

    public ContinuousCompetitionMarketDataCalculator(double diffParam, boolean isCournotTreatment) {
        this(diffParam, isCournotTreatment, false, false);
    }

    public ContinuousCompetitionMarketDataCalculator(double diffParam, boolean isCournotTreatment, boolean isTriopolyTreatment) {
        this(diffParam, isCournotTreatment, isTriopolyTreatment, false);
    }


    public ContinuousCompetitionMarketDataCalculator(double diffParam, boolean isCournotTreatment, boolean isTriopolyTreatment, boolean isQuadropolyTreatment) {
        theta = (2.0/3.0);
        this.isCournotTreatment = isCournotTreatment;
        this.isTriopolyTreatment = isTriopolyTreatment;
        this.isQuadropolyTreatment = isQuadropolyTreatment;
    }


    public ContinuousCompetitionParamObject calculateMarketData(double aFirmA, double aFirmB, double aFirmC) {

        ContinuousCompetitionParamObject marketUpdate = calculateMarketData(aFirmA, aFirmB, aFirmC, -1);
        return marketUpdate;

    }


    public ContinuousCompetitionParamObject calculateMarketData(double aFirmA, double aFirmB, double aFirmC, double aFirmD) {
        ContinuousCompetitionParamObject marketUpdate;

        if (isQuadropolyTreatment) {
            if (isCournotTreatment) {
                marketUpdate = calculateMarketDataFromGivenQuantitiesForQuadropoly(aFirmA, aFirmB, aFirmC, aFirmD);
            } else {
                marketUpdate = calculateMarketDataFromGivenPricesForQuadropoly(aFirmA, aFirmB, aFirmC, aFirmD);
            }
        } else {
            if (isTriopolyTreatment) {
                if (isCournotTreatment) {
                    marketUpdate = calculateMarketDataFromGivenQuantitiesForTriopoly(aFirmA, aFirmB, aFirmC);
                } else {
                    marketUpdate = calculateMarketDataFromGivenPricesForTriopoly(aFirmA, aFirmB, aFirmC);
                }
            } else {
                // isDuopolyTreatment == true
                if (isCournotTreatment) {
                    marketUpdate = calculateMarketDataFromGivenQuantitiesForDuopoly(aFirmA, aFirmB);
                } else {
                    marketUpdate = calculateMarketDataFromGivenPricesForDuopoly(aFirmA, aFirmB);
                }
            }
        }


        return marketUpdate;
    }


    public ContinuousCompetitionParamObject calculateMarketDataFromGivenPricesForQuadropoly(double aFirmA, double aFirmB, double aFirmC, double aFirmD) {

        double aMarket;
        double profitFirmA, profitFirmB, profitFirmC, profitFirmD;
        double oFirmA, oFirmB, oFirmC, oFirmD, oMarket;

        FirmRecord firmA = new FirmRecord(aFirmA);
        FirmRecord firmB = new FirmRecord(aFirmB);
        FirmRecord firmC = new FirmRecord(aFirmC);
        FirmRecord firmD = new FirmRecord(aFirmD);

        log4j.debug("Params - theta: {}, alpha: {}, beta: {}, gamma: {}", theta, calcAlpha(4), calcBeta(4), calcGamma(4));
        log4j.debug("Prices - aFirmA: {}, aFirmB: {}, aFirmC: {}, aFirmD: {}", aFirmA, aFirmB, aFirmC, aFirmD);

        aMarket = (aFirmA + aFirmB + aFirmC + aFirmD)/4;

        firmA.q = calculateDemandFromGivenPrices(aFirmA, 4, (aFirmB+aFirmC+aFirmD)/3);
        firmB.q = calculateDemandFromGivenPrices(aFirmB, 4, (aFirmA+aFirmC+aFirmD)/3);
        firmC.q = calculateDemandFromGivenPrices(aFirmC, 4, (aFirmA+aFirmB+aFirmD)/3);
        firmD.q = calculateDemandFromGivenPrices(aFirmD, 4, (aFirmA+aFirmB+aFirmC)/3);

        log4j.debug("Quantities - oFirmA: {}, oFirmB: {}, oFirmC: {}, oFirmD: {}", firmA.q, firmB.q, firmC.q, firmD.q);

        FirmRecord[] firms = {firmA, firmB, firmC, firmD};
        Arrays.sort(firms);

        log4j.debug("Sorted Quantities - min: {}, second: {}, third: {}, max: {}", firms[0].q, firms[1].q, firms[2].q, firms[3].q);


        FirmRecord[] minFirm = {firms[0]};
        FirmRecord[] thiFirm = {firms[1]};
        FirmRecord[] secFirm = {firms[2]};
        FirmRecord[] maxFirm = {firms[3]};

        if (maxFirm[0].q < 0) {
            minFirm[0].q = 0.0;
            thiFirm[0].q = 0.0;
            secFirm[0].q = 0.0;
            maxFirm[0].q = 0.0;
        } else {
            // Demand of maxfirm is positive

            if (secFirm[0].q < 0) {
                // Demand of maxFirm is positive while other three firms exhibit negative demand
                // Calculate adjusted demand with n=4, m=1
                minFirm[0].q = 0.0;
                thiFirm[0].q = 0.0;
                secFirm[0].q = 0.0;

                maxFirm[0].q = calcAlpha(1) - calcBeta(1) * maxFirm[0].p;
                aMarket = maxFirm[0].p;

                log4j.debug("Retail Market: Three firm exit the market (Quadropoly calculation)");
            } else {
                // Demand of two firms (maxFirm, secFirm) is positive

                if (thiFirm[0].q < 0) {
                    // Demand of two firms is positive, while two firms exhibit negative demand
                    minFirm[0].q = 0.0;
                    thiFirm[0].q = 0.0;

                    // Calculate adjusted demand with n=4, m=2 for secFirm;
                    // Note that this demand may be negative for m=2 although it was positive for m=4
                    secFirm[0].q = calculateDemandFromGivenPricesInDuopoly(secFirm[0].p, maxFirm[0].p);

                    if (secFirm[0].q < 0) {
                        // Demand of secFirm is found to be negative in calculation for m=2
                        secFirm[0].q = 0.0;
                        maxFirm[0].q = calcAlpha(1) - calcBeta(1) * maxFirm[0].p;
                        aMarket = maxFirm[0].p;
                        log4j.debug("One firm remains active, three firms exit the market (Duopoly calculation)");
                    } else {
                        // Demand of secFirm is found to be positive in calculation for m=2
                        maxFirm[0].q = calculateDemandFromGivenPricesInDuopoly(maxFirm[0].p, secFirm[0].p);
                        aMarket = (maxFirm[0].p+secFirm[0].p)/2;
                        log4j.debug("Two firms remain active, two firm exit the market (Duopoly calculation)");
                    }

                } else {
                    // Demand of three firms is positive

                    if (minFirm[0].q < 0) {
                        // Demand of three firms is positive while one firm exhibits negative demand
                        minFirm[0].q = 0.0;

                        // Calculate adjusted demand with n=4, m=3 for thiFirm;
                        // Note that this demand may be negative for m=3 although it was positive for m=4
                        thiFirm[0].q = calculateDemandFromGivenPricesInTriopoly(thiFirm[0].p, secFirm[0].p, maxFirm[0].p);

                        if (thiFirm[0].q < 0) {
                            // Demand of thiFirm is found to be negative in calculation for m=3
                            thiFirm[0].q = 0.0;

                            // Calculate adjusted demand with n=4, m=2 for secFirm;
                            // Note that this demand may be negative for m=2 although it was positive for m=4
                            secFirm[0].q = calculateDemandFromGivenPricesInDuopoly(secFirm[0].p, maxFirm[0].p);

                            if (secFirm[0].q < 0) {
                                // Demand of secFirm is found to be negative in calculation for m=2
                                secFirm[0].q = 0.0;
                                maxFirm[0].q = calcAlpha(1) - calcBeta(1) * maxFirm[0].p;
                                aMarket = maxFirm[0].p;
                                log4j.debug("One firm remains active, three firms exit the market (Duopoly calculation)");
                            } else {
                                // Demand of secFirm is found to be positive in calculation for m=2
                                maxFirm[0].q = calculateDemandFromGivenPricesInDuopoly(maxFirm[0].p, secFirm[0].p);
                                aMarket = (maxFirm[0].p+secFirm[0].p)/2;
                                log4j.debug("Two firms remain active, two firm exit the market (Duopoly calculation)");
                            }

                        }  else {
                            // Demand of thiFirm is found to be positive in calculation for m=3
                            secFirm[0].q = calculateDemandFromGivenPricesInTriopoly(secFirm[0].p, thiFirm[0].p, maxFirm[0].p);
                            maxFirm[0].q = calculateDemandFromGivenPricesInTriopoly(maxFirm[0].p, thiFirm[0].p, secFirm[0].p);
                            aMarket = (maxFirm[0].p+secFirm[0].p+thiFirm[0].p)/3;
                            log4j.debug("Three firms remain active, one firm exits the market (Triopoly calculation)");
                        }
                    }
                    // Else: Demand for all four firms is positive, i.e. the inital demand values are valid
                }
            }
        }

        oFirmA = firmA.q;
        oFirmB = firmB.q;
        oFirmC = firmC.q;
        oFirmD = firmD.q;

        oMarket = (oFirmA + oFirmB +oFirmC+oFirmD)/4;

        profitFirmA = aFirmA * oFirmA * coeff_profit_quadro_bertrand;
        profitFirmB = aFirmB * oFirmB * coeff_profit_quadro_bertrand;
        profitFirmC = aFirmC * oFirmC * coeff_profit_quadro_bertrand;
        profitFirmD = aFirmD * oFirmD * coeff_profit_quadro_bertrand;

        ContinuousCompetitionParamObject marketUpdate = new ContinuousCompetitionParamObject();

        marketUpdate.setaFirmA(aFirmA);
        marketUpdate.setaFirmB(aFirmB);
        marketUpdate.setaFirmC(aFirmC);
        marketUpdate.setaFirmD(aFirmD);
        marketUpdate.setoFirmA(oFirmA);
        marketUpdate.setoFirmB(oFirmB);
        marketUpdate.setoFirmC(oFirmC);
        marketUpdate.setoFirmD(oFirmD);
        marketUpdate.setProfitFirmA(profitFirmA);
        marketUpdate.setProfitFirmB(profitFirmB);
        marketUpdate.setProfitFirmC(profitFirmC);
        marketUpdate.setProfitFirmD(profitFirmD);
        marketUpdate.setaMarket(aMarket);
        marketUpdate.setoMarket(oMarket);

        return marketUpdate;
    }

    public ContinuousCompetitionParamObject calculateMarketDataFromGivenQuantitiesForQuadropoly(double aFirmA, double aFirmB, double aFirmC, double aFirmD) {

        double aMarket;
        double profitFirmA, profitFirmB, profitFirmC, profitFirmD;
        double oFirmA, oFirmB, oFirmC, oFirmD;

        log4j.debug("Params - theta: {}, alpha: {}, beta: {}, gamma: {}", theta, calcAlpha(4), calcBeta(4), calcGamma(4));
        log4j.debug("Quantities - oFirmA: {}, oFirmB: {}, oFirmC: {}, oFirmD: {}", aFirmA, aFirmB, aFirmC, aFirmD);

        double pFirmAAdjusted = aFirmA * coeff_q_quadro_cournot;
        double pFirmBAdjusted = aFirmB * coeff_q_quadro_cournot;
        double pFirmCAdjusted = aFirmC * coeff_q_quadro_cournot;
        double pFirmDAdjusted = aFirmD * coeff_q_quadro_cournot;

        oFirmA = calculatePricesFromGivenQuantities(pFirmAAdjusted, (pFirmBAdjusted + pFirmCAdjusted + pFirmDAdjusted));
        oFirmB = calculatePricesFromGivenQuantities(pFirmBAdjusted, (pFirmAAdjusted + pFirmCAdjusted + pFirmDAdjusted));
        oFirmC = calculatePricesFromGivenQuantities(pFirmCAdjusted, (pFirmAAdjusted + pFirmBAdjusted + pFirmDAdjusted));
        oFirmD = calculatePricesFromGivenQuantities(pFirmDAdjusted, (pFirmAAdjusted + pFirmBAdjusted + pFirmCAdjusted));

        log4j.debug("Prices - aFirmA: {}, aFirmB: {}, aFirmC: {}, aFirmD: {}", oFirmA, oFirmB, oFirmC, oFirmD);

        aMarket = (aFirmA+aFirmB+aFirmC+aFirmD)/4;

        profitFirmA = pFirmAAdjusted * oFirmA * coeff_profit_quadro_cournot;
        profitFirmB = pFirmBAdjusted * oFirmB * coeff_profit_quadro_cournot;
        profitFirmC = pFirmCAdjusted * oFirmC * coeff_profit_quadro_cournot;
        profitFirmD = pFirmDAdjusted * oFirmD * coeff_profit_quadro_cournot;

        ContinuousCompetitionParamObject marketUpdate = new ContinuousCompetitionParamObject();

        marketUpdate.setaFirmA(aFirmA);
        marketUpdate.setaFirmB(aFirmB);
        marketUpdate.setaFirmC(aFirmC);
        marketUpdate.setaFirmD(aFirmD);
        marketUpdate.setaMarket(aMarket);
        marketUpdate.setoFirmA(oFirmA);
        marketUpdate.setoFirmB(oFirmB);
        marketUpdate.setoFirmC(oFirmC);
        marketUpdate.setoFirmD(oFirmD);
        marketUpdate.setProfitFirmA(profitFirmA);
        marketUpdate.setProfitFirmB(profitFirmB);
        marketUpdate.setProfitFirmC(profitFirmC);
        marketUpdate.setProfitFirmD(profitFirmD);

        return marketUpdate;
    }

    public ContinuousCompetitionParamObject calculateMarketDataFromGivenPricesForTriopoly(double aFirmA, double aFirmB, double aFirmC) {

        double aMarket;
        double profitFirmA, profitFirmB, profitFirmC;
        double oFirmA, oFirmB, oFirmC, oMarket;

        FirmRecord firmA = new FirmRecord(aFirmA);
        FirmRecord firmB = new FirmRecord(aFirmB);
        FirmRecord firmC = new FirmRecord(aFirmC);

        log4j.debug("Params - theta: {}, alpha: {}, beta: {}, gamma: {}", theta, calcAlpha(3), calcBeta(3), calcGamma(3));
        log4j.debug("Prices - aFirmA: {}, aFirmB: {}, aFirmC: {}", aFirmA, aFirmB, aFirmC);

        aMarket = (aFirmA + aFirmB + aFirmC)/3;

        firmA.q = calculateDemandFromGivenPricesInTriopoly(aFirmA, aFirmB, aFirmC);
        firmB.q = calculateDemandFromGivenPricesInTriopoly(aFirmB, aFirmA, aFirmC);
        firmC.q = calculateDemandFromGivenPricesInTriopoly(aFirmC, aFirmA, aFirmB);

        log4j.debug("Quantities - oFirmA: {}, oFirmB: {}, oFirmC: {}", firmA.q, firmB.q, firmC.q);


        FirmRecord[] minFirm = {firmA};
        FirmRecord[] midFirm;
        FirmRecord[] maxFirm;
        if (firmB.q < firmA.q) {
            //oFirmA is updated to represent midFirm, oFirmB is set as new minFirm,
            midFirm = minFirm;
            minFirm = new FirmRecord[] {firmB};

        } else {
            //oFirmA is still minimum, oFirmB is set as midFirm
            midFirm = new FirmRecord[] {firmB};
        }

        if (firmC.q < minFirm[0].q) {
            maxFirm = midFirm;
            midFirm = minFirm;
            minFirm = new FirmRecord[] {firmC};
        } else {
            if (firmC.q < midFirm[0].q) {
                maxFirm = midFirm;
                midFirm = new FirmRecord[] {firmC};
            } else {
                maxFirm = new FirmRecord[] {firmC};
            }
        }


        if(maxFirm[0].q < 0) {
            // Retail demand < 0 for all firms, thus qFirm = 0 for all firms
            minFirm[0].q = 0;
            midFirm[0].q = 0;
            maxFirm[0].q = 0;
        } else {
            if (midFirm[0].q < 0) {
                // Retail demand of one firm is positive, while two firms exhibit negative demand.
                // Calculate adjusted demand with n=3, m=1 for firm that exhibits a positive demand in the case of m=n=3

                minFirm[0].q = 0.0;
                midFirm[0].q = 0.0;

                maxFirm[0].q = calcAlpha(1) - calcBeta(1) * maxFirm[0].p;
                aMarket = maxFirm[0].p;

                log4j.debug("Retail Market: Two firm exit the market (Triopoly calculation)");
            } else {
                if (minFirm[0].q < 0) {
                    // Retail demand of two firms is positive, while one firm exhibits negative demand.
                    // Calculate adjusted demand with n=3, m=2 for firms that exhibit a positive demand in the case of m=n=3

                    minFirm[0].q = 0.0;

                    midFirm[0].q = calcAlpha(2) - calcBeta(2) * midFirm[0].p + calcGamma(2) * (maxFirm[0].p);
                    log4j.debug("Duopoly calculation: midFirm.q: {}, midFirm.p: {}", midFirm[0].q, midFirm[0].p);

                    if (midFirm [0].q < 0) {
                        midFirm[0].q = 0.0;
                        maxFirm[0].q = calcAlpha(1) - calcBeta(1) * maxFirm[0].p;

                        aMarket = maxFirm[0].p;
                        log4j.debug("Retail Market: Two firm exit the market (Duopoly calculation)");
                    } else {
                        maxFirm[0].q = calcAlpha(2) - calcBeta(2) * maxFirm[0].p + calcGamma(2) * (midFirm[0].p);

                        aMarket = (maxFirm[0].p + midFirm[0].p)/2;
                        log4j.debug("Retail Market: One firm exits the market - (Positive quantities in duopoly)");
                        log4j.debug("Quantity check:"+minFirm[0].q+ " with price "+minFirm[0].p);
                    }

                }
                // else: qFirm > 0 for all firms, thus initial demand is valid
            }

        }

        oFirmA = firmA.q;
        oFirmB = firmB.q;
        oFirmC = firmC.q;

        oMarket = (oFirmA + oFirmB +oFirmC)/3;

        profitFirmA = aFirmA * oFirmA * coeff_profit_tri_betrand;
        profitFirmB = aFirmB * oFirmB * coeff_profit_tri_betrand;
        profitFirmC = aFirmC * oFirmC * coeff_profit_tri_betrand;

        ContinuousCompetitionParamObject marketUpdate = new ContinuousCompetitionParamObject();

        marketUpdate.setaFirmA(aFirmA);
        marketUpdate.setaFirmB(aFirmB);
        marketUpdate.setaFirmC(aFirmC);
        marketUpdate.setoFirmA(oFirmA);
        marketUpdate.setoFirmB(oFirmB);
        marketUpdate.setoFirmC(oFirmC);
        marketUpdate.setProfitFirmA(profitFirmA);
        marketUpdate.setProfitFirmB(profitFirmB);
        marketUpdate.setProfitFirmC(profitFirmC);
        marketUpdate.setaMarket(aMarket);
        marketUpdate.setoMarket(oMarket);

        return marketUpdate;
    }

    public ContinuousCompetitionParamObject calculateMarketDataFromGivenPricesForDuopoly(double aFirmA, double aFirmB) {

        double aMarket, oMarket;
        double profitFirmA, profitFirmB;
        double oFirmA, oFirmB;

        log4j.debug("Params - theta: {}, alpha: {}, beta: {}, gamma: {}", theta, calcAlpha(3), calcBeta(3), calcGamma(3));
        log4j.debug("Prices - aFirmA: {}, aFirmB: {}", aFirmA, aFirmB);

        aMarket = (aFirmA + aFirmB)/2;
        oFirmA = calculateDemandFromGivenPricesInDuopoly(aFirmA, aFirmB);
        oFirmB = calculateDemandFromGivenPricesInDuopoly(aFirmB, aFirmA);

        log4j.debug("Quantities - oFirmA: {}, oFirmB: {}", oFirmA, oFirmB);


        if (oFirmA < 0) {
            // firmB is only firm in market
            oFirmA = 0.0;
            oFirmB = calcAlpha(1) - calcBeta(1) * aFirmB;
            aMarket = aFirmB;
            log4j.debug("Duopoly calculation: firmA exits the market - oFirmB: {}, aFirmB: {}", oFirmB, aFirmB);
        } else {
            if (oFirmB < 0) {
                // firm A is only firm in market
                oFirmB = 0.0;
                oFirmA = calcAlpha(1) - calcBeta(1) * aFirmA;
                aMarket = aFirmA;
                log4j.debug("Duopoly calculation: firmB exits the market - oFirmA: {}, aFirmA: {}", oFirmA, aFirmA);
            }
            // else duopoly quantities are valid
        }

        oMarket = (oFirmA + oFirmB)/2;

        profitFirmA = aFirmA * oFirmA * coeff_profit_duo_bertrand;
        profitFirmB = aFirmB * oFirmB * coeff_profit_duo_bertrand;

        ContinuousCompetitionParamObject marketUpdate = new ContinuousCompetitionParamObject();

        marketUpdate.setaFirmA(aFirmA);
        marketUpdate.setaFirmB(aFirmB);
        marketUpdate.setoFirmA(oFirmA);
        marketUpdate.setoFirmB(oFirmB);
        marketUpdate.setProfitFirmA(profitFirmA);
        marketUpdate.setProfitFirmB(profitFirmB);
        marketUpdate.setaMarket(aMarket);
        marketUpdate.setoMarket(oMarket);

        marketUpdate.setaFirmC(0.0);
        marketUpdate.setoFirmC(0.0);
        marketUpdate.setProfitFirmC(0.0);

        return marketUpdate;
    }

    public ContinuousCompetitionParamObject calculateMarketDataFromGivenQuantitiesForTriopoly(double aFirmA, double aFirmB, double aFirmC) {

        double aMarket;
        double profitFirmA, profitFirmB, profitFirmC;
        double oFirmA, oFirmB, oFirmC;

        log4j.debug("Params - theta: {}, alpha: {}, beta: {}, gamma: {}", theta, calcAlpha(3), calcBeta(3), calcGamma(3));
        log4j.debug("Quantities - oFirmA: {}, oFirmB: {}, oFirmC: {}", aFirmA, aFirmB, aFirmC);

        double pFirmAAdjusted = aFirmA * coeff_q_trio_cournot;
        double pFirmBAdjusted = aFirmB * coeff_q_trio_cournot;
        double pFirmCAdjusted = aFirmC * coeff_q_trio_cournot;

        oFirmA = calculatePricesFromGivenQuantitiesInTriopoly(pFirmAAdjusted, pFirmBAdjusted, pFirmCAdjusted);
        oFirmB = calculatePricesFromGivenQuantitiesInTriopoly(pFirmBAdjusted, pFirmAAdjusted, pFirmCAdjusted);
        oFirmC = calculatePricesFromGivenQuantitiesInTriopoly(pFirmCAdjusted, pFirmAAdjusted, pFirmBAdjusted);
        log4j.debug("Prices - aFirmA: {}, aFirmB: {}, aFirmC: {}", oFirmA, oFirmB, oFirmC);

        aMarket = (aFirmA+aFirmB+aFirmC)/3;

        profitFirmA = pFirmAAdjusted * oFirmA * coeff_profit_tri_cournot;
        profitFirmB = pFirmBAdjusted * oFirmB * coeff_profit_tri_cournot;
        profitFirmC = pFirmCAdjusted * oFirmC * coeff_profit_tri_cournot;

        ContinuousCompetitionParamObject marketUpdate = new ContinuousCompetitionParamObject();

        marketUpdate.setaFirmA(aFirmA);
        marketUpdate.setaFirmB(aFirmB);
        marketUpdate.setaFirmC(aFirmC);
        marketUpdate.setaMarket(aMarket);
        marketUpdate.setoFirmA(oFirmA);
        marketUpdate.setoFirmB(oFirmB);
        marketUpdate.setoFirmC(oFirmC);
        marketUpdate.setProfitFirmA(profitFirmA);
        marketUpdate.setProfitFirmB(profitFirmB);
        marketUpdate.setProfitFirmC(profitFirmC);

        return marketUpdate;

    }

    public ContinuousCompetitionParamObject calculateMarketDataFromGivenQuantitiesForDuopoly(double aFirmA, double aFirmB) {

        double aMarket, oMarket;
        double profitFirmA, profitFirmB;
        double oFirmA, oFirmB;

        log4j.debug("Params - theta: {}, alpha: {}, beta: {}, gamma: {}", theta, calcAlpha(3), calcBeta(3), calcGamma(3));
        log4j.debug("Quantities - oFirmA: {}, oFirmB: {}", aFirmA, aFirmB);

        double pFirmAAdjusted = aFirmA * coeff_q_duo_cournot;
        double pFirmBAdjusted = aFirmB * coeff_q_duo_cournot;

        oFirmA = calculatePricesFromGivenQuantitiesInDuopoly(pFirmAAdjusted, pFirmBAdjusted);
        oFirmB = calculatePricesFromGivenQuantitiesInDuopoly(pFirmBAdjusted, pFirmAAdjusted);

        log4j.debug("Prices - aFirmA: {}, aFirmB: {}", oFirmA, oFirmB);

        aMarket = (aFirmA + aFirmB)/2;
        oMarket = (oFirmA + oFirmB)/2;

        profitFirmA = pFirmAAdjusted * oFirmA * coeff_profit_duo_cournot;
        profitFirmB = pFirmBAdjusted * oFirmB * coeff_profit_duo_cournot;

        ContinuousCompetitionParamObject marketUpdate = new ContinuousCompetitionParamObject();

        marketUpdate.setaFirmA(aFirmA);
        marketUpdate.setaFirmB(aFirmB);
        marketUpdate.setoFirmA(oFirmA);
        marketUpdate.setoFirmB(oFirmB);
        marketUpdate.setProfitFirmA(profitFirmA);
        marketUpdate.setProfitFirmB(profitFirmB);
        marketUpdate.setaMarket(aMarket);
        marketUpdate.setoMarket(oMarket);

        marketUpdate.setaFirmC(0.0);
        marketUpdate.setoFirmC(0.0);
        marketUpdate.setProfitFirmC(0.0);

        return marketUpdate;
    }

    private double calculatePricesFromGivenQuantitiesInDuopoly(double q, double qOtherFirm) {
        return (a - b * (q + theta * qOtherFirm));
    }

    private double calculatePricesFromGivenQuantitiesInTriopoly(double q, double qOtherFirm1, double qOtherFirm2) {
        return (a - b * (q + theta * (qOtherFirm1 + qOtherFirm2)));
    }

    private double calculatePricesFromGivenQuantities(double q, double Q) {
        return (a - b * (q + theta * Q));
    }

    private double calculateDemandFromGivenPrices(double p, double n, double pAverageOtherFirms) {
        return (calcAlpha(n) - calcBeta(n) * p + calcGamma(n) * (pAverageOtherFirms));
    }


    private double calculateDemandFromGivenPricesInTriopoly(double p, double pOtherFirm1, double pOtherFirm2) {
        return (calcAlpha(3) - calcBeta(3) * p + calcGamma(3) * ((pOtherFirm1+pOtherFirm2)/2));
    }

    private double calculateDemandFromGivenPricesInDuopoly(double p, double pOtherFirm) {
        return (calcAlpha(2) - calcBeta(2) * p + calcGamma(2) * pOtherFirm);
    }

    private double calcAlpha(double n) {
        return (a / (b * (1 + (n-1) * theta)));
    }

    private double calcBeta(double n) {
        return ((1 + (n-2) * theta) / (b * (1-theta) * (1 + (n-1) * theta)));
    }

    private double calcGamma(double n) {
        return (((n-1) * theta) / (b * (1-theta) * (1 + (n-1) * theta)));
    }

    /* Helper class to store (price, quantity) pairs */
    class FirmRecord implements Comparable<FirmRecord> {
        double p;
        double q;

        public FirmRecord (double p) {
            this.p = p;
        }

        public FirmRecord (double q, double p) {
            this.q = q;
            this.p = p;
        }

        @Override
        public int compareTo(FirmRecord o) {

            if (this.q < o.q) {
                return -1;
            }

            if (this.q > o.q) {
                return 1;
            }

            return 0;
        }
    }

}
