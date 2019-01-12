package com.test.httpforward.webdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class TestController {
    @Value("${aa:aadf}")
    private String aa;
    @Value("${bb:bbdf}")
    private String bb;
    @Value("${cc:ccdf}")
    private String cc;
    @Value("${dd:dddf}")
    private String dd;

    @RequestMapping("/cfg")
    Map cfg() {
        Map<String, String> map = new HashMap<>();
        map.put("aa", aa);
        map.put("bb", bb);
        map.put("cc", cc);
        map.put("dd", dd);
        System.out.println("ggggggggggggggggggggg====");
        return map;
    }
    @RequestMapping("/ccc/{id}")
    String cfg(@PathVariable String id) {
        return "打的费request id is ===>>>"+ id;
    }

    @RequestMapping("/testpostjson")
    Person testpostjson(@RequestBody Person p) {
        System.out.println("ggggggggggggggggggggg===="+ p);
        return p;
    }
}

class Person{
    private String pName;
    private int pAge;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getpAge() {
        return pAge;
    }

    public void setpAge(int pAge) {
        this.pAge = pAge;
    }

    @Override
    public String toString() {
        return "Person{" +
                "pName='" + pName + '\'' +
                ", pAge=" + pAge +
                '}';
    }
}

