package com.example.service.jasper_service;

import com.example.enums.ExportFormat;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.export.ExporterOutput;

import java.io.OutputStream;

public interface ReportExporter {

    JRAbstractExporter getExporterByFormat(ExportFormat exportFormat);

    ExporterOutput getExporterOutPut(OutputStream outputStream, ExportFormat exportFormat);
}
