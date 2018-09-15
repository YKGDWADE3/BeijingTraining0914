package bean;

import annotation.CreateOnTheFly;

public class MyBeanWithDependency {
    @CreateOnTheFly
    private MyDependency mydependency;

    public MyDependency getMydependency() {
        return mydependency;
    }
}
