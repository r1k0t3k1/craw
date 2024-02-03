package main.java.utils;

import burp.api.montoya.http.handler.HttpResponseReceived;
import burp.api.montoya.http.message.MimeType;
import com.google.gson.*;
import main.java.models.ProxyLogJsonModel;
import main.java.models.ProxyLogTableModel;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.zip.Deflater;

public class JsonConverter {

    public static String convertO2J(ProxyLogJsonModel proxyLog) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Color.class, new ColorSerializer());
        gsonBuilder.registerTypeAdapter(UUID.class, new UuidSerializer());
        gsonBuilder.registerTypeAdapter(MimeType.class, new MimeTypeSerializer());
        gsonBuilder.registerTypeAdapter(HttpResponseReceived.class, new HttpRequestResponseSerializer());
        Gson gson = gsonBuilder.create();
        return gson.toJson(proxyLog);
    }

    public static byte[] convertO2z(ProxyLogJsonModel proxyLog) {
        var json = convertO2J(proxyLog);
        return Compresser.compress(json.getBytes());
    }

    private static class DateSerializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(date.toString());
        }
    }

    private static class ColorSerializer implements JsonSerializer<Color> {
        @Override
        public JsonElement serialize(Color color, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(color.getRGB());
        }
    }

    private static class UuidSerializer implements JsonSerializer<UUID> {

        @Override
        public JsonElement serialize(UUID uuid, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(uuid.toString());
        }
    }

    private static class MimeTypeSerializer implements JsonSerializer<MimeType> {
        @Override
        public JsonElement serialize(MimeType mimeType, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(mimeType.name());
        }
    }

    private static class HttpRequestResponseSerializer implements JsonSerializer<HttpResponseReceived> {
        @Override
        public JsonElement serialize(HttpResponseReceived httpResponseReceived, Type type, JsonSerializationContext jsonSerializationContext) {
            var b64Req = Base64.getEncoder().encodeToString(httpResponseReceived.initiatingRequest().toByteArray().getBytes());
            var b64Res = Base64.getEncoder().encodeToString(httpResponseReceived.toByteArray().getBytes());
            JsonObject obj = new JsonObject();
            obj.addProperty("request", b64Req);
            obj.addProperty("response", b64Res);
            return obj;
        }
    }

    private static class ProxyLogSerializer implements JsonSerializer<ProxyLogTableModel> {
        @Override
        public JsonElement serialize(ProxyLogTableModel tableModel, Type type, JsonSerializationContext jsonSerializationContext) {
            // TODO
            return null;
        }
    }
}
