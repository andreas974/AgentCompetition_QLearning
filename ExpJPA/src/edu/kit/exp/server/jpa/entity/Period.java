package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the period database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Period.findAll", query = "SELECT p FROM Period p"), @NamedQuery(name = "Period.findByIdPeriod", query = "SELECT p FROM Period p WHERE p.idPeriod = :idPeriod") })
@Table(name = "period", schema = "exp")
public class Period implements Serializable, Comparable<Period> {
	private static final long serialVersionUID = -2114462329970815192L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_period", unique = true, nullable = false)
	private Integer idPeriod;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH })
	@JoinColumn(name = "treatment_block_sequence_element_idsequence_element", nullable = false)
	private TreatmentBlock treatmentBlock;

	@OneToMany(mappedBy = "period", fetch = LAZY, cascade = ALL)
	private List<SubjectGroup> subjectGroups;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "treatment_id_treatment", nullable = true)
	private Treatment treatment;

	@Column
	private Boolean done;

	@Column(name = "sequence_number")
	private Integer sequenceNumber;

	@Column(name = "practice")
	private Boolean practice;

	public Period() {
		subjectGroups = new ArrayList<SubjectGroup>();

		done = false;
		practice = false;
	}

	public Integer getIdPeriod() {
		return this.idPeriod;
	}

	public void setIdPeriod(Integer idPeriod) {
		this.idPeriod = idPeriod;
	}

	public TreatmentBlock getTreatmentBlock() {
		return this.treatmentBlock;
	}

	public void setTreatmentBlock(TreatmentBlock treatmentBlock) {
		this.treatmentBlock = treatmentBlock;
	}

	public List<SubjectGroup> getSubjectGroups() {
		return this.subjectGroups;
	}

	public void setSubjectGroups(List<SubjectGroup> subjectGroups) {
		this.subjectGroups = subjectGroups;
	}

	public Boolean getDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Boolean getPractice() {
		return practice;
	}

	public void setPractice(Boolean practice) {
		this.practice = practice;
	}

	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((done == null) ? 0 : done.hashCode());
		result = prime * result + ((idPeriod == null) ? 0 : idPeriod.hashCode());
		result = prime * result + ((practice == null) ? 0 : practice.hashCode());
		result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result + ((treatmentBlock == null) ? 0 : treatmentBlock.hashCode());
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
		Period other = (Period) obj;
		if (done == null) {
			if (other.done != null)
				return false;
		} else if (!done.equals(other.done))
			return false;
		if (idPeriod == null) {
			if (other.idPeriod != null)
				return false;
		} else if (!idPeriod.equals(other.idPeriod))
			return false;
		if (practice == null) {
			if (other.practice != null)
				return false;
		} else if (!practice.equals(other.practice))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		if (treatmentBlock == null) {
			if (other.treatmentBlock != null)
				return false;
		} else if (!treatmentBlock.equals(other.treatmentBlock))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Period [ID=" + idPeriod + ", Sequence Number=" + sequenceNumber + ", practice=" + practice + "]";
	}

	@Override
	public int compareTo(Period compareObject) {

		if (getSequenceNumber() < compareObject.getSequenceNumber())
			return -1;
		else if (getSequenceNumber() == compareObject.getSequenceNumber())
			return 0;
		else
			return 1;
	}

}