package com.example.toucheese_be.global.sheets.service;

import com.google.api.services.sheets.v4.Sheets;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class GoogleSheetsService {


    public void writeToSheets(String spreadsheetId, String range, List<List<Object>> values) {
    }
}
