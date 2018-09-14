package bean;

public class MyBeanThrowException {
    public MyBeanThrowException() throws MyException {
        throw new MyException();
    }
}
