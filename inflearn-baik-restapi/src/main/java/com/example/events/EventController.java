package com.example.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

	
	private final EventValidator eventValidator;
	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;

	
	
	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
	
		if(errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		eventValidator.validate(eventDto, errors);
		if(errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		
		Event event = modelMapper.map(eventDto, Event.class);
		Event newEvent = eventRepository.save(event);
		URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
		return ResponseEntity.created(createdUri).body(newEvent);
	}
	
}






















