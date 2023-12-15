package com.tcs.reservation.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.tcs.reservation.Dto.HotelManagement;

@Component
@FeignClient(name = "hotelManagement", url = "localhost:8082")
public interface HotelManagementClient {
	@GetMapping("/api/v1/hotelManagements/{id}")
	public ResponseEntity<HotelManagement> isHotelIdPresent(@PathVariable Long id);

	@PutMapping("/api/v1/hotelManagements/book/{id}")
	public ResponseEntity<String> bookHotelRoom(@PathVariable Long id);
}
