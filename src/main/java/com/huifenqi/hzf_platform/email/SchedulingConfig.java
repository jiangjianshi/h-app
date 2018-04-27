package com.huifenqi.hzf_platform.email;


import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.location.City;
import com.huifenqi.hzf_platform.context.entity.location.StatisticReport;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;
import com.huifenqi.hzf_platform.dao.repository.location.StatisticRepository;
import com.huifenqi.hzf_platform.utils.*;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时任务配置类
 *
 * @author arison
 * @create 2017年9月26日
 */
@Service
@EnableScheduling // 启用定时任务
public class SchedulingConfig {

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private Configuration globalConf;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StatisticRepository statisticRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 17 2 * * ?") // 每天执行一次
    public void scheduler() throws Exception{
        String prefix=Constants.statisticReport.statisticReport;
        Date currentDay=DateUtil.parseDate(DateUtil.formatCurrentDate());
        Date yesterDay=DateUtil.addDays(currentDay,-1);
        String dataStr=DateUtil.format("yyyyMMdd",yesterDay);
        List<City> cityList=cityRepository.findCityByStatus();
        int fsum=0;
        String[] titles = {"fpv", "fss", "fbdj", "fjg", "fppgy", "fhz", "fzz", "ffq","ftol"};


        System.out.println("------------------------test begin -------------------");
        for(City city:cityList) {
            System.out.println("city is " +city);
            int arys[] =new int[titles.length+1];
            for(int i=0;i<titles.length; i++) {
                //String key = prefix + ":" + dataStr + ":" + city.getCityId() + ":" + t;
                String key = RedisUtils.getInstance().getKey(prefix + ":" + dataStr + ":" + city.getCityId() + ":" + titles[i]);
                String value=redisCacheManager.getValue(key);
                if(StringUtil.isNoneEmpty(value)) {
                    Integer ival=Integer.parseInt(value);
                    arys[i]+=ival;
                    if(i>0) {
                        arys[8] += ival;
                    }
                }
            }

            if(arys[0]==0)
            {
                continue;
            }
            StatisticReport statisticReport=new StatisticReport();
            statisticReport.setCityId((int)city.getCityId());
            statisticReport.setCreateTime(new Date());
            statisticReport.setUpdateTime(new Date());
            statisticReport.setStrTime(dataStr);
            statisticReport.setFpv(arys[0]);
            statisticReport.setFss(arys[1]);
            statisticReport.setFbdj(arys[2]);
            statisticReport.setFjg(arys[3]);
            statisticReport.setFppgy(arys[4]);
            statisticReport.setFhz(arys[5]);
            statisticReport.setFzz(arys[6]);
            statisticReport.setFfq(arys[7]);
            statisticReport.setFtol(arys[8]);
            //将今天的数据持久化到数据库
            statisticRepository.save(statisticReport);
        }
        System.out.println("------------------------test end -------------------");
    }


    @Scheduled(cron = "0 0 3 * * ?") // 每天执行一次
    // @Scheduled(cron = "0 0/5 * * * ?") // 每5分钟执行一次
    public void reportScheduler() throws Exception{
        //查询今天以前的数据
        Date currentDay=DateUtil.parseDate(DateUtil.formatCurrentDate());
        String currDayStr=DateUtil.format("yyyyMMdd",currentDay);
        Date beforeDay=DateUtil.addDays(currentDay,-15);
        String beforeDayStr=DateUtil.format("yyyyMMdd",beforeDay);
        List<StatisticReport> statisticReport=statisticRepository.getStatisticReport(beforeDayStr,currDayStr);

        //将今天的数据按加入到数据列
        String [] columnsName=new String[]{"日期","PV","搜索框点击率","banner点击率","金刚点击率","品牌公寓点击率",
                "合租推荐点击率","整租推荐点击率","分期模块点击率","整页点击率"};
        try {
            StringBuffer sbf=new StringBuffer();
            String subject="【监控日报】会找房H5用户数据统计"+DateUtil.format("yyyy.MM.dd",currentDay);
            //查询14天以内的所有数据
            sbf.append("以下是近14日，会找房H5【首页】的用户数据统计：");
            sbf.append("<br/>");
            sbf.append("<br/>");
            sbf.append("<table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;\">" +
                    " <tr><th>日期</th> <th >PV</th><th>搜索框点击率</th><th>banner点击率</th><th>金刚点击率</th><th>品牌公寓点击率</th><th>合租推荐点击率</th><th>整租推荐点击率</th><th>分期模块点击率</th><th>整页点击率</th></tr> ");

            List<Object[]> data=new ArrayList<>();
            for(int i=0;i<statisticReport.size();i++){
                Object [] obj=new Object[columnsName.length];
                StatisticReport sr=statisticReport.get(i);
                if(sr.getFpv()==0){
                    continue;
                }
                obj[0]= DateUtil.formatDate(DateUtil.parse("yyyyMMdd",sr.getStrTime()));
                DecimalFormat df= new DecimalFormat("#0.00");
                obj[1]= sr.getFpv();
                obj[2]= df.format ((sr.getFss()*100.0)/sr.getFpv())+"%";
                obj[3]= df.format ((sr.getFbdj()*100.0)/sr.getFpv())+"%";
                obj[4]= df.format ((sr.getFjg()*100.0)/sr.getFpv())+"%";
                obj[5]= df.format ((sr.getFppgy()*100.0)/sr.getFpv())+"%";
                obj[6]= df.format ((sr.getFhz()*100.0)/sr.getFpv())+"%";
                obj[7]= df.format ((sr.getFzz()*100.0)/sr.getFpv())+"%";
                obj[8]= df.format ((sr.getFfq()*100.0)/sr.getFpv())+"%";
                obj[9]= df.format ((sr.getFtol()*100.0)/sr.getFpv())+"%";
                sbf.append("<tr>");
                sbf.append("<td width=\"10%\" height=\"8%\">").append(obj[0]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[1]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[2]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[3]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[4]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[5]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[6]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[7]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[8]).append("</td>");
                sbf.append("<td width=\"10%\">").append(obj[9]).append("</td>");
                sbf.append("</tr>");
                data.add(obj);
            }
            /*File file=new File(globalConf.xlsPath);
            if(!file.exists()){
                file.createNewFile();
            }else{
                file.delete();
            }
            FileOutputStream fos=new FileOutputStream(globalConf.xlsPath);
            ExcelUtil.exportExcel(fos,subject,columnsName,data,"");
            fos.close(); */
            sbf.append("</table>");

            //FileInputStream fis=new FileInputStream(globalConf.xlsPath);
            //logger.info("邮件内容 ： " +sbf.toString());
            SendEmailUtil.sendTextMail(subject,sbf.toString(),globalConf.wxToMails);
           // fis.close();
            logger.info("邮件内容已发送 --> by arison");
            //  sendMain();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}