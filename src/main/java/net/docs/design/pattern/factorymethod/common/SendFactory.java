package net.docs.design.pattern.factorymethod.common;

import net.docs.design.pattern.factorymethod.MailSender;
import net.docs.design.pattern.factorymethod.Sender;
import net.docs.design.pattern.factorymethod.SmsSender;

/**
 * ��ͨ����ģʽ
 *
 * @author arvin
 */
public class SendFactory {
    public Sender produce(String type) {
        if ("mail".equals(type)) {
            return new MailSender();
        } else if ("sms".equals(type)) {
            return new SmsSender();
        } else {
            System.out.println("��������ȷ������!");
            return null;  
        }  
    }
}
