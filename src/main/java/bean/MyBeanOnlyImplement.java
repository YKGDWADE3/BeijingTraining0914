package bean;

public class MyBeanOnlyImplement implements IMyBean {
    @Override
    public String greet() {
        return "hello world";
    }
}
