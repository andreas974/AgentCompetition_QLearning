package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.microeconomicsystem.Environment;

import java.util.List;

/**
 * Created by dschnurr on 17.10.14.
 */
public class ContinuousCompetitionInstitutionRB2 extends ContinuousCompetitionInstitution {

    public ContinuousCompetitionInstitutionRB2(ContinuousCompetitionEnvironmentDuopoly environment, List<Membership> memberships, ServerMessageSender messageSender, String gameId) {
        super(environment, memberships, messageSender, gameId);
    }

    @Override
    protected void setupTreatmentConditions() {
        isTriopolyTreatment = false;
        isCournotTreatment = false;
        isDiscreteTreatment = false;
        duration = 1800000;
        updateTimeStep = 500;
    }

}
