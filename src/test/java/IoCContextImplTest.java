import IoC.IoCContext;
import IoC.IoCContextImpl;
import bean.*;
import bean.step2.MyBean;
import bean.step2.MyBeanThrowException;
import bean.step2.MyBeanWithoutDefaultConstructor;
import bean.step3.*;
import bean.step4.MyBeanWithDependency;
import bean.step4.MyDependency;
import bean.step5.MySuperBeanWithDependency;
import bean.step6.MyAnotherAutoClose;
import bean.step6.MyAnotherExceptionAutoClose;
import bean.step6.MyBeanAutoClose;
import bean.step6.MyExceptionAutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.AbstractList;

import static org.junit.jupiter.api.Assertions.*;

public class IoCContextImplTest {

    private IoCContextImpl ioCContext;
    @BeforeEach
    void setUp() {
        ioCContext = new IoCContextImpl();
    }

    @Test
    void should_get_instance_for_once_normally() {
        registerClazzs(ioCContext, MyBean.class);
        MyBean myBeanInstance = ioCContext.getBean(MyBean.class);
        assertEquals(MyBean.class, myBeanInstance.getClass());
    }

    @Test
    void should_get_two_instance_after_get_bean_twice() {
        registerClazzs(ioCContext, MyBean.class);

        MyBean myBeanInstance1 = ioCContext.getBean(MyBean.class);
        MyBean myBeanInstance2 = ioCContext.getBean(MyBean.class);

        assertEquals(myBeanInstance1.getClass(), myBeanInstance2.getClass());
        assertNotSame(myBeanInstance1, myBeanInstance2);
    }

    @Test
    void should_get_instance_what_user_want() {
        registerClazzs(ioCContext, MyBean.class, String.class);
        String stringInstance = ioCContext.getBean(String.class);

        assertEquals(String.class, stringInstance.getClass());
    }

    @Test
    void should_throw_exception_when_parameter_null_in_get_bean() {
        final String expectedMsg = "beanClazz is mandatory";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> ioCContext.registerBean(null));
        assertEquals(expectedMsg, illegalArgumentException.getMessage());
    }

    @Test
    void should_throw_exception_when_parameter_in_register_is_abstract() {
        final String expectedMsg = AbstractList.class.getCanonicalName() + " is abstract";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> registerClazzs(ioCContext, AbstractList.class));
        assertEquals(expectedMsg, illegalArgumentException.getMessage());
    }

    @Test
    void should_throw_exception_when_parameter_in_register_is_interface() {
        final String expectedMsg = Iterable.class.getCanonicalName() + " is abstract";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> registerClazzs(ioCContext, Iterable.class));
        assertEquals(expectedMsg, illegalArgumentException.getMessage());
    }

    @Test
    void should_throw_exception_when_clazz_has_no_default_constructor() {
        final String expectedMsg = MyBeanWithoutDefaultConstructor.class.getCanonicalName() + " has no default constructor.";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> registerClazzs(ioCContext, MyBeanWithoutDefaultConstructor.class));
        assertEquals(expectedMsg, illegalArgumentException.getMessage());
    }

    @Test
    void should_not_throw_exception_register_again() {
        registerClazzs(ioCContext, String.class);

        assertDoesNotThrow(() -> registerClazzs(ioCContext, String.class));
    }

    @Test
    void should_throw_exception_when_resolveClazz_null(){
        assertThrows(IllegalArgumentException.class, () -> ioCContext.getBean(null));
    }

    @Test
    void should_throw_exception_when_get_bean_before_register() {
        assertThrows(IllegalStateException.class, () -> getBeanClazzs(ioCContext, String.class));

    }

    @Test
    void should_throw_exception_in_get_bean_when_constructor_throw_exception() {
        registerClazzs(ioCContext, MyBeanThrowException.class);
        try {
            getBeanClazzs(ioCContext, MyBeanThrowException.class);
        } catch (Exception e) {
            assertEquals(MyException.class, e.getClass());
        }
    }

    @Test
    void should_throw_exception_when_register_again_after_get_bean() throws Exception {
        registerAndGetBeanClazzs(ioCContext, String.class);
        assertThrows(IllegalStateException.class, () -> ioCContext.registerBean(String.class));
    }

    @Test
    void should_get_instance_by_overload_method() {
        ioCContext.registerBean(MyBaseBean.class, MyBean.class);
        MyBaseBean myBaseBean = ioCContext.getBean(MyBaseBean.class);
        assertEquals(MyBean.class, myBaseBean.getClass());
    }

    @Test
    void should_override_resolve_clazz_use_different_register() {
        ioCContext.registerBean(MyBaseBean.class, MyBean.class);
        ioCContext.registerBean(MyBaseBean.class, MyBeanExtendsAndImplements.class);
        MyBaseBean myBaseBean = ioCContext.getBean(MyBaseBean.class);
        assertEquals(MyBeanExtendsAndImplements.class, myBaseBean.getClass());
    }

    @Test
    void should_one_clazz_implement_interface_and_extends_clazz_get_two_different_instance() {
        ioCContext.registerBean(MyBaseBean.class, MyBeanExtendsAndImplements.class);
        ioCContext.registerBean(MyBeanBehavior.class, MyBeanExtendsAndImplements.class);

        MyBaseBean myExtendsBean = ioCContext.getBean(MyBaseBean.class);
        MyBeanBehavior myImplementsBean = ioCContext.getBean(MyBeanBehavior.class);

        assertNotSame(myExtendsBean, myImplementsBean);
        assertEquals(myExtendsBean.getClass(), myImplementsBean.getClass());
    }

    @Test
    void should_get_instance_when_implements_interface() {
        ioCContext.registerBean(MyBeanBehavior.class, MyBeanOnlyImplement.class);
        MyBeanBehavior bean = ioCContext.getBean(MyBeanBehavior.class);
        assertEquals(MyBeanOnlyImplement.class, bean.getClass());
    }

    @Test
    void should_throw_exception_when_bean_clazz_has_no_default_constructor() {
        Runnable runnable = () ->{
            ioCContext.registerBean(MyBeanWithoutDefaultConstructor.class, MyBeanExtendsWithoutDefaultConstructor.class);
        };
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, runnable::run);
        assertEquals(MyBeanExtendsWithoutDefaultConstructor.class.getCanonicalName() + " has no default constructor.", illegalArgumentException.getMessage());
    }

    @Test
    void should_throw_exception_when_bean_clazz_is_null() {
        Runnable runnable = () ->{
            ioCContext.registerBean(MyBeanWithoutDefaultConstructor.class, null);
        };
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, runnable::run);
        assertEquals("beanClazz is mandatory", illegalArgumentException.getMessage());
    }

    @Test
    void should_create_instance_with_dependency() throws NoSuchFieldException, IllegalAccessException {
        registerClazzs(ioCContext, MyBeanWithDependency.class, MyDependency.class, MyBean.class, MyBaseBean.class);

        MyBeanWithDependency myBeanWithDependency = ioCContext.getBean(MyBeanWithDependency.class);
        Field myDependency = myBeanWithDependency.getClass().getDeclaredField("myDependency");
        Field myBean = myBeanWithDependency.getClass().getDeclaredField("myBean");
        myBean.setAccessible(true);

        assertEquals(MyBeanWithDependency.class, myBeanWithDependency.getClass());
        assertEquals(MyDependency.class, myDependency.get(myBeanWithDependency).getClass());
        assertEquals(MyBean.class, myBean.get(myBeanWithDependency).getClass());
    }

    @Test
    void should_throw_exception_when_any_dependency_field_not_register() {
        registerClazzs(ioCContext, MyBeanWithDependency.class, MyDependency.class);
        assertThrows(IllegalStateException.class, () -> ioCContext.getBean(MyBeanWithDependency.class));
    }

    @Test
    void should_instance_all_the_field_with_annotation_even_in_super_clazz_and_super_first() {
        registerClazzs(ioCContext, MyBeanWithDependency.class, MyDependency.class, MyBean.class, MyBaseBean.class);

        MyBeanWithDependency myBeanWithDependency = ioCContext.getBean(MyBeanWithDependency.class);

        assertEquals(MyDependency.class, myBeanWithDependency.getMySuperDependencyField().getClass());
        assertEquals(MySuperBeanWithDependency.class.getName(), ioCContext.getOrderOfInitFieldList().get(0));
    }

    @Test
    void should_throw_exception_when_super_clazz_field_with_annotation_not_register() {
        registerClazzs(ioCContext, MyBeanWithDependency.class, MyDependency.class, MyBean.class);

        assertThrows(IllegalStateException.class, () -> ioCContext.getBean(MyBeanWithDependency.class));
    }

    @Test
    void should_invoke_close_method_after_ioc_close_if_auto_close_instance_init_by_get_bean() throws Exception {
        try (IoCContext ioCContext = new IoCContextImpl()){
            registerAndGetBeanClazzs(ioCContext, MyBeanAutoClose.class);
        }

        assertEquals(MyBeanAutoClose.class.getName(), IoCContextImpl.orderOfAutoCloseList.get(0));
    }

    @Test
    void should_invoke_close_in_opposite_order_of_get_bean() throws Exception {
        try (IoCContext ioCContext = new IoCContextImpl()){
            registerAndGetBeanClazzs(ioCContext, MyBeanAutoClose.class, MyAnotherAutoClose.class);
        }

        assertEquals(MyAnotherAutoClose.class.getName(), IoCContextImpl.orderOfAutoCloseList.get(0));
        assertEquals(MyBeanAutoClose.class.getName(), IoCContextImpl.orderOfAutoCloseList.get(1));
    }

    @Test
    void should_call_all_close_even_throw_exception_in_one_close() {
        assertThrows(MyException.class, () -> {
            try (IoCContext ioCContext = new IoCContextImpl()){
                registerAndGetBeanClazzs(ioCContext, MyBeanAutoClose.class, MyExceptionAutoClose.class);
            }
        });
        assertEquals(MyBeanAutoClose.class.getName(), IoCContextImpl.orderOfAutoCloseList.get(0));
    }

    @Test
    void should_throw_first_exception_when_two_exception_throw_in_close() {
        MyException myException = assertThrows(MyException.class, () -> {
            try (IoCContext ioCContext = new IoCContextImpl()) {
                registerAndGetBeanClazzs(ioCContext, MyExceptionAutoClose.class, MyAnotherExceptionAutoClose.class);
            }
        });
        assertEquals("Another myException", myException.getMessage());
    }

    private void registerAndGetBeanClazzs(IoCContext ioCContext, Class<?>... classes) {
        registerClazzs(ioCContext, classes);
        getBeanClazzs(ioCContext, classes);
    }

    private void registerClazzs(IoCContext ioCContext, Class<?>... classes) {
        for (Class<?> clazz : classes) {
            ioCContext.registerBean(clazz);
        }
    }

    private void getBeanClazzs(IoCContext ioCContext, Class<?>... classes) {
        for (Class<?> clazz : classes) {
            ioCContext.getBean(clazz);
        }
    }


}
