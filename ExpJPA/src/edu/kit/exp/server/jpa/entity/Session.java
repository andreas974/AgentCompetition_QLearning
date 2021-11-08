package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.sql.Timestamp;
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

import org.joda.time.DateTime;

/**
 * The persistent class for the session database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s"), @NamedQuery(name = "Session.findByIdSession", query = "SELECT s FROM Session s WHERE s.idSession = :idSession"), @NamedQuery(name = "Session.findByName", query = "SELECT s FROM Session s WHERE s.name = :name"), })
@Table(name = "session", schema = "exp")
public class Session implements Serializable, Comparable<Session> {
	private static final long serialVersionUID = -3962048711098385003L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_session", unique = true, nullable = false)
	private Integer idSession;

	@Column(length = 2147483647)
	private String description;

	@Column(length = 45)
	private String name;

	@OneToMany(mappedBy = "session", fetch = LAZY, cascade = ALL)
	private List<Cohort> cohorts;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH })
	@JoinColumn(name = "experiment_id_experiment", nullable = false)
	private Experiment experiment;

	@OneToMany(mappedBy = "session", cascade = ALL)
	private List<SequenceElement> sequenceElements;

	@Column(name = "planned_date")
	private Timestamp plannedDate;

	@Column
	private Boolean done;

	@Column
	private Boolean started;

	public Session() {
		sequenceElements = new ArrayList<SequenceElement>();
		cohorts = new ArrayList<Cohort>();
		done = false;
		started = false;

	}

	public Boolean getStarted() {
		return started;
	}

	public void setStarted(Boolean started) {
		this.started = started;
	}

	public void setCohorts(List<Cohort> cohorts) {
		this.cohorts = cohorts;
	}

	public Integer getIdSession() {
		return this.idSession;
	}

	public void setIdSession(Integer idSession) {
		this.idSession = idSession;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Cohort> getCohorts() {
		return this.cohorts;
	}

	public void setCohortes(List<Cohort> cohorts) {
		this.cohorts = cohorts;
	}

	public Experiment getExperiment() {
		return this.experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public List<SequenceElement> getSequenceElements() {
		return sequenceElements;
	}

	public void setSequenceElements(List<SequenceElement> sequenceElements) {
		this.sequenceElements = sequenceElements;
	}

	public Timestamp getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(Timestamp plannedDate) {
		this.plannedDate = plannedDate;
	}

	public Boolean getDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((idSession == null) ? 0 : idSession.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Session other = (Session) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idSession == null) {
			if (other.idSession != null)
				return false;
		} else if (!idSession.equals(other.idSession))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {

		DateTime d = new DateTime(plannedDate);

		return "Session [Date=" + d.toString("yyyy-MM-dd HH:mm:ss") + ", name=" + name + ", done=" + done + ", ID=" + idSession + "]";
	}

	@Override
	public int compareTo(Session compareObject) {

		if (getPlannedDate().before(compareObject.getPlannedDate()))
			return -1;
		else if (getPlannedDate().equals(compareObject.getPlannedDate()))
			return 0;
		else
			return 1;
	}

}