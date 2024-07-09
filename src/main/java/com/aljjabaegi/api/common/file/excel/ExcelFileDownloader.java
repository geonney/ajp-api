package kr.co.neighbor21.neighborApi.common.util.file.excel;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.neighbor21.neighborApi.common.excel.annotation.NsColumn;
import kr.co.neighbor21.neighborApi.common.excel.annotation.NsExcel;
import kr.co.neighbor21.neighborApi.common.exception.code.CommonErrorCode;
import kr.co.neighbor21.neighborApi.common.exception.custom.ServiceException;
import kr.co.neighbor21.neighborApi.common.util.file.excel.enumeration.ExcelDownloadType;
import kr.co.neighbor21.neighborApi.common.util.file.excel.record.DynamicHeaderRequest;
import kr.co.neighbor21.neighborApi.common.util.file.excel.record.ExcelDownloadRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel file downloader<br />
 * ExcelDownloadRequest 의 templateFileName 이 있을 경우 resources/template 로 내 .xlsx 파일을 찾아<br />
 * 데이터를 append 하고, 없을 경우 파일을 생성.<br />
 * 1. default excel download example
 * ExcelFileDownloader excelFileDownloader = new ExcelFileDownloader(httpServletResponse, ExcelDownloadRequest
 * .builder(BusStopSearchResponse.class, busStopList)
 * .fileName("전체 정류장")
 * .build());
 * excelFileDownloader.download();
 * <p>
 * 2. template file excel download example
 * ExcelFileDownloader excelFileDownloader = new ExcelFileDownloader(httpServletResponse, ExcelDownloadRequest
 * .builder(BusStopSearchResponse.class, busStopList)
 * .fileName("전체 정류장")
 * .templateFileName("busStopDetail")
 * .build());
 * excelFileDownloader.download();
 * <p>
 * 3. dynamic header
 * ExcelFileDownloader excelFileDownloader = new ExcelFileDownloader(httpServletResponse, ExcelDownloadRequest
 * .builder(NodeSearchResponse.class, nodeList)
 * .fileName("전체 노드")
 * .dynamicHeader(List.of(
 * DynamicHeaderRequest.builder("노드 ID", "nodeId")
 * .build(),
 * DynamicHeaderRequest.builder("노드 명", "nodeName")
 * .cellWidth(400)
 * .build())
 * )
 * .build());
 * excelFileDownloader.download();
 *
 * @author GEONLEE
 * @since 2024-07-05
 */
public class ExcelFileDownloader implements FileDownloader {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelFileDownloader.class);
    // excel file extension
    private final String extension = ".xlsx";
    // response
    private final HttpServletResponse httpServletResponse;
    // file name -> sheet 를 추가하는 경우 변경의 소지가 있어, 생성자에서만 세팅
    private final String fileName;
    // download excel parameter
    private ExcelDownloadRequest excelDownloadRequest;
    // excel workbook
    private Workbook workbook = new SXSSFWorkbook();
    // current sheet -> sheet 추가 시 변경 됨
    private Sheet sheet;
    // default header style
    private CellStyle headerStyle = getHeaderStyle();
    // excel download type -> NORMAL(@NsExcel, @NsColumn 을 사용한 다운로드), TEMPLATE(템플릿 파일 사용), DYNAMIC_HEADER(동적 헤더)
    private ExcelDownloadType excelDownloadType = ExcelDownloadType.NORMAL;

    public ExcelFileDownloader(HttpServletResponse httpServletResponse, ExcelDownloadRequest excelDownloadRequest) {
        this.httpServletResponse = httpServletResponse;
        this.excelDownloadRequest = excelDownloadRequest;
        this.fileName = (StringUtils.isEmpty(excelDownloadRequest.fileName())) ? "" : excelDownloadRequest.fileName();
        initialSetting();
    }

    /**
     * 다운르도 타입에 따라 로직이 변경되도록 초기 세팅을 한다.
     *
     * @author GEONLEE
     * @since 2024-07-08
     */
    private void initialSetting() {
        // excel download type setting. normal, template, dynamic header
        if (StringUtils.isNotBlank(this.excelDownloadRequest.templateFileName())) {
            this.excelDownloadType = ExcelDownloadType.TEMPLATE;
        } else if (ObjectUtils.isNotEmpty(this.excelDownloadRequest.dynamicHeader())) {
            this.excelDownloadType = ExcelDownloadType.DYNAMIC_HEADER;
        }
        createSheet();
    }

    /**
     * workbook 에 sheet 를 생성한다.
     * download type 에 따라 로직이 분기 된다.
     *
     * @author GEONLEE
     * @since 2024-07-09
     */
    private void createSheet() {
        switch (this.excelDownloadType) {
            case NORMAL -> {
                if (ObjectUtils.isEmpty(this.sheet)) {
                    String sheetName = getSheetNameFromRecord();
                    this.sheet = this.workbook.createSheet(sheetName);
                }
                appendHeader();
                appendNumberingBody();
            }
            case TEMPLATE -> {
                loadTemplateFile();
                this.sheet = this.workbook.getSheetAt(0);
                appendBody();
            }
            case DYNAMIC_HEADER -> {
                String sheetName = this.excelDownloadRequest.fileName();
                this.sheet = this.workbook.createSheet(sheetName);
                appendDynamicHeader();
                appendDynamicBody();
            }
        }
    }

    /**
     * NORMAL type 의 header 를 추가한다.
     *
     * @author GEONLEE
     * @since 2024-07-08
     */
    private void appendHeader() {
        Row headerRow = this.sheet.createRow(0);
        Class<?> record = this.excelDownloadRequest.recordType();
        List<NsColumn> columnList = Arrays.stream(record.getDeclaredFields())
                .filter(field -> field.getAnnotation(NsColumn.class) != null)
                .sorted(Comparator.comparingInt(x -> x.getAnnotation(NsColumn.class).index()))
                .map(field -> field.getAnnotation(NsColumn.class))
                .toList();
        if (ObjectUtils.isEmpty(columnList)) {
            throw new ServiceException(CommonErrorCode.LOGIC_ERROR, "No @NsColumn for excel export. -> " + record.getSimpleName());
        }
        Cell rowNumberCell = headerRow.createCell(0);
        rowNumberCell.setCellValue("No");
        rowNumberCell.setCellStyle(this.headerStyle);
        for (NsColumn column : columnList) {
            Cell headerCell = headerRow.createCell(column.index());
            headerCell.setCellValue(column.name());
            headerCell.setCellStyle(this.headerStyle);
            this.sheet.setColumnWidth(column.index(), 15 * column.cellWidth());
        }
    }

    /**
     * NORMAL type 의 data body 를 추가한다.
     *
     * @author GEONLEE
     * @since 2024-07-08
     */
    private void appendBody() {
        List<String> columnNames = getOrderedColumnList();
        List<Map<String, Object>> list = convertToMapList(this.excelDownloadRequest.data());
        int dataRowIndex = sheet.getLastRowNum() + 1;
        for (Map<String, Object> data : list) {
            Row row = this.sheet.createRow(dataRowIndex++);
            for (int i = 0, n = columnNames.size(); i < n; i++) {
                Object value = data.get(columnNames.get(i));
                row.createCell(i).setCellValue((Objects.isNull(value) ? "" : String.valueOf(value)));
            }
        }
    }

    /**
     * NORMAL type 의 data body 를 추가한다. 가장 좌측 numbering 추가
     *
     * @author GEONLEE
     * @since 2024-07-08
     */
    private void appendNumberingBody() {
        List<String> columnNames = getOrderedColumnList();
        List<Map<String, Object>> list = convertToMapList(this.excelDownloadRequest.data());
        int dataRowIndex = sheet.getLastRowNum() + 1;
        int numbering = 1;
        for (Map<String, Object> data : list) {
            Row row = this.sheet.createRow(dataRowIndex++);
            row.createCell(0).setCellValue(numbering++);
            for (int i = 1, n = columnNames.size() + 1; i < n; i++) {
                Object value = data.get(columnNames.get(i - 1));
                row.createCell(i).setCellValue((Objects.isNull(value) ? "" : String.valueOf(value)));
            }
        }
    }

    /**
     * DYNAMIC_HEADER type 의 header 를 추가한다.
     *
     * @author GEONLEE
     * @since 2024-07-08
     */
    private void appendDynamicHeader() {
        Row headerRow = this.sheet.createRow(0);
        Cell rowNumberCell = headerRow.createCell(0);
        rowNumberCell.setCellValue("No");
        rowNumberCell.setCellStyle(this.headerStyle);
        for (int i = 0, n = this.excelDownloadRequest.dynamicHeader().size(); i < n; i++) {
            DynamicHeaderRequest dynamicHeaderRequest = this.excelDownloadRequest.dynamicHeader().get(i);
            Cell headerCell = headerRow.createCell(i + 1);
            headerCell.setCellValue(dynamicHeaderRequest.headerName());
            headerCell.setCellStyle(this.headerStyle);
            this.sheet.setColumnWidth(i + 1,
                    15 * ((dynamicHeaderRequest.cellWidth()) == 0 ? 200 : dynamicHeaderRequest.cellWidth()));
        }
    }

    /**
     * DYNAMIC_HEADER type 의 body data 를 추가한다.<br />
     * 이 type 의 경우 header 와 body data 가 모두 list map 의 형태여야 한다.
     *
     * @author GEONLEE
     * @since 2024-07-08
     */
    @SuppressWarnings("unchecked")
    private void appendDynamicBody() {
        List<Map<String, Object>> list;
        try {
            list = (List<Map<String, Object>>) this.excelDownloadRequest.data();
        } catch (ClassCastException e) {
            throw new ServiceException(CommonErrorCode.LOGIC_ERROR, "In case of dynamic header, the data type must be List<Map> type.");
        }
        List<DynamicHeaderRequest> headerList = excelDownloadRequest.dynamicHeader();
        int dataRowIndex = sheet.getLastRowNum() + 1;
        int numbering = 1;
        for (Map<String, Object> data : list) {
            Row row = sheet.createRow(dataRowIndex++);
            row.createCell(0).setCellValue(numbering++);
            for (int i = 1, n = headerList.size() + 1; i < n; i++) {
                Object value = data.get(headerList.get(i - 1).columnName());
                row.createCell(i).setCellValue((Objects.isNull(value) ? "" : String.valueOf(value)));
            }
        }
    }

    /**
     * record type 에서 NsColumn 을 추출 해 index 로 정렬 후 name list 를 리턴한다.
     *
     * @return ordered column name list
     * @author GEONLEE
     * @since 2024-07-09
     */
    private List<String> getOrderedColumnList() {
        Class<?> record = this.excelDownloadRequest.recordType();
        return Arrays.stream(record.getDeclaredFields())
                .filter(field -> field.getAnnotation(NsColumn.class) != null)
                .sorted(Comparator.comparingInt(x -> x.getAnnotation(NsColumn.class).index()))
                .map(Field::getName)
                .toList();
    }

    /**
     * record @NsExcel 로 부터 sheet name 을 추출한다.<br />
     * 설정이 안되어 있을 경우 Sheet + (시트 수 +1) 로 초기화 한다.
     *
     * @author GEONLEE
     * @since 2024-07-09
     */
    private String getSheetNameFromRecord() {
        Class<?> record = this.excelDownloadRequest.recordType();
        if (ObjectUtils.isEmpty(record.getAnnotation(NsExcel.class))) {
            throw new ServiceException(CommonErrorCode.LOGIC_ERROR, "No @NsExcel for excel export at record. -> " + record.getSimpleName());
        }
        String sheetName = record.getAnnotation(NsExcel.class).sheetName();
        if (sheetName.isEmpty()) {
            sheetName = "Sheet" + (this.workbook.getNumberOfSheets() + 1);
            LOGGER.info("No sheetName at @NsExcel. Initialized sheet name to 'Sheet1'. -> " + record.getSimpleName());
        }
        return sheetName;
    }

    /**
     * 템플릿 파일을 로드 한다.
     *
     * @author GEONLEE
     * @since 2024-07-08
     */
    private void loadTemplateFile() {
        StringBuilder templateFileName = new StringBuilder()
                .append("template/")
                .append(this.excelDownloadRequest.templateFileName())
                .append(this.extension);
        try {
            InputStream templateFileInputStream = new ClassPathResource(templateFileName.toString()).getInputStream();
            this.workbook = new XSSFWorkbook(templateFileInputStream);
        } catch (IOException e) {
            throw new ServiceException(CommonErrorCode.LOGIC_ERROR, "Template file not found. -> template/"
                    + this.excelDownloadRequest.templateFileName() + this.extension);
        }
    }

    /**
     * 새로운 시트를 추가한다.<br />
     * 시트명이 중복 될 경우 Sheet + (시트 수 +1) 로 초기화 한다.
     *
     * @author GEONLEE
     * @since 2024-07-09
     */
    public void newSheet(ExcelDownloadRequest excelDownloadRequest, String sheetName) {
        this.excelDownloadRequest = excelDownloadRequest;
        if (StringUtils.isEmpty(sheetName)) {
            try {
                this.sheet = this.workbook.createSheet(getSheetNameFromRecord());
            } catch (IllegalArgumentException e) {
                LOGGER.info("Sheet name is duplicated. Modified sheet Name + (sheet index + 1)");
                this.sheet = this.workbook.createSheet(getSheetNameFromRecord() + (this.workbook.getNumberOfSheets() + 1));
            }
        } else {
            this.sheet = this.workbook.createSheet(sheetName);
        }
        initialSetting();
    }

    /**
     * 기본 Header style 을 설정한다.
     *
     * @return Cell style
     * @author GEONLEE
     * @since 2024-07-05
     */
    private CellStyle getHeaderStyle() {
        CellStyle defaultCellStyle = this.workbook.createCellStyle();
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) defaultCellStyle;
        xssfCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(20, 114, 255), new DefaultIndexedColorMap()));
        defaultCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        defaultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        defaultCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        defaultCellStyle.setBorderLeft(BorderStyle.THIN);
        defaultCellStyle.setBorderTop(BorderStyle.THIN);
        defaultCellStyle.setBorderRight(BorderStyle.THIN);
        defaultCellStyle.setBorderBottom(BorderStyle.THIN);
        Font font = this.workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        defaultCellStyle.setFont(font);
        return defaultCellStyle;
    }

    /**
     * header style 을 변경할 때 사용한다.
     *
     * @param headerStyle CellStyle
     * @author GEONLEE
     * @since 2024-07-09
     */
    public void setHeaderStyle(CellStyle headerStyle) {
        this.headerStyle = headerStyle;
    }

    /**
     * record list 를  map list 로 변환한다.
     *
     * @param list record list
     * @return map list
     * @author GEONLEE
     * @since 2024-07-06
     */
    private List<Map<String, Object>> convertToMapList(List<?> list) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Object item : list) {
            Map<String, Object> map = new HashMap<>();
            for (Field field : item.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    map.put(field.getName(), field.get(item));
                } catch (IllegalAccessException e) {
                    throw new ServiceException(CommonErrorCode.LOGIC_ERROR, "Failed to convert List<Record> to List<Map>.");
                }
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * Excel file 을 다운로드 한다.
     *
     * @author GEONLEE
     * @since 2024-07-05
     */
    @Override
    public void download() {
        LOGGER.info(ExcelDownloadType.NORMAL + " type excel download. -> " + this.fileName);
        setResponseHeader(this.httpServletResponse, this.fileName, this.extension);
        try {
            this.workbook.write(this.httpServletResponse.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new ServiceException(CommonErrorCode.LOGIC_ERROR, "Failed to download excel file.");
        }
    }
}
