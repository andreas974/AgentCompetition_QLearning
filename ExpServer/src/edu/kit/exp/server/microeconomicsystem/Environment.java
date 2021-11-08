package edu.kit.exp.server.microeconomicsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class represents a microeconomic environment.
 * 
 */
public abstract class Environment {

	protected SubjectGroupMatcher subjectGroupMatcher;
	protected RoleMatcher roleMatcher;

	protected List<String> roles = new ArrayList<String>();
	protected Boolean resetMatchersAfterTreatmentBlocks = false;

	/**
	 * Has to return a customized RoleMatcher, which matches subject to roles
	 * for each period.
	 * 
	 * @return
	 */
	public abstract RoleMatcher getRoleMatcher();

	/**
	 * Has to return a SubjectGroupMatcher (i.e. PartnerMatcher,
	 * StrangerMatcher) which is used to allocate the different roles in a
	 * SubjectGroup for each period.
	 */
	public abstract SubjectGroupMatcher getSubjectGroupMatcher();

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Boolean getResetMatchersAfterTreatmentBlocks() {
		return resetMatchersAfterTreatmentBlocks;
	}

	public void setResetMatchersAfterTreatmentBlocks(Boolean resetMatchersAfterTreatmentBlocks) {
		this.resetMatchersAfterTreatmentBlocks = resetMatchersAfterTreatmentBlocks;
	}

	public void setSubjectGroupMatcher(SubjectGroupMatcher subjectGroupMatcher) {
		this.subjectGroupMatcher = subjectGroupMatcher;
	}

	public void setRoleMatcher(RoleMatcher roleMatcher) {
		this.roleMatcher = roleMatcher;
	}

}
