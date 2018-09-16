package bean.step5;

import annotation.CreateOnTheFly;
import bean.step3.MyBaseBean;
import bean.step4.MyDependency;

import java.util.ArrayList;

public class MySuperBeanWithDependency {
    private ArrayList<String> initOrderList = new ArrayList<>();
    @CreateOnTheFly
    private MyDependency mySuperDependencyField;

    @CreateOnTheFly
    private MyBaseBean myBaseBean;

    public MyDependency getMySuperDependencyField() {
        return mySuperDependencyField;
    }
}
