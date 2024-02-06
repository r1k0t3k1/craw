package main.java.utils;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.java.models.ProxyLogItemModel;
import main.java.models.ProxyLogTableModel;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class JsonConverter {

    public static String convertLogToJson(ProxyLogTableModel proxyLog) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(ProxyLogTableModel.class, new ProxyLogSerializer());
        Gson gson = gsonBuilder.create();
        return gson.toJson(proxyLog);
    }
    public static ProxyLogTableModel convertJsonToLog(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(HttpRequest.class, new HttpRequestDeserializer());
        gsonBuilder.registerTypeAdapter(HttpResponse.class, new HttpResponseDeserializer());
        gsonBuilder.registerTypeAdapter(HttpService.class, new HttpServiceDeserializer());
        gsonBuilder.registerTypeAdapter(UUID.class, new UUIDDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(new TypeToken<List<Integer>>(){}.getType(), new IntListDeserializer());
        gsonBuilder.registerTypeAdapter(Color.class, new ColorDeserializer());
        Gson gson = gsonBuilder.create();

        ProxyLogItemModel[] logs = gson.fromJson(json, ProxyLogItemModel[].class);
        return new ProxyLogTableModel(logs);
    }

    public static byte[] convertLogToSevenZip(ProxyLogTableModel proxyLog) {
        var json = convertLogToJson(proxyLog);
        return Compresser.compress(json.getBytes());
    }

    public static ProxyLogTableModel convertSevenZipToLog(byte[] b) {
        var decompressed = Compresser.decompress(b);
        return convertJsonToLog(new String(decompressed, StandardCharsets.UTF_8));
    }

    private static class ProxyLogSerializer implements JsonSerializer<ProxyLogTableModel> {
        @Override
        public JsonElement serialize(ProxyLogTableModel tableModel, Type type, JsonSerializationContext jsonSerializationContext) {
            // TODO
            JsonArray array = new JsonArray();

            for (ProxyLogItemModel item: tableModel.getAllRows()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("url", item.getUrl());
                obj.addProperty("id", item.getId().toString());
                obj.addProperty("order", item.getOrder());
                obj.addProperty("requestName", item.getRequestName());
                obj.addProperty("note", item.getNote());
                obj.addProperty("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(item.getTime()));
                obj.addProperty("isTarget", item.isTarget());
                obj.addProperty("isCommit", item.isCommit());
                obj.addProperty("color", item.getColor() == null ? null : item.getColor().getRGB());

                JsonArray dupArray = new JsonArray();
                for (int dup: item.getDuplicateRequests()){
                   dupArray.add(dup);
                }
                obj.add("duplicateRequests", dupArray);

                JsonArray simArray = new JsonArray();
                for (int sim: item.getSimilarRequests()){
                    dupArray.add(sim);
                }
                obj.add("similarRequests", simArray);

                var b64Req = Base64.getEncoder().encodeToString(item.getHttpRequest().toByteArray().getBytes());
                var b64Res = Base64.getEncoder().encodeToString(item.getHttpResponse().toByteArray().getBytes());
                obj.addProperty("request", b64Req);
                obj.addProperty("response", b64Res);

                JsonObject svc = new JsonObject();
                svc.addProperty("host", item.getHost());
                svc.addProperty("port", item.getPort());
                svc.addProperty("secure", item.isSecure());
                obj.add("service", svc);
                array.add(obj);
            }
            return array;
        }
    }

    public static class UUIDDeserializer implements JsonDeserializer<UUID> {
        @Override
        public UUID deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return UUID.fromString(jsonElement.getAsString());
        }
    }

    public static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try {
                return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(jsonElement.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public static class ColorDeserializer implements JsonDeserializer<Color> {
        @Override
        public Color deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Color(jsonElement.getAsInt());
        }
    }

    public static class IntListDeserializer implements JsonDeserializer<List<Integer>> {
        @Override
        public List<Integer> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var list = new ArrayList<Integer>();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                list.add(element.getAsInt());
            }
            return list;
        }
    }

    public static class HttpRequestDeserializer implements JsonDeserializer<HttpRequest> {
        @Override
        public HttpRequest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var b64encoded = jsonElement.getAsString();
            var decoded = Base64.getDecoder().decode(b64encoded);
            return HttpRequest.httpRequest(ByteArray.byteArray(decoded));
        }
    }

    public static class HttpResponseDeserializer implements JsonDeserializer<HttpResponse> {
        @Override
        public HttpResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var b64encoded = jsonElement.getAsString();
            var decoded = Base64.getDecoder().decode(b64encoded);
            return HttpResponse.httpResponse(ByteArray.byteArray(decoded));
        }
    }

    public static class HttpServiceDeserializer implements JsonDeserializer<HttpService> {
        @Override
        public HttpService deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var obj = jsonElement.getAsJsonObject();
            return HttpService.httpService(
                    obj.get("host").getAsString(),
                    obj.get("port").getAsInt(),
                    obj.get("secure").getAsBoolean()
            );
        }
    }
}
