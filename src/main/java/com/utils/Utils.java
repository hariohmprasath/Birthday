package com.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public class Utils {
    public static LocalDateTime convertTimeZone(LocalDateTime date, String srcTz, String destTz) {
        DateTime srcDateTime = date.toDateTime(DateTimeZone.forID(srcTz));
        DateTime dstDateTime = srcDateTime.withZone(DateTimeZone.forID(destTz));
        return dstDateTime.toLocalDateTime();
    }

    public static boolean isEmpty(String data) {
        return data == null || data.trim().length() == 0;
    }
}
