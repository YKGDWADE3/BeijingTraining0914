package bean.step3;

import bean.step2.MyBeanWithoutDefaultConstructor;

public class MyBeanExtendsWithoutDefaultConstructor extends MyBeanWithoutDefaultConstructor {
    public MyBeanExtendsWithoutDefaultConstructor(String name) {
        super(name);
    }
}
