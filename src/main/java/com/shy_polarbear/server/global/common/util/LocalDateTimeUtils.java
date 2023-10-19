package com.shy_polarbear.server.global.common.util;

import com.shy_polarbear.server.global.common.constants.GlobalConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateTimeUtils {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT);

    public static String convertToString(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }
}
