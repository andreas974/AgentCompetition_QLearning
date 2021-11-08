package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;

/**
 * Created by dschnurr on 17.10.14.
 */
public class ContinuousCompetitionEnvironmentDuopoly extends ContinuousCompetitionEnvironment {


    public ContinuousCompetitionEnvironmentDuopoly() {

        this.roles.add(ROLE_FIRMA);
        this.roles.add(ROLE_FIRMB);
        this.roleMatcher = new ContinuousCompetitionRoleMatcher(roles);
        this.subjectGroupMatcher = new ContinuousCompetitionSubjectMatcherDuopoly(roles);
        this.setResetMatchersAfterTreatmentBlocks(false);
    }

    @Override
    public RoleMatcher getRoleMatcher() {
        return this.roleMatcher;
    }

    @Override
    public SubjectGroupMatcher getSubjectGroupMatcher() {
        return this.subjectGroupMatcher;
    }

    public static String getROLE_FIRMA() {
        return ROLE_FIRMA;
    }

    public static String getROLE_FIRMB() {
        return ROLE_FIRMB;
    }

}

