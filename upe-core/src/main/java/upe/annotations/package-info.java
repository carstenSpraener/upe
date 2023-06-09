/**
 * These packages contain the annotations to describe your Application.
 * <p>
 * You don't need to use annotations
 * and create your processes completely from hand. But with annotations it is much easier.
 * <p>
 * Each annotation is documented on how to use it.
 *
 * Example:
 * <pre>
 * &commat;UpeProcess("HelloWorld")
 * public class HelloWorldProcess extends AbstractProcessImpl {
 *
 *     &commat;UpeProcessField("name")
 *     private UProcessTextField nameField;
 *
 *     &commat;UpeProcessComponent()
 *     private AddressEditor address;
 *     ...
 *
 *     &commat;UpeProcessAction("AktSayHello")
 *     private Object aktSayHello(Map&lt;String, Object&gt; args) {
 * </pre>
 */
package upe.annotations;
