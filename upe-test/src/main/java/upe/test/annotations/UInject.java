package upe.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A field of type UProcessEngine with this annotation will get the running
 * UProcessEngine injected.
 * <pre>
 * {@code
 *
 * @ExtendWith(
 *         UpeTestExtension.class
 * )
 * public class ....Test {
 *     @UInject
 *     private TestUProcessEngine processEngine;
 *
 *     @Test
 *     void ... {
 *         this.processEngine.callProcess("processUnderTest", new HashMap<>(), null);
 *     }
 * }
 * }
 * </pre>
 * If you want TestUProcessEngine to start the process under test
 * @see UpeProcessToTest
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UInject {
}
