package kr.co.neighbor21.neighborApi.common.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.TYPE;

/**
 * @description 커스텀 어노테이션으로 적용
 * @author yh.kim
 * @see java.lang.annotation.Annotation
 */
@Documented
@Target({TYPE, CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface NsExcel {
    // 저장될 파일 이름
    String saveName() default "";

    // 저장될 파일 경로
    String savePath() default "";

    // 읽기 시작한 Line
    int startLine() default 1;

    // Only Write , 데이터를 쓸 때 삭제할 Sample Row 수
    int sampleRowCnt() default 2;

    int sheetNum() default 0;

    String sheetName() default "";

    boolean isTemplateVerify() default false;

    boolean isHeaderLocking() default false;

    int lineNumColumnIndex() default -1;

}
