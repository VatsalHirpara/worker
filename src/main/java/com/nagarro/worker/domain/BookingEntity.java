package com.nagarro.worker.domain;

import java.time.Instant;
import java.time.LocalDateTime;

import com.nagarro.worker.enums.BookingStatus;
import com.nagarro.worker.enums.ServiceCategory;
import com.nagarro.worker.enums.ServiceName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

	private String id;

	private BookingStatus bookingStatus = BookingStatus.PROCESSING;

	private Integer customerId;

	private Integer serviceId;

	private ServiceCategory serviceCategory;

	private ServiceName serviceName;

	private Integer workerId;

	private String workerName;

	private Instant creationTimeStamp;
	
	private LocalDateTime scheduledDate;
}
