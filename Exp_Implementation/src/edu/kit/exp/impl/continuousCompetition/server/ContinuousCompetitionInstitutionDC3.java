package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;

import java.util.List;

/**
 * Created by dschnurr on 24.10.14.
 */
public class ContinuousCompetitionInstitutionDC3 extends ContinuousCompetitionInstitution {
    public ContinuousCompetitionInstitutionDC3(ContinuousCompetitionEnvironment environment, List<Membership> memberships, ServerMessageSender messageSender, String gameId) {
        super(environment, memberships, messageSender, gameId);
    }

    @Override
    protected void setupTreatmentConditions() {
        isTriopolyTreatment = true;
        isCournotTreatment = true;
        isDiscreteTreatment = true;
        duration = 900000;
        updateTimeStep = 15000;
    }
}
