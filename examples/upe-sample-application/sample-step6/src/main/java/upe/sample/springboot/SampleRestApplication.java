package upe.sample.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import upe.annotations.UpeApplication;
import upe.backend.UProcessBackend;
import upe.process.ApplicationConfiguration;
import upe.sample.HelloWorldProcess;
import upe.sample.LoginProcess;
import upe.sample.UserRegistrationProcess;
import upe.sample.backend.UserMgr;

import javax.annotation.PostConstruct;

@SpringBootApplication
@UpeApplication({
        LoginProcess.class,
        UserRegistrationProcess.class,
        HelloWorldProcess.class
})
public class SampleRestApplication {
    private ApplicationContext context;

    public SampleRestApplication(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleRestApplication.class, args);
    }


    @PostConstruct
    public void initUPE() {
        ApplicationConfiguration.getInstance().readApplication(SampleRestApplication.class);
        UProcessBackend.addSupplier(UserMgr.NAME,()->context.getBean("UserMgrSpringBootImpl"));
    }
}
