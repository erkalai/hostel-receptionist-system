//package com.hostelbooking.service;
//
//import java.io.ByteArrayOutputStream;
//import java.time.LocalDate;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.hostelbooking.entity.Booking;
//import com.hostelbooking.enums.GuestStatus;
//import com.hostelbooking.repository.BookingRepository;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
//
//
//@Service
//public class ReportService {
//
//	 @Autowired
//	    private BookingRepository bookingRepository;
//
//	 public List<Booking> getReportByDay(LocalDate day) {
//	        return bookingRepository.findByCheckInDate(day);
//	    }
//
//	    public List<Booking> getReportByMonth(int month, int year) {
//	        LocalDate startOfMonth = LocalDate.of(year, month, 1);
//	        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
//	        return bookingRepository.findByCheckInDateBetween(startOfMonth, endOfMonth);
//	    }
//
//	    public List<Booking> getReportByYear(int year) {
//	        LocalDate startOfYear = LocalDate.of(year, 1, 1);
//	        LocalDate endOfYear = LocalDate.of(year, 12, 31);
//	        return bookingRepository.findByCheckInDateBetween(startOfYear, endOfYear);
//	    }
//
//	    public List<Booking> getReportByDateRange(LocalDate startDate, LocalDate endDate) {
//	        return bookingRepository.findByCheckInDateBetween(startDate, endDate);
//	    }
//
//	    public List<Booking> getReportByStatus(GuestStatus status) {
//	        return bookingRepository.findByStatus(status);
//	    }
//
//	    public List<Booking> getReportByDayAndStatus(LocalDate day, GuestStatus status) {
//	        return bookingRepository.findByCheckInDateAndStatus(day, status);
//	    }
//
//	    public List<Booking> getReportByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, GuestStatus status) {
//	        return bookingRepository.findByCheckInDateBetweenAndStatus(startDate, endDate, status);
//	    }
//
//	    public void generatePdfReport(List<Booking> bookings, String filePath) {
//	        // Implement PDF generation logic using a library like iText or Apache PDFBox.
//	    }
//
//
//}
