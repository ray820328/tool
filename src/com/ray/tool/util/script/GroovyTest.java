package com.ray.tool.util.script;

import java.util.Date;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class GroovyTest {
	public static void main(String[] args){
		try{
			evalScript();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void evalScript() throws Exception{  
	    ScriptEngineManager factory = new ScriptEngineManager();  
	    //每次生成一个engine实例  
	    ScriptEngine engine = factory.getEngineByName("groovy");  
	    assert engine != null;  
	    System.out.println(engine.toString());  
	    //javax.script.Bindings  
	    Bindings binding = engine.createBindings();  
	    binding.put("date", new Date());  
	    engine.eval("def getTime(){return date.getTime();}",binding);  
	    engine.eval("def sayHello(name,age){return 'Hello,I am ' + name + ',age' + age;}");  
	    Long time = (Long)((Invocable)engine).invokeFunction("getTime", null);  
	    System.out.println(time);  
	    String message = (String)((Invocable)engine).invokeFunction("sayHello", "zhangsan",new Integer(12));  
	    System.out.println(message);  
	}
}
