package com.huifenqi.hzf_platform.cache;


import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.configuration.MongoCrawlConfiguration;
import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;  
  
/** 
 * java 
 * mongodb的读取
 */  

@Component("mongoCrawlManager")
public class MongoCrawlManager {
	
    private static Mongo m = null;  
    private static DB db = null;  
    private final static Log log = LogFactory.getLog(MongoCrawlManager.class); 
    
    @Autowired
	private  MongoCrawlConfiguration mongoCrawlConfiguration;
   
      
    /** 
     * 数据读取 
     */  
    public  LinkedBlockingQueue<BasicDBObject> readColData(int crawlType,String dataTable,int limit){
    	
    	LinkedBlockingQueue<BasicDBObject> bdojQueue = new LinkedBlockingQueue<>(200000);
    	String collections = null;
   	 	//page_type条件
		BasicDBObject nameCond = new BasicDBObject();
		
    	if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_58){
 		    nameCond.put("page_type", 1);
 		    nameCond.put("city", "北京");
    		collections = mongoCrawlConfiguration.getCn();
    	}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_MOGU){
    		collections = mongoCrawlConfiguration.getMgn();
    	}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_GANJI){
    		nameCond.put("city", "北京");
    		collections = "ganji_community_";
    	}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_BAIDU){
    		collections = "baidu_house_house_";
    	}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_DANKE){
			collections = "danke_house_";
    	}
    	
    	//获取mongo数据表
        DBCollection dbCol = db.getCollection(collections+dataTable); 
        DBCursor ret = null;
        if(limit >= 0){
            ret = dbCol.find(nameCond).limit(limit);
        }else{
            ret = dbCol.find(nameCond);
        }
        log.info("从mongodb数据集中读取数据结束,总计"+ret.size()); 
        int count = 0 ;
        if(ret.size() > 0){
        	 while(ret.hasNext()){
             	count++;
                 BasicDBObject bdbObj = (BasicDBObject) ret.next();  
                 if(bdbObj != null){
                 	log.info("读取mongodb爬虫数据条数"+count);
                 	bdojQueue.offer(bdbObj);
                 }  
             }
        }
        
//        for(DBObject bdbObj : objList){
//        	count++;
//            if(bdbObj != null){
//            	log.info("读取mongodb爬虫数据条数"+count);
//            	bdojQueue.offer((BasicDBObject)bdbObj);
//            }  
//        }
		return bdojQueue;  
    }  
        
    /** 
     * 关闭mongodb数据库连接 
     */  
    public  void stopMondoDBConn(){  
        if (null != m) {  
            if (null != db) {  
                // 结束Mongo数据库的事务请求  
                try {  
                    db.requestDone();  
                } catch(Exception e) {  
                    e.printStackTrace();  
                }  
            }  
            try  
            {  
                m.close();  
            } catch(Exception e) {  
                e.printStackTrace();  
            }  
            m = null;  
            db = null;  
        }  
    }  
      
    /** 
     * 获取mongodb数据库连接 
     */  
    public  void startMongoDBConn(){  
        try {  
            //Mongo(p1, p2):p1=>IP地址     p2=>端口  
            m = new Mongo(mongoCrawlConfiguration.getCi(), mongoCrawlConfiguration.getCp());  
            //根据mongodb数据库的名称获取mongodb对象  
            db = m.getDB(mongoCrawlConfiguration.getCd());  
            //校验用户密码是否正确  
            if (!db.authenticate(mongoCrawlConfiguration.getUser(), mongoCrawlConfiguration.getPwd().toCharArray())){  
                log.info("连接MongoDB数据库,校验失败！");  
            }else{  
            	log.info("连接MongoDB数据库,校验成功！");  
            }  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (MongoException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static String getNextDay(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        date = calendar.getTime(); 
        SimpleDateFormat riqi= new SimpleDateFormat("YYYY-MM-dd");
        String biaodan=riqi.format(date);
        return biaodan;  
    } 
}  
