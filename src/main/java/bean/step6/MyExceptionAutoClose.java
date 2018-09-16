package bean.step6;

import bean.MyException;

public class MyExceptionAutoClose implements AutoCloseable {
    @Override
    public void close() throws MyException {
        throw new MyException("myException");
    }
}
