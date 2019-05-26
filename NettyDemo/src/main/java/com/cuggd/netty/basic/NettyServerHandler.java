package com.cuggd.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务器端的业务处理类
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据事件
     * @param ctx 上下文对象
     * @param msg 传入的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        System.out.println("Server:"+ctx);
        //客户端传过来的消息其实是在一个缓冲区中
        ByteBuf buf=(ByteBuf) msg;
        System.out.println("客户端发来的消息："+buf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕事件，这里是往客户端回复一条消息
     * @param ctx 上下文对象
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        //Unpooled为缓冲区工具类
        //利用上下文往客户端channel上发一条消息（从客户端链的开始往后传递）。传给客户端。
        ctx.writeAndFlush(Unpooled.copiedBuffer("就是没钱",CharsetUtil.UTF_8));
    }

    /**
     * 异常发生事件
     * @param ctx 上下文对象
     * @param t 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        //发生异常就关闭通道，这里关闭上下文就可关闭通道。
        ctx.close();
    }


}
