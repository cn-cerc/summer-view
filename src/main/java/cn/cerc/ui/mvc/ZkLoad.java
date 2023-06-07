package cn.cerc.ui.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.db.zk.ZkServer;
import cn.cerc.mis.register.center.ApplicationEnvironment;

public class ZkLoad implements Watcher {

    private static final Logger log = LoggerFactory.getLogger(ZkLoad.class);

    private Map<String, List<String>> map = null;
    private static ZkLoad instance = new ZkLoad();
    private String rootPath;
    private String childPath = null;
    private String wanIp = "";

    private volatile Integer current;

    private ZkLoad() {
        map = new HashMap<>();
        rootPath = String.format("/%s/%s/%s/", ServerConfig.getAppProduct(), ServerConfig.getAppVersion(), "services");
        current = 0;
    }

    public static ZkLoad get() {
        return instance;
    }

    public String getUrl(String application) {
        String path = rootPath + application;
//        log.info("获取服务 {}", path);
        List<String> childList = map.get(path);
        if (childList == null) {
            ZkServer zk = ZkServer.get();
            if (!zk.exists(path)) {
                // 判断服务节点
                zk.create(path, "", CreateMode.PERSISTENT);
            }
            childList = zk.getNodes(path, this);
            map.put(path, childList);
        }
        String server = "127.0.0.1";
        if (childList.size() > 0) {
            String zkServer = childList.get(current);
            current = (current + 1) % childList.size();
            String[] values = zkServer.split(":");
            if (values.length > 2) {
                String destWanIp = values[2];
                if (!Utils.isEmpty(destWanIp) && !destWanIp.equals(wanIp)) {
                    server = String.format("%s:%s", destWanIp, values[1]);
                } else {
                    server = String.format("%s:%s", values[0], values[1]);
                }
            } else {
                server = String.format("%s:%s", values[0], values[1]);
            }
        }
        return "http://" + server;
    }

    // 刷新内存
    public void refreshChild(String path) {
        ZkServer zk = ZkServer.get();
        List<String> childList = zk.getNodes(path, this);
        log.info(childList.toString());
        map.put(path, childList);
    }

    // 注册服务IP及端口
    public String register() {
        String lanIp = ApplicationEnvironment.hostIP();
        String port = ApplicationEnvironment.hostPort();
        wanIp = "";
        String path = rootPath + ServerConfig.getAppOriginal();
        ZkServer zk = ZkServer.get();
        if (!zk.exists(path)) {
            zk.create(path, "", CreateMode.PERSISTENT);
        }
        childPath = path + "/" + lanIp + ":" + port + ":" + wanIp;
        String content = "http://" + lanIp + ":" + port;
        zk.create(childPath, content.toString(), CreateMode.EPHEMERAL);
        log.info("注册服务 {}", childPath);
        return childPath;
    }

    // 删除服务注册信息
    public void unRegister() {
        if (!Utils.isEmpty(childPath)) {
            ZkServer zk = ZkServer.get();
            if (zk.exists(childPath)) {
                zk.delete(childPath);
                log.info("删除注册服务 {}", childPath);
            }
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        // zk 路径
        String path = watchedEvent.getPath();
        // 判断是否建立连接
        // 获取事件状态
        Event.KeeperState keeperState = watchedEvent.getState();
        // 获取事件类型
        Event.EventType eventType = watchedEvent.getType();
        log.info("进入到 process() keeperState: {} , eventType: {} , path: {}", keeperState, eventType, path);
        try {
            if (Event.KeeperState.SyncConnected == keeperState) {
                if (Event.EventType.NodeChildrenChanged == eventType) {
                    this.refreshChild(path);
                }
            }
        } catch (Exception e) {
            log.error("监听zk异常", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(ZkLoad.get().getUrl("fpl"));
        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(ApplicationEnvironment.hostPort());
    }
}
