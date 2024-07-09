package kr.co.neighbor21.neighborApi.common.util.file.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 파일 다운로드 Interface
 *
 * @author GEONLEE
 * @since 2024-07-05
 */
public interface FileDownloader {

    Logger LOGGER = LoggerFactory.getLogger(FileDownloader.class);

    void download();

    default void setResponseHeader(HttpServletResponse httpServletResponse, String fileName, String extension) {
        StringBuilder encodedFileName = new StringBuilder();
        if (StringUtils.isEmpty(fileName)) {
            LOGGER.info("There is no file name, so it is initialized to 'Excel'.");
            fileName = "Excel";
        }
        encodedFileName.append(UriUtils.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
        encodedFileName.append("_").append(getNowString());
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName + extension);
        httpServletResponse.setHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream;charset=UTF-8");
    }

    /**
     * 현재 날짜 String return
     *
     * @author GEONLEE
     * @since 2024-06-03
     */
    default String getNowString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
