package com.test.httpforward.fwdserver;

import com.test.fwdcommon.utils.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SpringContextHolder.class})
public class FwdServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FwdServerApplication.class, args);
	}

}

