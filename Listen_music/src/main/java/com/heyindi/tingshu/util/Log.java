package com.heyindi.tingshu.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void println(Object o) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        String now = formatter.format(localDateTime);
        long threadId = Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        System.out.printf("%s: (%d - %s): %s\n", now, threadId, threadName, o.toString());
    }
}
