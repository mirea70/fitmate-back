//package com.fitmate.config;
//
//import com.google.gson.FieldNamingPolicy;
//import com.google.gson.internal.GsonBuildConfig;
//import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//@Configuration
//public class GsonConfiguration {
//
//    private static String PATTERN_DATE = "yyyy-mm-dd";
//    private static String PATTERN_TIME = "HH:mm:ss";
//    private static String PATTERN_DATETIME = String.format("%s %s", PATTERN_DATE, PATTERN_TIME);
//
//    @Bean
//    public GsonBuilderCustomizer typeAdapterRegistration() {
//        return builder -> {
//            builder.disableHtmlEscaping()
//                    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
//                    .setDateFormat(PATTERN_DATETIME)
//                    .registerTypeAdapter(LocalDateTime.class, new GsonUtil.LocalDateTimeAdapter().nullSafe())
//                    .registerTypeAdapter(LocalDate.class, new GsonUtil.LocalDateAdapter().nullSafe())
//                    .registerTypeAdapter(LocalTime.class, new GsonUtil.LocalTimeAdapter().nullSafe());
//        };
//    }
//}
