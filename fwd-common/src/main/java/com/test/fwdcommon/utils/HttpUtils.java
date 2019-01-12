package com.test.fwdcommon.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    static RestTemplate restTemplate = new RestTemplate();
    static {
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        String baseUrl = "http://localhost:9090/ppp";
//        baseUrl = "http://localhost:9999/aaaa";
//        Map<String, String> headerMap = new HashMap<>();
//        System.out.println(new ObjectMapper().writeValueAsString("aaaaaaaaaaaa"));
//        //postJson_string(baseUrl+"cfg_unneedrole", null, null);
//        postJson_string(baseUrl+"/cfg", null, null);
        postJson_string(baseUrl+"/testpostjson", "{\n" +
                "    \"pName\": \"nnnn\", \n" +
                "    \"pAge\": 33\n" +
                "}", null);
//        getText_string(baseUrl+"/ccc/33333", null, null);


//        getText_string("https://www.baidu.com/", null, null);
    }

    public static String postJson_string(String url, String jsonBody, Map<String, String> headerMap) {
        return postJson(url, jsonBody, headerMap).getBody();
    }
    public static ResponseEntity<String> postJson(String url, String jsonBody, Map<String, String> headerMap) {
        // 注意：必须 http、https……开头，不然报错，浏览器地址栏不加 http 之类不出错是因为浏览器自动帮你补全了
        HttpHeaders headers = new HttpHeaders();
        // 这个对象有add()方法，可往请求头存入信息
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        if(headerMap!=null)
            headers.setAll(headerMap);
        // 解决中文乱码的关键 , 还有更深层次的问题 关系到 StringHttpMessageConverter，先占位，以后补全*/
        HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);
        // body是Http消息体例如json串
        ResponseEntity rsp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        //上面这句返回的是往 url发送 post请求 请求携带信息为entity时返回的结果信息
        //String.class 是可以修改的，其实本质上就是在指定反序列化对象类型，这取决于你要怎么解析请求返回的参数 */
        System.out.println("request url:"+ url);
        System.out.println("request entity:"+ entity.toString());
        System.out.println("response statusCode:"+rsp.getStatusCode());
        System.out.println("response headers:"+rsp.getHeaders().toString());
        System.out.println("response body:"+rsp.getBody());
        return rsp;
    }

    public static String postForm_string(String url, Map<String, Object> formParams, Map<String, String> headerMap) {
        return postForm(url, formParams, headerMap).getBody();
    }
    public static ResponseEntity<String> postForm(String url, Map<String, Object> formParams, Map<String, String> headerMap) {
        // 注意：必须 http、https……开头，不然报错，浏览器地址栏不加 http 之类不出错是因为浏览器自动帮你补全了
        HttpHeaders headers = new HttpHeaders();
        // 这个对象有add()方法，可往请求头存入信息
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if(headerMap!=null)
            headers.setAll(headerMap);

        MultiValueMap<String, Object> params= new LinkedMultiValueMap<>();
        //  也支持中文
        if(formParams!=null)
            params.setAll(formParams);
        // 解决中文乱码的关键 , 还有更深层次的问题 关系到 StringHttpMessageConverter，先占位，以后补全*/
        HttpEntity<Map> entity = new HttpEntity<Map>(params, headers);
        // body是Http消息体例如json串
        ResponseEntity rsp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        //上面这句返回的是往 url发送 post请求 请求携带信息为entity时返回的结果信息
        //String.class 是可以修改的，其实本质上就是在指定反序列化对象类型，这取决于你要怎么解析请求返回的参数 */
        System.out.println("request url:"+ url);
        System.out.println("request entity:"+ entity.toString());
        System.out.println("response statusCode:"+rsp.getStatusCode());
        System.out.println("response headers:"+rsp.getHeaders().toString());
        System.out.println("response body:"+rsp.getBody());
        return rsp;
    }

    public static String getText_string(String url, Map<String, Object> formParams, Map<String, String> headerMap) {
        return getText(url, formParams, headerMap).getBody();
    }
    public static ResponseEntity<String> getText(String url, Map<String, Object> formParams, Map<String, String> headerMap) {
        // 注意：必须 http、https……开头，不然报错，浏览器地址栏不加 http 之类不出错是因为浏览器自动帮你补全了
        HttpHeaders headers = new HttpHeaders();
//        // 这个对象有add()方法，可往请求头存入信息
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        if(headerMap!=null)
            headers.setAll(headerMap);

        MultiValueMap<String, Object> params= new LinkedMultiValueMap<>();
        //  也支持中文
        if(formParams!=null)
            params.setAll(formParams);
        // 解决中文乱码的关键 , 还有更深层次的问题 关系到 StringHttpMessageConverter，先占位，以后补全*/
        HttpEntity<Map> entity = new HttpEntity<Map>(params, headers);
        // body是Http消息体例如json串
        ResponseEntity rsp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        //上面这句返回的是往 url发送 post请求 请求携带信息为entity时返回的结果信息
        //String.class 是可以修改的，其实本质上就是在指定反序列化对象类型，这取决于你要怎么解析请求返回的参数 */
        System.out.println("request url:"+ url);
        System.out.println("request entity:"+ entity.toString());
        System.out.println("response statusCode:"+rsp.getStatusCode());
        System.out.println("response headers:"+rsp.getHeaders().toString());
        System.out.println("response body:"+rsp.getBody());
        return rsp;
    }


}
