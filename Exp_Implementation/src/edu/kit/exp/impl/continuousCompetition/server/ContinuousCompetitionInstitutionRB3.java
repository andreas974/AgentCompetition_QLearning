package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;

import java.util.List;

/**
 * Created by dschnurr on 17.10.14.
 */
public class ContinuousCompetitionInstitutionRB3 extends ContinuousCompetitionInstitution {

    public ContinuousCompetitionInstitutionRB3(ContinuousCompetitionEnvironmentTriopoly environment, List<Membership> memberships, ServerMessageSender messageSender, String gameId) {
        super(environment, memberships, messageSender, gameId);
    }

    @Override
    protected void setupTreatmentConditions() {
        isTriopolyTreatment = true;
        isCournotTreatment = false;
        isDiscreteTreatment = false;
        duration = 1800000;
        updateTimeStep = 500;
    }

}
