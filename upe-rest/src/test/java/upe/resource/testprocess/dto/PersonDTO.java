package upe.resource.testprocess.dto;

import java.util.ArrayList;
import java.util.List;

public class PersonDTO {
    private String name;
    private List<AddressDTO> addressList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AddressDTO> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressDTO> addressList) {
        this.addressList = addressList;
    }
}
