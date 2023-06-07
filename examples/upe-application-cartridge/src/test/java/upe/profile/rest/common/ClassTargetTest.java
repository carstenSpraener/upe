package upe.profile.rest.common;

import de.spraener.nxtgen.oom.model.MAttribute;
import de.spraener.nxtgen.oom.model.MClass;
import org.junit.jupiter.api.Test;
import upe.profile.rest.common.java.JavaAspects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClassTargetTest {
    ClassTarget uut = new ClassTarget();

    @Test
    void testCanAddSection() {
        uut.addSection(JavaSections.IMPORTS, new UniqueLineSection());

        assertNotNull(uut.getSection(JavaSections.IMPORTS));
    }

    @Test
    void canAddSnippetToSectionViaWithersAndEvaluates() {
        MClass myClass = new MClass();
        myClass.setName("AClazz");

        uut.withSection(
                JavaSections.IMPORTS,
                new UniqueLineSection()
                        .withSnippet(JavaAspects.POJO, "import java.util.Map;")
        );
        uut.getSection(JavaSections.IMPORTS)
                .getSnippet(JavaAspects.POJO)
                .insertAfter(JavaAspects.JPA, "import javax.persistence.*;");
        assertThat(uut.evaluate())
                .isEqualTo("""
                        import java.util.Map;
                        import javax.persistence.*;
                        """);
    }

    @Test
    void canUniqueLineSectionEliminatesDoubles() {
        MClass myClass = new MClass();
        myClass.setName("AClazz");

        uut.withSection(
                JavaSections.IMPORTS,
                new UniqueLineSection()
                        .withSnippet(JavaAspects.POJO, "import java.util.Map;")
        );
        uut.getSection(JavaSections.IMPORTS)
                .getSnippet(JavaAspects.POJO)
                .insertAfter(JavaAspects.JPA, "import javax.persistence.*;")
                .insertAfter(JavaAspects.JPA, "import javax.persistence.*;");
        assertThat(uut.evaluate())
                .isEqualTo("""
                        import java.util.Map;
                        import javax.persistence.*;
                        """);
    }

    @Test
    void testGetSnippetByAspectAndModelElementAndInsertBefore() {
        MAttribute attrA = new MAttribute();
        MAttribute attrB = new MAttribute();

        uut.withSection(JavaSections.ATTRIBUTE_DECLARATION,
                new CodeBlockSection()
                        .withSnippet(JavaAspects.ATTRIBUTE,
                                new CodeBlockSnippet(JavaAspects.ATTRIBUTE, attrA, "    private Long id;\n")
                        )
                        .withSnippet(JavaAspects.ATTRIBUTE,
                                new CodeBlockSnippet(JavaAspects.ATTRIBUTE, attrB, "    private String name;\n")
                        )
        );

        uut.getSection(JavaSections.ATTRIBUTE_DECLARATION)
                .getSnippet(JavaAspects.ATTRIBUTE, attrA)
                .insertBefore(JavaAspects.JPA, attrA, "    @Id\n");

        uut.getSection(JavaSections.ATTRIBUTE_DECLARATION)
                .getSnippet(JavaAspects.ATTRIBUTE, attrB)
                .insertBefore(JavaAspects.JPA, attrB, "    @Column(name=\"name\", type=\"VARCHAR(64)\")\n");

        assertThat(uut.evaluate())
                .contains("""
                            @Id
                            private Long id;
                            @Column(name="name", type="VARCHAR(64)")
                            private String name;
                        """);
    }
}
