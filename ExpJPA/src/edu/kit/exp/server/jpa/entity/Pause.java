package edu.kit.exp.server.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * The persistent class for the initial_break database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "InitialBreak.findAll", query = "SELECT t FROM Pause t"), @NamedQuery(name = "InitialBreak.findByIdInitialBreak", query = "SELECT t FROM Pause t WHERE t.idsequenceElement = :idPause") })
@Table(name = "pause", schema = "exp")
@PrimaryKeyJoinColumn(name = "sequence_element_idsequence_element", referencedColumnName = "idsequence_element")
@DiscriminatorValue("br")
public class Pause extends SequenceElement implements Serializable {
	private static final long serialVersionUID = -1732876018171957678L;

	@Column(length = 255)
	private String message;

	private Long time;

	public Pause() {
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Pause [SeqNo= " + sequenceNumber + ", time=" + time + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pause other = (Pause) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

}