package upe.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import upe.demo.process.PersonProcess;
import upe.process.ApplicationConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class UpeDemo {

    public static void main(String... args) throws Exception {
        SpringApplication.run(UpeDemo.class, args);
    }

    @PostConstruct
    public void initUPE() {
        ApplicationConfiguration.getInstance().addProcessClass("person", PersonProcess.class.getName());
    }
}
