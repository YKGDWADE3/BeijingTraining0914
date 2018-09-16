package bean.step6;

import bean.MyException;

public class MyAnotherExceptionAutoClose implements AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new MyException("Another myException");
    }
}
