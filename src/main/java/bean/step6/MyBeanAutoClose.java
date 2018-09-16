package bean.step6;

import IoC.IoCContextImpl;

public class MyBeanAutoClose implements AutoCloseable {
    @Override
    public void close() throws Exception {
        IoCContextImpl.orderOfAutoCloseList.add(MyBeanAutoClose.class.getName());
    }
}
