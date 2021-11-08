package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.run.RandomGeneratorException;
import edu.kit.exp.server.run.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dschnurr on 05.03.14.
 */
public class ContinuousCompetitionRoleMatcher extends RoleMatcher {

    private RandomNumberGenerator randomNumberGenerator = RandomNumberGenerator.getInstance();


    public ContinuousCompetitionRoleMatcher(List<String> roles) {
        super(roles);
    }

    @Override
    public List<Subject> setupSubjectRoles(List<Subject> subjects) throws RandomGeneratorException {

        // Agent-Human Matching is currently implemented in edu.kit.exp.server.run.SubjectRegistration,
        // Since subject-client matching is conducted only after while role-subject matching is conducted
        // prior to
        //
        // Divide roles into roles that are assumed by humans and into roles that are assumed by agents
        /*List<String> humanRoles = roles.subList(0,2);
        List<String> agentRoles = roles.subList(2,3);

        if ( !(humanRoles.contains("Firma A") && humanRoles.contains("Firma B")) || humanRoles.size() != 2 ) {
            throw new IllegalStateException("List of humanRoles does not contain both firms A and B or includes other entities: "+humanRoles);
        }

        if ( !agentRoles.contains("Firma E") || agentRoles.size() !=1 ) {
            throw new IllegalStateException("List of agentRoles does not firm C or includes other entities: "+agentRoles);
        }*/

        int numberOfSubjects = subjects.size();
        int max = numberOfSubjects - 1;
        int min = 0;
        int numberOfRoles = roles.size();

        int copiesPerRole = numberOfSubjects / numberOfRoles;

        ArrayList<String> bowle = new ArrayList<String>();

        // Add roles to bowle
        for (String role : roles) {

            for (int i = 0; i < copiesPerRole; i++) {
                bowle.add(role);
            }
        }

        ArrayList<Integer> randomNumbers = randomNumberGenerator.generateNonRepeatingIntegers(min, max);

        Subject subject = null;
        int roleCopyNumber;

        for (int index = 0; index < subjects.size(); index++) {

            subject = subjects.get(index);
            roleCopyNumber = randomNumbers.get(index);
            subject.setRole(bowle.get(roleCopyNumber));
        }

        return subjects;
    }

    @Override
    public List<Subject> rematch(Period period, List<Subject> subjects) throws RandomGeneratorException {
        return subjects;
    }
}
