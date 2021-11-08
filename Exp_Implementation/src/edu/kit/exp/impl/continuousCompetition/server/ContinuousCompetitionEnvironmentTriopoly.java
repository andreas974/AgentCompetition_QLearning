package edu.kit.exp.impl.continuousCompetition.server;

/**
 * Created by dschnurr on 17.10.14.
 */
public class ContinuousCompetitionEnvironmentTriopoly extends ContinuousCompetitionEnvironment {

    public ContinuousCompetitionEnvironmentTriopoly() {
        this.roles.add(ROLE_FIRMA);
        this.roles.add(ROLE_FIRMB);
        this.roles.add(ROLE_FIRMC);
        this.roleMatcher = new ContinuousCompetitionRoleMatcher(roles);
        this.subjectGroupMatcher = new ContinuousCompetitionSubjectMatcher(roles);
        this.setResetMatchersAfterTreatmentBlocks(false);
    }
}
