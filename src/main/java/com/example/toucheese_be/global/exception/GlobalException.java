package com.example.toucheese_be.global.exception;

import com.example.toucheese_be.global.rsData.RsData;
import com.example.toucheese_be.standard.base.Empty;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    
        private final RsData<Empty> rsData;

        public GlobalException(String resultCode, String msg) {
            super("resultCode=" + resultCode + ",msg=" + msg);
            this.rsData = RsData.of(resultCode, msg);
        }

//    public GlobalException(CodeMsg codeMsg) {
//        super(codeMsg);
//    }
//    public GlobalException(CodeMsg codeMsg) {
//        this(codeMsg.getCode(), codeMsg.getMessage());
//    }

        // GlobalException를 컨트롤러에 입력해주면 경고 문구 사라짐
        public GlobalException() {
            this("400-0", "에러");
        }

        public GlobalException(String msg) {
            this("400-0", msg);
        }
}