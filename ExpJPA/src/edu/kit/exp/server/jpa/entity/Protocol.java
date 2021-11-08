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
 * The persistent class for the protocol database table.
 * 
 */
@Entity
@Table(name = "protocol", schema = "exp")
@NamedQueries({ @NamedQuery(name = "Protocol.findAll", query = "SELECT t FROM Protocol t"), @NamedQuery(name = "Protocol.findByIdProtocol", query = "SELECT t FROM Protocol t WHERE t.idprotocol = :idProtocol") })
public class Protocol implements Serializable {
	private static final long serialVersionUID = -2707012121750032945L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, table = "protocol")
	private Integer idprotocol;

	private Boolean passed;

	@Column(length = 2147483647)
	private String solution;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id_subject", nullable = false)
	private Subject subject;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "quiz_sequence_element_idsequence_element", nullable = false)
	private Quiz quiz;

	public Protocol() {
	}

	public Integer getIdprotocol() {
		return this.idprotocol;
	}

	public void setIdprotocol(Integer idprotocol) {
		this.idprotocol = idprotocol;
	}

	public Boolean getPassed() {
		return this.passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public String getSolution() {
		return this.solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Quiz getQuiz() {
		return this.quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

}