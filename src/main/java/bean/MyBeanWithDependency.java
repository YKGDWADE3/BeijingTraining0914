package bean;

import annotation.CreateOnTheFly;

public class MyBeanWithDependency extends MySuperBeanWithDependency{
    @CreateOnTheFly
    public MyDependency myDependency;

    @CreateOnTheFly
    private MyBean myBean;
}
