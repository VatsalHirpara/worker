package com.nagarro.worker.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nagarro.worker.domain.BookingEntity;
import com.nagarro.worker.domain.WorkerEntity;
import com.nagarro.worker.dto.ResponseTO;
import com.nagarro.worker.enums.BookingStatus;
import com.nagarro.worker.enums.ServiceCategory;
import com.nagarro.worker.enums.ServiceName;
import com.nagarro.worker.service.WorkerService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@Service
public class WorkerServiceImpl implements WorkerService {
	private List<WorkerEntity> workersList = Collections.synchronizedList(new ArrayList<>());

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	private EurekaClient eurekaClient;

	/**
	 * populates data into workersList after object is created.
	 */
	@PostConstruct
	public void populateData() {
		WorkerEntity worker1 = new WorkerEntity();
		worker1.setId(1);
		worker1.setName("Ramesh");
		worker1.setEmail("ramesh@uc.com");
		worker1.setServiceCategory(ServiceCategory.SALON_FOR_MEN);
		worker1.setServiceName(ServiceName.HAIRCUT);
		worker1.setCity("Gurugram");
		WorkerEntity worker2 = new WorkerEntity();
		worker2.setId(2);
		worker2.setName("Suresh");
		worker2.setEmail("suresh@uc.com");
		worker2.setCity("Noida");
		worker2.setServiceCategory(ServiceCategory.APPLIANCE_REPAIR);
		worker2.setServiceName(ServiceName.AC_REPAIR);
		WorkerEntity worker3 = new WorkerEntity();
		worker3.setId(3);
		worker3.setName("Kamlesh");
		worker3.setEmail("kamlesh@uc.com");
		worker3.setCity("Delhi");
		worker3.setServiceCategory(ServiceCategory.PLUMBERS);
		worker3.setServiceName(ServiceName.BATH_FITTING);
		workersList.add(worker1);
		workersList.add(worker2);
		workersList.add(worker3);
	}

	@Override
	public List<WorkerEntity> getAllWorkers() {
		return workersList;
	}

	@JmsListener(destination = "BookingRequestReceived")
	public void assignWorker(String bookingID) throws Exception {
		Thread.sleep(10000);
		BookingEntity bookingEntity = getBookingEntityById(bookingID);
		WorkerEntity assignedWorker = findWorkerForService(bookingEntity.getServiceCategory(),
				bookingEntity.getServiceName());
		bookingEntity.setWorkerId(assignedWorker.getId());
		bookingEntity.setWorkerName(assignedWorker.getName());
		bookingEntity.setBookingStatus(BookingStatus.WORKER_ASSIGNED);
		sendNotificationToworker(bookingID);
		InstanceInfo instance = eurekaClient.getNextServerFromEureka("apigateway", false);
		String bookingUrl = instance.getHomePageUrl() + "/bookings";
		restTemplate.put(bookingUrl, bookingEntity);
	}

	private void sendNotificationToworker(String bookingID) {
		jmsTemplate.convertAndSend("AcceptOrRejectBooking", bookingID);
	}

	private WorkerEntity findWorkerForService(ServiceCategory serviceCategory, ServiceName serviceName)
			throws Exception {
		for (WorkerEntity workerEntity : workersList) {
			if (workerEntity.getServiceCategory().equals(serviceCategory)
					&& workerEntity.getServiceName().equals(serviceName)) {
				return workerEntity;
			}
		}
		throw new Exception("No worker found for given service");
	}

	@Override
	public void acceptBooking(String bookingId) {
		BookingEntity bookingEntity = getBookingEntityById(bookingId);
		bookingEntity.setBookingStatus(BookingStatus.ACCEPTED_BY_WORKER);
		InstanceInfo instance = eurekaClient.getNextServerFromEureka("apigateway", false);
		String bookingUrl = instance.getHomePageUrl() + "/bookings";
		restTemplate.put(bookingUrl, bookingEntity);
		sendAcceptNotificationTocustomer(bookingId);
	}

	private void sendAcceptNotificationTocustomer(String bookingId) {
		jmsTemplate.convertAndSend("BookingAcceptedByWorker", bookingId);
	}

	@Override
	public void rejectBooking(String bookingId) {
		InstanceInfo instance = eurekaClient.getNextServerFromEureka("apigateway", false);
		String serviceUrl = instance.getHomePageUrl() + "/bookings/" + bookingId;
		ResponseEntity<ResponseTO<BookingEntity>> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseTO<BookingEntity>>() {
				});
		BookingEntity bookingEntity = response.getBody().getData();
		bookingEntity.setBookingStatus(BookingStatus.REJECTED_BY_WORKER);
		String bookingUrl = instance.getHomePageUrl() + "/bookings";
		restTemplate.put(bookingUrl, bookingEntity);
		sendRejectionNotificationToCustomer(bookingId);
	}

	private void sendRejectionNotificationToCustomer(String bookingId) {
		jmsTemplate.convertAndSend("BookingRejectedByWorker", bookingId);
	}

	private BookingEntity getBookingEntityById(String bookingId) {
		InstanceInfo instance = eurekaClient.getNextServerFromEureka("apigateway", false);
		String serviceUrl = instance.getHomePageUrl() + "/bookings/" + bookingId;
		ResponseEntity<ResponseTO<BookingEntity>> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseTO<BookingEntity>>() {
				});
		return response.getBody().getData();
	}

	@Override
	public WorkerEntity getWorkerById(Integer id) throws Exception {
		for (WorkerEntity workerEntity : workersList) {
			if (workerEntity.getId() == id)
				return workerEntity;
		}
		throw new Exception("No worker found for given id");
	}
}
