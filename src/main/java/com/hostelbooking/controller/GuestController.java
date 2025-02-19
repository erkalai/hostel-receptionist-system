package com.hostelbooking.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hostelbooking.component.ExcelHelper;
import com.hostelbooking.entity.PreBookedGuest;
import com.hostelbooking.entity.User;
import com.hostelbooking.repository.PreBookedGuestRepository;
import com.hostelbooking.service.GuestService;
import com.hostelbooking.service.UserService;

import org.apache.poi.ss.usermodel.*;



@Controller
@RequestMapping("/guests")
public class GuestController {

	private final GuestService guestService;

	private PreBookedGuestRepository preBookedGuestRepository;

	private final UserService userService;
	@Autowired
	public GuestController(GuestService guestService, PreBookedGuestRepository preBookedGuestRepository, UserService userService) {
		this.guestService = guestService;
		this.userService = userService;
		this.preBookedGuestRepository = preBookedGuestRepository;
	}

	@GetMapping("/fetch")
	@ResponseBody
	public ResponseEntity<?> fetchGuestDetails(@RequestParam("kid") String kid) {
		PreBookedGuest guest = preBookedGuestRepository.findByKid(kid).orElse(null);

		if (guest == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guest not found");
		}

		Map<String, Object> response = new HashMap<>();
		response.put("name", guest.getName());
		response.put("programName", guest.getProgramName());
		response.put("idType", guest.getIdType());
		response.put("idNumber", guest.getIdNumber());
		response.put("mobileNumber", guest.getMobileNumber());
		System.out.println("*********************" + response);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/upload")
	public String uploadExcelFile(@RequestParam("file") MultipartFile file, Model model, Principal principal) {
	    User user = userService.findUserByEmail(principal.getName());
	    model.addAttribute("userName", user);
	    
	    System.out.println("Inside Upload");
	    if (!ExcelHelper.hasExcelFormat(file)) {
	        model.addAttribute("message", "Please upload an Excel file!");
	        return "bookings/preEntry";
	    }

	    try {
	        List<PreBookedGuest> guests = guestService.saveGuestsFromExcel(file);
	        model.addAttribute("message", "Guest data uploaded successfully!");
	        model.addAttribute("guests", guests);
	        return "bookings/preEntry"; // Redirect to a page displaying the uploaded guests
	    } catch (RuntimeException e) {
	        model.addAttribute("message", "Error reading Excel file: " + e.getMessage());
	        return "bookings/preEntry"; // Return to the upload page with an error message
	    }
	}


//	    // This method processes the uploaded Excel file
//	    private List<PreBookedGuest> processExcelFile(MultipartFile file) throws IOException {
//	        List<PreBookedGuest> guestList = new ArrayList<>();
//	        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
//	            Sheet sheet = workbook.getSheetAt(0);
//	            Iterator<Row> rows = sheet.iterator();
//
//	            // Skipping the header row
//	            if (rows.hasNext()) {
//	                rows.next();
//	            }
//
//	            while (rows.hasNext()) {
//	                Row currentRow = rows.next();
//	                PreBookedGuest guest = new PreBookedGuest();
//	                Iterator<Cell> cellsInRow = currentRow.iterator();
//
//	                int cellIndex = 0;
//	                while (cellsInRow.hasNext()) {
//	                    Cell currentCell = cellsInRow.next();
//
//	                    switch (cellIndex) {
//	                    case 0 -> guest.setIdNumber(currentCell.getStringCellValue());
//	                    case 1 -> guest.setIdType(currentCell.getStringCellValue());
//						case 2 -> guest.setKid(currentCell.getStringCellValue());
//						case 3 -> guest.setMobileNumber(currentCell.getStringCellValue());
//						case 4 -> guest.setName(currentCell.getStringCellValue());
//						case 5 -> guest.setProgramName(currentCell.getStringCellValue());
//						case 6 -> guest.setKid(currentCell.getStringCellValue());
//	                    
//	                    
////	                    case 0 -> guest.setIdType(currentCell.getStringCellValue());
////						case 1 -> guest.setKid(currentCell.getStringCellValue());
////						case 3 -> guest.setMobileNumber(currentCell.getStringCellValue());
////						case 4 -> guest.setName(currentCell.getStringCellValue());
////						case 5 -> guest.setProgramName(currentCell.getStringCellValue());
////						case 6 -> guest.setKid(currentCell.getStringCellValue());
//	                        default -> {}
//	                    }
//	                    cellIndex++;
//	                }
//	                guestList.add(guest);
//	            }
//	        }
//	        return guestList;
//	    }

	    private String getCellValueAsString(Cell cell) {
	        if (cell == null) {
	            return "";
	        }

	        DataFormatter dataFormatter = new DataFormatter();
	        return dataFormatter.formatCellValue(cell).trim();
	    }
}