package IoC;

import annotation.CreateOnTheFly;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IoCContextImpl implements IoCContext {

    public static final Class<CreateOnTheFly> DEPENDENCY_CLASS = CreateOnTheFly.class;
    private HashMap<Class<?>, Boolean> registerHashMap;
    private HashMap<Class<?>, Class<?>> actualClassHashMap;
    private List<String> orderInitFieldList;

    public IoCContextImpl() {
        this.registerHashMap = new HashMap<>();
        this.actualClassHashMap = new HashMap<>();
        this.orderInitFieldList = new ArrayList<>();
    }

    @Override
    public void registerBean(Class<?> beanClazz) {
        checkResolveClazzWhenRegister(beanClazz, beanClazz);
        storeResolveAndBeanClazz(beanClazz, beanClazz);
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) {
        checkResolveClazzWhenRegister(resolveClazz, beanClazz);
        storeResolveAndBeanClazz(resolveClazz, beanClazz);
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {
        checkClazzHasBeenRegistered(resolveClazz);

        T instance = null;
        try {
            instance = (T) actualClassHashMap.get(resolveClazz).newInstance();
            checkSuperClazzField(instance, instance.getClass());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        registerHashMap.put(resolveClazz, true);
        return instance;
    }

    private <T> void checkSuperClazzField(T instance, Class<?> instanceClazz) throws IllegalAccessException {
        Class<?> superclass = instanceClazz.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            checkSuperClazzField(instance, superclass);
        }
        checkDependencyOnFieldAndInit(instance, instanceClazz);

    }


    private <T> void checkDependencyOnFieldAndInit(T instance, Class<?> superClazz) throws IllegalAccessException {
        Field[] declaredFields = superClazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getAnnotation(DEPENDENCY_CLASS) != null) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (checkClazzHasBeenRegistered(fieldType)) {
                    field.set(instance, getBean(fieldType));
                    orderInitFieldList.add(superClazz.getName());
                    field.setAccessible(false);
                }

            }
        }
    }



    private <T> boolean checkClazzHasBeenRegistered(Class<T> resolveClazz) {
        if (resolveClazz == null) {
            throw new IllegalArgumentException();
        }
        if (!registerHashMap.containsKey(resolveClazz)) {
            throw new IllegalStateException();
        }
        return true;
    }

    private void checkResolveClazzWhenRegister(Class<?> resolveClazz, Class<?> beanClazz) {
        if (resolveClazz == null || beanClazz == null) {
            throw new IllegalArgumentException("beanClazz is mandatory");
        }
        int modifiers = beanClazz.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers) || beanClazz == Class.class) {
            throw new IllegalArgumentException(beanClazz.getCanonicalName() + " is abstract");
        }

        boolean hasDefaultConstructor = false;
        Constructor<?>[] declaredConstructors = beanClazz.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            if (constructor.getParameterTypes().length == 0) {
                hasDefaultConstructor = true;
            }
        }
        if (!hasDefaultConstructor) {
            throw new IllegalArgumentException(beanClazz.getCanonicalName() + " has no default constructor.");
        }
        if (registerHashMap.containsKey(resolveClazz) && registerHashMap.get(resolveClazz)) {
            throw new IllegalStateException();
        }
    }



    private void storeResolveAndBeanClazz(Class<?> resolveClazz, Class<?> beanClazz) {
        if (!actualClassHashMap.containsKey(resolveClazz)) {
            registerHashMap.put(resolveClazz, false);
        }
        actualClassHashMap.put(resolveClazz, beanClazz);
    }

    public List<String> getOrderInitFieldList() {
        return orderInitFieldList;
    }
}
