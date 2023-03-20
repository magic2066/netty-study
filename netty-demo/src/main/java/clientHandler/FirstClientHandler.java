package clientHandler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端写出数据");

        //获取数据
        ByteBuf buffer = getByteBuff(ctx);

        //写数据
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;

        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }

    private ByteBuf getByteBuff(ChannelHandlerContext ctx) {
        //1.获取二进制抽象ByteBuffer
        ByteBuf buffer = ctx.alloc().buffer();

        //2.准备数据,指定字符串的字符集为UTF-8
        byte[] bytes = "你好, Magic".getBytes(Charset.forName("utf-8"));

        //3.填充数据到ByteBuffer
        buffer.writeBytes(bytes);

        return buffer;
    }
}
