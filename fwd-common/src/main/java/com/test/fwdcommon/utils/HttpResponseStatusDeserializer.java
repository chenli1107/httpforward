package com.test.fwdcommon.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;

public class HttpResponseStatusDeserializer extends JsonDeserializer<HttpResponseStatus> {

    @Override
    public HttpResponseStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        return new HttpResponseStatus(node.get("code").asInt(), node.get("reasonPhrase").asText());
    }
}
