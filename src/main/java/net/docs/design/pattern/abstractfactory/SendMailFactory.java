package net.docs.design.pattern.abstractfactory;



/**
 * ���󹤳�ģʽ
 * @author arvin
 */
public class SendMailFactory implements Provider {

	@Override
	public Sender produce() {
		return new MailSender();  
	}

}
