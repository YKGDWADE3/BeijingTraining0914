package bean;

public class MyBeanExtendsAndImplements extends MyBaseBean implements IMyBean {
    @Override
    public String greet() {
        return "hello world";
    }
}
