package com.example.solidconnection.application.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Embeddable // 해당 클래스는 다른 엔티티에 내장될 수 있음

// 명시된 필드를 기준으로 객체 비교 및 해시 코드 생성 메서드를 구현함
@EqualsAndHashCode(of = {"gpa", "gpaCriteria", "gpaReportUrl"})
public class Gpa {

    @Column(nullable = false, name = "gpa")
    private Double gpa;

    @Column(nullable = false, name = "gpa_criteria")
    private Double gpaCriteria;

    @Column(nullable = false, name = "gpa_report_url", length = 500)
    private String gpaReportUrl;
}
