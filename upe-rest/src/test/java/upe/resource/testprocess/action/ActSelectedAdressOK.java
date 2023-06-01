package upe.resource.testprocess.action;

import upe.process.UProcessComponent;
import upe.process.UProcessElement;
import upe.process.UProcessField;
import upe.process.impl.AbstractUActionImpl;
import upe.process.impl.UProcessComponentListImpl;
import upe.resource.testprocess.AdressEditor;
import upe.resource.testprocess.PersonProcess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


public class ActSelectedAdressOK extends AbstractUActionImpl  {

    public ActSelectedAdressOK(UProcessComponent parent, String name) {
        super(parent, name);
    }

    @Override
    public Serializable internalExecute(Map<String, Serializable> args) {
        PersonProcess prsP = getPersonProcess();
        AdressEditor adrEd = getAdressEditor();
        AdressEditor newAdr = ((UProcessComponentListImpl<AdressEditor>) getPersonProcess().getProcessElement("addressList")).createNewInstance();
        mapValues(adrEd, newAdr);
        cleaValues(adrEd);
        return null;
    }

    private void cleaValues(AdressEditor adrEd) {
        ArrayList<UProcessElement> elementList = new ArrayList<>();
        adrEd.getProcessElements(elementList);
        for( var e : elementList ) {
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
        return (AdressEditor)getPersonProcess().getProcessElement("adress");
    }
}
