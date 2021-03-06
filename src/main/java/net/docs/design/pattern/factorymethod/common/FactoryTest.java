package net.docs.design.pattern.factorymethod.common;

import net.docs.design.pattern.factorymethod.Sender;

public class FactoryTest {
	 public static void main(String[] args) {  
	        SendFactory factory = new SendFactory();  
	        Sender sender = factory.produce("sms");
	        sender.send();
		    Sender sender2 = factory.produce("mail");
		    sender2.send();
	 }  
}
