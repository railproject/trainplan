package org.railway.com.trainplan.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;

public class StattionUtils {

	
	public static void main(String[] args){
		/******第一种情况:02677-2678/5-2676/7-02678 ***************************/
		/*Station station11 = new Station("桂林北一场","2014-06-24 08:36:00");
		Station station12 = new Station("桂林北二场","2014-06-24 08:40:00");
		Station station13 = new Station("桂林","2014-06-24 08:46:00");
		
		LinkedList<Station>  trainList1 = new LinkedList<Station>();
		trainList1.add(station11);
		trainList1.add(station12);
		trainList1.add(station13);
		
		Station station21 = new Station("桂林","2014-06-24 09:25:00");
		Station station22 = new Station("桂林北二场","2014-06-24 09:32:00");
		Station station23 = new Station("桂林北一场","2014-06-24 09:34:00");
		Station station24 = new Station("灵川","2014-06-24 09:39:00");
		Station station26 = new Station("井山","2014-06-24 18:22:00");
		Station station27 = new Station("钟山","2014-06-24 18:41:00");
		Station station28 = new Station("贺州","2014-06-24 18:57:00");
		
		LinkedList<Station>  trainList2 = new LinkedList<Station>();
		trainList2.add(station21);
		trainList2.add(station22);
		trainList2.add(station23);
		trainList2.add(station24);
		trainList2.add(station26);
		trainList2.add(station27);
		trainList2.add(station28);
		
		
		Station station31 = new Station("贺州","2014-06-25 08:00:00");
		Station station32 = new Station("钟山","2014-06-25 08:18:00");
		Station station33 = new Station("井山","2014-06-25 08:32:00");
		Station station34 = new Station("灵川","2014-06-25 16:40:00");
		Station station35 = new Station("桂林北一场","2014-06-25 17:10:00");
		Station station36 = new Station("桂林北二场","2014-06-25 17:14:00");
		Station station37 = new Station("桂林","2014-06-25 17:20:00");
		
		LinkedList<Station>  trainList3 = new LinkedList<Station>();
		trainList3.add(station31);
		trainList3.add(station32);
		trainList3.add(station33);
		trainList3.add(station34);
		trainList3.add(station35);
		trainList3.add(station36);
		trainList3.add(station37);
		
		Station station41 = new Station("桂林","2014-06-25 18:12:00");
		Station station42 = new Station("桂林北二场","2014-06-25 18:19:00");
		Station station43 = new Station("桂林北一场","2014-06-25 18:22:00");
	
		LinkedList<Station>  trainList4 = new LinkedList<Station>();
		trainList4.add(station41);
		trainList4.add(station42);
		trainList4.add(station43);
		
		List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
		list1.add(trainList1);
		list1.add(trainList2);
		list1.add(trainList3);
		list1.add(trainList4);*/
		
		
		/******第二种情况05505-5505-5506-05506   **************/
		/*Station station11 = new Station("桂林北一场","2014-06-24 08:15:00");
		Station station12 = new Station("桂林北二场","2014-06-24 08:19:00");
		Station station13 = new Station("桂林","2014-06-24 08:25:00");
		
		LinkedList<Station>  trainList1 = new LinkedList<Station>();
		trainList1.add(station11);
		trainList1.add(station12);
		trainList1.add(station13);
		
		Station station21 = new Station("桂林","2014-06-24 09:10:00");
		Station station22 = new Station("二塘","2014-06-24 09:19:00");
		Station station23 = new Station("横山","2014-06-24 09:27:00");
		Station station24 = new Station("大溪河","2014-06-24 09:33:00");
		Station station26 = new Station("塘堡","2014-06-24 09:38:00");
		Station station27 = new Station("化州","2014-06-24 19:31:00");
		Station station28 = new Station("山底岭","2014-06-24 20:07:00");
		Station station29 = new Station("茂名","2014-06-24 20:20:00");
		
		LinkedList<Station>  trainList2 = new LinkedList<Station>();
		trainList2.add(station21);
		trainList2.add(station22);
		trainList2.add(station23);
		trainList2.add(station24);
		trainList2.add(station26);
		trainList2.add(station27);
		trainList2.add(station28);
		trainList2.add(station29);
		
		
		Station station31 = new Station("茂名","2014-06-25 08:45:00");
		Station station32 = new Station("山底岭","2014-06-25 08:57:00");
		Station station33 = new Station("化州","2014-06-25 09:09:00");
		Station station34 = new Station("塘堡","2014-06-25 18:34:00");
		Station station35 = new Station("大溪河","2014-06-25 18:42:00");
		Station station36 = new Station("横山","2014-06-25 19:06:00");
		Station station37 = new Station("二塘","2014-06-25 19:15:00");
		Station station38 = new Station("桂林","2014-06-25 19:25:00");
		
		LinkedList<Station>  trainList3 = new LinkedList<Station>();
		trainList3.add(station31);
		trainList3.add(station32);
		trainList3.add(station33);
		trainList3.add(station34);
		trainList3.add(station35);
		trainList3.add(station36);
		trainList3.add(station37);
		trainList3.add(station38);
		
		
		Station station41 = new Station("桂林","2014-06-25 20:02:00");
		Station station42 = new Station("桂林北二场","2014-06-25 20:09:00");
		Station station43 = new Station("桂林北一场","2014-06-25 20:12:00");
	
		LinkedList<Station>  trainList4 = new LinkedList<Station>();
		trainList4.add(station41);
		trainList4.add(station42);
		trainList4.add(station43);
		
		List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
		list1.add(trainList1);
		list1.add(trainList2);
		list1.add(trainList3);
		list1.add(trainList4);*/
		
		
		/**
		 * *****************************
齐齐哈尔|2014-06-24 17:44:00|0
齐齐哈尔南场|2014-06-24 17:48:30|BT
大民屯|2014-06-24 17:53:30|BT
榆树屯|2014-06-24 18:09:00|TZ
三间房东场|2014-06-24 18:19:00|BT
汤池|2014-06-24 18:26:30|BT
大兴|2014-06-24 18:39:30|BT
江桥|2014-06-24 18:57:30|BT
平洋|2014-06-24 19:27:30|BT
泰来|2014-06-24 20:12:00|FJK
街基|2014-06-24 20:20:30|BT
坦途|2014-06-24 20:29:30|BT
镇赉|2014-06-24 20:58:00|TZ
白城|2014-06-24 21:39:00|TZ
穆家店|2014-06-24 21:47:00|BT
洮南|2014-06-24 22:05:00|TZ
黑水|2014-06-24 22:20:00|BT
开通|2014-06-24 22:59:00|TZ
太平川|2014-06-24 23:40:00|TZ
保康|2014-06-25 00:06:00|TZ
茂林|2014-06-25 00:18:00|BT
卧虎屯|2014-06-25 00:39:00|BT
大土山|2014-06-25 00:46:00|BT
山场屯|2014-06-25 00:52:00|BT
郑家屯|2014-06-25 01:06:00|TZ
金宝屯|2014-06-25 01:18:00|BT
三江口|2014-06-25 01:26:00|BT
曲家店|2014-06-25 01:41:00|BT
八面城|2014-06-25 01:52:00|TZ
泉沟|2014-06-25 02:07:00|BT
四平|2014-06-25 02:25:00|TZ
毛家店|2014-06-25 02:36:30|BT
双庙子|2014-06-25 02:43:30|BT
昌图|2014-06-25 02:56:30|BT
马仲河|2014-06-25 03:02:30|BT
开原|2014-06-25 03:16:00|TZ
高台子|2014-06-25 03:20:00|BT
中固|2014-06-25 03:25:00|BT
平顶堡|2014-06-25 03:31:00|BT
铁岭|2014-06-25 03:37:00|BT
得胜台|2014-06-25 03:42:30|BT
乱石山|2014-06-25 03:48:30|BT
新台子|2014-06-25 03:53:30|BT
新城子|2014-06-25 04:01:00|BT
虎石台|2014-06-25 04:08:30|BT
文官屯|2014-06-25 04:29:00|TZ
沈阳北|2014-06-25 04:56:00|TZ
皇姑屯|2014-06-25 05:02:00|BT
辽中|2014-06-25 05:37:00|BT
台安|2014-06-25 05:54:00|BT
盘锦北|2014-06-25 06:26:00|TZ
锦州南|2014-06-25 07:04:00|BT
葫芦岛北|2014-06-25 07:35:00|TZ
绥中北|2014-06-25 08:14:00|BT
东戴河|2014-06-25 08:47:00|BT
山海关|2014-06-25 09:06:00|FJK
龙家营线路所|2014-06-25 09:16:00|BT
龙家营西咽喉|2014-06-25 09:20:00|BT
秦皇岛|2014-06-25 09:27:00|TZ
北戴河|2014-06-25 09:42:30|BT
留守营|2014-06-25 09:49:30|BT
张家庄|2014-06-25 09:55:00|BT
昌黎|2014-06-25 10:12:00|TZ
后封台|2014-06-25 10:19:30|BT
九龙山|2014-06-25 10:23:00|BT
石门|2014-06-25 10:29:45|BT
朱各庄|2014-06-25 10:32:30|BT
滦县东|2014-06-25 10:35:45|BT
滦县|2014-06-25 10:38:00|FJK
杨各庄|2014-06-25 10:58:00|TZ
马柳|2014-06-25 11:05:45|BT
福山寺|2014-06-25 11:10:00|BT
石郎庄|2014-06-25 11:13:45|BT
狼窝铺|2014-06-25 11:18:15|BT
唐山东|2014-06-25 11:26:15|BT
杨家口|2014-06-25 11:32:30|BT
唐山|2014-06-25 11:55:00|TZ
丰南|2014-06-25 12:01:00|BT
七道桥|2014-06-25 12:04:15|BT
田庄|2014-06-25 12:14:45|BT
芦台|2014-06-25 12:22:15|BT
汉沽|2014-06-25 12:26:00|BT
茶淀|2014-06-25 13:02:00|TZ
北塘|2014-06-25 13:11:00|BT
183线路所|2014-06-25 13:14:15|BT
塘沽|2014-06-25 13:20:00|TZ
十三冶线路所|2014-06-25 13:28:45|BT
军粮城|2014-06-25 13:32:30|BT
张贵庄|2014-06-25 13:42:00|BT
天津四号楼|2014-06-25 13:51:30|BT
天津普速场|2014-06-25 14:01:00|0
*****************************
天津普速场|2014-06-25 15:51:00|0
天津四号楼|2014-06-25 15:59:00|BT
张贵庄|2014-06-25 16:06:30|BT
军粮城|2014-06-25 16:13:00|BT
十三冶线路所|2014-06-25 16:14:45|BT
塘沽|2014-06-25 16:21:30|BT
183线路所|2014-06-25 16:25:00|BT
北塘|2014-06-25 16:28:30|BT
茶淀|2014-06-25 16:35:30|BT
汉沽|2014-06-25 16:40:15|BT
芦台|2014-06-25 16:44:00|BT
田庄|2014-06-25 16:50:30|BT
七道桥|2014-06-25 17:00:00|BT
丰南|2014-06-25 17:05:15|BT
唐山|2014-06-25 17:17:00|TZ
杨家口|2014-06-25 17:25:00|BT
唐山东|2014-06-25 17:31:15|BT
狼窝铺|2014-06-25 17:38:15|BT
石郎庄|2014-06-25 17:41:45|BT
福山寺|2014-06-25 17:45:30|BT
马柳|2014-06-25 17:49:45|BT
杨各庄|2014-06-25 17:54:45|BT
滦县|2014-06-25 18:01:00|FJK
滦县东|2014-06-25 18:03:15|BT
朱各庄|2014-06-25 18:06:15|BT
石门|2014-06-25 18:09:00|BT
九龙山|2014-06-25 18:15:30|BT
后封台|2014-06-25 18:19:00|BT
昌黎|2014-06-25 18:30:00|TZ
张家庄|2014-06-25 18:35:30|BT
留守营|2014-06-25 18:41:30|BT
北戴河|2014-06-25 18:55:00|TZ
秦皇岛|2014-06-25 19:19:00|TZ
龙家营|2014-06-25 19:31:00|BT
山海关|2014-06-25 19:51:00|FJK
山海关运转场|2014-06-25 20:00:00|BT
万家屯|2014-06-25 20:09:00|BT
高岭|2014-06-25 20:30:00|BT
前卫|2014-06-25 20:56:00|TZ
绥中|2014-06-25 21:17:00|TZ
东辛庄|2014-06-25 21:25:00|BT
沙后所|2014-06-25 21:34:00|BT
大甸|2014-06-25 21:42:00|BT
兴城|2014-06-25 21:45:00|BT
葫芦岛|2014-06-25 22:03:00|TZ
塔山|2014-06-25 22:12:00|BT
高桥镇|2014-06-25 22:19:00|BT
女儿河|2014-06-25 22:31:00|BT
桃园|2014-06-25 22:35:00|BT
锦州|2014-06-25 22:49:00|TZ
锦州上直通场|2014-06-25 22:54:00|BT
双羊店|2014-06-25 22:58:00|BT
凌海|2014-06-25 23:05:00|BT
红旗|2014-06-25 23:09:00|BT
石山|2014-06-25 23:14:00|BT
沟帮子|2014-06-25 23:31:00|TZ
青堆子|2014-06-25 23:42:00|BT
高山子|2014-06-25 23:51:00|BT
大虎山|2014-06-25 23:57:00|BT
唐家|2014-06-26 00:05:00|BT
绕阳河|2014-06-26 00:16:00|BT
大红旗|2014-06-26 00:25:00|BT
新民|2014-06-26 00:37:00|BT
高台山|2014-06-26 00:41:00|BT
兴隆店|2014-06-26 00:52:00|BT
三台|2014-06-26 00:59:00|BT
马三家|2014-06-26 02:00:00|TZ
永安|2014-06-26 02:09:00|BT
大成|2014-06-26 02:14:00|BT
皇姑屯|2014-06-26 02:20:00|BT
沈阳北|2014-06-26 02:41:00|TZ
文官屯|2014-06-26 02:48:30|BT
虎石台|2014-06-26 02:53:00|BT
新城子|2014-06-26 02:59:30|BT
新台子|2014-06-26 03:06:00|BT
乱石山|2014-06-26 03:34:00|TZ
得胜台|2014-06-26 03:41:00|BT
铁岭|2014-06-26 03:46:30|BT
平顶堡|2014-06-26 03:52:30|BT
中固|2014-06-26 03:58:30|BT
高台子|2014-06-26 04:03:00|BT
开原|2014-06-26 04:05:00|BT
马仲河|2014-06-26 04:15:30|BT
昌图|2014-06-26 04:23:00|BT
双庙子|2014-06-26 04:50:00|TZ
毛家店|2014-06-26 05:01:00|BT
四平|2014-06-26 05:19:00|TZ
泉沟|2014-06-26 05:29:00|BT
八面城|2014-06-26 05:42:00|TZ
曲家店|2014-06-26 05:49:00|BT
三江口|2014-06-26 06:06:00|BT
金宝屯|2014-06-26 06:16:00|BT
郑家屯|2014-06-26 06:34:00|TZ
山场屯|2014-06-26 06:40:00|BT
大土山|2014-06-26 06:45:00|BT
卧虎屯|2014-06-26 06:52:00|BT
茂林|2014-06-26 07:14:00|BT
保康|2014-06-26 07:30:00|TZ
太平川|2014-06-26 08:00:00|TZ
开通|2014-06-26 08:36:00|TZ
黑水|2014-06-26 09:09:00|BT
洮南|2014-06-26 09:24:00|TZ
穆家店|2014-06-26 09:43:00|BT
白城|2014-06-26 10:04:00|TZ
镇赉|2014-06-26 10:34:00|TZ
坦途|2014-06-26 10:59:30|BT
街基|2014-06-26 11:10:30|BT
泰来|2014-06-26 11:30:00|FJK
平洋|2014-06-26 11:49:00|BT
江桥|2014-06-26 12:09:00|TZ
大兴|2014-06-26 12:25:00|TZ
汤池|2014-06-26 12:42:00|TZ
三间房西场|2014-06-26 12:59:00|TZ
榆树屯|2014-06-26 13:51:00|TZ
大民屯|2014-06-26 14:01:00|BT
齐齐哈尔南场|2014-06-26 14:05:00|BT
齐齐哈尔|2014-06-26 14:11:00|0
*****************************
齐齐哈尔|2014-06-26 16:42:00|0
齐齐哈尔南场|2014-06-26 16:48:30|BT
大民屯|2014-06-26 16:53:30|BT
榆树屯|2014-06-26 17:00:00|BT
红旗营|2014-06-26 17:04:00|BT
烟筒屯|2014-06-26 17:18:00|BT
泰康|2014-06-26 17:57:00|TZ
高家|2014-06-26 18:07:00|BT
喇嘛甸|2014-06-26 18:18:00|BT
让湖路西|2014-06-26 18:23:00|BT
大庆西|2014-06-26 18:47:00|TZ
壮志|2014-06-26 18:54:30|BT
独立屯|2014-06-26 19:01:00|BT
银浪|2014-06-26 19:14:00|TZ
创业村|2014-06-26 19:24:00|BT
林源|2014-06-26 19:31:00|TZ
八村|2014-06-26 19:37:00|BT
兴无|2014-06-26 19:44:00|BT
新华屯|2014-06-26 20:12:00|TZ
向阳村|2014-06-26 20:23:00|BT
立志|2014-06-26 20:30:30|BT
太阳升|2014-06-26 20:43:00|FJK
劳动屯|2014-06-26 20:50:00|BT
新肇|2014-06-26 21:06:00|TZ
他石海|2014-06-26 21:20:00|BT
大安北|2014-06-26 21:50:00|TZ
大安|2014-06-26 21:56:00|BT
长山屯|2014-06-26 22:12:00|BT
通途|2014-06-26 22:27:00|BT
松原西|2014-06-26 22:32:00|BT
松原|2014-06-26 22:42:00|TZ
七家子|2014-06-26 22:54:00|BT
王府|2014-06-26 23:10:00|BT
哈拉海|2014-06-26 23:25:00|BT
柴岗|2014-06-26 23:34:00|BT
农安|2014-06-26 23:49:00|TZ
华家|2014-06-27 00:01:00|BT
开安|2014-06-27 00:10:00|BT
小合隆|2014-06-27 00:19:00|BT
长春北|2014-06-27 00:31:00|BT
长春普速场|2014-06-27 00:54:00|TZ
长春南|2014-06-27 01:02:30|BT
大屯|2014-06-27 01:08:30|BT
范家屯|2014-06-27 01:14:00|BT
陶家屯|2014-06-27 01:19:30|BT
公主岭|2014-06-27 01:31:00|BT
大榆树|2014-06-27 01:35:00|BT
蔡家|2014-06-27 01:39:30|BT
郭家店|2014-06-27 01:45:00|BT
十家堡|2014-06-27 01:52:00|BT
四平|2014-06-27 02:08:00|TZ
毛家店|2014-06-27 02:19:30|BT
双庙子|2014-06-27 02:26:30|BT
昌图|2014-06-27 02:39:30|BT
马仲河|2014-06-27 02:45:30|BT
开原|2014-06-27 02:56:00|BT
高台子|2014-06-27 02:58:00|BT
中固|2014-06-27 03:03:00|BT
平顶堡|2014-06-27 03:09:00|BT
铁岭|2014-06-27 03:18:00|TZ
得胜台|2014-06-27 03:25:30|BT
乱石山|2014-06-27 03:31:30|BT
新台子|2014-06-27 03:36:30|BT
新城子|2014-06-27 03:44:00|BT
虎石台|2014-06-27 03:51:30|BT
文官屯|2014-06-27 03:59:00|BT
沈阳北|2014-06-27 04:07:00|BT
沈阳|2014-06-27 04:27:00|TZ
浑河|2014-06-27 04:36:00|BT
上苏北|2014-06-27 04:41:00|BT
苏家屯客场|2014-06-27 04:43:00|BT
林盛堡|2014-06-27 04:49:00|BT
灯塔|2014-06-27 04:58:00|BT
张台子|2014-06-27 05:03:00|BT
辽阳|2014-06-27 05:16:00|TZ
首山|2014-06-27 05:24:00|BT
灵山乘降所|2014-06-27 05:26:00|BT
小乐屯|2014-06-27 05:28:00|BT
鞍山|2014-06-27 05:40:00|TZ
旧堡|2014-06-27 05:46:00|BT
汤岗子|2014-06-27 05:52:30|BT
南台|2014-06-27 05:59:00|BT
海城|2014-06-27 06:09:00|TZ
唐王山|2014-06-27 06:15:00|BT
葫芦峪|2014-06-27 06:19:00|BT
他山|2014-06-27 06:23:30|BT
分水|2014-06-27 06:30:30|BT
大石桥|2014-06-27 06:42:00|TZ
大石桥南|2014-06-27 06:46:00|BT
盖州|2014-06-27 07:04:00|TZ
沙岗|2014-06-27 07:12:00|BT
芦家屯|2014-06-27 07:18:00|BT
熊岳城|2014-06-27 07:27:00|TZ
九寨|2014-06-27 07:35:00|BT
许家屯|2014-06-27 07:43:00|TZ
万家岭|2014-06-27 07:53:00|BT
松树|2014-06-27 08:03:30|BT
得利寺|2014-06-27 08:08:00|BT
王家|2014-06-27 08:16:00|BT
瓦房店|2014-06-27 08:26:00|TZ
田家|2014-06-27 08:34:30|BT
普兰店|2014-06-27 08:48:00|TZ
石河|2014-06-27 08:56:30|BT
三十里堡|2014-06-27 09:02:30|BT
二十里台|2014-06-27 09:08:30|BT
金州|2014-06-27 09:22:00|TZ
大连北|2014-06-27 09:35:30|BT
南关岭|2014-06-27 09:37:00|BT
十三公里|2014-06-27 09:40:00|BT
周水子|2014-06-27 09:44:00|BT
沙河口|2014-06-27 09:49:00|BT
大连|2014-06-27 09:55:00|0
*****************************
大连|2014-06-27 14:29:00|0
沙河口|2014-06-27 14:35:00|BT
周水子|2014-06-27 14:39:00|BT
十三公里|2014-06-27 14:42:00|BT
南关岭|2014-06-27 14:45:00|BT
大连北|2014-06-27 14:46:30|BT
金州|2014-06-27 14:59:00|TZ
二十里台|2014-06-27 15:11:00|BT
三十里堡|2014-06-27 15:17:00|BT
石河|2014-06-27 15:23:00|BT
普兰店|2014-06-27 15:30:30|BT
田家|2014-06-27 15:40:30|BT
瓦房店|2014-06-27 15:51:00|TZ
王家|2014-06-27 15:58:00|BT
得利寺|2014-06-27 16:05:00|BT
松树|2014-06-27 16:09:30|BT
万家岭|2014-06-27 16:20:00|BT
许家屯|2014-06-27 16:31:00|TZ
九寨|2014-06-27 16:37:30|BT
熊岳城|2014-06-27 16:43:30|BT
芦家屯|2014-06-27 16:49:00|BT
沙岗|2014-06-27 16:55:00|BT
盖州|2014-06-27 17:04:00|TZ
大石桥南|2014-06-27 17:23:00|BT
大石桥|2014-06-27 17:31:00|TZ
分水|2014-06-27 17:39:00|BT
他山|2014-06-27 17:44:00|BT
葫芦峪|2014-06-27 17:46:30|BT
唐王山|2014-06-27 17:50:00|BT
海城|2014-06-27 17:57:00|TZ
南台|2014-06-27 18:04:30|BT
汤岗子|2014-06-27 18:11:00|BT
旧堡|2014-06-27 18:17:30|BT
鞍山|2014-06-27 18:27:00|TZ
小乐屯|2014-06-27 18:35:00|BT
灵山乘降所|2014-06-27 18:36:00|BT
首山|2014-06-27 18:38:00|BT
辽阳|2014-06-27 18:48:00|TZ
张台子|2014-06-27 18:58:00|BT
灯塔|2014-06-27 19:03:00|BT
林盛堡|2014-06-27 19:15:00|BT
苏家屯客场|2014-06-27 19:24:00|TZ
苏家屯下到场|2014-06-27 19:27:00|BT
下苏北|2014-06-27 19:29:00|BT
浑河|2014-06-27 19:33:00|BT
沈阳|2014-06-27 19:50:00|TZ
沈阳北|2014-06-27 19:56:00|BT
文官屯|2014-06-27 20:01:30|BT
虎石台|2014-06-27 20:06:00|BT
新城子|2014-06-27 20:12:30|BT
新台子|2014-06-27 20:19:00|BT
乱石山|2014-06-27 20:23:00|BT
得胜台|2014-06-27 20:28:00|BT
铁岭|2014-06-27 20:37:00|TZ
平顶堡|2014-06-27 20:45:00|BT
中固|2014-06-27 20:51:00|BT
高台子|2014-06-27 20:55:30|BT
开原|2014-06-27 20:57:30|BT
马仲河|2014-06-27 21:08:00|BT
昌图|2014-06-27 21:27:00|TZ
双庙子|2014-06-27 21:43:30|BT
毛家店|2014-06-27 21:52:30|BT
四平|2014-06-27 22:08:00|TZ
十家堡|2014-06-27 22:19:00|BT
郭家店|2014-06-27 22:25:00|BT
蔡家|2014-06-27 22:30:30|BT
大榆树|2014-06-27 22:35:00|BT
公主岭|2014-06-27 22:39:30|BT
陶家屯|2014-06-27 22:51:00|BT
范家屯|2014-06-27 22:56:30|BT
大屯|2014-06-27 23:04:00|BT
长春南|2014-06-27 23:12:00|BT
长春普速场|2014-06-27 23:36:00|TZ
长春北|2014-06-27 23:44:00|BT
小合隆|2014-06-27 23:56:00|BT
开安|2014-06-28 00:12:00|TZ
华家|2014-06-28 00:23:00|BT
农安|2014-06-28 00:37:00|TZ
柴岗|2014-06-28 00:57:00|TZ
哈拉海|2014-06-28 01:07:00|BT
王府|2014-06-28 01:22:00|BT
七家子|2014-06-28 01:37:00|BT
松原|2014-06-28 01:47:00|BT
松原西|2014-06-28 01:53:00|BT
通途|2014-06-28 01:58:00|BT
长山屯|2014-06-28 02:12:00|BT
大安|2014-06-28 02:29:00|BT
大安北|2014-06-28 03:04:00|TZ
他石海|2014-06-28 03:12:00|BT
新肇|2014-06-28 03:27:00|TZ
劳动屯|2014-06-28 03:41:00|BT
太阳升|2014-06-28 03:50:00|FJK
立志|2014-06-28 03:57:00|BT
向阳村|2014-06-28 04:02:30|BT
新华屯|2014-06-28 04:13:00|TZ
兴无|2014-06-28 04:20:30|BT
八村|2014-06-28 04:27:30|BT
林源|2014-06-28 04:35:00|TZ
创业村|2014-06-28 04:42:00|BT
银浪|2014-06-28 04:50:00|BT
独立屯|2014-06-28 04:57:00|BT
壮志|2014-06-28 05:04:00|BT
大庆西|2014-06-28 05:31:00|TZ
让湖路西|2014-06-28 05:35:30|BT
喇嘛甸|2014-06-28 05:40:30|BT
高家|2014-06-28 05:53:30|BT
泰康|2014-06-28 06:04:00|TZ
烟筒屯|2014-06-28 06:23:30|BT
红旗营|2014-06-28 06:42:00|TZ
榆树屯|2014-06-28 06:48:00|BT
大民屯|2014-06-28 06:58:00|BT
齐齐哈尔南场|2014-06-28 07:05:00|BT
齐齐哈尔|2014-06-28 07:12:00|0
		 */
		
		
		
		
		/*PlanLineGrid grid = getPlanLineGridForAll(list1,"2014-06-23","2014-06-25");
		List<PlanLineGridY> yList = grid.getCrossStns();
		for(PlanLineGridY y : yList ){
			System.err.println("" + y.getStnName() );
		}*/
		
	}
	
	/**
	 * 判断两列车的list对象是否是一个顺序的
	 * @param stationList1
	 * @param stationList2
	 * @return
	 */
	public static boolean isSameDirectionTrainList(List<Station> stationList1,List<Station> stationList2){
		boolean isSame = false;
		int list1Index1=0;
		int list1Index2=0;
		int list2Index1=0;
		int list2Index2=0;
		int count = 0;
		for(Station  station : stationList1){
			if(count==2){
				break;
			}
			if(stationList2.contains(station)){
				if(count==0){
					list1Index1 = stationList1.indexOf(station);
					list2Index1 = stationList2.indexOf(station);
				}else{
					list1Index2 = stationList1.indexOf(station);
					list2Index2 = stationList2.indexOf(station);
				}
				
				count++;
			}
		}
		
		int temp1 = list1Index1 - list1Index2;
		int temp2 = list2Index1 - list2Index2;
		if(temp1 <0 && temp2<0){
			isSame = true;
		}else if(temp1>0&& temp2>0){
			isSame = true;
		}else{
			isSame = false;
		}
		return isSame;
	}
	
	/**
	 * 判断列车是否是同一个方向
	 * @return
	 */
	public static boolean isSameDirection(List<Station> stationList1,List<Station> stationList2){
		boolean result = true;
		int sameStationCount = 0;
		for(int i = 0;i<stationList1.size();i++){
			if(sameStationCount >=2){
				break;
			}
			Station station1 = stationList1.get(i);
			for(int j = 0;j<stationList2.size();j++){
				Station station2 = stationList2.get(j);
				if(station1.equals(station2)){
					sameStationCount++;
					break;
				}
			}
		}
		if(sameStationCount > 1){
			result = false;
		}
		return result;
	}
	/**
	 * 合并同方向的两车的经由站
	 * @param longList
	 * @param shortList
	 * @return
	 */
	public static List<Station> mergeStationTheSameDirection(List<Station> longList,List<Station> shortList){
		List<Station> result = new LinkedList<Station>();
		result.addAll(longList);
		for(Station station :shortList){
			if(!result.contains(station)){
				result.add(station);
			}
		}
		return result;
		
	}
	
	/**
	 * 合并反方向的两列列车的经由站
	 * @param longList
	 * @param shortList
	 * @return
	 */
	public static List<Station> mergeStation(List<Station> longList,List<Station> shortList)
	{
		if(!isSameDirectionTrainList(longList, shortList)){
			//方向调整成一样的
			Collections.reverse(shortList);
		}
		
		List<Station> result = new LinkedList<Station>();
		
		Station longListStation = null;
		
		Station shortListStaion = null;
		//记录当发现longList中有元素在shortList中没有时，result中最后一个元素的index
	    int resultIndex = 0;
	  //存放shortList中有，而longList中没有的元素
		List<Station> tempShortList = new LinkedList<Station>();
		//存放shortList中无，而longList中有的元素
		List<Station> tempLongList = new LinkedList<Station>();
		/**********************************/
		for(int i = 0;i<longList.size();i++){
			Station current = longList.get(i);
			
			if(!shortList.contains(current) ){
				if(result.size() != 0){
					resultIndex = 	result.size() -1 ;
					
				}
				//longListStation = current;
				tempLongList.add(current);
			}else{
				
				int indexCurrent = shortList.indexOf(current);
				
				//在shortList中的当前被匹配到的元素
				Station currentShort = shortList.get(indexCurrent);
				if(tempLongList.size()==0){
					longListStation = current;
				}
				if(indexCurrent == 0){
					if(tempLongList !=null && tempLongList.size() > 0){
						result.addAll(tempLongList);
					}
					result.add(currentShort);
					shortListStaion = currentShort;
					shortList.remove(indexCurrent);
					tempLongList.clear();
					tempShortList.clear();
				}else{ 
					
					for(int k = 0;k<indexCurrent;k++){
						tempShortList.add(shortList.get(k));
					}
					
					
					//排序tempLongList和tempShortList中的数据，然后将排好循序的列表加在result的resultIndex后面
					List<Station> sortedList = sortStations(tempLongList,tempShortList,longListStation,shortListStaion);
					result.addAll(sortedList);
					result.add(current);
					shortList.removeAll(tempShortList);
					shortList.remove(currentShort);
					
					longListStation = current;
					shortListStaion = currentShort;
					tempLongList.clear();
					tempShortList.clear();
				}
	
			}
		}
		if(tempLongList != null && tempLongList.size() > 0 ){
			result.addAll(tempLongList);
		}
		/*if(shortList != null && shortList.size() > 0 ){
			for(int j=0;j<shortList.size();j++){
				result.add(shortList.get(j));
			}
		}
		*/
		
		/**********************************/
		
		return result;
	}
	
	

	
	
	private static List<Station>   sortStations(List<Station> longList,List<Station> shortList,Station longStation,Station shortStation){
		List<Station> result = new LinkedList<Station>();
		if(longList != null && longList.size() > 0){
			String dpttime = longStation.getDptTime();
			for(Station stationLong : longList){
				int minites =  Math.abs(DateUtil.getBetweenMinite(stationLong.getDptTime(), dpttime));
				stationLong.setMinites(minites);
				result.add(stationLong);
			}
		}
		
		if(shortList != null && shortList.size() > 0){
			String dpttime = shortStation.getDptTime();
			for(Station  stationShort : shortList){
				int minites =  Math.abs(DateUtil.getBetweenMinite(stationShort.getDptTime(), dpttime));
				stationShort.setMinites(minites);	
				result.add(stationShort);
			}
		}
		//排序
		ComparatorStation comparator = new ComparatorStation();
		Collections.sort(result, comparator);
		return result;
	}
	
	private static PlanLineGrid getPlanLineGridForAll(List<LinkedList<Station>> list1,String crossStartDate,String crossEndDate){
		//纵坐标
		 List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		 //横坐标
		 List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>(); 
		 
		 /****组装纵坐标****/
		 int trainSize = list1.size();
		 List<Station> mergeList = new LinkedList<Station>();
		 for(int i = 0;i<trainSize;i++){
			
			if(i==0){
				LinkedList<Station> listStation1 = list1.get(0);
				LinkedList<Station> listStation2 = list1.get(1);
				boolean isSameDirection = StattionUtils.isSameDirection(listStation1, listStation2);
				if(isSameDirection){
					mergeList = StattionUtils.mergeStationTheSameDirection(listStation1, listStation2);
				}else{
					
					if(listStation1.size() >=listStation2.size()){
						mergeList=StattionUtils.mergeStation(listStation1, listStation2);
					}else{
						mergeList=StattionUtils.mergeStation(listStation2, listStation1);
					}
					
				}
			}else if(i>1){
				LinkedList<Station> trainStationCurrentList = list1.get(i);
				boolean isSameDirection = StattionUtils.isSameDirection(trainStationCurrentList, mergeList);
				if(isSameDirection){
					mergeList = StattionUtils.mergeStationTheSameDirection(mergeList, trainStationCurrentList);
				}else{
					if(mergeList.size() >= trainStationCurrentList.size()){
						mergeList=StattionUtils.mergeStation(mergeList, trainStationCurrentList);
					}else{
						mergeList=StattionUtils.mergeStation(trainStationCurrentList, mergeList);	
					}
				}
				
			}
			
		 }
		  
			for(Station station : mergeList){
			    if(station != null){
			    	//planLineGridYList.add(new PlanLineGridY(stationName));
			    	planLineGridYList.add(new PlanLineGridY(
			    			station.getStnName(),
			    			0,
			    			station.getStationType()));
			    }
				
			}
					
			
		 
		/*****组装横坐标  *****/
		
		 LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossStartDate);
	     LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossEndDate));
	     while(!start.isAfter(end)) {
	            gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
	            start = start.plusDays(1);
	        }
	     
		 return new PlanLineGrid(gridXList, planLineGridYList);
	}
	
}
