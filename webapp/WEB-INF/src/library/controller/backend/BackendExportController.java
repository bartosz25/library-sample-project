package library.controller.backend;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import library.annotation.LocaleLang;
import library.controller.MainController;
import library.model.entity.Borrowing;
import library.model.entity.Lang;
import library.model.entity.Subscriber;
import library.service.BorrowingService;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TODOS : 
 * - faire plus dynamique (sans les chiffres en dur)
 * - rajoutes des styles, fonctionnalités en plus
 */
@Controller
@RequestMapping(value = "/backend/export")
public class BackendExportController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendExportController.class);
    private final String TYPE_EXCEL = "excel";
    private final String SOURCE_BORROWINGS = "borrowings";
    @Autowired
    private BorrowingService borrowingService;
    @Autowired
    private MessageSource messageSource;

// Good tutorial found here http://krams915.blogspot.fr/2011/02/spring-3-apache-poi-hibernate-creating.html
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'EXPORT_DO')")
    @RequestMapping(value = "/{type}/{source}", method = RequestMethod.GET)
    public void doExport(@PathVariable String type, @PathVariable String source, @LocaleLang Lang lang, 
    HttpServletResponse response, HttpServletRequest request) {
        if (type.equals(TYPE_EXCEL)) {
            String fileName = "SalesReport.xls";
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            // Make sure to set the correct content type
            response.setContentType("application/vnd.ms-excel");

            if (source.equals(SOURCE_BORROWINGS)) {
                Map<Long, Map<String, Object>> borrowings = borrowingService.getReportByLangId(lang.getId());
                logger.info("=> Borrowings : " + borrowings);
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet worksheet = workbook.createSheet(messageSource.getMessage("borrowing.excel.title", null, Locale.getDefault()));
                worksheet.setColumnWidth(0, 2500);
                worksheet.setColumnWidth(1, 7500);
                worksheet.setColumnWidth(2, 10000);
                
                Font fontTitle = worksheet.getWorkbook().createFont();
                fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
                fontTitle.setFontHeightInPoints((short) 16);
                fontTitle.setUnderline(Font.U_SINGLE);
                HSSFCellStyle cellStyleTitle = worksheet.getWorkbook().createCellStyle();
                cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyleTitle.setWrapText(true);
                cellStyleTitle.setFont(fontTitle);
                
                Font fontBody = worksheet.getWorkbook().createFont();
                fontBody.setFontHeightInPoints((short) 14);
                
                short rowsCounter = 0;
                
                HSSFRow rowTitle = worksheet.createRow(rowsCounter);
                rowsCounter++;
                rowTitle.setHeight((short) 700);
                HSSFCell cellTitle = rowTitle.createCell(0);
                cellTitle.setCellValue(messageSource.getMessage("borrowing.excel.title", null, LocaleContextHolder.getLocale()));
                cellTitle.setCellStyle(cellStyleTitle);
                // merge rows and cells : 0, 1 - rows 0 to 1, 0, 2 - cells 0 to 2
		        worksheet.addMergedRegion(new CellRangeAddress(0,1,0,2));
                rowsCounter++;
                
                HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
                headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
                headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
                headerCellStyle.setWrapText(true);
                headerCellStyle.setFont(fontTitle);
                headerCellStyle.setBorderBottom(CellStyle.BORDER_DASHED);
                headerCellStyle.setBottomBorderColor(HSSFColor.GREY_80_PERCENT.index);
                
                HSSFRow rowHeader = worksheet.createRow(rowsCounter);
                rowHeader.setHeight((short) 500);
                
                HSSFCell cell1 = rowHeader.createCell(0);
                cell1.setCellValue(messageSource.getMessage("borrowing.excel.id", null, LocaleContextHolder.getLocale()));
                cell1.setCellStyle(headerCellStyle);

                HSSFCell cell2 = rowHeader.createCell(1);
                cell2.setCellValue(messageSource.getMessage("borrowing.excel.book", null, LocaleContextHolder.getLocale()));
                cell2.setCellStyle(headerCellStyle);

                HSSFCell cell3 = rowHeader.createCell(2);
                cell3.setCellValue(messageSource.getMessage("borrowing.excel.subscriber", null, LocaleContextHolder.getLocale()));
                cell3.setCellStyle(headerCellStyle);
                rowsCounter++;
                for (Map.Entry<Long, Map<String, Object>> entry : borrowings.entrySet()) {
                    Map<String, Object> mapEntry = (Map<String, Object>)entry.getValue();
                    HSSFRow entryRow = worksheet.createRow(rowsCounter);
                    HSSFCell cellId = entryRow.createCell(0);
                    HSSFCell cellBook = entryRow.createCell(1);
                    HSSFCell cellSubscriber = entryRow.createCell(2);
                    
                    Map<String, String> translations = (Map<String, String>)mapEntry.get("translations");
                    logger.info("Got translations " + translations);
                    cellId.setCellValue(((Borrowing)mapEntry.get("borrowing")).getId());
                    cellBook.setCellValue(translations.get("titl"));
                    cellSubscriber.setCellValue(((Subscriber)mapEntry.get("subscriber")).getLogin());
                    
                    rowsCounter++;
                }
                ServletOutputStream outputStream = null;
                try {
                    // Retrieve the output stream
                    outputStream = response.getOutputStream();
                    // Write to the output stream
                    worksheet.getWorkbook().write(outputStream);
                    // Flush the stream
                    // outputStream.flush();
                } catch (Exception e) {
                    logger.error("Unable to write report to the output stream", e);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.flush();
                            outputStream.close();
                        } catch (Exception ex) {
                            logger.error("An error occured on flushing or closing outputStream", ex);
                        }
                    }
                }
            }
        }
    }
}