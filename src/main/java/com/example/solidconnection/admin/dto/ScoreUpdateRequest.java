package com.example.solidconnection.admin.dto;

import com.example.solidconnection.type.VerifyStatus;

// 다양한 점수(어학, 학점) 업데이트 요청에 대한 공통 인터페이스
public interface ScoreUpdateRequest {
    VerifyStatus verifyStatus();
    String rejectedReason();
}
