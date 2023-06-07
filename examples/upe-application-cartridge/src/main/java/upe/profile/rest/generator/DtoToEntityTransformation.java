package upe.profile.rest.generator;

import de.spraener.nxtgen.model.Stereotype;
import de.spraener.nxtgen.model.impl.StereotypeImpl;
import de.spraener.nxtgen.oom.ModelHelper;
import de.spraener.nxtgen.oom.model.*;
import de.spraener.nxtgen.oom.StereotypeHelper;

public class DtoToEntityTransformation extends DtoToEntityTransformationBase {

    @Override
    public void doTransformationIntern(MClass me) {
        MPackage pkg = ((MPackage)me.getPackage().getParent()).findOrCreatePackage("backend").findOrCreatePackage("model");
        MClass entity = pkg.createMClass(toEntityName(me.getName()));

        Stereotype stype = new StereotypeImpl("Entity");
        stype.setTaggedValue("dbTable", me.getName().toLowerCase().replace("dto", ""));
        entity.getStereotypes().add(stype);
        entity.putObject(GeneratorGapTransformation.ORIGINAL_CLASS, me);
        entity.setModel(me.getModel());

        MAttribute idAttr = null;
        for( MAttribute attr : me.getAttributes() ) {
            MAttribute entityAttr = attr.cloneTo(entity);
            if( StereotypeHelper.hasStereotye(attr, UPEStereotypes.IDENTIFIER.getName())) {
                idAttr = entityAttr;
                entityAttr.getStereotypes().add(new StereotypeImpl(UPEStereotypes.IDENTIFIER.getName()));
            }
        }
        if( idAttr==null ) {
            idAttr = entity.createAttribute("id", "Long");
            idAttr.setModel(me.getModel());
            idAttr.getStereotypes().add(new StereotypeImpl(UPEStereotypes.IDENTIFIER.getName()));
        }
        for( MAssociation assoc : me.getAssociations() ) {
            MAssociation entityAssoc = new MAssociation();
            entityAssoc.setName(assoc.getName());
            ModelHelper.cloneProperties(assoc, entityAssoc);
            entityAssoc.setType(toEntityName(assoc.getType()));
            entity.getAssociations().add(entityAssoc);
            entityAssoc.setMultiplicity(assoc.getMultiplicity());
            entityAssoc.setModel(assoc.getModel());
        }
    }

    private String toEntityName(String name) {
        String entityName = (name+"Entity").replace("DTO", "").replace("Dto", "");
        if( entityName.indexOf('.')!=-1 ) {
            entityName = patchPackageName(entityName);
        }
        return entityName;
    }

    private String patchPackageName(String entityName) {
        String className = entityName.substring(entityName.lastIndexOf('.')+1);
        entityName = entityName.substring(0, entityName.lastIndexOf('.'));
        entityName = entityName.substring(0, entityName.lastIndexOf('.'));
        return entityName+".backend.model."+className;
    }
}
