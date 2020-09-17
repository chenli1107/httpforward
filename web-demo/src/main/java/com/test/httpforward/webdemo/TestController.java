package com.test.httpforward.webdemo;

import com.test.httpforward.webdemo.po.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class TestController {
    @Value("${aa:aadf}")
    private String aa;
    @Value("${bb:bbdf}")
    private String bb;
    @Value("${cc:ccdf}")
    private String cc;
    @Value("${dd:dddf}")
    private String dd;

    @GetMapping("/cfg")
    public Map cfg() {
        Map<String, String> map = new HashMap<>();
        map.put("aa", aa);
        map.put("bb", bb);
        map.put("cc", cc);
        map.put("dd", dd);
        log.info("ggggggggggggggggggggg===={}", map);
        return map;
    }
    @GetMapping("/ccc/{id}")
    public String cfg(@PathVariable String id) {
        return "打的费request id is ===>>>"+ id;
    }

    @PostMapping("/testpostjson")
    public Person testpostjson(@RequestBody Person p) {
        log.info("ggggggggggggggggggggg===={}", p);
        return p;
    }
}

