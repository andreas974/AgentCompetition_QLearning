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
 * The persistent class for the trial database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Trial.findAll", query = "SELECT t FROM Trial t"), @NamedQuery(name = "Trial.findByIdTrial", query = "SELECT t FROM Trial t WHERE t.idTrial = :idTrial"), @NamedQuery(name = "Trial.findByScreenName", query = "SELECT t FROM Trial t WHERE t.screenName = :screenName"), @NamedQuery(name = "Trial.findByEvent", query = "SELECT t FROM Trial t WHERE t.event = :event"), @NamedQuery(name = "Trial.findByValue", query = "SELECT t FROM Trial t WHERE t.value = :value"), @NamedQuery(name = "Trial.findByClientTime", query = "SELECT t FROM Trial t WHERE t.clientTime = :clientTime"), @NamedQuery(name = "Trial.findByServerTime", query = "SELECT t FROM Trial t WHERE t.serverTime = :serverTime") })
@Table(name = "trial", schema = "exp")
public class Trial implements Serializable {
	private static final long serialVersionUID = 5971066095333204160L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_trial", unique = true, nullable = false)
	private Long idTrial;

	@Column(name = "client_time")
	private Long clientTime;

	@Column(nullable = false, length = 255)
	private String event;

	@Column(name = "screen_name", length = 255)
	private String screenName;

	@Column(name = "server_time")
	private Long serverTime;

	@Column(length = 1024)
	private String value;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_group_id_subject_group", nullable = false)
	private SubjectGroup subjectGroup;

	@ManyToOne(cascade = { MERGE, REFRESH, DETACH })
	@JoinColumn(name = "subject_id_subject")
	private Subject subject;

	public Trial() {
	}

	public Long getIdTrial() {
		return this.idTrial;
	}

	public void setIdTrial(Long idTrial) {
		this.idTrial = idTrial;
	}

	public Long getClientTime() {
		return this.clientTime;
	}

	public void setClientTime(Long clientTime) {
		this.clientTime = clientTime;
	}

	public String getEvent() {
		return this.event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getScreenName() {
		return this.screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Long getServerTime() {
		return this.serverTime;
	}

	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public SubjectGroup getSubjectGroup() {
		return subjectGroup;
	}

	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientTime == null) ? 0 : clientTime.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((idTrial == null) ? 0 : idTrial.hashCode());
		result = prime * result + ((screenName == null) ? 0 : screenName.hashCode());
		result = prime * result + ((serverTime == null) ? 0 : serverTime.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Trial other = (Trial) obj;
		if (clientTime == null) {
			if (other.clientTime != null)
				return false;
		} else if (!clientTime.equals(other.clientTime))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (idTrial == null) {
			if (other.idTrial != null)
				return false;
		} else if (!idTrial.equals(other.idTrial))
			return false;
		if (screenName == null) {
			if (other.screenName != null)
				return false;
		} else if (!screenName.equals(other.screenName))
			return false;
		if (serverTime == null) {
			if (other.serverTime != null)
				return false;
		} else if (!serverTime.equals(other.serverTime))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Trial [idTrial=" + idTrial + ", clientTime=" + clientTime + ", event=" + event + ", screenName=" + screenName + ", serverTime=" + serverTime + ", value=" + value + "]";
	}

}