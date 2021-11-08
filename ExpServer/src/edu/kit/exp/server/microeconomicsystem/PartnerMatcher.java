package edu.kit.exp.server.microeconomicsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import edu.kit.exp.server.run.RandomNumberGenerator;

/**
 * This class implements a partner matching with two different roles
 *
 */
public class PartnerMatcher extends SubjectGroupMatcher {

	public PartnerMatcher(List<String> roles) {
		super(roles);
	}

	private RandomNumberGenerator randomNumberGenerator = RandomNumberGenerator.getInstance();
	private List<SubjectGroup> subjectGroupList = new ArrayList<SubjectGroup>();
	private HashMap<Integer, Subject> partnerCache = new HashMap<Integer, Subject>();

	/**
	 * Matches role1 to role2 randomly for the first period
	 * 
	 * @throws Exception
	 */
	@Override
	public List<SubjectGroup> setupSubjectGroups(Period period, List<Subject> subjects) throws Exception {

		ArrayList<Subject> subjectsRole1 = new ArrayList<Subject>();
		ArrayList<Subject> subjectsRole2 = new ArrayList<Subject>();
		
	
		// Divide Subjects by Role
		for (Subject subject : subjects) {

			if (subject.getRole()!=null && subject.getRole().equals(roles.get(0))) {
				subjectsRole1.add(subject);
			} else {
				subjectsRole2.add(subject);
			}
		}

		// Check illegal state
		if (subjectsRole1.size() != subjectsRole2.size()) {
			throw new IllegalStateException("Number of Subject in role1 has to be equal the number of subjects in role2");
		}

		int numberOfSubjectsRole1 = subjectsRole1.size();
		ArrayList<Integer> randomNumbers = null;

		// Only 2 Players -> no random matching possible
		if (numberOfSubjectsRole1 > 1) {
			randomNumbers = randomNumberGenerator.generateNonRepeatingIntegers(0, numberOfSubjectsRole1 - 1);
		}

		Subject partner;

		for (int i = 0; i < numberOfSubjectsRole1; i++) {

			SubjectGroup subjectGroup = new SubjectGroup();

			Subject subject = subjectsRole1.get(i);
			Membership m1 = new Membership();
			m1.setSubjectGroup(subjectGroup);
			m1.setSubject(subject);
			m1.setRole(subject.getRole());

			if (numberOfSubjectsRole1 > 1) {
				partner = subjectsRole2.get(randomNumbers.get(i));
			}else{
				//there is only one
				partner=subjectsRole2.get(0);
			}
			Membership m2 = new Membership();
			m2.setRole(partner.getRole());
			m2.setSubject(partner);
			m2.setSubjectGroup(subjectGroup);

			addToPartnerCache(subject, partner);

			subjectGroup.getMemberships().add(m1);
			subjectGroup.getMemberships().add(m2);
			subjectGroup.setPeriod(period);

			subjectGroupList.add(subjectGroup);
		}

		return subjectGroupList;

	}

	private void addToPartnerCache(Subject subject, Subject partner) {

		partnerCache.put(subject.getIdSubject(), partner);
		partnerCache.put(partner.getIdSubject(), subject);

	}

	@Override
	public List<SubjectGroup> rematch(Period period, List<Subject> subjects) {

		List<SubjectGroup> result = new ArrayList<SubjectGroup>();
		SubjectGroup subjectGroup;
		Subject partner;
		HashMap<Subject, Membership> newMemberships = new HashMap<Subject, Membership>();

		for (Subject subject : subjects) {
			Membership m = new Membership();
			m.setRole(subject.getRole());
			m.setSubject(subject);
			newMemberships.put(subject, m);
		}

		for (Subject subject : subjects) {

			if (newMemberships.containsKey(subject)) {

				subjectGroup = new SubjectGroup();
				subjectGroup.setPeriod(period);

				partner = partnerCache.get(subject.getIdSubject());

				Membership membership = newMemberships.get(subject);
				membership.setSubjectGroup(subjectGroup);
				subjectGroup.getMemberships().add(membership);
				newMemberships.remove(subject);

				Membership partnerMembership = newMemberships.get(partner);
				partnerMembership.setSubjectGroup(subjectGroup);
				subjectGroup.getMemberships().add(partnerMembership);
				newMemberships.remove(partner);

				result.add(subjectGroup);

			} else {
				continue;
			}

		}

		return result;
	}
}
