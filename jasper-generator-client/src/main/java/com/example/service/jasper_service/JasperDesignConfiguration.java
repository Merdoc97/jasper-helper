package com.example.service.jasper_service;

import com.example.enums.ExportFormat;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.export.ReportExportConfiguration;

import java.util.Map;

/**
 * design configuration interface , useful for advanced configuration of table veiw
 */
public interface JasperDesignConfiguration {

    JasperDesign createDesign(Map<String, String> columnsNames, ExportFormat exportFormat) throws JRException;

    ReportExportConfiguration getExportConfiguration(ExportFormat exportFormat);
}
