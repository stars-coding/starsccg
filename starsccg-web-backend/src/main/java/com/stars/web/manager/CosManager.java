package com.stars.web.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.stars.web.config.CosClientConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * COS 管理器
 *
 * @author stars
 * @version 1.0.0
 */
@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 复用下载对象
     */
    private TransferManager transferManager;

    /**
     * 初始化
     * Bean 加载完成后执行
     */
    @PostConstruct
    public void init() {
        System.out.println("Bean 初始化成功");
        // 多线程并发上传下载
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        this.transferManager = new TransferManager(cosClient, threadPool);
    }

    /**
     * 上传对象
     *
     * @param key
     * @param localFilePath
     * @return
     */
    public PutObjectResult putObject(String key, String localFilePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.cosClientConfig.getBucket(), key,
                new File(localFilePath));
        return this.cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传对象
     *
     * @param key
     * @param file
     * @return
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.cosClientConfig.getBucket(), key,
                file);
        return this.cosClient.putObject(putObjectRequest);
    }

    /**
     * 下载对象
     *
     * @param key
     * @return
     */
    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(this.cosClientConfig.getBucket(), key);
        return this.cosClient.getObject(getObjectRequest);
    }

    /**
     * 下载对象到本地文件
     *
     * @param key
     * @param localFilePath
     * @return
     */
    public Download download(String key, String localFilePath) throws InterruptedException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(this.cosClientConfig.getBucket(), key);
        File downloadFile = new File(localFilePath);
        Download download = this.transferManager.download(getObjectRequest, downloadFile);
        download.waitForCompletion();
        return download;
    }
}
