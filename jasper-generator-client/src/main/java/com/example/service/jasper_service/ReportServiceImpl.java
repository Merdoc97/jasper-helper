package com.example.service.jasper_service;

import com.example.enums.ExportFormat;
import com.example.exceptions.JasperServiceException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;


/**
 * .
 */
@Slf4j
public class ReportServiceImpl implements ReportService, InitializingBean {

    private final Map<String, JasperDesignConfiguration> jasperDesignConfigurationMap;
    private ReportExporter reportExporter;

    public ReportServiceImpl() {
        this.jasperDesignConfigurationMap = new HashMap<>();
        this.reportExporter = new ReportExporterImpl();
        jasperDesignConfigurationMap.put("default", new GeneralJasperDesignConfigurationImpl());
    }

    public ReportServiceImpl(Map<String, JasperDesignConfiguration> jasperDesignConfigurationMap,
                             ReportExporter reportExporter) {
        this.jasperDesignConfigurationMap = jasperDesignConfigurationMap;
        this.reportExporter = reportExporter;
    }

    @Override
    public void generateReport(InputStream reportStream, OutputStream outputStream,
                               JRRewindableDataSource data, ExportFormat format) {
        try {
            // compile the report from the stream
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            // fill out the report into a print object, ready for export.
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), data);
            //print report
            printValues(outputStream, print, format, null);
        } catch (Exception e) {
            log.error("error in creating report: ", e);
            throw new JasperServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void generateReport(Map<String, String> columnsHeaders,
                               JRRewindableDataSource data,
                               OutputStream outputStream,
                               ExportFormat exportFormat,
                               String configurationKeyName) {
        try {
            String configName = isNull(configurationKeyName) || configurationKeyName.isEmpty() ? "default" : configurationKeyName;
            //create design "template" for report
            JasperDesign jasperDesign = jasperDesignConfigurationMap.get(configName).createDesign(columnsHeaders, exportFormat);
            // compile the report from the stream
            JasperReport report = JasperCompileManager.compileReport(jasperDesign);
            //create print
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, new HashMap<>(), data);
            printValues(outputStream, jasperPrint, exportFormat, jasperDesignConfigurationMap.get(configName).getExportConfiguration(exportFormat));

        } catch (Exception e) {
            log.error("error in creating report: ", e);
            throw new JasperServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void addConfiguration(String keyName, JasperDesignConfiguration configuration) {
        jasperDesignConfigurationMap.put(keyName, configuration);
    }

    private void printValues(OutputStream outputStream, JasperPrint jasperPrint,
                             ExportFormat format,
                             ReportExportConfiguration configuration) throws JRException {
        //print report to outputStream
        JRAbstractExporter exporter = reportExporter.getExporterByFormat(format);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(reportExporter.getExporterOutPut(outputStream, format));
        exporter.setConfiguration(configuration);
        exporter.exportReport();

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notEmpty(jasperDesignConfigurationMap, "design configuration map can't be empty");
        Assert.notNull(reportExporter, "report exporter can't be null");
    }
}
