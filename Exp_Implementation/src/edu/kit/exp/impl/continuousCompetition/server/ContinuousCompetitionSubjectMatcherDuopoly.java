package edu.kit.exp.impl.continuousCompetition.server;

import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dschnurr on 17.10.14.
 */
public class ContinuousCompetitionSubjectMatcherDuopoly extends SubjectGroupMatcher {

    private static final Logger log4j = LogManager.getLogger(ContinuousCompetitionSubjectMatcherDuopoly.class.getName());

    public ContinuousCompetitionSubjectMatcherDuopoly(List<String> roles) {
        super(roles);
    }

    @Override
    public List<SubjectGroup> setupSubjectGroups(Period period, List<Subject> subjects) throws Exception {
        List<SubjectGroup> subjectGroupList = new ArrayList<SubjectGroup>();

        ArrayList<Subject> subjectsRole1 = new ArrayList<Subject>();
        ArrayList<Subject> subjectsRole2 = new ArrayList<Subject>();

        // Divide Subjects by Role
        for (Subject subject : subjects) {

            if (subject.getRole()!=null && subject.getRole().equals(roles.get(0))) {
                subjectsRole1.add(subject);
            }

            if (subject.getRole()!=null && subject.getRole().equals(roles.get(1))) {
                subjectsRole2.add(subject);
            }
        }

        // Check illegal state
        if (subjectsRole1.size() != subjectsRole2.size()) {
            throw new IllegalStateException("Number of Subject in role1 has to be equal the number of subjects in role2");
        }


        for (int i = 0; i < subjectsRole1.size(); i++) {
            log4j.warn("loop index of subjectGroups {}",i);
            SubjectGroup subjectGroup = new SubjectGroup();

            Subject subjectWithRole1 = subjectsRole1.get(i);
            Membership m1 = new Membership();
            m1.setSubjectGroup(subjectGroup);
            m1.setSubject(subjectWithRole1);
            m1.setRole(subjectWithRole1.getRole());

            Subject subjectWithRole2 = subjectsRole2.get(i);
            Membership m2 = new Membership();
            m2.setSubjectGroup(subjectGroup);
            m2.setSubject(subjectWithRole2);
            m2.setRole(subjectWithRole2.getRole());

            subjectGroup.getMemberships().add(m1);
            subjectGroup.getMemberships().add(m2);
            subjectGroup.setPeriod(period);

            subjectGroupList.add(subjectGroup);
        }

        return subjectGroupList;
    }

    @Override
    public List<SubjectGroup> rematch(Period period, List<Subject> subjects) throws Exception {
        List<SubjectGroup> rematchedSubjectGroupList = setupSubjectGroups(period, subjects);
        return rematchedSubjectGroupList;
    }

}
