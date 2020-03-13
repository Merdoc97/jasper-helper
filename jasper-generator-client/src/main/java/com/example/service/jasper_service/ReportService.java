package com.example.service.jasper_service;

import com.example.enums.BasicExportExportFormat;
import com.example.enums.ExportFormat;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * current abstraction allows create any reports with/without jrxml files.
 * It simple to use and can't be useful in a lot of cases.
 * Current abstraction not support batch printing
 */
public interface ReportService {

    /**
     * current method useful for reports which contains jrxml files
     *
     * @param reportStream - stream for jrxml file
     * @param outputStream - stream for generated report
     * @param data         - report data which can be any implementation of {@link JRRewindableDataSource}
     * @param exportFormat - supported pdf, docx, xls, xlsx and html see {@link BasicExportExportFormat}
     */
    void generateReport(InputStream reportStream, OutputStream outputStream,
                        JRRewindableDataSource data, ExportFormat exportFormat);

    /**
     * current method allows print any tables and doesn't need jrxml file
     * @param columnsHeaders - map which contains name of table headers, for print using only values
     * @param data           - @see {@link #generateReport(Map, JRRewindableDataSource, OutputStream, ExportFormat, String)} explanation
     * @param outputStream   - @see {@link #generateReport(Map, JRRewindableDataSource, OutputStream, ExportFormat, String)} explanation
     * @param exportFormat   - @see {@link #generateReport(Map, JRRewindableDataSource, OutputStream, ExportFormat, String)} explanation
     * @param configName - name of custom implementation of design for tables, it use default configuration which present by default,
     *                   also for more details @see {@link #addConfiguration(String, JasperDesignConfiguration)}
     */
    void generateReport(Map<String, String> columnsHeaders,
                        JRRewindableDataSource data,
                        OutputStream outputStream,
                        ExportFormat exportFormat,
                        String configName);

    /**
     * current method will be useful only for {@link #generateReport(Map, JRRewindableDataSource, OutputStream, ExportFormat, String)} method
     * @param keyName  - key for {@link JasperDesignConfiguration} which can be used in {@link #generateReport(Map, JRRewindableDataSource, OutputStream, ExportFormat, String)} method
     * @param configuration - additional design configuration for printing
     */
    void addConfiguration(String keyName, JasperDesignConfiguration configuration);

}
