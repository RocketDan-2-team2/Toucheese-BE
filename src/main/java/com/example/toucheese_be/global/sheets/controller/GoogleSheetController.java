package com.example.toucheese_be.global.sheets.controller;

import com.example.toucheese_be.global.sheets.service.GoogleSheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sheets")
public class GoogleSheetController {

    private GoogleSheetsService googleSheetsService;

    // 기존 시트id를 복사해둔 곳을 이 Url에 저장
    private static final String SPREADSHEET_ID = "https://docs.google.com/spreadsheets/d/1zZu34Qb3UfO6JkqGkynefgb1uLkvRrGUj_VktiDKe9A/edit?gid=0#gid=0";

    // 작성할 행 입력 Ex) A1
    private static final String RANGE = "A1";

    @PostMapping("/write")
    public ResponseEntity<String> writeToSheets(@RequestParam String word){
        try {

            // 데이터를 스프레드시트에 쓰기 위해 전달되는 형식
            // 행과 열에 데이터를 매핑하기 위함
            List<List<Object>> values = List.of(Collections.singletonList(word));

            googleSheetsService.writeToSheets(SPREADSHEET_ID, RANGE, values);

            return ResponseEntity.ok("데이터가 입력되었습니다. : " + word);

        }catch (Exception e){

            e.printStackTrace();

            return ResponseEntity.internalServerError().body("데이터 입력에 실패하였습니다:" + e.getMessage());
        }

    }

}
