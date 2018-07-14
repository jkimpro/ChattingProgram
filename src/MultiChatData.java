/*
 * Sejong University Digital Contents
 * 객체지향 설계기술 과제#7
 *
 * MultiChatData.class
 * 작성자: 122235 김준혁
 * 작성일: 2017년 12월 28일
 * 
 * */
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTextArea;

public class MultiChatData {

	JTextArea area;								//UI 에서 upcall 받을 객체 생성
	JTextArea memberArea;						//UI 에서 맴버 이름을 기입할 곳 객체 생성
	
	public void addObj(JComponent comp)			//UI 의 TextArea 에 메세지를 바로 출력할 수 있도록 객체 붙이기
	{
		area = (JTextArea)comp;
	}
	public void refreshData(String msg)			//메세지 UI 의 TextArea 에 추가
	{
		area.append(msg);
	}
	
	public void connectMember(JComponent comp)	//UI의 맴버 출력 부분 연결
	{
		memberArea = (JTextArea)comp;
	}
	
	public void updateMember(String mem)		//UI의 맴버 출력 부분 초기화 및 재설정
	{	
		memberArea.setText("");
		memberArea.setText(mem);
	}
	
}
