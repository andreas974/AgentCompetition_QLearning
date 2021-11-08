package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

/**
 * Created by dschnurr on 05.03.14.
 */
public class ContinuousCompetitionEnvironment extends Environment {


    protected static String ROLE_FIRMA = "Firma A";
    protected static String ROLE_FIRMB = "Firma B";
    protected static String ROLE_FIRMC = "Firma C";
    protected static String ROLE_FIRMD = "Firma D";

    public ContinuousCompetitionEnvironment() {

    }

    @Override
    public RoleMatcher getRoleMatcher() {
        return this.roleMatcher;
    }

    @Override
    public SubjectGroupMatcher getSubjectGroupMatcher() {
        return this.subjectGroupMatcher;
    }

    protected static String getROLE_FIRMA() {
        return ROLE_FIRMA;
    }

    protected static String getROLE_FIRMB() {
        return ROLE_FIRMB;
    }

    protected static String getROLE_FIRME() {
        return ROLE_FIRMC;
    }

    protected static String getROLE_FIRMD() {
        return ROLE_FIRMD;
    }
}