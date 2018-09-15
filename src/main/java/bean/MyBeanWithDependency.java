package bean;

import annotation.CreateOnTheFly;

public class MyBeanWithDependency {
    @CreateOnTheFly
    public MyDependency myDependency;

    @CreateOnTheFly
    private MyBean myBean;
}
