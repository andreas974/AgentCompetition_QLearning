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
 * The persistent class for the Cohort database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Cohort.findAll", query = "SELECT k FROM Cohort k"), @NamedQuery(name = "Cohort.findByIdCohort", query = "SELECT k FROM Cohort k WHERE k.idCohort = :idCohort") })
@Table(name = "cohort", schema = "exp")
public class Cohort implements Serializable {
	private static final long serialVersionUID = -1838860550391285118L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cohort", unique = true, nullable = false)
	private Integer idCohort;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH })
	@JoinColumn(name = "session_id_session", nullable = false)
	private Session session;

	@OneToMany(mappedBy = "cohort", cascade = ALL)
	private List<Subject> subjects;

	@Column(name = "size", table = "cohort")
	private Integer size;

	public Cohort() {
		subjects = new ArrayList<Subject>();
		size = 0;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getIdCohort() {
		return idCohort;
	}

	public void setIdCohort(Integer idCohort) {
		this.idCohort = idCohort;
	}

	public Session getSession() {
		return this.session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public List<Subject> getSubjects() {
		return this.subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	@Override
	public String toString() {
		return "Cohort [idCohort=" + idCohort + ", session=" + session + ", subjects=" + subjects + ", size=" + size + "]";
	}

}