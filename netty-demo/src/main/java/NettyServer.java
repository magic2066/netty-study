import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import serverHandler.FirstServerHandler;

public class NettyServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap
                .group(boss, worker) //配置线程组
                .channel(NioServerSocketChannel.class) //指定IO模型 如果想指定为BIO：OioServerSocketChannel.class
                .childHandler(new ChannelInitializer<NioSocketChannel>() {  //定义后续每条连接的数据读写
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                });
        bind(serverBootstrap, 8000);

        //指定在服务端启动过程中的一些逻辑，通常情况下呢，我们用不着这个方法
        serverBootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                System.out.println("服务端启动成功");
            }
        });

        //表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
    }

    /**
     * 绑定端口，若端口被占用则自动+1 直至成功
     * @param serverBootstrap 引导类
     * @param port 端口
     */
    private static void bind(ServerBootstrap serverBootstrap, final int port){
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()){
                System.out.println("端口[" + port + "]绑定成功");
            }else {
                System.out.println("端口[" + port + "]绑定绑定失败");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
