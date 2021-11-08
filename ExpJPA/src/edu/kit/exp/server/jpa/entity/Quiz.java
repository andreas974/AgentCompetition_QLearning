package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * The persistent class for the quiz database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Quiz.findAll", query = "SELECT t FROM Quiz t"), @NamedQuery(name = "Quiz.findByIdQuiz", query = "SELECT t FROM Quiz t WHERE t.idsequenceElement = :idQuiz") })
@Table(name = "quiz", schema = "exp")
@PrimaryKeyJoinColumn(name = "sequence_element_idsequence_element", referencedColumnName = "idsequence_element")
@DiscriminatorValue("qu")
public class Quiz extends SequenceElement implements Serializable {
	private static final long serialVersionUID = -3675384234891004318L;

	@Column(name = "quiz_factory_key", length = 255)
	private String quizFactoryKey;

	@OneToMany(mappedBy = "quiz", cascade = ALL)
	private List<Protocol> protocols;

	public Quiz() {
		protocols = new ArrayList<Protocol>();
	}

	public String getQuizFactoryKey() {
		return this.quizFactoryKey;
	}

	public void setQuizFactoryKey(String quizFactoryKey) {
		this.quizFactoryKey = quizFactoryKey;
	}

	public List<Protocol> getProtocols() {
		return this.protocols;
	}

	public void setProtocols(List<Protocol> protocols) {
		this.protocols = protocols;
	}

	@Override
	public String toString() {
		return "Quiz [SeqNo= " + sequenceNumber + ", quizFactoryKey=" + quizFactoryKey + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((quizFactoryKey == null) ? 0 : quizFactoryKey.hashCode());
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
		Quiz other = (Quiz) obj;
		if (quizFactoryKey == null) {
			if (other.quizFactoryKey != null)
				return false;
		} else if (!quizFactoryKey.equals(other.quizFactoryKey))
			return false;
		return true;
	}

}