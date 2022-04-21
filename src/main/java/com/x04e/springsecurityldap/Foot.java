package com.x04e.springsecurityldap;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Foot {

	@Id
	@Column
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", referencedColumnName = "ID")
	@JsonIgnore
	private Person person;

	public Long getId(){ return id; }
	public Person getPerson(){ return person; }

	@Override
	public String toString(){
		return String.format("Foot [%s, %s]", id, person);
	}
}
