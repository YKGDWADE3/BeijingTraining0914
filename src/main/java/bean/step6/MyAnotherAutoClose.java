package bean.step6;

import IoC.IoCContextImpl;

public class MyAnotherAutoClose implements AutoCloseable {
    @Override
    public void close() throws Exception {
        IoCContextImpl.orderOfAutoCloseList.add(MyAnotherAutoClose.class.getName());
    }
}
