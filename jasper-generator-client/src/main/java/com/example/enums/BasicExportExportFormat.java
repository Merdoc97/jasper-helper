package com.example.enums;

/**
 *
 */
public enum BasicExportExportFormat implements ExportFormat {
    PDF,
    XLSX,
    DOCX,
    HTML,
    XLS;

    @Override
    public BasicExportExportFormat getFormat(ExportFormat exportFormat) {
        return BasicExportExportFormat.valueOf(exportFormat.toString());
    }
}
