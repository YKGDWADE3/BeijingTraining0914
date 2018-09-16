package bean.step4;

import annotation.CreateOnTheFly;
import bean.step5.MySuperBeanWithDependency;
import bean.step2.MyBean;

public class MyBeanWithDependency extends MySuperBeanWithDependency {
    @CreateOnTheFly
    public MyDependency myDependency;

    @CreateOnTheFly
    private MyBean myBean;
}
