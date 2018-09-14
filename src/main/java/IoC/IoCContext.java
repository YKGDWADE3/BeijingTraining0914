package IoC;

public interface IoCContext {
    void registerBean(Class<?> beanClazz);
    <T> T getBean(Class<T> resolveClazz);
    <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz);

}
