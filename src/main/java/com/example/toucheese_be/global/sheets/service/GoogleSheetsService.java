package com.example.toucheese_be.global.sheets.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleSheetsService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSheetsService.class); // Google Sheets와 상호작용하기 위한 메서드를 포함
    private static final String APPLICATION_NAME = "터치즈 이메일 테스트"; // Google Sheets 애플리케이션의 이름을 나타냄
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance(); // 상수는 JSON 데이터를 처리하기 위한 JsonFactory 인스턴스를 제공
    private static final String CREDENTIALS_FILE_PATH = "/googlesheet/google.json"; // 인증에 사용되는 JSON 파링릐 경로를 지정
    private Sheets sheetsService;

    // 현재의 메소드는 Sheets 인스턴스를 얻는 데 사용
    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        if (sheetsService == null) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
                    // Google API를 호출할 떄 필요한 권한을 지정하는 부분 , 읽기/쓰기 권한을 나타냄
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));
            sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return sheetsService;
    }

    public void writeToSheets(String spreadsheetId, String range, List<List<Object>> values) {
        try {
            Sheets service = getSheetsService();
            ValueRange body = new ValueRange().setValues(values);
            UpdateValuesResponse result = service.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("데이터를 입력하세요.")
                    .execute();
            logger.info("행 업데이트: {}", result.getUpdatedRows());
        } catch (Exception e) {
            logger.error("데이터 입력에 실패했습니다.", e);
            throw new RuntimeException("데이터 입력에 실패했습니다.: " + e.getMessage(), e);
        }
    }
}
