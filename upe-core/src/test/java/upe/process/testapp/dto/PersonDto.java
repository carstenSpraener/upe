package upe.process.testapp.dto;

import java.util.Date;

public class PersonDto {
    private String name;
    private String surname;
    private Integer heightCM;
    private Date dateOfBirth;
    private AddressDto address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getHeightCM() {
        return heightCM;
    }

    public void setHeightCM(Integer heightCM) {
        this.heightCM = heightCM;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
