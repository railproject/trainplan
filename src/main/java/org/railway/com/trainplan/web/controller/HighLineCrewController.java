package org.railway.com.trainplan.web.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.HighLineCrewInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.service.HighLineCrewService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

/**
 * Created by speeder on 2014/6/27.
 */
@RestController
@RequestMapping(value = "/crew/highline")
public class HighLineCrewController {

    private static Log logger = LogFactory.getLog(HighLineCrewController.class);

    @Autowired
    private HighLineCrewService highLineCrewService;

    @RequestMapping(value = "getHighLineCrew", method = RequestMethod.POST)
    public Result getHighLineCrew(@RequestBody Map<String,Object> reqMap) {
        logger.debug("getHighLineCrew:::::::");
        Result result = new Result(); 
        try{
        	Map<String, Object> params = Maps.newHashMap();
            params.put("crewHighlineId", StringUtil.objToStr(reqMap.get("crewHighLineId")));
            HighLineCrewInfo highLineCrewInfo = highLineCrewService.findHighLineCrew(params);	
            result.setData(highLineCrewInfo);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        
        return result;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public Result getHighLineCrewList(@RequestBody Map<String,Object> reqMap) {
        logger.debug("getHighLineCrewList:::::::");
        Result result = new Result(); 
        try{
        
        	String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
        	//将时间格式：yyyy-MM-dd转换成yyyyMMdd
        	crewDate = DateUtil.getFormateDayShort(crewDate);
        	String crewType = StringUtil.objToStr(reqMap.get("crewType"));
        	
             List<HighLineCrewInfo> list = highLineCrewService.findList(crewDate,crewType);
             result.setData(list);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
       
        return result;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addHighLineCrewInfo(@RequestBody HighLineCrewInfo highLineCrewInfo) {
        logger.debug("addHighLineCrewInfo:::::::");
        Result result = new Result(); 
        try{
        	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        	 highLineCrewInfo.setCrewHighlineId(UUID.randomUUID().toString());
             String crewDate = DateUtil.getFormateDayShort(highLineCrewInfo.getCrewDate());
             highLineCrewInfo.setCrewDate(crewDate);
             //所属局简称
             highLineCrewInfo.setCrewBureau(user.getBureauShortName());
         	 highLineCrewInfo.setRecordPeople(user.getName());
        	 highLineCrewInfo.setRecordPeopleOrg(user.getDeptName());
             highLineCrewService.addCrew(highLineCrewInfo);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        return result;
        
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public Result updateHighLineCrewInfo(@RequestBody HighLineCrewInfo highLineCrewInfo) {
        logger.debug("updateHighLineCrewInfo:::::::" + highLineCrewInfo.getCrewDate() + "|" + highLineCrewInfo.getCrewCross());
        Result result = new Result(); 
        try{
        	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        	//所属局简称
        	highLineCrewInfo.setCrewBureau(user.getBureauShortName());
        	highLineCrewInfo.setRecordPeople(user.getName());
        	highLineCrewInfo.setRecordPeopleOrg(user.getDeptName());
        	String crewDate = DateUtil.getFormateDayShort(highLineCrewInfo.getCrewDate());
            highLineCrewInfo.setCrewDate(crewDate);
        	highLineCrewService.update(highLineCrewInfo);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        return result;

    }

    /**
     * 批量删除highline_crew表中数据
     * @param reqMap
     * @return
     */
    @RequestMapping(value = "deleteHighLineCrewInfo", method = RequestMethod.DELETE)
    public Result deleteHighLineCrewInfo(@RequestBody Map<String,Object> reqMap ) {
        logger.debug("deleteHighLineCrewInfo:::::::");
        Result result = new Result(); 
        try{
            String crewHighLineId = StringUtil.objToStr(reqMap.get("crewHighLineId"));
            logger.debug("crewHighLineId==" + crewHighLineId);
            highLineCrewService.delete(crewHighLineId);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        return result;
    }
    
    /**
     * 更新字段submitType字段的值为1
     * @param reqMap
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "updateSubmitType", method = RequestMethod.POST)
    public Result updateSubmitType(@RequestBody Map<String,Object> reqMap){
    	Result result = new Result(); 
    	try{
    		logger.debug("updateSubmitType~~~~~reqMap="+reqMap);
    		String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
    		crewDate = DateUtil.getFormateDayShort(crewDate);
    		String crewType =  StringUtil.objToStr(reqMap.get("crewType"));
    		highLineCrewService.updateSubmitType(crewDate,crewType);
    	}catch(Exception e){
    		logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
    	}
    	return result;
    }
    /**
	 * 获取运行线信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getRunLineListForRunDate", method = RequestMethod.POST)
	public Result getRunLineListForRunDate(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
	    try{
	    	logger.debug("getRunLineListForRunDate~~~~~reqMap="+reqMap);
	    	String runDate = StringUtil.objToStr(reqMap.get("runDate"));
	    	//格式化时间
	    	runDate = DateUtil.getFormateDayShort(runDate);
	    	String trainNbr =  StringUtil.objToStr(reqMap.get("trainNbr"));
	    	String rownumstart =  StringUtil.objToStr(reqMap.get("rownumstart"));
	    	String rownumend =  StringUtil.objToStr(reqMap.get("rownumend"));
	    	QueryResult queryResult = highLineCrewService.getRunLineListForRunDate(runDate, "".equals(trainNbr)?null:trainNbr, rownumstart, rownumend);
	    	PagingResult page = new PagingResult(queryResult.getTotal(), queryResult.getRows());
	    	result.setData(page);
	    }catch(Exception e){
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	
	
	/**
	 * 根据日期获取乘务计划列表信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getHighlineCrewListForRunDate", method = RequestMethod.POST)
	public Result getHighlineCrewListForRunDate(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
	    try{
	    	logger.debug("getHighlineCrewListForRunDate~~~~~reqMap="+reqMap);
	    	String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
	    	//格式化时间
	    	crewDate = DateUtil.getFormateDayShort(crewDate);
	    	String crewType = StringUtil.objToStr(reqMap.get("crewType"));
	    	String rownumstart =  StringUtil.objToStr(reqMap.get("rownumstart"));
	    	String rownumend =  StringUtil.objToStr(reqMap.get("rownumend"));
	    	String trainNbr =  StringUtil.objToStr(reqMap.get("trainNbr"));
	    	QueryResult queryResult = highLineCrewService.getHighlineCrewListForRunDate(crewDate,crewType, trainNbr,rownumstart, rownumend);
	    	PagingResult page = new PagingResult(queryResult.getTotal(), queryResult.getRows());
	    	result.setData(page);
	    }catch(Exception e){
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	
	
	
	
	
	

	/**
	 * 导出excel
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportExcel/{crewType}/{crewDate}/{trainNbr}", method = RequestMethod.GET)
	public void exportExcel(@PathVariable("crewType") String crewType,@PathVariable("crewDate") String crewDate,@PathVariable("trainNbr") String trainNbr, HttpServletRequest request, HttpServletResponse response){
		try {
			System.err.println("~~~~~~~~~  exportExcel crewDate="+crewDate+"    crewType="+crewType+"    trainNbr="+trainNbr);
			String name = "";//乘务类型（1车长、2司机、3机械师）
			if("1".equals(crewType)) {
				name = "车长";
			} else if("2".equals(crewType)) {
				name = "司机";
			} else if("3".equals(crewType)) {
				name = "机械师";
			}

	    	//格式化时间
	    	crewDate = DateUtil.getFormateDayShort(crewDate);
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFSheet sheet = workBook.createSheet(name+"乘务信息_"+crewDate);// 创建一个工作薄对象
			
			HSSFCellStyle style = workBook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);//左边边框
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);//顶部边框粗线
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);//右边边框
			HSSFDataFormat format = workBook.createDataFormat();
			style.setDataFormat(format.getFormat("@"));//表文为普通文本
			style.setWrapText(true);
			// 设置表文字体
			HSSFFont tableFont = workBook.createFont();
			tableFont.setFontHeightInPoints((short) 12); // 设置字体大小
			tableFont.setFontName("宋体"); // 设置为黑体字
			style.setFont(tableFont);
			
			HSSFCellStyle dataStyle = workBook.createCellStyle();
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);//左边边框
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);//顶部边框粗线
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);//右边边框
			dataStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
			
			
			HSSFCellStyle styleTitle = workBook.createCellStyle();
			//设置字体
			HSSFFont font = workBook.createFont();//创建字体对象
			font.setFontHeightInPoints((short)12);//设置字体大小
			font.setFontName("黑体");//设置为黑体字
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中  
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
			styleTitle.setFont(font);//将字体加入到样式对象
			
			
			HSSFRow row = sheet.createRow(0);// 创建一个行对象
			row.setHeightInPoints(23);
			// 序号
			HSSFCell indexCell = row.createCell((short)0);
			indexCell.setCellStyle(styleTitle);
			indexCell.setCellValue("序号");

			// 乘务交路
			HSSFCell crewCrossCell = row.createCell((short)1);
			crewCrossCell.setCellStyle(styleTitle);
			crewCrossCell.setCellValue("乘务交路");
			// 车队组号
			HSSFCell crewGroupCell = row.createCell((short)2);
			crewGroupCell.setCellStyle(styleTitle);
			crewGroupCell.setCellValue("车队组号");
			// 经由铁路线
			HSSFCell throughLineCell = row.createCell((short)3);
			throughLineCell.setCellStyle(styleTitle);
			throughLineCell.setCellValue("经由铁路线");
			// 司机1
			HSSFCell sj1Cell = row.createCell((short)4);
			sj1Cell.setCellStyle(styleTitle);
			sj1Cell.setCellValue(name+"1");//乘务类型（1车长、2司机、3机械师）
			HSSFCell blankCell = row.createCell((short)5);
			HSSFCell blankCell1 = row.createCell((short)6);
			
			// 司机2
			HSSFCell sj2Cell = row.createCell((short)7);
			sj2Cell.setCellStyle(styleTitle);
			sj2Cell.setCellValue(name+"2");//乘务类型（1车长、2司机、3机械师）
			HSSFCell blankCell8 = row.createCell((short)8);
			HSSFCell blankCell9 = row.createCell((short)9);
			
			// 备注
			HSSFCell noteCell = row.createCell((short)10);
			noteCell.setCellStyle(styleTitle);
			noteCell.setCellValue("备注");
			
			
			

			HSSFRow row1 = sheet.createRow(1);// 创建一个行对象
			row1.setHeightInPoints(23);
			HSSFCell blankRowCell0 = row1.createCell((short)0);
			HSSFCell blankRowCell1 = row1.createCell((short)1);
			HSSFCell blankRowCell2 = row1.createCell((short)2);
			HSSFCell blankRowCell3 = row1.createCell((short)3);
			//姓名1
			HSSFCell name1Cell = row1.createCell((short)4);
			name1Cell.setCellStyle(styleTitle);
			name1Cell.setCellValue("姓名");
			//电话1
			HSSFCell tel1Cell = row1.createCell((short)5);
			tel1Cell.setCellStyle(styleTitle);
			tel1Cell.setCellValue("电话");
			//政治面貌1
			HSSFCell identity1Cell = row1.createCell((short)6);
			identity1Cell.setCellStyle(styleTitle);
			identity1Cell.setCellValue("政治面貌");
			//姓名2
			HSSFCell name2Cell = row1.createCell((short)7);
			name2Cell.setCellStyle(styleTitle);
			name2Cell.setCellValue("姓名");
			//电话2
			HSSFCell tel2Cell = row1.createCell((short)8);
			tel2Cell.setCellStyle(styleTitle);
			tel2Cell.setCellValue("电话");
			//政治面貌2
			HSSFCell identity2Cell = row1.createCell((short)9);
			identity2Cell.setCellStyle(styleTitle);
			identity2Cell.setCellValue("政治面貌");
			HSSFCell blankRowCell10 = row1.createCell((short)10);

			
			//查询乘务上报信息
			List<HighLineCrewInfo> list = highLineCrewService.findList(crewDate, crewType);
			System.err.println("########## list="+list);
			//循环生成列表
			if(list!=null && list.size() > 0) {
				for (int i=0;i<list.size();i++) {
					HighLineCrewInfo obj = list.get(i);
					HSSFRow rowX = sheet.createRow(2+i);// 创建一个行对象
					rowX.setHeightInPoints(23);
					
					// 序号
					HSSFCell indexCellFor = rowX.createCell((short)0);
					indexCellFor.setCellStyle(styleTitle);
					indexCellFor.setCellValue(i+1);

					// 乘务交路
					HSSFCell crewCrossCellFor = rowX.createCell((short)1);
					crewCrossCellFor.setCellStyle(styleTitle);
					crewCrossCellFor.setCellValue(obj.getCrewCross());
					// 车队组号
					HSSFCell crewGroupCellFor = rowX.createCell((short)2);
					crewGroupCellFor.setCellStyle(styleTitle);
					crewGroupCellFor.setCellValue(obj.getCrewGroup());
					// 经由铁路线
					HSSFCell throughLineCellFor = rowX.createCell((short)3);
					throughLineCellFor.setCellStyle(styleTitle);
					throughLineCellFor.setCellValue(obj.getThroughLine());
					//姓名1
					HSSFCell name1CellFor = rowX.createCell((short)4);
					name1CellFor.setCellStyle(styleTitle);
					name1CellFor.setCellValue(obj.getName1());
					//电话1
					HSSFCell tel1CellFor = rowX.createCell((short)5);
					tel1CellFor.setCellStyle(styleTitle);
					tel1CellFor.setCellValue(obj.getTel1());
					//政治面貌1
					HSSFCell identity1CellFor = rowX.createCell((short)6);
					identity1CellFor.setCellStyle(styleTitle);
					identity1CellFor.setCellValue(obj.getIdentity1());
					//姓名2
					HSSFCell name2CellFor = rowX.createCell((short)7);
					name2CellFor.setCellStyle(styleTitle);
					name2CellFor.setCellValue(obj.getName2());
					//电话2
					HSSFCell tel2CellFor = rowX.createCell((short)8);
					tel2CellFor.setCellStyle(styleTitle);
					tel2CellFor.setCellValue(obj.getTel2());
					//政治面貌2
					HSSFCell identity2CellFor = rowX.createCell((short)9);
					identity2CellFor.setCellStyle(styleTitle);
					identity2CellFor.setCellValue(obj.getIdentity2());
					//备注
					HSSFCell noteCellFor = rowX.createCell((short)10);
					noteCellFor.setCellStyle(styleTitle);
					noteCellFor.setCellValue(obj.getNote());
				}
			}
			
			
			//行列合并	四个参数分别是：起始行，起始列，结束行，结束列
			sheet.addMergedRegion(new Region((short)0, (short)0, (short)1, (short)0)); //序号  合并2行1列
			sheet.addMergedRegion(new Region((short)0, (short)1, (short)1, (short)1)); //乘务交路  合并2行1列
			sheet.addMergedRegion(new Region((short)0, (short)2, (short)1, (short)2)); //车队组号  合并2行1列
			sheet.addMergedRegion(new Region((short)0, (short)3, (short)1, (short)3)); //经由铁路线  合并2行1列
			sheet.addMergedRegion(new Region((short)0, (short)4, (short)0, (short)6)); //司机1  合并1行3列
			sheet.addMergedRegion(new Region((short)0, (short)7, (short)0, (short)9)); //司机2  合并1行3列
			sheet.addMergedRegion(new Region((short)0, (short)10, (short)1, (short)10)); //备注  合并2行1列
			
			
			
			String filename = this.encodeFilename("司机乘务信息_"+crewDate+".xls", request);
			response.setHeader("Content-disposition", "attachment;filename=" + filename);
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				workBook.write(ouputStream);
//				ouputStream.flush();
				ouputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ouputStream!=null) {
					ouputStream.close();
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
				
	}
	
	
	
	
	
	
    /** 
     * 设置下载文件中文件的名称 
     *  
     * @param filename 
     * @param request 
     * @return 
     */  
    public static String encodeFilename(String filename, HttpServletRequest request) {  
      /** 
       * 获取客户端浏览器和操作系统信息 
       * 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar) 
       * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6 
       */  
      String agent = request.getHeader("USER-AGENT");  
      try {  
        if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {  
          String newFileName = URLEncoder.encode(filename, "UTF-8");  
          newFileName = StringUtils.replace(newFileName, "+", "%20");  
          if (newFileName.length() > 150) {  
            newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");  
            newFileName = StringUtils.replace(newFileName, " ", "%20");  
          }  
          return newFileName;  
        }  
        if ((agent != null) && (-1 != agent.indexOf("Mozilla")))  
          return MimeUtility.encodeText(filename, "UTF-8", "B");  
    
        return filename;  
      } catch (Exception ex) {
    	  ex.printStackTrace();
        return filename;  
      }  
    }
	
	
	
}
