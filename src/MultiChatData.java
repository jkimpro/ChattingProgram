/*
 * Sejong University Digital Contents
 * ��ü���� ������ ����#7
 *
 * MultiChatData.class
 * �ۼ���: 122235 ������
 * �ۼ���: 2017�� 12�� 28��
 * 
 * */
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTextArea;

public class MultiChatData {

	JTextArea area;								//UI ���� upcall ���� ��ü ����
	JTextArea memberArea;						//UI ���� �ɹ� �̸��� ������ �� ��ü ����
	
	public void addObj(JComponent comp)			//UI �� TextArea �� �޼����� �ٷ� ����� �� �ֵ��� ��ü ���̱�
	{
		area = (JTextArea)comp;
	}
	public void refreshData(String msg)			//�޼��� UI �� TextArea �� �߰�
	{
		area.append(msg);
	}
	
	public void connectMember(JComponent comp)	//UI�� �ɹ� ��� �κ� ����
	{
		memberArea = (JTextArea)comp;
	}
	
	public void updateMember(String mem)		//UI�� �ɹ� ��� �κ� �ʱ�ȭ �� �缳��
	{	
		memberArea.setText("");
		memberArea.setText(mem);
	}
	
}
