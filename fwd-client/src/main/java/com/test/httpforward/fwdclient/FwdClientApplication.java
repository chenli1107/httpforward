package com.test.httpforward.fwdclient;

import com.test.fwdcommon.utils.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class FwdClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FwdClientApplication.class, args);
	}

}

