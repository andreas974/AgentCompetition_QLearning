package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the subject_group database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "SubjectGroup.findAll", query = "SELECT s FROM SubjectGroup s"), @NamedQuery(name = "SubjectGroup.findByIdSubjectGroup", query = "SELECT s FROM SubjectGroup s WHERE s.idSubjectGroup = :idSubjectGroup") })
@Table(name = "subject_group", schema = "exp")
public class SubjectGroup implements Serializable {
	private static final long serialVersionUID = 2416028461429869946L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_subject_group", unique = true, nullable = false)
	private Long idSubjectGroup;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH })
	@JoinColumn(name = "period_id_period", nullable = false)
	private Period period;

	@OneToMany(mappedBy = "subjectGroup", cascade = ALL)
	private List<Membership> memberships;

	@OneToMany(mappedBy = "subjectGroup", cascade = ALL)
	private List<Trial> trials;

	public SubjectGroup() {
		memberships = new ArrayList<Membership>();
	}

	public Long getIdSubjectGroup() {
		return this.idSubjectGroup;
	}

	public void setIdSubjectGroup(Long idSubjectGroup) {
		this.idSubjectGroup = idSubjectGroup;
	}

	public Period getPeriod() {
		return this.period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public List<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	public List<Trial> getTrials() {
		return trials;
	}

	public void setTrials(List<Trial> trials) {
		this.trials = trials;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSubjectGroup == null) ? 0 : idSubjectGroup.hashCode());
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
		SubjectGroup other = (SubjectGroup) obj;
		if (idSubjectGroup == null) {
			if (other.idSubjectGroup != null)
				return false;
		} else if (!idSubjectGroup.equals(other.idSubjectGroup))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubjectGroup [idSubjectGroup=" + idSubjectGroup + ", period=" + period.getIdPeriod() + "]";
	}

}