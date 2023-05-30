package upe.demo.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import upe.annotations.UpeApplication;
import upe.annotations.UpeBackendFacade;
import upe.backend.UProcessBackend;
import upe.demo.process.ClientBrowserDispatcherProcess;
import upe.demo.process.ClientBrowsingProcess;
import upe.demo.process.PersonProcess;
import upe.process.ApplicationConfiguration;

import javax.annotation.PostConstruct;
import java.util.function.Supplier;

@SpringBootApplication
@UpeApplication({
        ClientBrowserDispatcherProcess.class,
        ClientBrowsingProcess.class,
        PersonProcess.class
})
public class UpeDemo {
    private ApplicationContext context;

    public UpeDemo(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String... args) throws Exception {
        SpringApplication.run(UpeDemo.class, args);
    }

    @PostConstruct
    public void initUPE() {
        ApplicationConfiguration.getInstance().readApplication(UpeDemo.class);
        UProcessBackend.addSupplier("personMgr",()->context.getBean("PersonMgrImpl"));
    }
}
