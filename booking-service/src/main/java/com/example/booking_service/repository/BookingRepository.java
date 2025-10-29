package com.example.booking_service.repository;

import com.example.booking_service.entity.Booking;
import com.example.booking_service.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByRoomIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long roomId, BookingStatus status, LocalDate endDate, LocalDate startDate);
}
