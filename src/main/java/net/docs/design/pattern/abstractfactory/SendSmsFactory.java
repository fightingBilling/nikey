package net.docs.design.pattern.abstractfactory;



/**
 * ���󹤳�ģʽ
 * @author arvin
 */
public class SendSmsFactory implements Provider {

	@Override
	public Sender produce() {
		// TODO Auto-generated method stub
		 return new SmsSender();  
	}

}
