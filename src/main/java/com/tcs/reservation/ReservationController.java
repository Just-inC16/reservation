package com.tcs.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.reservation.Dto.HotelManagement;
import com.tcs.reservation.feign.HotelManagementClient;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

	private ReservationRepository reservationRepository;
	private HotelManagementClient hotelManagementClient;

	public ReservationController(ReservationRepository reservationRepository,
			HotelManagementClient hotelManagementClient) {
		this.reservationRepository = reservationRepository;
		this.hotelManagementClient = hotelManagementClient;
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
	public ResponseEntity<HotelManagement> reserveHotel(@RequestBody Reservation reservation) {
		Long hotelId = reservation.getHotelId();
		return hotelManagementClient.isHotelIdPresent(hotelId);
	}
}