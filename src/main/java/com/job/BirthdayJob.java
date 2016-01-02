package com.job;

import com.utils.TwilloService;
import com.utils.Utils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.InputStream;
import java.util.TimeZone;


public class BirthdayJob {
    public static final String EXCEL_FILE = "Big Family Relatives.xlsx";
    public static final String MY_TIMEZONE = "America/Los_Angeles";

    @Scheduled(cron = "0 0 8-10 * * *")
    public void processJob() {
        XSSFWorkbook workbook = processRecords(EXCEL_FILE);
        if (workbook != null) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            //iterate rows
            for (Row row : sheet) {
                String name = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null;
                String birthDateString = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : null;
                String type = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null;
                String relation = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null;
                String targetTimeZone = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : null;

                LocalDateTime localTime = LocalDateTime.now();
                LocalDateTime tmp = Utils.convertTimeZone(localTime, TimeZone.getDefault().getID(), MY_TIMEZONE);
                LocalDateTime conversionDate = Utils.convertTimeZone(tmp, MY_TIMEZONE, targetTimeZone);
                conversionDate = conversionDate.plusHours(5);

                if (birthDateString != null) {
                    String[] dateSplit = birthDateString.split("-");
                    LocalDateTime remoteTime = new LocalDateTime(localTime.getYear(),
                            Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), 0, 0);

                    if (conversionDate.compareTo(remoteTime) == 1 || conversionDate.compareTo(remoteTime) == 0) {
                        if (type != null) {
                            StringBuilder message = new StringBuilder();
                            message.append(type);
                            message.append(" for ");
                            message.append(name);
                            message.append(" on ");
                            message.append(birthDateString);
                            message.append(" in MM-YY format ");

                            if (relation != null) {
                                message.append("related by ");
                                message.append(relation);
                            }

                            // Send SMS message
                            TwilloService.sendMessage("+1", "4083071310", message.toString());
                        }
                    }
                }

            }
        }
    }

    private XSSFWorkbook processRecords(String fileName) {
        if (fileName != null && fileName.trim().length() > 0) {
            try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
                if (inputStream != null) {
                    return new XSSFWorkbook(inputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
