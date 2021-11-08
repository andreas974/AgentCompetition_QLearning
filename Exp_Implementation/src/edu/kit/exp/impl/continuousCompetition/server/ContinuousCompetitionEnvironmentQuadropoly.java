package edu.kit.exp.impl.continuousCompetition.server;

/**
 * Created by dschnurr on 17.10.14.
 */
public class ContinuousCompetitionEnvironmentQuadropoly extends ContinuousCompetitionEnvironment {

    public ContinuousCompetitionEnvironmentQuadropoly() {
        this.roles.add(ROLE_FIRMA);
        this.roles.add(ROLE_FIRMB);
        this.roles.add(ROLE_FIRMC);
        this.roles.add(ROLE_FIRMD);
        this.roleMatcher = new ContinuousCompetitionRoleMatcher(roles);
        this.subjectGroupMatcher = new ContinuousCompetitionSubjectMatcherQuadropoly(roles);
        this.setResetMatchersAfterTreatmentBlocks(false);
    }
}
