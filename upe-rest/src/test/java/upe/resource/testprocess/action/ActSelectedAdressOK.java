package upe.resource.testprocess.action;

import upe.process.*;
import upe.process.impl.AbstractUActionImpl;
import upe.process.impl.UProcessComponentListImpl;
import upe.resource.testprocess.AdressEditor;
import upe.resource.testprocess.PersonProcess;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;


public class ActSelectedAdressOK extends AbstractUActionImpl  {

    public ActSelectedAdressOK(UProcessComponent parent, String name) {
        super(parent, name);
    }

    @Override
    public Object internalExecute(Map<String, Object> args) {
        PersonProcess prsP = getPersonProcess();
        AdressEditor adrEd = getAdressEditor();
        UProcessComponentList<AdressEditor> addressList = (UProcessComponentListImpl<AdressEditor>) getPersonProcess().getProcessElement("addressList");
        AdressEditor newAdr = null;
        if( adrEd.getRowIDValue() == null ) {
            newAdr = addressList.createNewInstance();
            newAdr.setRowIDValue(addressList.size()-1);
        } else {
            newAdr = findByRowId(addressList, adrEd.getRowIDValue());
        }
        mapValues(adrEd, newAdr);
        clearValues(adrEd);

        return null;
    }

    private AdressEditor findByRowId(UProcessComponentList<AdressEditor> addressList, BigDecimal rowIDValue) {
        for( AdressEditor adrEditor : addressList.getComponentList() ) {
            if( adrEditor.getRowIDValue().equals(rowIDValue) ) {
                return adrEditor;
            }
        }
        return null;
    }

    private void clearValues(AdressEditor adrEd) {
        for( var e : adrEd.getProcessElements() ) {
            if (e instanceof UProcessField pf) {
                pf.setValue(null);
            }
        }
    }

    private void mapValues(AdressEditor adrEd, AdressEditor newAdr) {
        ArrayList<UProcessElement> elementList = new ArrayList<>();
        adrEd.getProcessElements(elementList);
        for( var e : elementList ) {
            if( e instanceof UProcessField pf ) {
                UProcessField targetField = (UProcessField) newAdr.getProcessElement(pf.getName());
                targetField.setValue(pf.getValue());
            }
        }
    }

    private PersonProcess getPersonProcess() {
        return (PersonProcess)getProcess().getProcessElement(getElementPath()+"/..");
    }

    private AdressEditor getAdressEditor() {
        return (AdressEditor)getPersonProcess().getProcessElement("selectedAddress");
    }
}
