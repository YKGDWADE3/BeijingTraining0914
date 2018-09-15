package bean;

import annotation.CreateOnTheFly;

public class MyBeanWithDependency {
    @CreateOnTheFly
    private Mydependency mydependency;
}
