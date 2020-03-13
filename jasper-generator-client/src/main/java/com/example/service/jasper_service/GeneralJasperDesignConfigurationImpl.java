package com.example.service.jasper_service;

import com.example.enums.BasicExportExportFormat;
import com.example.enums.ExportFormat;
import com.example.exceptions.JasperServiceException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import java.awt.*;
import java.util.Map;


@Slf4j
public class GeneralJasperDesignConfigurationImpl implements JasperDesignConfiguration {

    public JasperDesign createDesign(Map<String, String> columnsToPrint, ExportFormat basicExportFormat) throws JRException {
        int rowHeight = 25;
        int columnWidth = 160;

        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("report");
        jasperDesign.setColumnWidth(columnWidth);
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(0);
        jasperDesign.setRightMargin(0);
        jasperDesign.setTopMargin(0);
        jasperDesign.setBottomMargin(0);
        jasperDesign.setIgnorePagination(true);
        jasperDesign
                .setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

        JRDesignStyle normalStyle = new JRDesignStyle();
        normalStyle.setName("Default style");
        normalStyle.setDefault(true);
        normalStyle.setFontName("DejaVu Sans");
        normalStyle.setFontSize(8.5f);
        normalStyle.setBlankWhenNull(true);
        normalStyle.setBold(false);


        jasperDesign.addStyle(normalStyle);

        JRLineBox lineBox;
        lineBox = normalStyle.getLineBox();
        lineBox.getTopPen().setLineWidth(Float.valueOf("0.5"));
        lineBox.getRightPen().setLineWidth(Float.valueOf("0.5"));
        lineBox.getBottomPen().setLineWidth(Float.valueOf("0.5"));
        lineBox.getLeftPen().setLineWidth(Float.valueOf("0.5"));

        JRDesignStyle headerStyle = new JRDesignStyle();
        headerStyle.setBackcolor(new Color(192, 192, 192));
        headerStyle.setMode(ModeEnum.OPAQUE);
        headerStyle.setName("header");
        headerStyle.setFontName("DejaVu Sans");
        headerStyle.setFontSize(8.5f);
        headerStyle.setBold(Boolean.TRUE);


        lineBox = headerStyle.getLineBox();
        lineBox.getTopPen().setLineWidth(Float.valueOf("0.5"));
        lineBox.getRightPen().setLineWidth(Float.valueOf("0.5"));
        lineBox.getBottomPen().setLineWidth(Float.valueOf("0.5"));
        lineBox.getLeftPen().setLineWidth(Float.valueOf("0.5"));

        jasperDesign.addStyle(headerStyle);

        // Report's field
        columnsToPrint.forEach((key, value) -> {
            JRDesignField field = new JRDesignField();
            field.setName(key);
            field.setValueClass(String.class);
            try {
                jasperDesign.addField(field);
            } catch (JRException e) {
                log.error("Can't create report with field {}", key, e);
                throw new JasperServiceException("Can't create report with field " + key);
            }
        });

        //Column's names
        int x = 0;
        int y = 10;

        JRDesignBand band = new JRDesignBand();
        band.setHeight(2 * rowHeight + 10);


        for (Map.Entry<String, String> entry : columnsToPrint.entrySet()) {
            JRDesignStaticText staticText = new JRDesignStaticText();
            staticText.setX(x);
            staticText.setY(y);
            staticText.setStretchType(StretchTypeEnum.ELEMENT_GROUP_HEIGHT);
            staticText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            staticText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            staticText.setPrintWhenDetailOverflows(true);
            staticText.setWidth(columnWidth);
            staticText.setHeight(2 * rowHeight);
            staticText.setStyle(headerStyle);
            staticText.setMode(ModeEnum.OPAQUE);
            staticText.setText(entry.getValue());
            band.addElement(staticText);
            x += staticText.getWidth();
        }

        jasperDesign.setColumnHeader(band);

        //Detail band (report data)
        band = new JRDesignBand();
        band.setHeight(rowHeight);
        x = 0;
        y = 0;

        for (Map.Entry<String, String> entry : columnsToPrint.entrySet()) {
            JRDesignTextField textField = new JRDesignTextField();
            textField.setX(x);
            textField.setY(y);
            textField.setStretchWithOverflow(true);
            textField.setStretchType(StretchTypeEnum.ELEMENT_GROUP_HEIGHT);
            textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            textField.setWidth(columnWidth);
            textField.setHeight(rowHeight);
            textField.setBlankWhenNull(true);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setText("$F{" + entry.getKey() + "}");
            textField.setExpression(expression);
            textField.setStyle(normalStyle);
            band.addElement(textField);
            x += textField.getWidth();
        }

        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);

        // Column footer
        band = new JRDesignBand();
        jasperDesign.setColumnFooter(band);

        // Page basement
        band = new JRDesignBand();
        jasperDesign.setPageFooter(band);

        return jasperDesign;
    }

    @Override
    public ReportExportConfiguration getExportConfiguration(ExportFormat basicExportFormat) {
        BasicExportExportFormat format = BasicExportExportFormat.valueOf(basicExportFormat.toString());
        switch (format) {
            case XLSX:
            case XLS:
                return getXlsFormat();
            case PDF:
                return getPdfFormat();
            case HTML:
                return getHtmlFormat();
            //docx not support in default configuration please implement yourself config and ad to map in @com.example.service.jasper_service.ReportServiceImpl
            default:
                return null;
        }
    }


    private ReportExportConfiguration getXlsFormat() {
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setWhitePageBackground(false);
        configuration.setIgnoreCellBackground(true);
        configuration.setDetectCellType(true);
        configuration.setAutoFitPageHeight(false);
        return configuration;
    }

    private ReportExportConfiguration getPdfFormat() {
        SimplePdfReportConfiguration configuration = new SimplePdfReportConfiguration();
        configuration.setSizePageToContent(true);
        return configuration;
    }

    private ReportExportConfiguration getHtmlFormat() {
        SimpleHtmlReportConfiguration configuration = new SimpleHtmlReportConfiguration();
        configuration.setAccessibleHtml(true);
        return configuration;
    }
}
