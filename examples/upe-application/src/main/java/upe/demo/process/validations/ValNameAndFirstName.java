package upe.demo.process.validations;

public class ValNameAndFirstName extends ValNameAndFirstNameBase {

    boolean isEmpty(String value) {
        return value==null || "".equals(value);
    }

    @Override
    protected boolean hasError(String name, String firstName) {
        return isEmpty(name) && isEmpty(firstName);
    }
}
