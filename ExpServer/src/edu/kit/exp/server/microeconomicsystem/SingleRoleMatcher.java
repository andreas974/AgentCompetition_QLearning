/**
 * 
 */
package edu.kit.exp.server.microeconomicsystem;

import java.util.ArrayList;
import java.util.List;

import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * @author ahariharan
 *
 */
public class SingleRoleMatcher extends SubjectGroupMatcher {
	private List<SubjectGroup> subjectGroupList = new ArrayList<SubjectGroup>();

	public SingleRoleMatcher(List<String> roles) {
		super(roles);
	}

	/* (non-Javadoc)
	 * @see edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher#rematch(edu.kit.exp.server.jpa.entity.Period, java.util.List)
	 */
	@Override
	public List<SubjectGroup> rematch(Period period, List<Subject> subjects) {
		ArrayList<Subject> subjectsRole = new ArrayList<Subject>();
		// Divide Subjects by Role
		for (Subject subject : subjects) {
				subjectsRole.add(subject);
			} 

		int numberOfSubjectsRole1 = subjectsRole.size();
	
		for (int i = 0; i < numberOfSubjectsRole1; i++) {

			SubjectGroup subjectGroup = new SubjectGroup();

			Subject subject = subjectsRole.get(i);
			Membership m1 = new Membership();
			m1.setSubjectGroup(subjectGroup);
			m1.setSubject(subject);
			if (subject.getRole()!=null) {
					m1.setRole(subject.getRole());
			} else {
				m1.setRole(roles.get(0));
			}

			subjectGroup.getMemberships().add(m1);
			subjectGroup.setPeriod(period);
			subjectGroupList.remove(0);
			subjectGroupList.add(subjectGroup);
		}
		return subjectGroupList;
	}

	/* (non-Javadoc)
	 * @see edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher#setupSubjectGroups(edu.kit.exp.server.jpa.entity.Period, java.util.List)
	 */
	@Override
	public List<SubjectGroup> setupSubjectGroups(Period period,
			List<Subject> subjects) throws Exception {
		
		ArrayList<Subject> subjectsRole = new ArrayList<Subject>();
		// Divide Subjects by Role
		for (Subject subject : subjects) {
				subjectsRole.add(subject);
			} 

		int numberOfSubjectsRole1 = subjectsRole.size();
		


		for (int i = 0; i < numberOfSubjectsRole1; i++) {

			SubjectGroup subjectGroup = new SubjectGroup();

			Subject subject = subjectsRole.get(i);
			Membership m1 = new Membership();
			m1.setSubjectGroup(subjectGroup);
			m1.setSubject(subject);
			if (subject.getRole()!=null) {					m1.setRole(subject.getRole());
			} else {
				m1.setRole(roles.get(0));
			}

			subjectGroup.getMemberships().add(m1);
			subjectGroup.setPeriod(period);
			subjectGroupList.add(subjectGroup);
		}

		return subjectGroupList;

	}

	
}
