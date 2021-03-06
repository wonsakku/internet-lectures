package com.example.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.example.common.BaseControllerTest;
import com.example.common.TestDescription;

public class EventControllerTests extends BaseControllerTest{

	@Autowired
	EventRepository eventRepository;
	
//	@MockBean
//	EventRepository eventRepository;
	
	@Test
	@TestDescription("??????????????? ???????????? ???????????? ?????????")
	public void createEvent() throws Exception {
		EventDto event = EventDto.builder()
				.name("Spring")
				.description("RESTAPI Development")
				.beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 31, 15, 0))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 8, 1, 15, 0))
				.beginEventDateTime(LocalDateTime.of(2021, 8, 2, 15, 0))
				.endEventDateTime(LocalDateTime.of(2021, 8, 3, 15, 0))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("????????? D2 ????????? ?????????")
				.build();
		
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaTypes.HAL_JSON)
					.content(objectMapper.writeValueAsString(event))
				)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").exists())
			.andExpect(header().exists(HttpHeaders.LOCATION))
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
			.andExpect(jsonPath("id").value(Matchers.not(100)))
			.andExpect(jsonPath("free").value(Matchers.not(true)))
			.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
//			.andExpect(jsonPath("_links.self").exists())
//			.andExpect(jsonPath("_links.query-events").exists())
//			.andExpect(jsonPath("_links.update-event").exists())
			.andDo(document("create-event",
							links(
								linkWithRel("self").description("link to self"),
								linkWithRel("query-events").description("link to query events"),
								linkWithRel("update-event").description("link to update an existing event"),
								linkWithRel("profile").description("link to profile")
							),
							requestHeaders(
								headerWithName(HttpHeaders.ACCEPT).description("aceept header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
							),
							requestFields(
								fieldWithPath("name").description("Name of new Event"),
								fieldWithPath("description").description("description of new Event"),
								fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new Event"),
								fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new Event"),
								fieldWithPath("beginEventDateTime").description("beginEventDateTime of new Event"),
								fieldWithPath("endEventDateTime").description("endEventDateTime of new Event"),
								fieldWithPath("location").description("location of new Event"),
								fieldWithPath("basePrice").description("basePrice of new Event"),
								fieldWithPath("maxPrice").description("maxPrice of new Event"),
								fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new Event")
							),
							responseHeaders(
								headerWithName(HttpHeaders.LOCATION).description("Location header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
							),
//							relaxedResponseFields(
							responseFields(
								fieldWithPath("id").description("Id of new Event"),
								fieldWithPath("name").description("Name of new Event"),
								fieldWithPath("description").description("description of new Event"),
								fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new Event"),
								fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new Event"),
								fieldWithPath("beginEventDateTime").description("beginEventDateTime of new Event"),
								fieldWithPath("endEventDateTime").description("endEventDateTime of new Event"),
								fieldWithPath("location").description("location of new Event"),
								fieldWithPath("basePrice").description("basePrice of new Event"),
								fieldWithPath("maxPrice").description("maxPrice of new Event"),
								fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new Event"),
								fieldWithPath("free").description("it tells if this event is free or not"),
								fieldWithPath("offline").description("it tells if this event is offline or not"),
								fieldWithPath("eventStatus").description("eventStatus"),
								fieldWithPath("_links.self.href").description("link to self"),
								fieldWithPath("_links.query-events.href").description("link to query event"),
								fieldWithPath("_links.update-event.href").description("link to update event"),
								fieldWithPath("_links.profile.href").description("link to profile")
							)
						)
				)
			;
	}
	
	
	@Test
	@TestDescription("???????????? ??? ?????? ?????? ???????????? ????????? ????????? ???????????? ?????????")
	public void badRequest() throws Exception {
		
		Event event = Event.builder()
				.id(100)
				.name("Spring")
				.description("RESTAPI Development")
				.beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 31, 15, 0))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 8, 1, 15, 0))
				.beginEventDateTime(LocalDateTime.of(2021, 8, 2, 15, 0))
				.endEventDateTime(LocalDateTime.of(2021, 8, 3, 15, 0))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("????????? D2 ????????? ?????????")
				.free(true)
				.offline(false)
				.eventStatus(EventStatus.PUBLISHED)
				.build();
		
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaTypes.HAL_JSON)
					.content(objectMapper.writeValueAsString(event))
				)
			.andDo(print())
			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("id").exists())
//			.andExpect(header().exists(HttpHeaders.LOCATION))
//			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
//			.andExpect(jsonPath("id").value(Matchers.not(100)))
//			.andExpect(jsonPath("free").value(Matchers.not(true)))
//			.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
			
			;
	}
	
	@Test
	@TestDescription("?????? ?????? ???????????? ?????? ????????? ???????????? ?????????")
	public void createEvent_Bad_Request_Empty_Input() throws Exception{
		
		
		EventDto eventDto = EventDto.builder().build();
		
		mockMvc.perform(post("/api/events")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(this.objectMapper.writeValueAsString(eventDto))
				)
		
			.andExpect(status().isBadRequest())
			.andDo(print())
			;
	}
	
	
	@Test
	@TestDescription("?????? ?????? ????????? ?????? ????????? ???????????? ?????????")
	public void createEvent_Bad_Request_Wrong_Input() throws Exception{
//	public void ??????_??????_?????????_??????_?????????_????????????_?????????() throws Exception{
		
		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("RESTAPI Development")
				.beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 31, 15, 0))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 8, 1, 15, 0))
				.beginEventDateTime(LocalDateTime.of(2021, 8, 2, 15, 0))
				.endEventDateTime(LocalDateTime.of(2021, 8, 3, 14, 0))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("????????? D2 ????????? ?????????")
				.build();
		
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.objectMapper.writeValueAsString(eventDto))
				)
		
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("content[0].objectName").exists()) // $[0] -> ??????
		.andExpect(jsonPath("content[0].defaultMessage").exists())
		.andExpect(jsonPath("content[0].code").exists())
		.andExpect(jsonPath("_links.index").exists())
//		.andExpect(jsonPath("$[0].objectName").exists()) // $[0] -> ??????
//		.andExpect(jsonPath("$[0].defaultMessage").exists())
//		.andExpect(jsonPath("$[0].code").exists())
//		.andExpect(jsonPath("$[0].field").exists())
//		.andExpect(jsonPath("$[0].rejectedValue").exists())
		.andDo(print())
		;
	}
	
	
	@Test
	@TestDescription("30?????? ???????????? 10?????? ????????? ????????? ????????????")
	public void queryEvents() throws Exception{
		
		
		//given
		IntStream.range(0, 30).forEach(this::generateEvent);
//		IntStream.range(0, 30).forEach(i ->{
//			this.generateEvent(i);
//		});
		
		
		//when
		ResultActions perform = this.mockMvc.perform(get("/api/events")
					.param("page", "1") // paging??? ??? 0??? 1?????? , 1??? 2??????
					.param("size", "10")
					.param("sort", "name,DESC")
				);
		
		
		// then
		perform.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("page").exists())
				.andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.profile").exists())
				.andDo(document("query-events"))
				;
	}
	
	private Event generateEvent(int i) {

		Event event = Event.builder()
				.name("event_" + i)
				.description("test event")
				.beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 31, 15, 0))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 8, 1, 15, 0))
				.beginEventDateTime(LocalDateTime.of(2021, 8, 2, 15, 0))
				.endEventDateTime(LocalDateTime.of(2021, 8, 3, 15, 0))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("????????? D2 ????????? ?????????")
				.free(false)
				.offline(true)
				.eventStatus(EventStatus.DRAFT)
				.build();
		this.eventRepository.save(event);
		
		return event;
	}

	
	@Test
	@TestDescription("????????? ???????????? ?????? ????????????")
	public void getEvent() throws Exception {
		//given
		Event event = this.generateEvent(100);
		
		//when & then
		this.mockMvc.perform(get("/api/events/{id}", event.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").exists())
			.andExpect(jsonPath("id").exists())
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.profile").exists())
			.andDo(document("get-an-event"))
			;
	}
	
	
	
	@Test
	@TestDescription("?????? ???????????? ???????????? ??? 404 ????????????")
	public void getEvent404() throws Exception{
		this.mockMvc.perform(get("/api/events/123123"))
			.andExpect(status().isNotFound())
			;
	}

	
	@Test
	@TestDescription("???????????? ??????????????? ????????????")
	public void updateEvent() throws Exception{
		
		//given
		Event event = this.generateEvent(200);
		
		EventDto eventDto = modelMapper.map(event, EventDto.class);
		String eventName = "Updated Event";
		eventDto.setName(eventName);
		
		//when & then
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(this.objectMapper.writeValueAsString(eventDto))
				)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(eventName))
			.andExpect(jsonPath("_links.self").exists())
			.andDo(document("update-event"))
			;
				
	}

	
	@Test
	@TestDescription("???????????? ???????????? ????????? ????????? ?????? ??????")
	public void updateEvent400_Empty() throws Exception{
		
		//given
		Event event = this.generateEvent(200);
		
		EventDto eventDto = new EventDto();
		String eventName = "Updated Event";
		eventDto.setName(eventName);
		
		//when & then
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(this.objectMapper.writeValueAsString(eventDto))
				)
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	}
	
	
	@Test
	@TestDescription("???????????? ????????? ????????? ????????? ?????? ??????")
	public void updateEvent400_Wrong() throws Exception{
		
		//given
		Event event = this.generateEvent(200);

		EventDto eventDto = this.modelMapper.map(event, EventDto.class);
		eventDto.setBasePrice(20000);
		eventDto.setMaxPrice(200);
		
		//when & then
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.objectMapper.writeValueAsString(eventDto))
				)
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	}
	
	
	@Test
	@TestDescription("???????????? ?????? ????????? ?????? ??????")
	public void updateEvent404() throws Exception{
		
		//given
		Event event = this.generateEvent(200);
		
		EventDto eventDto = this.modelMapper.map(event, EventDto.class);
		eventDto.setBasePrice(20000);
		eventDto.setMaxPrice(200);
		
		//when & then
		this.mockMvc.perform(put("/api/events/9999")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.objectMapper.writeValueAsString(eventDto))
				)
		.andDo(print())
		.andExpect(status().isNotFound())
		;
	}
	
	
	
	
	
}


















































