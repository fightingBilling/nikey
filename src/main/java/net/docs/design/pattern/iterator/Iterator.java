package net.docs.design.pattern.iterator;

/**
 * ������ģʽ
 *
 * @author arvin
 */
public interface Iterator {
	// ǰ��
	public Object previous();

	// ����
	public Object next();

	public boolean hasNext();

	// ȡ�õ�һ��Ԫ��
	public Object first();
}
