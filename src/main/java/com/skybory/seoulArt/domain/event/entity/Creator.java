package com.skybory.seoulArt.domain.event.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Creator {
	@Id
	long id;
	
	String name;
	String department;
	String description;
	@ManyToOne		// Creator가 이벤트의 주인이 됨
	@JoinColumn
	@JsonIgnoreProperties({"creator"})
	Event event;

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void changeEvent(Event event) {
		this.event = event;
		event.getCreator().add(this);
	}
	
	
	
}
