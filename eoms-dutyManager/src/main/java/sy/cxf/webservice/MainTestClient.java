package sy.cxf.webservice;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class MainTestClient {
	 public static void main(String[] args)throws Exception {

//	       JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//
//	       factory.setServiceClass(IHelloWorld.class);
//
//	       factory.setAddress("http://localhost:8080/spring-mybatis/webservice/helloService?wsdl");
//
//	       IHelloWorld client = (IHelloWorld) factory.create();
//
//	       String response = client.sayHello("Hai,jiangqiao");
//
//	       System.out.println("Response:" + response);
		 
		 JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();       //这个是不是用到了工厂方法设计模式呢？学习中 
         String wsUrl = "http://localhost:8080/spring-mybatis/webservice/helloService?wsdl";    //wsdl地址
         String method = "sayHello";//webservice的方法名 
         Client client = dcf.createClient(wsUrl); 
         Object[] res = null; 
         try { 
           res = client.invoke(method,"Hai,jiangqiao");//调用webservice 
         } catch (Exception e) { 
           e.printStackTrace(); 
         } 
         System.out.println("res:"+res[0]); 
         System.exit(0);    

	    }

}
