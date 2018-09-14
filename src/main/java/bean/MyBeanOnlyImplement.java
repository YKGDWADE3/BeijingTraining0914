package bean;

public class MyBeanOnlyImplement implements MyBeanBehavior {
    @Override
    public String greet() {
        return "hello world";
    }
}
