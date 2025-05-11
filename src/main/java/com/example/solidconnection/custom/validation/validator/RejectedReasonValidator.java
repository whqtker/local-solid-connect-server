package com.example.solidconnection.custom.validation.validator;

import com.example.solidconnection.admin.dto.ScoreUpdateRequest;
import com.example.solidconnection.custom.validation.annotation.RejectedReasonRequired;
import com.example.solidconnection.type.VerifyStatus;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.example.solidconnection.custom.exception.ErrorCode.REJECTED_REASON_REQUIRED;

public class RejectedReasonValidator implements ConstraintValidator<RejectedReasonRequired, ScoreUpdateRequest> {

    private static final String REJECTED_REASON = "rejectedReason";

    @Override
    public boolean isValid(ScoreUpdateRequest request, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation(); // 기본 오류 메세지를 사용하지 않도록 설정

        // 거절 상태이고 거절 사유가 없다면 검증 실패, 유효하지 않음
        if (isRejectedWithoutReason(request)) {
            addValidationError(context, REJECTED_REASON_REQUIRED.getMessage());
            return false;
        }
        return true;
    }

    // verifyStatus 가 REJECTED 이고 rejectedReason 가 비어있다면 true 리턴
    private boolean isRejectedWithoutReason(ScoreUpdateRequest request) {
        return request.verifyStatus().equals(VerifyStatus.REJECTED)
                && StringUtils.isBlank(request.rejectedReason());
    }

    // buildConstraintViolationWithTemplate 메서드를 사용하여 커스텀 오류 메세지를 설정
    // addPropertyNode(): 어떤 필드에 오류가 있는지 지정
    // addConstraintViolation(): 오류를 등록
    private void addValidationError(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(REJECTED_REASON)
                .addConstraintViolation();
    }
}
