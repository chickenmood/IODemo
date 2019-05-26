package com.cuggd.rpc.serverStub;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

//服务器端业务处理类
public class InvokeHandler extends ChannelInboundHandlerAdapter {


    /**
     * 得到某接口下某个实现类的名字
     *
     * @param classInfo 服务消费方发来的消息（包含了需要调用哪个接口的哪个方法）
     * @return 接口的实现类的全限定名
     * @throws Exception 异常
     */
    private String getImplClassName(ClassInfo classInfo) throws Exception {
        //服务方接口和实现类所在的包路径
        String interfacePath = "com.cuggd.rpc.server";

        //得到接口名（需要调用哪个接口）
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);

        //将得到的接口名和服务方接口和实现类所在的包名进行拼接，得到服务提供方接口的权限定名
        Class superClass = Class.forName(interfacePath + interfaceName);

        /**Reflections为第三方jar包，作用是根据一个接口的全限定名得到其所有的实现类。
         参数为存有这个接口和实现类的包路径名称*/
        Reflections reflections = new Reflections(interfacePath);
        //得到某接口下的所有实现类
        Set<Class> ImplClassSet = reflections.getSubTypesOf(superClass);
        if (ImplClassSet.size() == 0) {
            System.out.println("未找到实现类");
            return null;
        } else if (ImplClassSet.size() > 1) {
            System.out.println("找到多个实现类，未明确使用哪一个");
            return null;
        } else {
            //把集合转换为数组
            Class[] classes = ImplClassSet.toArray(new Class[0]);
            //得到实现类的名字
            return classes[0].getName();
        }
    }

    /**
     * 读取客户端发来的数据并通过反射调用实现类的方法
     *
     * @param ctx 上下文
     * @param msg 客户端（服务消费方）发来的消息
     * @throws Exception 异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo) msg;
        Object clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        Method method = clazz.getClass().
                getMethod(classInfo.getMethodName(), classInfo.getTypes());
        //通过反射调用实现类的方法
        Object result = method.invoke(clazz, classInfo.getObjects());
        ctx.writeAndFlush(result);
    }
}

