package net.docs.design.pattern.factorymethod.multiple;


import net.docs.design.pattern.factorymethod.MailSender;
import net.docs.design.pattern.factorymethod.Sender;
import net.docs.design.pattern.factorymethod.SmsSender;


/**
 * �����������ģʽ
 * @author arvin
 */
public class SendFactory {

	public Sender produceMail() {
		return new MailSender();
	}

	public Sender produceSms() {
		return new SmsSender();
	}
}
