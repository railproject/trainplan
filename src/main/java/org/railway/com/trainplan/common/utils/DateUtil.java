
package org.railway.com.trainplan.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;


public class DateUtil {
	public static final Long DATE_DAY_LONG = 1l;
	//一天时间得毫秒数
	public static final Long DATE_DAY_MILLISECOND = 24*60*60*1000*DATE_DAY_LONG;
	private static String defaultDatePattern = "yyyy-MM-dd hh:mm:ss";
	private static String defaultDatePattern1 = "yyyy-MM-dd";
	private static String defaultDatePattern2 = "yyyyMMdd";
	
	
	/**
	 * 
	 * @param startTime yyyy-MM-dd HH:mm
	 * @param endTime yyyy-MM-dd HH:mm
	 * @return
	 */
	public static String calcBetweenTwoTimes(String startTime,String endTime){
		String returnStr = "";
		try{
			
		
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   java.util.Date begin=dfs.parse(startTime);
		   java.util.Date end = dfs.parse(endTime);
		   long between=(end.getTime()-begin.getTime())/1000/60;//除以1000是为了转换成秒

		   long day1=between/(24*60);
		   long hour1=between%(24*60)/60;
		   long minute1=between%60;
		   if(day1==0){
			   if(hour1 ==0){
				   returnStr = minute1+"分";
			   }else{
				   returnStr =  hour1 + "小时" + minute1+"分";
			   }
		   }else{
			   returnStr = day1+"天" + hour1 + "小时" + minute1+"分";
		   }
		   
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnStr;
	}
	/**
	 * 使用预设格式将字符串转为Date
	 */
	public static Date parseDate(String strDate)  {
		Date date = null;
		try {
			date =  strDate == null ? null : parseDate(strDate,defaultDatePattern);
		} catch (ParseException e) {
			
		}
		return date;
	}
	
	/**
	 * 使用预设格式将字符串转为Date
	 */
	public static Date parse(String strDate)  {
		Date date = null;
		try {
			date =  strDate == null ? null : parseDate(strDate,defaultDatePattern1);
		} catch (ParseException e) {
			
		}
		return date;
	}
	/**
	 * 使用参数Format将字符串转为Date
	 */
	public static Date parseDate(String strDate, String pattern)
			throws ParseException {
		return strDate == null ? null : new SimpleDateFormat(
				pattern).parse(strDate);
	}
	
	/**
	 * 试用参数Format格式化Calendar成字符串
	 * 
	 * @param cal
	 * @param pattern
	 * @return
	 */
	public static String format(Calendar cal, String pattern) {
		return cal == null ? "" : new SimpleDateFormat(pattern).format(cal
				.getTime());
	}
	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String format(Date date, String pattern) {
		return date == null ? "" : new SimpleDateFormat(pattern).format(date);
	}
	/**
	 * 按照给定的Long型数据,获得指定格式的时间字符串
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getDateTime(Long time,String format){
		SimpleDateFormat ft = new SimpleDateFormat(format);
		return ft.format(time);
	}
	
	
	public static Long getNowStamp(){
		return (new Date().getTime())/1000;
	}
	
	
	
	
	
	
	
	/**
	 * 获取当前时间的前/后的一段时间
	 * @param day 前几天时间 (day等于0则取当前时间),day为正数则时间向前推，day为负数时间向后推
	 * @param strDate 输入的日期，格式yyyy-yy-dd
	 * @return 返回格式如：2012-10-10
	 */
	public static String getDateByDay(String strDate,int day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date data = new Date();
		String startTime = "";
		try {
			data = strDate == null ? null : sdf.parse(strDate);
			long time = data.getTime();
			//如果传入的参数为0 则取当前时间
			if(day<1 && day >-1){
				
			}else{
				long timeOld10 = DATE_DAY_MILLISECOND*day;
				time = time - timeOld10;
			}
		    startTime = sdf.format(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return startTime;
	}
	
	/**
	 * 获取yyyy-MM-dd HH:mm:ss格式的时间
	 * @param timestamp
	 * @return
	 */
	public static String getStringTimestamp(Long timestamp){
		Timestamp ts = new Timestamp(timestamp);  
        String tsStr = "";  
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        try {  
            //方法一  
            tsStr = sdf.format(ts);  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return tsStr;
	}
	
	/**
	 * 
	 * @param date 格式yyyyMMdd
	 * @return 格式yyyy-MM-dd
	 */
	public static String formateDate(String date){
		String returntime = "";
		try {
			returntime = format(parseDate(date,defaultDatePattern2),defaultDatePattern1);
		} catch (ParseException e) {
			
		}
		return returntime;
	}
	
	/**
	 * 给定开始日期和天数，生成日期队列
	 * @param startDate 格式yyyy-mm-dd
	 * @param dayCount 天数
	 * @return
	 */
	public static List<String> getDateListWithDayCount(String startDate,int dayCount){
		 List<String> days = new ArrayList<String>();
		 for(int i = 0;i<dayCount;i++){
			 days.add(getDateByDay(startDate,-i));
		 }
		// System.out.println("" + days);
		 return days;
	}
	
	/**
	 * 计算两个日期之间相隔的天数
	 * @param startDate格式为yyyy-MM-dd
	 * @param endDate格式为yyyy-MM-dd
	 * @return
	 */
	public static int getDaysBetween(String startDate,String endDate){
		DateTime start = new DateTime(startDate);
		DateTime end = new DateTime(endDate);
		return Days.daysBetween(start, end).getDays();
	}
	/**
	 * 将yyyyMMdd格式时间转为yyyy-MM-dd格式
	 * @param day 格式为yyyyMMdd
	 * @return 格式为yyyy-MM-dd
	 */
	public static String getFormateDay(String day){
		Date date = null;
		try {
			date = parseDate(day,defaultDatePattern2);
		} catch (ParseException e) {
			
			//e.printStackTrace();
		}
		return format(date,defaultDatePattern1);
	}
	public static void main(String[] args) {
		//System.out.println(getDuration(1310067170,1390267170));
		//System.out.println(System.currentTimeMillis()/1000);
		// System.err.println(calcBetweenTwoTimes("2014-05-01 23:58:00","2014-05-01 23:58:00"));
		//System.out.println(DateUtil.format(DateUtil.parse("20140501"), "yyyy-MM-dd"));
		//DateTime dt = new DateTime();
		//System.err.println( getDaysBetween("2014-02-27","2014-02-27"));
		LocalDate sourceDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate("20140519");
		System.err.println("" + sourceDate.toString("yyyy-MM-dd") );
	}
	
	
	
}



