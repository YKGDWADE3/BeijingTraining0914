package bean;

import annotation.CreateOnTheFly;

import java.util.ArrayList;

public class MySuperBeanWithDependency {
    private ArrayList<String> initOrderList = new ArrayList<>();
    @CreateOnTheFly
    private MyDependency mySuperDependencyField;

    public MyDependency getMySuperDependencyField() {
        return mySuperDependencyField;
    }
}
