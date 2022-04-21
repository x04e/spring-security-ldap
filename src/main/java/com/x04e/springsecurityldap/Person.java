package com.x04e.springsecurityldap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import java.util.List;

@Entity
public class Person {

	@Id
	@Column
	@GeneratedValue(generator = "SEQ_PERSON")
	@SequenceGenerator(name = "SEQ_PERSON", sequenceName = "SEQ_PERSON", allocationSize = 1)
	private Long id;
	@Column
	private String name;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="person")
	private List<Foot> feet;

	public Long getId(){ return id; }

	public void setId(long id){ this.id= id; }

	public String getName(){ return name; }

	public void setName(String name){ this.name=name; }

	public List<Foot> getFeet(){ return feet; }

	@Override
	public String toString(){
		return String.format("Person [%s, %s]", id, name);
	}
}
