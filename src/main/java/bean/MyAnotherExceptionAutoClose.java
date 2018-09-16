package bean;

public class MyAnotherExceptionAutoClose implements AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new MyException("Another myException");
    }
}
