package com.tcs.reservation.Dto;

import lombok.Data;

@Data
public class HotelManagement {
	private Long id;
	private String name;
	private Integer roomNumber;
	private Status status;

}
