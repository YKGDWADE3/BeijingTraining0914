package bean.step3;

public class MyBeanExtendsAndImplements extends MyBaseBean implements MyBeanBehavior {
    @Override
    public String greet() {
        return "hello world";
    }
}
