package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the experiment database table.
 * 
 */
@Entity
@Table(name = "experiment", schema = "exp")
@NamedQueries({ @NamedQuery(name = "Experiment.findAll", query = "SELECT e FROM Experiment e"), @NamedQuery(name = "Experiment.findByIdExperiment", query = "SELECT e FROM Experiment e WHERE e.idExperiment = :idExperiment"), @NamedQuery(name = "Experiment.findByName", query = "SELECT e FROM Experiment e WHERE e.name = :name"), })
public class Experiment implements Serializable {
	private static final long serialVersionUID = -4416851193446561700L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_experiment", unique = true, nullable = false)
	private Integer idExperiment;

	@Column(length = 2147483647)
	private String description;

	@Column(length = 45)
	private String name;

	@Column
	private Boolean done;

	@OneToMany(mappedBy = "experiment", cascade = ALL, fetch = EAGER)
	private List<Session> sessions;

	public Experiment() {

		this.sessions = new ArrayList<Session>();
		this.done = false;

	}

	public Integer getIdExperiment() {
		return this.idExperiment;
	}

	public void setIdExperiment(Integer idExperiment) {
		this.idExperiment = idExperiment;
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

	public synchronized List<Session> getSessions() {
		return this.sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
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
		result = prime * result + ((idExperiment == null) ? 0 : idExperiment.hashCode());
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
		Experiment other = (Experiment) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idExperiment == null) {
			if (other.idExperiment != null)
				return false;
		} else if (!idExperiment.equals(other.idExperiment))
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
		return "Experiment [ID= " + idExperiment + ", Name= " + name + "]";
	}

}