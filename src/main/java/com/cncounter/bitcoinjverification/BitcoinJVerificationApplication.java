package com.cncounter.bitcoinjverification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class}
)
public class BitcoinJVerificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitcoinJVerificationApplication.class, args);
    }

}
