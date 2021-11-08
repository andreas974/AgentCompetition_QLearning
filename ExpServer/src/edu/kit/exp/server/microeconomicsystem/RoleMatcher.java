package edu.kit.exp.server.microeconomicsystem;

import java.util.List;

import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.run.RandomGeneratorException;

/**
 * The RoleMatcher matches roles to subjects and has to be defined in every
 * Environment. If a Session is run, <code>setupSubjectRoles</code> is called
 * for Period 1 in the first TreatmentBlock and for all following periods and
 * TreatmentBlocks <code>rematch</code> is called. If
 * environment.getResetMatchersAfterTreatmentBlocks == true
 * <code>setupSubjectRoles</code> will be called in Period 1 of each
 * TreatmentBlock.
 * 
 */
public abstract class RoleMatcher {

	protected List<String> roles;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public RoleMatcher(List<String> roles) {

		this.roles = roles;
	}

	/**
	 * Creates the initial mapping of subjects and roles. The Attribute
	 * <code>subject.setRole(String)</code> has to be set here.
	 * 
	 * @param subjects Subjects of a cohort.
	 * @return
	 * @throws RandomGeneratorException
	 */
	public abstract List<Subject> setupSubjectRoles(List<Subject> subjects) throws RandomGeneratorException;

	/**
	 * This method reallocates the roles for a given period.
	 * 
	 * @param period
	 * @param subjects
	 * @return
	 * @throws RandomGeneratorException
	 */
	public abstract List<Subject> rematch(Period period, List<Subject> subjects) throws RandomGeneratorException;

}
