package com.example.events;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class EventTest {


	@Test
	public void builder() {
		Event event = Event.builder()
				.build();
		assertThat(event).isNotNull();
	}
	
	@Test
	public void javaBean() {
		
		// given
		String name = "sik";
		String description = "description";
		
		// when
		Event event = new Event();
		event.setName(name);
		event.setDescription(description);
		
		//then
		assertThat(event.getName()).isEqualTo(name);
		assertThat(event.getDescription()).isEqualTo(description);
	}
	
	
}




























