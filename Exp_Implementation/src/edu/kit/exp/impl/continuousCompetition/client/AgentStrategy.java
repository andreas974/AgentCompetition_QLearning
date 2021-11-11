package edu.kit.exp.impl.continuousCompetition.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.*;

/**
 * Created by dschnurr on 18.03.15.
 */

public class AgentStrategy {

    private static final Logger log4j = LogManager.getLogger(AgentStrategy.class.getName());
    private final AgentCore agentCore;

    private int myRole;
    private double[][] q = new double[101][101];
    private int state;
    private Parameter parameter;
    private final Random rnd = new Random(42);
    private int numberOfEpisodes;       // Number of episodes that have been conducted.

    public AgentStrategy(AgentCore agentCore) {
        log4j.info("AgentStrategy(): {}", agentCore);
        this.agentCore = agentCore;
        log4j.info("this.AgentInterface = {}", this.agentCore);
        parameter = new AgentStrategy.Parameter(0.055, 0.95, 1);
    }


    void init(ContinuousCompetitionParamObject initialMarketUpdate) {
        log4j.info("Initialize information based on received initial market update.");

        // Optional: read and process information conveyed by the initial marketUpdate
        // For example: read role code
        myRole = initialMarketUpdate.getRoleCode();

        // Todo: set inital action for the start of the simulation
        // For example: set action 25 as the initial action
        int initAction = 25;
        readcsv();
        initQMatrix(readcsv());
        printMatrix(readcsv());
        //printq();

        // Initial action is sent to server
        agentCore.updateAction(initAction);
        log4j.info("Set initial action to {}", initAction);


    }


    // Market updates are received every 500ms
    void processMarketUpdate(ContinuousCompetitionParamObject marketUpdate) {
        log4j.info("Received marketUpdate (id: {})", marketUpdate.getCountId());

        // Todo: implement information processing
        // countId identifies the marketUpdate and is incremented for every new marketUpdate
        int countId = marketUpdate.getCountId();
        log4j.info("(int) countID: {}", countId);

        // read average market action
        double averageMarketAction = marketUpdate.getaMarket();
        log4j.info("(double) pMarket: {}", averageMarketAction);
        log4j.info("(int) pMarket: {}",(int) averageMarketAction);

        // Store actions of all four firms in array
        int[] actions = new int[4];
        actions[0] = marketUpdate.getaFirmA();
        actions[1] = marketUpdate.getaFirmB();
        actions[2] = marketUpdate.getaFirmC();
        actions[3] = marketUpdate.getaFirmD();
        // The current own action can then be retrieved as follows:
        int myLastAction = actions[myRole];
        log4j.info("Own action in last round: {}", myLastAction);

        int newAction = myLastAction;
        // You may want to set a new action based on the received information, e.g. the average market price
        // newAction = (int) averageMarketAction;

        //Check Role for Agent:
        System.out.println("Rolle" + myRole);


        /* Test for Triopoly:

        if (myRole==0) {
            newAction = selectAction((int) ((marketUpdate.getaFirmB()+marketUpdate.getaFirmC())/2));
            updateMatrix(myLastAction, marketUpdate.getProfitFirmA(), marketUpdate);
            runEpisode(marketUpdate);
        }
        else if (myRole==1){
            newAction = selectAction((int) ((marketUpdate.getaFirmA()+marketUpdate.getaFirmC())/2));
            updateMatrix(myLastAction, marketUpdate.getProfitFirmB(), marketUpdate);
            runEpisode(marketUpdate);
        }
        else if (myRole==2){
            newAction = selectAction((int) ((marketUpdate.getaFirmA()+marketUpdate.getaFirmB())/2));
            updateMatrix(myLastAction, marketUpdate.getProfitFirmC(), marketUpdate);
            runEpisode(marketUpdate);
        }*/


        // Set new action according to the Q-Matrix and update Q-Matrix
        if (myRole==0) {
            newAction = selectAction((int) marketUpdate.getaFirmB());
            updateMatrix(myLastAction, marketUpdate.getProfitFirmA(), marketUpdate);
            runEpisode(marketUpdate);
        }
        else if (myRole==1){
            newAction = selectAction(marketUpdate.getaFirmA());
            updateMatrix(myLastAction, marketUpdate.getProfitFirmB(), marketUpdate);
            runEpisode(marketUpdate);
        }

        /**Just to check: Print Matrix

        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < q.length; j++) {
                System.out.println(i+". Zeile und " + j + ".Spalte, Wert:"+ (q[i][j]) + " ");
            }
        }*/



        // newAction = (int) marketUpdate.getaFirmA();
        // Alternatively you may want to set a random action every 10 seconds (10 * 2 * 500ms updates => countid = 20)
        // if (countId % 20 == 0) {
        //     newAction = setNewRandomAction();
        //}


        // Send updated action to server
        agentCore.updateAction(newAction);

        log4j.info("Updated action to {}", newAction);
    }

    int setNewRandomAction() {

        // Todo: implement action update based on internal processing (e.g. learning algorithm)
        // Simple example: set a random action between 0 and 100:
        Random rand = new Random();
        int randomAction = rand.nextInt(101);

        log4j.info("Created new random action {}", randomAction);
        return randomAction;
    }


    /**
     * Calculates the state of this Q-Learning algorithm object by weighting the minimum and maximum
     * of all other firms' actions (prices/quantities) with the parameter gamma.
     *
     * @param marketUpdate all other firms' actions.
     * @return state of this Q-Learning algorithm object as a weighted number.
     */
    private int getState(ContinuousCompetitionParamObject marketUpdate) {
        int result = 0;
        if(myRole==0){
            result = (int) marketUpdate.getaFirmB();
        }
        else if(myRole==1){
            result = (int) marketUpdate.getaFirmA();
        }
        return result;
    }


    /**
     * Selects an action according to epsilon-greedy policy. Here, the perceived optimal action is chosen
     * if a random value is greater than the current epsilon (exploit), while a random action gets chosen otherwise
     * (explore).
     *
     * @param state current state of the firm deploying this Q-Learning algorithm.
     * @return action determined by the epsilon-greedy policy.
     */
    private int selectAction(int state){
        int index;

        index = getMaxActionIndex(state);


        //No Explore strategy -> Agent should always exploit (Calvano et al., 2020)
        /*if (rnd.nextDouble() > parameter.epsilon) {
            // Exploit
            index = getMaxActionIndex(state);
        } else {
            // Explore
            index = rnd.nextInt(101);
        }*/

        return index;
    }


    /**
     * Get the action's index of the Q matrix with the highest (optimal) value given a certain state.
     *
     * @param state state to which the optimal action is requested.
     * @return the index of the optimal action given a certain state.
     */
    private int getMaxActionIndex(int state) {
        double value;
        double maxValue = 0;
        int maxIndex = 0;

        for (int i = 0; i < 101; i++) {
            value = q[state][i];

            if (value > maxValue) {
                maxValue = value;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * For simultaneous interaction:
     * Updates a cell of the Q-matrix based on the previous action (price/quantity) and reward (profit)
     * of the corresponding firm and by determining the new state based on the latest actions (prices/quantities)
     * of all other firms.
     *
     * @param marketUpdate latest prices/quantities of all other other firms to determine the new state.
     * @param action previous price/quantity chosen by the corresponding firm.
     * @param reward previous profit generated by the corresponding firm after choosing the action (price/quantity).
     */
    void updateMatrix(int action, Double reward, ContinuousCompetitionParamObject marketUpdate) {
        // Get the new state based on all other firms' actions.
        int newState = getState(marketUpdate);

        // Observe maxQ for the new state.
        double nextMaxQ = q[newState][getMaxActionIndex(newState)];

        // Update corresponding Q-matrix cell.
        q[state][action] = (1 - parameter.alpha) * q[state][action] + parameter.alpha * (reward + parameter.delta * nextMaxQ);
    }


    /**
     * Carries out one episode of the Q-Learning algorithm. To do so, one first has to determine the current state
     * (= (weighted) price/quantity of the competing firm(s)) and select an action by using a policy
     * (either by exploration or exploitation). Finally, the counter for the number of episodes is increased,
     * beta (necessary for the action-selection policy) is decreased, and the chosen action is returned.
     *
     * @param marketUpdate prices/quantities of all other other firms to determine the state.
     * @return result (= price/quantity) of this Q-Learning episode.
     */
    int runEpisode(ContinuousCompetitionParamObject marketUpdate) {

        state = (int) marketUpdate.getaMarket();

        // Choose A from S using policy.
        int action = selectAction(state);
        numberOfEpisodes++;
        // Decrease epsilon: not necessary according to Calvano et al. 2020
        parameter.decreaseEpsilon(numberOfEpisodes);

        return action;
    }

    public static class Parameter {
        private double alpha;                   // Learning factor
        private double delta;                   // Discount factor

        private double gamma = 1.0;                   // Weighting factor

        private double epsilon = 1;             // Probability of exploration
        private double beta = 1 - Math.pow(0.000001, epsilon / 51005000); // Factor for decreasing epsilon

        public Parameter(double alpha, double delta, double gamma) {
            this.alpha = alpha;
            this.delta = delta;
            this.gamma = gamma;
        }

        public Parameter(double alpha, double delta) {
            this.alpha = alpha;
            this.delta = delta;
        }

        public double getAlpha() {
            return alpha;
        }

        public double getDelta() {
            return delta;
        }

        public double getGamma() {
            return gamma;
        }

        public double getBeta() {
            return beta;
        }

        public void decreaseEpsilon(int numberOfEpisodes) {
            epsilon = Math.pow(1 - beta, numberOfEpisodes);
        }
    }

    /**
     * Read the csv file from Sebastian's Q-Learning Agent and store all the values in an array called QMatrix
     *
     * @return QMatrix is an Array of all the QMatrix values
     */
    public double[] readcsv(){

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("edu/kit/exp/common/resources/SEQ_P_2_Export.csv");

        BufferedReader reader = null;
        String line = "";
        double[] QMatrix_temp = new double[20402];
        double[] QMatrix = new double[10201];

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream ));
            int i = 0;
            while((line = reader.readLine()) != null) {
                String[] row = line.split(":");
                QMatrix_temp[i] = Double.parseDouble(row[1]);
                i++;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        for (int i = 0,  j = 0; i < 10201 && j < 10201; i++, j++ ){
                QMatrix[j] = QMatrix_temp[i];
        }
        return QMatrix;
    }

    public void printMatrix(double[] matrix){
        for (int i = 0; i < matrix.length; i++){
            System.out.println(matrix[i]);
        }
    }

    public void initQMatrix(double[] qliste) {
        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < q.length; j++) {
                q[i][j] = qliste[j%q.length+i*q.length];
            }
        }
    }

    public void printq(){
        for (int i = 0; i < 101; i++) {
            for (int j = 0; j < 101; j++) {
                System.out.println(q[i][j]);
            }
        }

    }

    void fillq(){
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                q[i][j]=0;
            }
        }
    }

}




