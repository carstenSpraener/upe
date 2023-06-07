import de.spraener.nxtgen.ProtectionStrategieDefaultImpl
import de.spraener.nxtgen.oom.StereotypeHelper
import de.spraener.nxtgen.oom.model.MAttribute
import de.spraener.nxtgen.oom.model.MClass
import de.spraener.nxtgen.oom.model.OOModel
import upe.profile.rest.generator.GeneratorGapTransformation
import upe.profile.rest.generator.MyModelHelper
import upe.profile.rest.generator.UPEStereotypes

MClass mClass = this.getProperty("modelElement");
JavaClassTarget target = new JavaClassTarget();

target.header.add("""// ${ProtectionStrategieDefaultImpl.GENERATED_LINE}
package ${mClass.getPackage().getFQName()};

""")

target.imports.addIfNotPresent("import javax.persistence.*;")

target.declaration.add("@Entity()")
String tableName = StereotypeHelper.getStereotype(mClass, UPEStereotypes.ENTITY.name).getTaggedValue("dbTable");
target.declaration.add("@Table(name=\"${tableName}\")")

new JavaClassDeclarationAspect(mClass).apply(target);

new JavaAttributeAspect(mClass)
        .withPreAttributeDefinition( (attr, ct) -> {
            if( StereotypeHelper.hasStereotye(attr, UPEStereotypes.IDENTIFIER.name)) {
                ct.attributes.add("    @Id")
                ct.attributes.add("    @GeneratedValue(strategy = GenerationType.IDENTITY)")
            }
        })
        .apply(target);

new JavaAssociationAspect(mClass)
        .withPreAttributeDefinition((assoc, cTarget) -> {
            if( MyModelHelper.Assoc.isToN(assoc)) {
                cTarget.imports.addIfNotPresent("import javax.persistence.OneToMany;")
                cTarget.attributes.add("    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)")
            }
        })
        .apply(target);

return target.evaluate();
