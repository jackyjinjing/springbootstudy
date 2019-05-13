package com.jinjing.springboot.springbootstudy.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author JinJing
 * @date 2019/5/13 下午3:59
 */
@Component
@Slf4j
public class ZkClient implements InitializingBean {

    private CuratorFramework zooKeeperClient;

    public void createNode(String nodeName,String value) {
        try {
            zooKeeperClient.create().withMode(CreateMode.PERSISTENT).forPath(nodeName, value.getBytes());
        }catch (Exception e){
            log.error("创建节点失败");
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String connectionInfo = "localhost:2080,localhost:2081";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zooKeeperClient =
                CuratorFrameworkFactory.newClient(
                        connectionInfo,
                        5000,
                        3000,
                        retryPolicy);
        zooKeeperClient.start();
    }
}
