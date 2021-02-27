package com.nagarro.worker.service;

import java.util.List;

import com.nagarro.worker.domain.WorkerEntity;

public interface WorkerService {

	public List<WorkerEntity> getAllWorkers();

	public void acceptBooking(String bookingId);

	public void rejectBooking(String bookingId);

	public WorkerEntity getWorkerById(Integer id) throws Exception;

}
