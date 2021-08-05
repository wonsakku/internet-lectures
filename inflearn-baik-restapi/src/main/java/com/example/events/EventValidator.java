package com.example.events;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {

	public void validate(EventDto eventDto, Errors errors) {
		
		
		// rejectValue => field error \ reject => global error
		if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
			errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong.");
			errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong.");
			errors.reject("wrongPrices", "Value of prices are wrong");
		}
		
		LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
		
		if(endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
			endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
			endEventDateTime.isBefore(eventDto.getBeginEventDateTime())) {
			
			errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong.");
		}
		
		// TODO beginEventDateTime
		// TODO colseEnrollmentDateTime
		
	}
}
