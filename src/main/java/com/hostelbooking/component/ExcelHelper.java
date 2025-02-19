package com.hostelbooking.component;

import com.hostelbooking.entity.PreBookedGuest;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<PreBookedGuest> excelToGuests(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<PreBookedGuest> guestList = new ArrayList<>();
            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) { // Skip header row
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                PreBookedGuest guest = new PreBookedGuest();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    String cellValue = getCellValueAsString(currentCell);

                    System.out.println("Cell Index: " + cellIndex + " Cell Value: " + cellValue);

                    switch (cellIndex) {
                        case 0 -> guest.setIdNumber(cellValue.isEmpty() ? null : cellValue);
                        case 1 -> guest.setIdType(cellValue.isEmpty() ? null : cellValue);
                        case 2 -> guest.setKid(cellValue.isEmpty() ? null : cellValue);
                        case 3 -> guest.setMobileNumber(cellValue.isEmpty() ? null : cellValue);
                        case 4 -> guest.setName(cellValue.isEmpty() ? null : cellValue);
                        case 5 -> guest.setProgramName(cellValue.isEmpty() ? null : cellValue);
                        case 6 -> guest.setStatus("NOT_PRECENT");
                        default -> {}
                    }

                    cellIndex++;
                }

                System.out.println("Added Guest: " + guest);

                if (guestList.stream().noneMatch(g -> g.getIdNumber().equals(guest.getIdNumber()))) {
                	
                    guestList.add(guest);
                    

                    System.out.println("Added Guest: " + guestList);
                } else {
                    System.out.println("Duplicate guest found with ID: " + guest.getIdNumber());
                }
            }

            return guestList;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell).trim();
    }
}