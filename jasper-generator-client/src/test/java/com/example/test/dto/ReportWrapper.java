package com.example.test.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ReportWrapper {

    private List<Map<String, String>> entities;

    public ReportWrapper() {
    }

    public ReportWrapper(List<Map<String, String>> entities) {
        this.entities = entities;
    }
}
