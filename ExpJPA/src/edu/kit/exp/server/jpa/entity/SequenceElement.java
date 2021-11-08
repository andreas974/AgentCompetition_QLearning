package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.JOINED;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the sequence_element database table.
 * 
 */
@Entity
@Table(name = "sequence_element", schema = "exp")
@NamedQueries({ @NamedQuery(name = "SequenceElement.findAll", query = "SELECT t FROM SequenceElement t"), @NamedQuery(name = "SequenceElement.findByIdSequenceElement", query = "SELECT t FROM SequenceElement t WHERE t.idsequenceElement = :idSequenceElement") })
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "type_2", discriminatorType = STRING)
public abstract class SequenceElement implements Serializable, Comparable<SequenceElement> {
	private static final long serialVersionUID = -4384182788670695559L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idsequence_element", unique = true, nullable = false)
	protected Integer idsequenceElement;

	protected Boolean done;

	@Column(name = "sequence_number")
	protected Integer sequenceNumber;

	@Column(name = "type_2", length = 45)
	protected String type2;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { MERGE, REFRESH, DETACH })
	@JoinColumn(name = "session_id_session", nullable = false)
	protected Session session;

	public SequenceElement() {
		done = false;
	}

	public Integer getIdsequenceElement() {
		return this.idsequenceElement;
	}

	public void setIdsequenceElement(Integer idsequenceElement) {
		this.idsequenceElement = idsequenceElement;
	}

	public Boolean getDone() {
		return this.done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public Integer getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getType2() {
		return this.type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((done == null) ? 0 : done.hashCode());
		result = prime * result + ((idsequenceElement == null) ? 0 : idsequenceElement.hashCode());
		result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result + ((type2 == null) ? 0 : type2.hashCode());
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
		SequenceElement other = (SequenceElement) obj;
		if (done == null) {
			if (other.done != null)
				return false;
		} else if (!done.equals(other.done))
			return false;
		if (idsequenceElement == null) {
			if (other.idsequenceElement != null)
				return false;
		} else if (!idsequenceElement.equals(other.idsequenceElement))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		if (type2 == null) {
			if (other.type2 != null)
				return false;
		} else if (!type2.equals(other.type2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SequenceElement [Sequence Number=" + sequenceNumber + ", ID=" + idsequenceElement + "]";
	}

	@Override
	public int compareTo(SequenceElement compareObject) {

		if (getSequenceNumber() < compareObject.getSequenceNumber())
			return -1;
		else if (getSequenceNumber() == compareObject.getSequenceNumber())
			return 0;
		else
			return 1;
	}

}