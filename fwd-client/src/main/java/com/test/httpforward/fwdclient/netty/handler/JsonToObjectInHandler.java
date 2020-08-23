package com.test.httpforward.fwdclient.netty.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fwdcommon.entity.BaseEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class JsonToObjectInHandler extends JsonObjectDecoder {
    static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        String jsonStr = new String(bytes, Charset.defaultCharset());
        JsonNode jsonNode = mapper.readTree(jsonStr);
        JsonNode idNode = jsonNode.findValue(BaseEntity.CLASS_NAME_FIELD);
        if(idNode == null){
            log.error("接受到未知的json数据: {}", jsonStr);
            return;
        }
        try {
            out.add(mapper.readValue(jsonStr, Class.forName(idNode.asText())));
        }catch (Exception e){
            log.error("json转实体异常", e);
        }
    }
}
