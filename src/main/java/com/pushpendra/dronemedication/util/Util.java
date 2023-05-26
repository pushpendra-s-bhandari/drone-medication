package com.pushpendra.dronemedication.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Util {
    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return currentDateTime.format(format);
    }

    public static void sleepInMinutes (Integer minutes) throws InterruptedException {
        TimeUnit.MINUTES.sleep(minutes);
    }
}