package com.jinjing.springboot.springbootstudy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.util.Date;

/**
 * @author JinJing
 * @date 2019/5/9 下午9:43
 */
public class TimerClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private ByteBuf msg;

    public TimerClientHandler() {
        byte[] req = "query time".getBytes();
        msg = Unpooled.buffer(req.length);
        msg.writeBytes(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] req = new byte[msg.readableBytes()];
        msg.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("now is:"+body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
