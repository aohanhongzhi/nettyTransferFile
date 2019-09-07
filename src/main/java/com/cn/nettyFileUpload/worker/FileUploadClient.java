package com.cn.nettyFileUpload.worker;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;

import com.cn.nettyFileUpload.handler.FileUploadClientHandler;
import com.cn.nettyFileUpload.listener.FileListenerImpl;
import com.cn.nettyFileUpload.listener.FileMonitor;
import com.cn.nettyFileUpload.util.FileUploadFile;


//文件上传客户端
/**
 * @author 黄文华
 *
 */
public class FileUploadClient {
	public void connect(int port, String host,
			final FileUploadFile fileUploadFile) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline()
									.addLast(
											new ObjectDecoder(
													ClassResolvers
															.weakCachingConcurrentResolver(null)));
							ch.pipeline()
									.addLast(
											new FileUploadClientHandler(
													fileUploadFile));
						}
					});
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
			System.out.println("FileUploadClient connect()结束");
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		long startTime  = System.currentTimeMillis();
		long size = 0;
		int port = FILE_PORT;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		try {


			FileUploadFile uploadFile = new FileUploadFile();
			String pathName = "/home/eric/Downloads/download/OS/elementaryos-5.0-stable.20181016.iso";
			File file = new File(pathName);// 待上传文件
			String fileMd5 = file.getName();// 文件名
			uploadFile.setFile(file);
			uploadFile.setFile_md5(fileMd5);
			uploadFile.setStartPos(0);// 文件开始位置
			size = file.length()/1024/1024;//MB


			FileMonitor monitor = new FileMonitor(1000);
			 
			monitor.addFile(new File("/home/eric/backup.sh"));//这个不知道有啥用
			
			monitor.addListener(new FileListenerImpl());
	 
//			while (!false)
//			;
			new FileUploadClient().connect(port, "127.0.0.1", uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.printf("文件大小%sMB",size);
		long tatolTime =(endTime-startTime)/1000;
		System.out.printf("\ttotal time : %s s, speed : %s Mb/s",tatolTime,size/tatolTime);
	}

	public static final int FILE_PORT = 9991;
}

