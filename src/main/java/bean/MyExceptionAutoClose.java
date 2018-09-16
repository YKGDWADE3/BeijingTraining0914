package bean;

public class MyExceptionAutoClose implements AutoCloseable {
    @Override
    public void close() throws MyException {
        throw new MyException();
    }
}
