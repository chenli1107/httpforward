package com.test.httpforward.fwdclient.netty.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.test.fwdcommon.entity.BaseEntity;
import com.test.fwdcommon.entity.HttpRequestEntity;
import com.test.fwdcommon.entity.HttpUrlMapping;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

public class JsonToObjectInHandler extends JsonObjectDecoder {
    private Logger logger = LoggerFactory.getLogger(getClass());
    static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        String jsonStr = new String(bytes, Charset.defaultCharset());
        JsonNode jsonNode = mapper.readTree(jsonStr);
        JsonNode idNode = jsonNode.findValue(BaseEntity.CLASS_NAME_FIELD);
        if(idNode == null){
            logger.error("接受到未知的json数据: {}", jsonStr);
            return;
        }
        try {
            out.add(mapper.readValue(jsonStr, Class.forName(idNode.asText())));
        }catch (Exception e){
            e.printStackTrace();
            logger.error("json转实体异常");
        }
    }
}
