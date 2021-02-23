package com.nagarro.worker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.worker.domain.Worker;
import com.nagarro.worker.service.WorkerService;

@RestController
@RequestMapping("/workers")
public class WorkerController {
	
	@Autowired
	WorkerService workerService;

	@GetMapping
	public List<Worker> getAllWorkers(){
		return workerService.getAllWorkers();
	}
}
