package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.impl.continuousCompetition.client.ContinuousCompetitionParamObject;
import edu.kit.exp.impl.continuousCompetition.client.ContinuousCompetitionResponseObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by dschnurr on 04.07.14.
 */
public class ContinuousCompetitionProfitCalculator {

    private static final Logger log4j = LogManager.getLogger(ContinuousCompetitionProfitCalculator.class.getName());
    private static double diffParam;
    private static boolean isNonForclosureTreatment = true;


    public ContinuousCompetitionProfitCalculator(double diffParam, boolean isNonForclosureTreatment) {
        this.diffParam = diffParam;
        this.isNonForclosureTreatment = isNonForclosureTreatment;
        log4j.info("Started ContinuousCompetitionFirmScreen with diffParam = {}, nonForeclosureTreatment = {}", this.diffParam, this.isNonForclosureTreatment);
    }

    public ContinuousCompetitionParamObject calculateMarketData (ContinuousCompetitionResponseObject priceUpdate) {
        return calculateMarketData(priceUpdate.getaFirmA(), priceUpdate.getaFirmB(), priceUpdate.getaFirmC());
    }


    public ContinuousCompetitionParamObject calculateMarketData(double pFirmA, double pFirmB, double pFirmC) {


        log4j.trace("calculateMarketData() - start of execution");
        /* Calculate average market prices */
        double profitFirmA, profitFirmB, profitFirmC;
        double wholesaleProfitFirmA, wholesaleProfitFirmB;
        double retailProfitFirmA, retailProfitFirmB;
        double qFirmA, qFirmB, qFirmC;

        double pMarket = (pFirmA + pFirmB + pFirmC)/3;

        FirmRecord firmA = new FirmRecord(pFirmA);
        FirmRecord firmB = new FirmRecord(pFirmB);
        FirmRecord firmC = new FirmRecord(pFirmC);

        firmA.q = ((1 - firmA.p - diffParam * (firmA.p - pMarket)) / 3);
        firmB.q = ((1 - firmB.p - diffParam * (firmB.p - pMarket)) / 3);
        firmC.q = ((1 - firmC.p - diffParam * (firmC.p - pMarket)) / 3);

        /*
        if (aMarket < aForeclosure) {
            firmC.q = ((1 - firmC.p - diffParam * (firmC.p - pMarket)) / 3);


        } else {
            firmC.q = 0;
        }*/

        log4j.debug("Initial quantities: firmA: "+firmA.q+", firmB: "+firmB.q+", firmC: "+firmC.q);

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

        if(maxFirm[0].q <= 0) {
            // Retail demand < 0 for all firms, thus qFirm = 0 for all firms
            minFirm[0].q = 0;
            midFirm[0].q = 0;
            maxFirm[0].q = 0;
        } else {
            if (midFirm[0].q <= 0) {
                // Retail demand of one firm is positive, while two firms exhibit negative demand.
                // Calculate adjusted demand with n=3, m=1 for firm that exhibits a positive demand in the case of m=n=3

                minFirm[0].q = 0.0;
                midFirm[0].q = 0.0;

                maxFirm[0].q = ((1+diffParam)/3)*(1-maxFirm[0].p-(diffParam*(1-maxFirm[0].p))/(diffParam+3));
                log4j.debug("Retail Market: Two firm exit the market (Triopoly calculation)");
            } else {
                if (minFirm[0].q <= 0) {
                    // Retail demand of two firms is positive, while one firm exhibits negative demand.
                    // Calculate adjusted demand with n=3, m=2 for firms that exhibit a positive demand in the case of m=n=3

                    minFirm[0].q = 0.0;

                    midFirm[0].q = ((1+diffParam)/3)*(1-midFirm[0].p-(diffParam*(2-midFirm[0].p-maxFirm[0].p)/(3+2*diffParam)));

                    if (midFirm [0].q < 0) {
                        midFirm[0].q = 0.0;
                        maxFirm[0].q = ((1+diffParam)/3)*(1-maxFirm[0].p-(diffParam*(1-maxFirm[0].p))/(diffParam+3));

                        log4j.debug("Retail Market: Two firm exit the market (Duopoly calculation)");
                    } else {
                        maxFirm[0].q = ((1+diffParam)/3)*(1-maxFirm[0].p-(diffParam*(2-midFirm[0].p-maxFirm[0].p)/(3+2*diffParam)));
                        log4j.debug("Retail Market: One firm exits the market - (Positive quantities in duopoly)");
                        log4j.debug("Quantity check:"+minFirm[0].q+ " with price "+minFirm[0].p);
                    }

                }
                // else: qFirm > 0 for all firms, thus initial demand is valid
            }

        }

        qFirmC = firmC.q;
        qFirmB = firmB.q;
        qFirmA = firmA.q;
        log4j.debug("Quantities are" + qFirmA + " " + qFirmB + " " +qFirmC);

        /* Profit of firmC is independent of the identity of the respective access provider */
        profitFirmC = pFirmC * qFirmC;
        profitFirmA = pFirmA * qFirmA;
        profitFirmB = pFirmB * qFirmB;


        log4j.info("ProfitCalculator: Prices are"+pFirmA+" "+pFirmB+" "+pFirmC);
        log4j.info("ProfitCalculator: Quantities are" + qFirmA + " " + qFirmB + " " +qFirmC);
        log4j.info("ProfitCalculator: Profits are " + profitFirmA + " " + profitFirmB + " " + profitFirmC);

        ContinuousCompetitionParamObject marketUpdate = new ContinuousCompetitionParamObject();
        marketUpdate.setaFirmA(pFirmA);
        marketUpdate.setaFirmB(pFirmB);
        marketUpdate.setaFirmC(pFirmC);
        marketUpdate.setaMarket(pMarket);
        marketUpdate.setProfitFirmA(profitFirmA);
        marketUpdate.setProfitFirmB(profitFirmB);
        marketUpdate.setProfitFirmC(profitFirmC);
        marketUpdate.setoFirmA(qFirmA);
        marketUpdate.setoFirmB(qFirmB);
        marketUpdate.setoFirmC(qFirmC);

        return marketUpdate;
    }




    /* Helper class to store (price, quantity) pairs */
    class FirmRecord {
        double p;
        double q;

        public FirmRecord (double p) {
            this.p = p;
        }

        public FirmRecord (double q, double p) {
            this.q = q;
            this.p = p;
        }
    }


}
