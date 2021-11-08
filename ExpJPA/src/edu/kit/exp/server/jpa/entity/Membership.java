package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the membership database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Membership.findAll", query = "SELECT k FROM Membership k"), @NamedQuery(name = "Membership.findByIdMembership", query = "SELECT k FROM Membership k WHERE k.idMembership = :idMembership") })
@Table(name = "membership", schema = "exp")
public class Membership implements Serializable {
	private static final long serialVersionUID = 8659177635204071327L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_membership", unique = true, nullable = false, table = "membership")
	private Long idMembership;

	@Column(length = 255)
	private String role;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id_subject", nullable = false)
	private Subject subject;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_group_id_subject_group", nullable = false)
	private SubjectGroup subjectGroup;

	public Membership() {
	}

	public Long getIdMembership() {
		return this.idMembership;
	}

	public void setIdMembership(Long idMembership) {
		this.idMembership = idMembership;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public SubjectGroup getSubjectGroup() {
		return this.subjectGroup;
	}

	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMembership == null) ? 0 : idMembership.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((subjectGroup == null) ? 0 : subjectGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Membership other = (Membership) obj;
		if (idMembership == null) {
			if (other.idMembership != null)
				return false;
		} else if (!idMembership.equals(other.idMembership))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (subjectGroup == null) {
			if (other.subjectGroup != null)
				return false;
		} else if (!subjectGroup.equals(other.subjectGroup))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Membership [idMembership=" + idMembership + ", role=" + role + ", subject=" + subject + ", subjectGroup=" + subjectGroup + "]";
	}

}