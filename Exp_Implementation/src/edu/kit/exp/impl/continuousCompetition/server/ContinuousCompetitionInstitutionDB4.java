package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;

import java.util.List;

/**
 * Created by dschnurr on 24.10.14.
 */
public class ContinuousCompetitionInstitutionDB4 extends ContinuousCompetitionInstitution {

    public ContinuousCompetitionInstitutionDB4(ContinuousCompetitionEnvironmentQuadropoly environment, List<Membership> memberships, ServerMessageSender messageSender, String gameId) {
        super(environment, memberships, messageSender, gameId);
    }

    @Override
    protected void setupTreatmentConditions() {
        isTriopolyTreatment = false;
        isCournotTreatment = false;
        isDiscreteTreatment = true;
        isQuadropolyTreatment = true;
        duration = 900000;
        updateTimeStep = 15000;
    }
}
