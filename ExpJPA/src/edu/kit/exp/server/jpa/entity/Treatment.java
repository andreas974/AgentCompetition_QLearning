package edu.kit.exp.server.jpa.entity;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the institution database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Treatment.findAll", query = "SELECT i FROM Treatment i"), @NamedQuery(name = "Treatment.findByIdTreatment", query = "SELECT i FROM Treatment i WHERE i.idTreatment = :idTreatment"), @NamedQuery(name = "Treatment.findByName", query = "SELECT i FROM Treatment i WHERE i.name = :name") })
@Table(name = "treatment", schema = "exp")
public class Treatment implements Serializable {
	private static final long serialVersionUID = -5843557490223152511L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_treatment", unique = true, nullable = false)
	private Integer idTreatment;

	@Column(length = 2147483647)
	private String description;

	@Column(length = 45)
	private String name;

	@Column(name = "environment_factory_key", length = 200)
	private String environmentFactoryKey;

	@Column(name = "institution_factory_key", length = 200)
	private String institutionFactoryKey;

	@ManyToMany(mappedBy = "treatments", cascade = { MERGE, REFRESH, DETACH })
	private List<TreatmentBlock> treatmentBlocks;

	public Treatment() {
	}

	public Integer getIdTreatment() {
		return idTreatment;
	}

	public void setIdTreatment(Integer idTreatment) {
		this.idTreatment = idTreatment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreatmentBlock> getTreatmentBlocks() {
		return treatmentBlocks;
	}

	public void setTreatmentBlocks(List<TreatmentBlock> treatmentBlocks) {
		this.treatmentBlocks = treatmentBlocks;
	}

	public String getEnvironmentFactoryKey() {
		return environmentFactoryKey;
	}

	public void setEnvironmentFactoryKey(String environmentFactoryKey) {
		this.environmentFactoryKey = environmentFactoryKey;
	}

	public String getInstitutionFactoryKey() {
		return institutionFactoryKey;
	}

	public void setInstitutionFactoryKey(String institutionFactoryKey) {
		this.institutionFactoryKey = institutionFactoryKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((environmentFactoryKey == null) ? 0 : environmentFactoryKey.hashCode());
		result = prime * result + ((idTreatment == null) ? 0 : idTreatment.hashCode());
		result = prime * result + ((institutionFactoryKey == null) ? 0 : institutionFactoryKey.hashCode());
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
		Treatment other = (Treatment) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (environmentFactoryKey == null) {
			if (other.environmentFactoryKey != null)
				return false;
		} else if (!environmentFactoryKey.equals(other.environmentFactoryKey))
			return false;
		if (idTreatment == null) {
			if (other.idTreatment != null)
				return false;
		} else if (!idTreatment.equals(other.idTreatment))
			return false;
		if (institutionFactoryKey == null) {
			if (other.institutionFactoryKey != null)
				return false;
		} else if (!institutionFactoryKey.equals(other.institutionFactoryKey))
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
		return name + " (ID: " + idTreatment + ")";
	}

}