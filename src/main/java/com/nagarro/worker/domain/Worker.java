package com.nagarro.worker.domain;

import com.nagarro.worker.enums.ServiceCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Worker {
	private Integer id;

	private String name;

	private String email;

	private String city;
	
	private ServiceCategory serviceCategory;
}