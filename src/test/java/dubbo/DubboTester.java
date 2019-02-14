package dubbo;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class DubboTester implements Closeable {

    private final EventLoopGroup group = new NioEventLoopGroup();
    private final String host;
    private final int port;

    private volatile Channel channel;

    private DubboHandler dubboHandler;

    public DubboTester() {
        this("127.0.0.1", 7100);
    }

    public DubboTester(String host, int port) {
        this.host = host;
        this.port = port;
        this.dubboHandler = new DubboHandler();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(dubboHandler);
                    }
                });
        try {
            this.channel = b.connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String invoke(String method, Object... args) throws TimeoutException {
        StringBuilder invoke = new StringBuilder();
        invoke.append("invoke ").append(method).append("(");

        for (Object arg : args) {
            if (isSimpleObject(arg)) {
                invoke.append(arg.toString()).append(",");
            } else {
                invoke.append(JSON.toJSONString(arg));
            }
        }

        if (invoke.charAt(invoke.length() - 1) == ',') {
            invoke.deleteCharAt(invoke.length() - 1);
        }

        invoke.append(")\n");
        this.channel.writeAndFlush(Unpooled.wrappedBuffer(invoke.toString().getBytes(Charset.forName("GBK"))));
        return dubboHandler.getContent();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    public String invoke(String className, String methodName, Object... args) throws TimeoutException {
        return invoke(className + "." + methodName, args);
    }

    @Override
    public void close() throws IOException {
        this.group.shutdownGracefully();
    }

    private boolean isSimpleObject(Object o) {
        return o instanceof Boolean || o instanceof Number;
    }


    public static class DubboHandler extends ChannelInboundHandlerAdapter {

        private volatile String content;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String content = ((ByteBuf) msg).toString(Charset.forName("GBK"));
            this.content = content;
        }

        public String getContent() throws TimeoutException {
            int count = 1;
            while (this.content == null) {
                await();
                count++;
                if (count > 50) {
                    throw new TimeoutException("调用超时");
                }
            }
            String content = this.content;
            this.content = null;
            return content;
        }

        private synchronized void await() {
            try {
                this.wait(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
