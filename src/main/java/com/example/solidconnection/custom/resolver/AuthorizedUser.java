package com.example.solidconnection.custom.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER}) // 메서드의 파라미터에 적용되는 어노테이션이다.
@Retention(RetentionPolicy.RUNTIME) // 해당 어노테이션은 런타임 시점까지 유지됨
public @interface AuthorizedUser {
    boolean required() default true; // 사용자를 찾을 수 없다면 오류 발생됨
}
