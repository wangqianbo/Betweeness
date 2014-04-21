package ict.ada.graph.util;


import ict.ada.graph.common.AdaConfig;
import ict.ada.graph.common.ZKUtil;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.recipes.lock.WriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BCUtil {
  private static String ZKPATH = "/ada/graph/bc";// 保持于mode一致.为入库状态.
  private static String ZKLOCKPATH= "/ada/graph/bclock";
  private static final int SESSION_TIMEOUT = 40*1000;//40s 
  private static final String CONNECTION_STRING = AdaConfig.CONNECTION_STRING;
  private static ZKUtil zkUtil = new ZKUtil();
  private static WriteLock lock;
  static{
    zkUtil.createConnection(CONNECTION_STRING, SESSION_TIMEOUT, new BCZKWatcher());
    lock= new WriteLock(zkUtil.getZK(), ZKLOCKPATH, null);
  }
  public static int getSource(int len) throws KeeperException, InterruptedException{
    int result;
    lock.lock();
   String source =   zkUtil.readData(ZKPATH, false);
   result = Integer.parseInt(source);
   zkUtil.writeData(ZKPATH,String.valueOf( result+len));
   lock.unlock();
   return result;
  }
  static class BCZKWatcher implements Watcher {
    private static final Logger LOG = LoggerFactory
        .getLogger(BCZKWatcher.class);
    
    public void process(WatchedEvent event) {
      // 连接状态
      KeeperState keeperState = event.getState();
      // 事件类型
      EventType eventType = event.getType();
      // 受影响的path
      String path = event.getPath();
      String logPrefix = "";
           
      LOG.info(logPrefix + "收到Watcher通知");
      LOG.info(logPrefix + "连接状态:\t" + keeperState.toString());
      LOG.info(logPrefix + "事件类型:\t" + eventType.toString());
  
      if (KeeperState.SyncConnected == keeperState) {
        // 成功连接上ZK服务器
      //  System.out.println("ddddddddddddddddd");
        if (EventType.None == eventType) {
          LOG.info(logPrefix + "成功连接上ZK服务器");
          ZKUtil.connectedSemaphore.countDown();
        }
      }else if (KeeperState.Expired == keeperState){//
        LOG.info(logPrefix + "与ZK连接超时，重新连接");//don't kown why?
        zkUtil.createConnection(CONNECTION_STRING, SESSION_TIMEOUT, new BCZKWatcher());
        lock= new WriteLock(zkUtil.getZK(), ZKLOCKPATH, null);
      } 
      else if (KeeperState.Disconnected == keeperState) {
        LOG.info(logPrefix + "与ZK服务器断开连接");
      } else if (KeeperState.AuthFailed == keeperState) {
        LOG.info(logPrefix + "权限检查失败");
      } else if (KeeperState.Expired == keeperState) {
        LOG.info(logPrefix + "会话失效");
      }

      LOG.info("--------------------------------------------");
    }
    }
  }
