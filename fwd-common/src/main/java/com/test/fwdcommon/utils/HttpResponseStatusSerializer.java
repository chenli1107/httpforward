package com.test.fwdcommon.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;

public class HttpResponseStatusSerializer extends JsonSerializer<HttpResponseStatus> {
    @Override
    public void serialize(HttpResponseStatus value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeNumberField("code", value.code());
        gen.writeStringField("reasonPhrase", value.reasonPhrase());
        gen.writeEndObject();
    }
}
