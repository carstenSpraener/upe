package upe.process.testapp;

import upe.annotations.UpeApplication;
import upe.process.ApplicationConfiguration;
import upe.process.UProcess;
import upe.process.engine.BaseUProcessEngine;

import java.util.HashMap;

/**
 * @startuml
 * BaseUProcesss <|-- DerivedUProcess
 * SecondUProcess *-->"secondPersonEditor (1)" PersonEditor
 * PersonEditor ..> "scaffolds" PersonDTO
 * AddressEditor ..> "scaffolds" AddressDTO
 * PersonEditor *--> "addressEditor (1)" AddressEditor
 * PersonEditor *--> "otherAddresses (0..n)" AddressEditor
 * PersonDTO *--> "otherAddresses (0..n)" AddressDTO
 * PersonDTO *--> "address (1)" AddressDTO
 * class PersonDTO {
 *     name: String
 * }
 * class AddressDTO {
 *     street: String
 * }
 *
 * DerivedUProcess *-->"personList 0..n" PersonEditor
 * note top of DerivedUProcess : This is the "TestProcess"
 * @enduml
 */
@UpeApplication({
        DerivedUProcess.class,
        SecondUProcess.class,
        SubUProcess.class
})
public class Application {

    public static UProcess createTestProcess() {
        ApplicationConfiguration.getInstance().readApplication(Application.class);
        BaseUProcessEngine engine = new BaseUProcessEngine();
        engine.callProcess("TestProcess", new HashMap<>(), null);

        UProcess activeUProcess = engine.getActiveProcessInfo().getProcess();
        return activeUProcess;
    }
}
