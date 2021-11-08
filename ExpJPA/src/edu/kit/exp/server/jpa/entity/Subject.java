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
import javax.persistence.Transient;

/**
 * The persistent class for the subject database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Subject.findAll", query = "SELECT s FROM Subject s"), @NamedQuery(name = "Subject.findByIdSubject", query = "SELECT s FROM Subject s WHERE s.idSubject = :idSubject"), @NamedQuery(name = "Subject.findByIdClient", query = "SELECT s FROM Subject s WHERE s.idClient = :idClient"), })
@Table(name = "subject", schema = "exp")
public class Subject implements Serializable {
	private static final long serialVersionUID = -4818145911324302360L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_subject", unique = true, nullable = false)
	private Integer idSubject;

	@Column(name = "id_client", length = 60)
	private String idClient;

	private Double payoff;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH })
	@JoinColumn(name = "cohort_id_cohort", nullable = false)
	private Cohort cohort;

	@OneToMany(mappedBy = "subject", cascade = ALL)
	private List<Membership> memberships;

	@OneToMany(mappedBy = "subject", cascade = ALL, fetch = FetchType.LAZY)
	private List<Trial> trials;

	@OneToMany(mappedBy = "subject", cascade = ALL)
	private List<Protocol> protocols;

	@Transient
	private String role;

	public Subject() {
		payoff = 0.0d;
		trials = new ArrayList<Trial>();
		memberships = new ArrayList<Membership>();
		protocols = new ArrayList<Protocol>();

	}

	public Integer getIdSubject() {
		return this.idSubject;
	}

	public void setIdSubject(Integer idSubject) {
		this.idSubject = idSubject;
	}

	public String getIdClient() {
		return this.idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public Double getPayoff() {
		return this.payoff;
	}

	public void setPayoff(Double payoff) {
		this.payoff = payoff;
	}

	public Cohort getCohort() {
		return this.cohort;
	}

	public void setCohort(Cohort kohorte) {
		this.cohort = kohorte;
	}

	public List<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	public List<Trial> getTrials() {
		return this.trials;
	}

	public void setTrials(List<Trial> trials) {
		this.trials = trials;
	}

	public List<Protocol> getProtocols() {
		return protocols;
	}

	public void setProtocols(List<Protocol> protocols) {
		this.protocols = protocols;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Subject [idSubject=" + idSubject + ", idClient=" + idClient + ", payoff=" + payoff + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idClient == null) ? 0 : idClient.hashCode());
		result = prime * result + ((idSubject == null) ? 0 : idSubject.hashCode());
		long temp;
		temp = Double.doubleToLongBits(payoff);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		Subject other = (Subject) obj;
		if (idClient == null) {
			if (other.idClient != null)
				return false;
		} else if (!idClient.equals(other.idClient))
			return false;
		if (idSubject == null) {
			if (other.idSubject != null)
				return false;
		} else if (!idSubject.equals(other.idSubject))
			return false;
		if (Double.doubleToLongBits(payoff) != Double.doubleToLongBits(other.payoff))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

}