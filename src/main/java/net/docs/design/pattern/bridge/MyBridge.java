package net.docs.design.pattern.bridge;

/**
 * �Ž�ģʽ
 * @author arvin
 */
public class MyBridge extends Bridge {
	public void method() {
		getSource().method();
	}
}
