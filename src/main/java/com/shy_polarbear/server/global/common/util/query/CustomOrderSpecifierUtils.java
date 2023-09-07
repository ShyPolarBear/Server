package com.shy_polarbear.server.global.common.util.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;

public class CustomOrderSpecifierUtils {
    // TODO: ORDER BY에 INDEX를 활용할 수 없어서 비효율적으로 동작 -> 랜덤 쿼리 튜닝 https://leezzangmin.tistory.com/28
    public static OrderSpecifier<Double> makeRandom() { // Mysql RAND 함수를 지원하지 않아서 만든 함수
        return Expressions.numberTemplate(Double.class, "function('rand')").asc();
    }
}
