package com.nagarro.worker.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.nagarro.worker.domain.Worker;
import com.nagarro.worker.enums.ServiceCategory;
import com.nagarro.worker.service.WorkerService;

@Service
public class WorkerServiceImpl implements WorkerService {
	private List<Worker> workersList = new ArrayList<>();

	/**
	 * populates data into workersList after object is created.
	 */
	@PostConstruct
	public void populateData() {
		Worker worker1 = new Worker();
		worker1.setId(1);
		worker1.setName("Ramesh");
		worker1.setEmail("ramesh@uc.com");
		worker1.setServiceCategory(ServiceCategory.SALON_FOR_MEN);
		worker1.setCity("Gurugram");
		Worker worker2 = new Worker();
		worker2.setId(2);
		worker2.setName("Suresh");
		worker2.setEmail("suresh@uc.com");
		worker2.setCity("Noida");
		worker2.setServiceCategory(ServiceCategory.APPLIANCE_REPAIR);
		Worker worker3 = new Worker();
		worker3.setId(3);
		worker3.setName("Kamlesh");
		worker3.setEmail("kamlesh@uc.com");
		worker3.setCity("Delhi");
		worker3.setServiceCategory(ServiceCategory.PLUMBERS);
		workersList.add(worker1);
		workersList.add(worker2);
		workersList.add(worker3);
	}

	@Override
	public List<Worker> getAllWorkers() {
		return workersList;
	}

}
