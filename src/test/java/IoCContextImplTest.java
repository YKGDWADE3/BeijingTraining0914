import IoC.IoCContext;
import IoC.IoCContextImpl;
import bean.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IoCContextImplTest {

    private IoCContextImpl ioCContext;
    @BeforeEach
    void setUp() {
        ioCContext = new IoCContextImpl();
    }

    @Test
    void should_get_instance_for_once_normally() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        MyBean myBeanInstance = context.getBean(MyBean.class);
        assertEquals(MyBean.class, myBeanInstance.getClass());
    }

    @Test
    void should_get_two_instance_after_get_bean_twice() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);

        MyBean myBeanInstance1 = context.getBean(MyBean.class);
        MyBean myBeanInstance2 = context.getBean(MyBean.class);

        assertEquals(myBeanInstance1.getClass(), myBeanInstance2.getClass());
        assertNotSame(myBeanInstance1, myBeanInstance2);
    }

    @Test
    void should_get_instance_what_user_want() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        context.registerBean(String.class);
        String stringInstance = context.getBean(String.class);

        assertEquals(String.class, stringInstance.getClass());
    }

    @Test
    void should_throw_exception_when_parameter_null_in_get_bean() {
        Runnable runnable = () ->{
            ioCContext.registerBean(null);
        };
        final String expectedMsg = "beanClazz is mandatory";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, runnable::run);
        assertEquals(expectedMsg, illegalArgumentException.getMessage());
    }

    @Test
    void should_throw_exception_when_parameter_in_register_is_abstract() {
        Runnable runnableForCanNotInit = () ->{
            ioCContext.registerBean(List.class);
        };
        final String expectedMsg = List.class.getCanonicalName() + " is abstract";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, runnableForCanNotInit::run);
        assertEquals(expectedMsg, illegalArgumentException.getMessage());
    }

    @Test
    void should_throw_exception_when_parameter_in_register_is_interface() {
        Runnable runnableForCanNotInit = () ->{
            ioCContext.registerBean(Iterable.class);
        };
        final String expectedMsg = Iterable.class.getCanonicalName() + " is abstract";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, runnableForCanNotInit::run);
        assertEquals(expectedMsg, illegalArgumentException.getMessage());
    }

    @Test
    void should_throw_exception_when_clazz_has_no_default_constructor() {
        Runnable runnableForNoDefaultConstructor = () ->{
            ioCContext.registerBean(MyBeanWithoutDefaultConstructor.class);
        };
        final String expectedMsg = MyBeanWithoutDefaultConstructor.class.getCanonicalName() + " has no default constructor.";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, runnableForNoDefaultConstructor::run);
        assertEquals(expectedMsg, illegalArgumentException.getMessage());
    }

    @Test
    void should_not_throw_exception_register_again() {
        ioCContext.registerBean(String.class);
        Runnable runnableForRegisterAgain = () ->{
            ioCContext.registerBean(String.class);
        };
        assertDoesNotThrow(runnableForRegisterAgain::run);
    }

    @Test
    void should_throw_exception_when_resolveClazz_null(){
        Runnable runnableForArgument = () -> {
            ioCContext.getBean(null);
        };
        assertThrows(IllegalArgumentException.class, runnableForArgument::run);
    }

    @Test
    void should_throw_exception_when_get_bean_before_register() {
        Runnable runnableForState = () -> {
            ioCContext.getBean(String.class);
        };
        assertThrows(IllegalStateException.class, runnableForState::run);

    }

    @Test
    void should_throw_exception_in_get_bean_when_constructor_throw_exception() {
        ioCContext.registerBean(MyBeanThrowException.class);
        try {
            ioCContext.getBean(MyBeanThrowException.class);
        } catch (Exception e) {
            assertEquals(MyException.class, e.getClass());
        }
    }

    @Test
    void should_throw_exception_when_register_again_after_get_bean() throws Exception {
        ioCContext.registerBean(String.class);
        ioCContext.getBean(String.class);
        Runnable runnableForState = () -> {
            ioCContext.registerBean(String.class);

        };
        assertThrows(IllegalStateException.class, runnableForState::run);
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
    void should_create_instance_with_dependency() {
        ioCContext.registerBean(MyBeanWithDependency.class);
        ioCContext.registerBean(Mydependency.class);
        MyBeanWithDependency myBeanWithDependency = ioCContext.getBean(MyBeanWithDependency.class);
        assertEquals(MyBeanWithDependency.class, myBeanWithDependency.getClass());
    }
}
