package com.nagarro.worker.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.worker.domain.WorkerEntity;
import com.nagarro.worker.dto.ResponseTO;
import com.nagarro.worker.service.WorkerService;

@RestController
@RequestMapping("/workers")
public class WorkerController {

	@Autowired
	WorkerService workerService;

	@GetMapping
	public List<WorkerEntity> getAllWorkers() {
		return workerService.getAllWorkers();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseTO<WorkerEntity>> getWorkerById(@PathVariable @Valid Integer id) throws Exception {
		ResponseTO<WorkerEntity> response = new ResponseTO<>();
		response.setData(workerService.getWorkerById(id));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/acceptBooking/{bookingId}")
	public ResponseEntity<ResponseTO<String>> acceptBooking(@PathVariable String bookingId) throws Exception {
		ResponseTO<String> response = new ResponseTO<>();
		workerService.acceptBooking(bookingId);
		response.setData("service accepted by worker");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/rejectBooking/{bookingId}")
	public ResponseEntity<ResponseTO<String>> rejectBooking(@PathVariable String bookingId) throws Exception {
		ResponseTO<String> response = new ResponseTO<>();
		workerService.rejectBooking(bookingId);
		response.setData("service rejected by worker");
		return ResponseEntity.ok(response);
	}
}
