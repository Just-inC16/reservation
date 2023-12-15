package com.tcs.reservation;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.reservation.Dto.HotelManagement;
import com.tcs.reservation.Dto.Payment;
import com.tcs.reservation.feign.HotelManagementClient;
import com.tcs.reservation.feign.PaymentClient;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

	private ReservationRepository reservationRepository;
	private HotelManagementClient hotelManagementClient;
	private PaymentClient paymentClient;

	public ReservationController(ReservationRepository reservationRepository,
			HotelManagementClient hotelManagementClient, PaymentClient paymentClient) {
		this.reservationRepository = reservationRepository;
		this.hotelManagementClient = hotelManagementClient;
		this.paymentClient = paymentClient;
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
	public ResponseEntity<Payment> reserveHotel(@RequestBody Reservation reservation) {
		Long hotelId = reservation.getHotelId();
		Long customerId = reservation.getCustomerId();
		HotelManagement isHotelPresent = hotelManagementClient.isHotelIdPresent(hotelId).getBody();
		// Check if hotel room is present
		if (isHotelPresent != null) {
			ResponseEntity<BigDecimal> hotelRoomAmountResponseEntity = hotelManagementClient.bookHotelRoom(hotelId);
			if (hotelRoomAmountResponseEntity.getStatusCode() == HttpStatus.OK) {
				BigDecimal hotelRoomAmount = hotelRoomAmountResponseEntity.getBody();
				Payment payment = new Payment(customerId, hotelRoomAmount);
				System.out.println("Payment****************" + payment);
				return paymentClient.makePayment(payment);
			} else {
				return ResponseEntity.status(409).build();
			}
		} else {
			return ResponseEntity.status(409).build();
		}

	}

	@PostMapping("/sendPayment")
	public ResponseEntity<Payment> sendPayment(@RequestBody Payment payment) {
		return paymentClient.makePayment(payment);
	}
}