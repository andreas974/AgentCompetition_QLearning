package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the treatment_block database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "TreatmentBlock.findAll", query = "SELECT t FROM TreatmentBlock t"), @NamedQuery(name = "TreatmentBlock.findByIdTreatment", query = "SELECT t FROM TreatmentBlock t WHERE t.idsequenceElement = :idTreatmentBlock") })
@Table(name = "treatment_block", schema = "exp")
@PrimaryKeyJoinColumn(name = "sequence_element_idsequence_element", referencedColumnName = "idsequence_element")
@DiscriminatorValue("tb")
public class TreatmentBlock extends SequenceElement implements Serializable {
	private static final long serialVersionUID = -7308642576530605790L;

	@Column(length = 45)
	private String name;

	@Column(length = 2147483647)
	private String description;

	@OneToMany(mappedBy = "treatmentBlock", fetch = FetchType.LAZY, cascade = ALL)
	private List<Period> periods;

	@ManyToMany(cascade = { MERGE, REFRESH, DETACH })
	@JoinTable(name = "treatment_block_has_treatment", joinColumns = { @JoinColumn(name = "treatment_block_sequence_element_idsequence_element", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "treatment_id_treatment", nullable = false) })
	private List<Treatment> treatments;

	@Column(name = "practice")
	private Boolean practice;

	@Column(length = 255, name = "randomization")
	private String randomization;

	@Transient
	private boolean firstTreatmentBlock;

	public TreatmentBlock() {
		periods = new ArrayList<Period>();
		practice = false;
		treatments = new ArrayList<Treatment>();
	}

	public String getRandomization() {
		return randomization;
	}

	public void setRandomization(String randomization) {
		this.randomization = randomization;
	}

	public Boolean getPractice() {
		return practice;
	}

	public void setPractice(Boolean practice) {
		this.practice = practice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public synchronized List<Period> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Period> periods) {
		this.periods = periods;
	}

	public List<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}

	@Override
	public String toString() {
		return "TreatmentBlock [SeqNo: " + sequenceNumber + ", name=" + name + ", practice=" + practice + "]";
	}

	public boolean isFirstTreatmentBlock() {

		return firstTreatmentBlock;
	}

}