package com.tcs.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

	private ReservationRepository reservationRepository;
	private RestTemplate restTemplate;

	public ReservationController(ReservationRepository reservationRepository, RestTemplate restTemplate) {
		this.reservationRepository = reservationRepository;
		this.restTemplate = restTemplate;
	}

	@PostMapping
	public ResponseEntity<?> makeReservation(@RequestBody Reservation reservation) {
		return ResponseEntity.ok(reservationRepository.save(reservation));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getReservationById(@PathVariable Long id) {
		Reservation reservationById = reservationRepository.getReferenceById(id);
		Reservation reservationDto = new Reservation(reservationById.getId(), reservationById.getCustomerId(),
				reservationById.getHotelId(), reservationById.getStartDate(), reservationById.getEndDate());
		return ResponseEntity.ok(reservationDto);
	}

	@PostMapping("/reserveHotel")
	public String reserveHotel(@RequestBody Reservation reservation) {
		return restTemplate.getForObject("http://localhost:8082/api/v1/hotelManagements/" + reservation.getHotelId(),
				String.class);
	}
}