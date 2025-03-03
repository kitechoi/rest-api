package me.celine.demo.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 테스트코드에서 주석 대용으로 사용할 자체제작 "@TestDesciption 애노테이션"

// @interface: 단순한 인터페이스가 아니라 특별한 목적을 가진 애너테이션을 정의하는 문법
// @Target:  애너테이션이 적용될 수 있는 위치를 제한
    // ElementType.METHOD:  해당 애너테이션이 메서드에만 적용될 수 있도록 제한
// @Retention: 애너테이션의 얼마 동안 유지될 것인지 결정
    // RetentionPolicy.SOURCE: 컴파일할 때만 존재하고, 컴파일 이후에는 사라지는 애너테이션
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface TestDescription {
    String value();
}
