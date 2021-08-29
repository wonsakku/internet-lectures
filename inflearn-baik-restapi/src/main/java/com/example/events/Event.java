package com.example.events;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.account.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Event {

	@Id @GeneratedValue
	private Integer id;
	private String name;
	private String description;
	private LocalDateTime beginEnrollmentDateTime;
	private LocalDateTime closeEnrollmentDateTime;
	private LocalDateTime beginEventDateTime;
	private LocalDateTime endEventDateTime;
	private String location;
	private int basePrice;
	private int maxPrice;
	private int limitOfEnrollment;
	private boolean offline;
	private boolean free;
	@Enumerated(EnumType.STRING)
	private EventStatus eventStatus = EventStatus.DRAFT;
	
	@ManyToOne
	private Account manager;
	
	public void update() {
		if(this.basePrice == 0 && this.maxPrice == 0) {
			this.free = true;
		}else {
			this.free = false;
		}
	}
	
	
}

























