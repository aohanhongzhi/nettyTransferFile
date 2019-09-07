package com.cn.nettyFileUpload;

import org.junit.Test;

import java.io.File;

/**
 * @author eric
 * @date 9/7/19 10:23 PM
 */
public class TestSize {
    @Test
    public void testSize(){

        String pathName = "/home/eric/Downloads/download/OS/elementaryos-5.0-stable.20181016.iso";
        File file = new File(pathName);// 待上传文件
        System.out.printf("文件大小:%sMB",file.length()/1024/1024);
    }

}
