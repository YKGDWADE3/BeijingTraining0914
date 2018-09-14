package bean;

public class MyBeanExtendsAndImplements extends MyBaseBean implements MyBeanBehavior {
    @Override
    public String greet() {
        return "hello world";
    }
}
