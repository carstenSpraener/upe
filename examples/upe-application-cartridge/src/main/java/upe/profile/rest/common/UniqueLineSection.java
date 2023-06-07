package upe.profile.rest.common;

import java.util.HashSet;
import java.util.Set;

/**
 * This section evaluates, so that there is no duplicate line. It can be used to
 * generate import statements.
 */
public class UniqueLineSection extends AbstractClassSection {

    @Override
    public void evaluate(StringBuilder parentSB) {
        Set<String> containedCode = new HashSet<>();
        StringBuilder tmpSB = new StringBuilder();
        super.evaluate(tmpSB);
        for( String line : tmpSB.toString().split("\n") ) {
            if( !containedCode.contains(line) ) {
                parentSB.append(line).append("\n");
                containedCode.add(line);
            }
        }
    }

}
