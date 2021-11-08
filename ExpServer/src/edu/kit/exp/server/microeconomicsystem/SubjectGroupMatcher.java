package edu.kit.exp.server.microeconomicsystem;

import java.util.List;

import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * The SubjectGroupMatcher matches subject with role to SubjectGroups and has to
 * be defined in every Environment. If a Session is run,
 * <code>setupSubjectGroups</code> is called for Period 1 in the first
 * TreatmentBlock and for all following periods and TreatmentBlocks
 * <code>rematch</code> is called. If
 * environment.getResetMatchersAfterTreatmentBlocks == true
 * <code>setupSubjectGroups</code> will be called in Period 1 of each
 * TreatmentBlock.
 * 
 */
public abstract class SubjectGroupMatcher {

	protected List<String> roles;

	/**
	 * Create a SubjectGroupMatcher that matches Subjects independent of roles.
	 * 
	 * @param roles
	 */
	public SubjectGroupMatcher(List<String> roles) {
		this.roles = roles;
	}

	/**
	 * Creates the initial mapping of subjects with roles to SubjectGroups. The
	 * Attribute <code>subject.setRole(String)</code> has to be set via a
	 * RoleMatcher first.
	 * 
	 * @param subjects
	 *            Subjects of a Cohort
	 * @return List of SubjectGroups for the first Period
	 * @throws Exception
	 */
	public abstract List<SubjectGroup> setupSubjectGroups(Period period, List<Subject> subjects) throws Exception;

	/**
	 * This method reallocates (or not) the Subjects in each period.
	 * 
	 * @param period
	 * @param subjects
	 * @return
	 * @throws Exception 
	 */
	public abstract List<SubjectGroup> rematch(Period period, List<Subject> subjects) throws Exception;

}
