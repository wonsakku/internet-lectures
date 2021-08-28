package com.example.events;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.ErrorResource;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.Optional;

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
//			return ResponseEntity.badRequest().body(errors);
			return badRequest(errors);
		}
		
		eventValidator.validate(eventDto, errors);
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		
		Event event = modelMapper.map(eventDto, Event.class);
		Event newEvent = eventRepository.save(event);
		ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
		URI createdUri = selfLinkBuilder.toUri();
		
		EventResource eventResource = new EventResource(event);
		eventResource.add(selfLinkBuilder.withRel("query-events"));
//		eventResource.add(selfLinkBuilder.withSelfRel()); eventResource를 만들 때  생성자로 추가함.
		eventResource.add(selfLinkBuilder.withRel("update-event"));
		eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
		
		return ResponseEntity.created(createdUri).body(eventResource);
	}

	
	@GetMapping
	public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		
		Page<Event> page = this.eventRepository.findAll(pageable);
//		PagedResources<Resource<Event>> resoures = assembler.toResource(page);
		PagedResources<Resource<Event>> resoures = assembler.toResource(page, e -> new EventResource(e));
		resoures.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
		return ResponseEntity.status(HttpStatus.OK).body(resoures);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if(optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Event event = optionalEvent.get();
		EventResource eventResourse = new EventResource(event);
		eventResourse.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
		return ResponseEntity.ok(eventResourse);
	}

	private ResponseEntity<ErrorResource> badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorResource(errors));
	}
	
}






















