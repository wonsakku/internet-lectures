package com.example.events;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;


import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.session.HeaderWebSessionIdResolver;

import com.example.common.RestDocsConfiguration;
import com.example.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.util.ContentTypeUtil;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	
//	@MockBean
//	EventRepository eventRepository;
	
	@Test
	@TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
				.location("강남역 D2 스타텁 팩토리")
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
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.query-events").exists())
			.andExpect(jsonPath("_links.update-event").exists())
			.andDo(document("create-event",
							links(
								linkWithRel("self").description("link to self"),
								linkWithRel("query-events").description("link to query events"),
								linkWithRel("update-event").description("link to update an existing event")
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
								fieldWithPath("_links.update-event.href").description("link to update event")
							)
						)
				)
			;
	}
	
	
	@Test
	@TestDescription("입력받을 수 없는 값을 사용하는 경우에 에러가 발생하는 테스트")
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
				.location("강남역 D2 스타텁 팩토리")
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
	@TestDescription("입력 값이 비어있는 경우 에러가 발생하는 테스트")
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
	@TestDescription("입력 값이 잘못된 경우 에러가 발생하는 테스트")
	public void createEvent_Bad_Request_Wrong_Input() throws Exception{
//	public void 입력_값이_잘못된_경우_에러가_발생하는_테스트() throws Exception{
		
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
				.location("강남역 D2 스타텁 팩토리")
				.build();
		
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.objectMapper.writeValueAsString(eventDto))
				)
		
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$[0].objectName").exists()) // $[0] -> 배열
		.andExpect(jsonPath("$[0].defaultMessage").exists())
		.andExpect(jsonPath("$[0].code").exists())
//		.andExpect(jsonPath("$[0].field").exists())
//		.andExpect(jsonPath("$[0].rejectedValue").exists())
		.andDo(print())
		;
	}
	
	
	
	
	

	
}


















































