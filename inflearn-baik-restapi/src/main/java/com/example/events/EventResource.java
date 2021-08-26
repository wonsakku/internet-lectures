package com.example.events;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//@AllArgsConstructor
//@Getter
//public class EventResource extends ResourceSupport{
//	@JsonUnwrapped
//	private Event event;
//}
public class EventResource extends Resource<Event>{

	public EventResource(Event event, Link... links) {
		super(event, links);
		add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
	}
}
