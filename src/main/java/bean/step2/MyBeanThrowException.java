package bean.step2;

import bean.MyException;

public class MyBeanThrowException {
    public MyBeanThrowException() throws MyException {
        throw new MyException();
    }
}
