package net.docs.design.pattern.adapterclass;


/**
 * ���������ģʽ
 * 
 * @author arvin
 */
public class Adapter extends Source implements Targetable {

	@Override
	public void method2() {
		// TODO Auto-generated method stub
		System.out.println("this is the targetable method!");  
	}

}
