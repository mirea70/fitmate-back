package com.fitmate.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonUtil {
    private static final String PATTERN_DATE = "yyyy-mm-dd";
    private static final String PATTERN_TIME = "HH:mm:ss";
    private static final String PATTERN_DATETIME = String.format("%s %s", PATTERN_DATE, PATTERN_TIME);

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        DateTimeFormatter format = DateTimeFormatter.ofPattern(PATTERN_DATETIME);

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if(value != null) {
                out.value(value.format(format));
            }
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            return LocalDateTime.parse(in.nextString(), format);
        }

//        @Override
//        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
//            return new JsonPrimitive(format.format(src));
//        }
//
//        @Override
//        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//            return LocalDateTime.parse(json.getAsString(), format);
//        }
    }

    public static Gson buildGson() {
        return  new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setDateFormat(PATTERN_DATETIME)
                .registerTypeAdapter(LocalDateTime.class, new GsonUtil.LocalDateTimeAdapter().nullSafe())
                .create();
    }
}
