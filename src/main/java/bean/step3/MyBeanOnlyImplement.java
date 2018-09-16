package bean.step3;

public class MyBeanOnlyImplement implements MyBeanBehavior {
    @Override
    public String greet() {
        return "hello world";
    }
}
