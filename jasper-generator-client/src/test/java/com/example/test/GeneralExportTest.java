package com.example.test;

import com.example.enums.BasicExportExportFormat;
import com.example.service.jasper_service.ReportService;
import com.example.service.jasper_service.ReportServiceImpl;
import com.example.test.dto.DtoHelper;
import com.example.test.dto.ReportWrapper;
import com.example.util.ObjectHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralExportTest {
    private ReportService reportService = new ReportServiceImpl();

    @Test
    public void testGenerateViaMap() throws IOException {
        File fileTo = new File("test_export.pdf");
        fileTo.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
        // generate report main method
        reportService.generateReport(this.getClass().getResourceAsStream("/report.test/test_report.jrxml"),
                fileOutputStream,
                new JRBeanCollectionDataSource(Arrays.asList(new ReportWrapper(DtoHelper.generateData(5, 15)))),
                BasicExportExportFormat.PDF);

        fileOutputStream.close();
    }

    @Test
    public void testGenerateToTableWithoutJrxmlReport() throws IOException {
        File fileTo = new File("test_export2.pdf");
        fileTo.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
        //generate report
        reportService.generateReport(DtoHelper.getMap(10),
                new JRBeanCollectionDataSource(DtoHelper.generateData(10, 10)),
                fileOutputStream,
                BasicExportExportFormat.PDF, null);

        reportService.generateReport(DtoHelper.getMap(10),
                new JRBeanCollectionDataSource(DtoHelper.generateData(10, 10)),
                fileOutputStream,
                BasicExportExportFormat.PDF, null);
        fileOutputStream.close();
    }

    @Test
    public void testGenerateToTableWithoutJrxmlReportXLS() throws IOException {
        File fileTo = new File("test_export2.xlsx");
        fileTo.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
        //generate report
        reportService.generateReport(DtoHelper.getMap(10),
                new JRBeanCollectionDataSource(DtoHelper.generateData(100, 10)),
                fileOutputStream,
                BasicExportExportFormat.XLSX, null);

        fileOutputStream.close();
    }

    @Test
    public void testGenerateToTableWithoutJrxmlReportHtml() throws IOException {
        File fileTo = new File("test_export2.html");
        fileTo.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
        //generate report
        reportService.generateReport(DtoHelper.getMap(50),
                new JRBeanCollectionDataSource(DtoHelper.generateData(50, 50)),
                fileOutputStream,
                BasicExportExportFormat.HTML, null);

        fileOutputStream.close();
    }

    @Test
    public void testGenerateToTableWithoutJrxmlReportDoc() throws IOException {
        File fileTo = new File("test_export2.docx");
        fileTo.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
        //generate report
        reportService.generateReport(DtoHelper.getMap(10),
                new JRBeanCollectionDataSource(DtoHelper.generateData(100, 10)),
                fileOutputStream,
                BasicExportExportFormat.DOCX, null);

        fileOutputStream.close();
    }

    @Test
    public void testUtil() {

        TestClass testClass = TestClass.builder()
                .name("test")
                .value(2)
                .build();
        Map<String, String> result = ObjectHelper.getFieldsName(testClass);
        Assertions.assertEquals(result.get("name"), "name");
        Assertions.assertEquals(result.get("value"), "value");

    }

    @Test
    public void testUtilsForValue() {
        TestClass testClass = TestClass.builder()
                .name("test2")
                .value(2)
                .build();

        List<Map<String, String>> resultList = ObjectHelper.getFieldsAsMap(Arrays.asList(testClass));
        Map<String, String> result = resultList.get(0);
        Assertions.assertEquals(result.get("name"), "test2");
        Assertions.assertEquals(result.get("value"), "2");
    }

    @Test
    public void testWithUtil() throws IOException {
        File fileTo = new File("test_export3.pdf");
        fileTo.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
        List<TestClass>testData=generateData();
        //generate report
        reportService.generateReport(ObjectHelper.getFieldsName(testData.get(0)),
                new JRBeanCollectionDataSource(ObjectHelper.getFieldsAsMap(testData)),
                fileOutputStream,
                BasicExportExportFormat.PDF, null);
    }

    @Test
    private List<TestClass> generateData() {
        return Stream.iterate(0, n -> n + 1)
                .limit(100)
                .map(integer -> {
                    return TestClass.builder()
                            .name("test2")
                            .value(2)
                            .build();
                }).collect(Collectors.toList());
    }

    @Getter
    @Setter
    @Builder
    private static class TestClass {
        private String name;
        private Integer value;

    }
}
