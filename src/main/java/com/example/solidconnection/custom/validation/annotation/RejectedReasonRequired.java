package com.example.solidconnection.custom.validation.annotation;

import com.example.solidconnection.custom.validation.validator.RejectedReasonValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // 클래스 수준에 적용되는 어노테이션임을 명시
@Retention(RetentionPolicy.RUNTIME) // 해당 어노테이션은 런타임까지 유지됨

// Valid 어노테이션이 붙은 객체가 검증될 때 RejectedReasonRequired 어노테이션이 붙었는지 확인한다.
// 그렇다면 RejectedReasonValidator 를 사용하여 검증을 수행한다.
@Constraint(validatedBy = RejectedReasonValidator.class)
public @interface RejectedReasonRequired {

    // 유효성 검사 실패 시 리턴할 기본 메세지
    String message() default "거절 사유 입력값이 올바르지 않습니다.";

    // 검증 그룹을 지정
    Class<?>[] groups() default {};

    // 검증 오류와 관련된 추가 메타데이터 제공
    Class<? extends Payload>[] payload() default {};
}
