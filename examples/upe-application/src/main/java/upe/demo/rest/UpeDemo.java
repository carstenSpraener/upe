package upe.demo.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import upe.annotations.UpeApplication;
import upe.demo.process.PersonProcess;
import upe.process.ApplicationConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication
@UpeApplication({
        PersonProcess.class
})
public class UpeDemo {

    public static void main(String... args) throws Exception {
        SpringApplication.run(UpeDemo.class, args);
    }

    @PostConstruct
    public void initUPE() {
        ApplicationConfiguration.getInstance().readApplication(UpeDemo.class);
    }
}
