package com.example.toucheese_be.global.sheets.controller;

import com.example.toucheese_be.global.sheets.service.GoogleSheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sheets")
public class GoogleSheetController {

    @Autowired
    private GoogleSheetsService googleSheetsService;

    @GetMapping("/read")
    public List<List<Object>> readSheets(@RequestParam String range) throws IOException {
        return googleSheetsService.readSheets(range);
    }

    @PostMapping("/write")
    public void writeSheets(@RequestBody List<List<Object>> values, @RequestParam String range) throws IOException{
        googleSheetsService.writeSheets(values, range);
    }

}
