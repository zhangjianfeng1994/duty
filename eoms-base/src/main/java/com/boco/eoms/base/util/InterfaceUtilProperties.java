package com.boco.eoms.base.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.boco.eoms.base.tool.LogOutTool;

/**
  * @ClassName: InterfaceUtilProperties
  * @Description: 解析配置文件 <content columnName="mainId"  interfaceName="mainId"  defauleValue=""/>
  * 将接口传递过来的map对象与配置文件进行映射
  * @author szy
  * @date 2017-07-07 15:37:19
  * @version V1.0
  * @since JDK 1.8
  * 
 */
public class InterfaceUtilProperties {
	
	private static InterfaceUtilProperties _crmUtilProperties;
	
	public static InterfaceUtilProperties getInstance(){
		_crmUtilProperties = new InterfaceUtilProperties();
		return _crmUtilProperties;
	}
	
	/**
	 * 将接口传来的数据与main表字段对应，并存放到map中
	 * @param interfaceMap
	 * @param filePath
	 * @param nodePath
	 * @return map 包含interfaceMap
	 * @throws Exception
	 */
	public Map<String,String> getMapFromXml(Map<String,String> interfaceMap,String filePath,String nodePath) throws Exception{	
		return this.getMapFromXml(interfaceMap, filePath, nodePath, true);
	}
	/**
	 * 将接口传来的数据与main表字段对应，并存放到map中
	 * @param interfaceMap
	 * @param filePath
	 * @param nodePath
	 * @param isAll	true返回的map包含interfaceMap,false返回的map中不包含interfaceMap
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getMapFromXml(Map<String,String> interfaceMap,String filePath,String nodePath,boolean isAll) throws Exception{
		Map<String,String> map = null;
		if(isAll)
			map = interfaceMap;
		else
			map = new HashMap<String,String>();
		try{
			SAXBuilder dc=new SAXBuilder();
			Document doc = dc.build(new File(filePath));
			Element element = doc.getRootElement();
			String[] paths = nodePath.split("\\.");
			for(int i=0;i<paths.length;i++){
				String nodeName = paths[i];
				element = element.getChild(nodeName);
				if(element==null){
					LogOutTool.info(InterfaceUtilProperties.class,nodePath+" not find");
					throw new Exception("not found node:" + nodePath);
				}
			}
			
			@SuppressWarnings("unchecked")
			List<Element> list = element.getChildren();
			for(Element node : list){
				String interfaceName = node.getAttribute("interfaceName").getValue();
				String columnName = node.getAttribute("columnName").getValue();
				System.out.println("interfaceName="+interfaceName);
				System.out.println("columnName="+columnName);
				String value = "";
				if(interfaceName.length()>0){
					value = StaticMethod.nullObject2String(interfaceMap.get(interfaceName)); 	
				}
				if(value.length()<=0){
					value = node.getAttribute("defauleValue").getValue();
				}
				if(value.length()>0){
					if(node.getAttribute("dictNodePath")!=null){//需要从xml文件转换数据字典
//						String dictNodePath = node.getAttribute("dictNodePath").getValue();
//						System.out.println("dictNodePath="+dictNodePath);
//						value = this.getDictIdByInterfaceCode(dictNodePath, value);
					}else if(node.getAttribute("rootDictId")!=null){//需要从数据库转换数据字典
//						System.out.println("rootDictId="+node.getAttribute("rootDictId").getValue());
//						value = dictMgr.getDictIdByDictCode(node.getAttribute("rootDictId").getValue(), value);
					}else if(node.getAttribute("cityCode")!=null){
//						String code = node.getAttribute("cityCode").getValue();
//						System.out.println("cityCode="+code);
//						System.out.println("value="+value);
//						TawSystemArea area = areaMgr.getAreaByCode(value);
//						if(area!=null)
//							value = area.getAreaid();
//						else
//							System.out.println("没有找到'"+code+"'映射的地市");
					}
					if(node.getAttribute("maxLength")!=null){//最长字节
//						try{
//							value = SheetStaticMethod.splitString(value, Integer.valueOf(node.getAttribute("maxLength").getValue()).intValue(), "...");
//						}catch(Exception e){
//							System.out.println("maxLength的值非法");
//						}
					}
//					if(node.getAttribute("type")!=null){//转换日期格式
//						String typeName = node.getAttribute("type").getValue();
//						if(typeName.equalsIgnoreCase("date")){
//							value = this.formateDateStr(value);
//						}
//					}
					System.out.println("value="+value);
					if(value!=null && value.length()>0){
						map.put(columnName, value);
					}
						
				}
			}
			return map;
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("生成map出错："+e.getMessage());
		}
	}
	
	/**
	 * 生成接口的xml字符
	 * @param objectMap
	 * @param filePath
	 * @param nodePath
	 * @return
	 * @throws Exception
	 */
	public String getXmlFromMap(Map objectMap,String filePath,String nodePath) throws Exception{	
		List chNameList = new ArrayList();
		List enNameList = new ArrayList();
		List contentList = new ArrayList();
		
		return this.getXmlFromMap(objectMap, filePath, nodePath,chNameList, enNameList, contentList);
		
	}
	public String getXmlFromMap(Map objectMap,String filePath,String nodePath,List chNameList,List enNameList,List contentList) throws Exception{	
		try{		
			
			SAXBuilder dc=new SAXBuilder();
			Document doc = dc.build(new File(filePath));
			
			Element element = doc.getRootElement();
			element = element.getChild(nodePath);
			
			List list = element.getChildren();
			for(int i=0;i<list.size();i++){
				Element node = (Element)list.get(i);
				String interfaceCnName = node.getAttribute("interfaceCnName").getValue();
				String interfaceEnName = node.getAttribute("interfaceEnName").getValue();
				String columnName = node.getAttribute("columnName").getValue();
				
				System.out.println("interfaceCnName="+interfaceCnName);
				System.out.println("interfaceEnName="+interfaceEnName);
				System.out.println("columnName="+columnName);
				
				String value = "";
				if(columnName.length()>0){
					Object obj = objectMap.get(columnName);
					if (obj != null){
						if(obj.getClass().isArray())
							obj = ((Object[]) obj)[0];
						else if (obj instanceof Date) {
							String type = "yyyy-MM-dd HH:mm:ss";
							if(node.getAttribute("type")!=null)//转换日期格式
								type = node.getAttribute("type").getValue();
							SimpleDateFormat dateformat = new SimpleDateFormat(type);
							value = dateformat.format(obj);							
						}
					}
					if("".equals(value))
						value = StaticMethod.nullObject2String(obj); 	
				}
				if(value.length()<=0)
					value = node.getAttribute("defauleValue").getValue();
	
				if(value.length()>0){	
					if(node.getAttribute("dictNodePath")!=null){//需要从xml文件转换数据字典
						String dictNodePath = node.getAttribute("dictNodePath").getValue();
					}else if(node.getAttribute("type")!=null){
						String type = node.getAttribute("type").getValue();
						System.out.println("type="+type);
						if("dept".equalsIgnoreCase(type)){	//获取部门名称
						}else if("user".equalsIgnoreCase(type)){	//获取人员
						}else if("dict".equalsIgnoreCase(type)){	//获取字典
						}else if("dictcode".equalsIgnoreCase(type)){	//获取字典code
						}else if("areacode".equalsIgnoreCase(type)){	//获取地市code
						}
					}
				}
				chNameList.add(interfaceCnName);
				enNameList.add(interfaceEnName);
				contentList.add(value);
			}
			
			String opDetail = createOpDetailXml(chNameList, enNameList,
					contentList);
			LogOutTool.info(this, nodePath+" opDetail="+opDetail);
			return opDetail;
		}catch(Exception err){
			err.printStackTrace();
			throw new Exception("生成xml出错："+err.getMessage());
		}
	}

	public static String createOpDetailXml(List chNameList, List enNameList, List contentList)
	{
		org.dom4j.Document dom4jDoc = org.dom4j.DocumentHelper.createDocument();
		org.dom4j.Element opDetailElement = dom4jDoc.addElement("opDetail");
		org.dom4j.Element recordInfo = opDetailElement.addElement("recordInfo");
		for (int i = 0; i < chNameList.size(); i++)
			addContentXML(recordInfo, StaticMethod.nullObject2String(chNameList.get(i)), StaticMethod.nullObject2String(enNameList.get(i)), contentList.get(i));

		String xml = dom4jDoc.asXML();
		String opt = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		int index = xml.indexOf(opt);
		if (index >= 0)
			xml = xml.substring(index + opt.length());
		return xml;
	}
	private static void addContentXML(org.dom4j.Element recordInfo, String cnname, String name, Object object)
	{
		org.dom4j.Element filedInfo = recordInfo.addElement("fieldInfo");
		org.dom4j.Element fieldCnName = filedInfo.addElement("fieldChName");
		fieldCnName.setText(cnname);
		org.dom4j.Element fieldEnName = filedInfo.addElement("fieldEnName");
		fieldEnName.setText(name);
		org.dom4j.Element fieldContent = filedInfo.addElement("fieldContent");
		if (object == null){
			fieldContent.setText("");
		}
		else{
			fieldContent.setText(object.toString());
			//fieldContent.addCDATA(object.toString());
		}
	}
}
