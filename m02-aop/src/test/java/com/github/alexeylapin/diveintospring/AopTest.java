package com.github.alexeylapin.diveintospring;

import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AopTest {

    @Test
    void name() {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(Runnable.class);
        proxyFactory.setTargetSource(new SingletonTargetSource(new CustomRunnable()));
        Runnable proxy = (Runnable) proxyFactory.getProxy();

        System.out.println(proxy);
        proxy.run();
    }

    @Test
    void name2() {
        Runnable o = (Runnable) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{Runnable.class},
                new MyInvocationHandler(new CustomRunnable()));
        System.out.println(o);
        o.run();
    }

    static class CustomRunnable {

        public void run() {
            System.out.println("custom run");
        }

    }

    private static class MyInvocationHandler implements InvocationHandler {

        private final Object delegate;
        private final Map<MethodKey, Method> methods = new HashMap<>();

        public MyInvocationHandler(Object delegate) {
            this.delegate = delegate;
            for (Method method : delegate.getClass().getMethods()) {
                methods.put(new MethodKey(method.getName(), method.getParameterTypes()), method);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method method1 = methods.get(new MethodKey(method.getName(), method.getParameterTypes()));
            if (method1 != null) {
                return method1.invoke(delegate, args);
            }
            throw new UnsupportedOperationException();
        }

    }

    private static class MethodKey {

        private final String name;
        private final Class<?>[] parameterTypes;

        public MethodKey(String name, Class<?>[] parameterTypes) {
            this.name = name;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodKey methodKey = (MethodKey) o;
            return name.equals(methodKey.name) && Arrays.equals(parameterTypes, methodKey.parameterTypes);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name);
            result = 31 * result + Arrays.hashCode(parameterTypes);
            return result;
        }
    }

}
