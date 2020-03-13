package com.example.service.jasper_service;

import com.example.enums.BasicExportExportFormat;
import com.example.enums.ExportFormat;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import java.io.OutputStream;

public class ReportExporterImpl implements ReportExporter {

    public JRAbstractExporter getExporterByFormat(ExportFormat basicExportFormat) {
        BasicExportExportFormat format = BasicExportExportFormat.valueOf(basicExportFormat.toString());
        switch (format) {
            case DOCX:
                return new JRDocxExporter();
            case XLS:
            case XLSX:
                return new JRXlsExporter();
            case HTML:
                return new HtmlExporter();
            default:
                return new JRPdfExporter();
        }
    }

    public ExporterOutput getExporterOutPut(OutputStream outputStream, ExportFormat exportFormat) {
        BasicExportExportFormat format = BasicExportExportFormat.valueOf(exportFormat.toString());
        switch (format) {
            case HTML:
                return new SimpleHtmlExporterOutput(outputStream);
            default:
                return new SimpleOutputStreamExporterOutput(outputStream);

        }
    }
}
