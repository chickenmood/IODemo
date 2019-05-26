package com.cuggd.rpc.serverStub;

import java.io.Serializable;

/**
 * 封装类信息
 * （服务调用方想要调用服务提供方的哪个类的哪个方法，参数类型和参数值是什么）
 */
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    //类名
    private String className;
    //方法名
    private String methodName;
    //参数类型
    private Class<?>[] types;
    //参数列表
    private Object[] objects;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public void setTypes(Class<?>[] types) {
        this.types = types;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }
}

