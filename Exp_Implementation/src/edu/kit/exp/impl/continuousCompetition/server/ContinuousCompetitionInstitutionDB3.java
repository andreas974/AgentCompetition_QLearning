package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.client.gui.screens.DefaultWaitingScreen;
import edu.kit.exp.impl.continuousCompetition.client.FirmDescription;
import edu.kit.exp.impl.continuousCompetition.client.ContinuousCompetitionFirmScreen;
import edu.kit.exp.impl.continuousCompetition.client.ContinuousCompetitionParamObject;
import edu.kit.exp.impl.continuousCompetition.client.ContinuousCompetitionResponseObject;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.Trial;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.TrialManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dschnurr on 05.03.14.
 */
public class ContinuousCompetitionInstitutionDB3 extends ContinuousCompetitionInstitution {

    public ContinuousCompetitionInstitutionDB3(ContinuousCompetitionEnvironmentTriopoly environment, List<Membership> memberships, ServerMessageSender messageSender, String gameId) {
        super(environment, memberships, messageSender, gameId);
    }

    @Override
    protected void setupTreatmentConditions() {
        isTriopolyTreatment = true;
        isCournotTreatment = false;
        isDiscreteTreatment = true;
        duration = 900000;
        updateTimeStep = 15000;
    }





}
