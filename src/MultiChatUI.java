/*
 * Sejong University Digital Contents
 * 객체지향 설계기술 과제#7
 *
 * MultiChatUI.class
 * 작성자: 122235 김준혁
 * 작성일: 2017년 12월 28일
 * 
 * */
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MultiChatUI extends JFrame{
	
	public String id;									//Client의 개인사용자 id를 저장할 곳
	
	private JPanel loginPanel;							//loginPanel 생성
	protected JButton loginButton;						//로그인 버튼 선언
	
	private JLabel inLabel;								//대화명 설정 예정
	protected JLabel outLabel;							//대화명 : id 설정 예정
	protected JTextField idInput;						//id 입력칸 생성
	
	
	
	private JPanel logoutPanel;							//logoutPanel 생성
	protected JButton logoutButton;						//logoutButton 생성
	
	private JPanel msgPanel;							//채팅창 하단 패널 생성
	protected JTextField msgInput;						//메세지 입력란 생성
	protected JButton exitButton;						//종료 버튼 생성
	
	protected Container tab;							//로그인, 로그 아웃 Panel을 모두 포함할예정
	protected CardLayout cardLayout;					//tab 을 위한 Card Layout 생성
	
	protected JTextArea msgOut;							//채팅창 부분 TextArea 생성
	
	//우측 패널
	protected JPanel memberPanel;						//맴버 출력 예정 패널 생성
	protected JLabel memberlbl;							//맴버 제목 라벨 생성
	protected JTextArea info;							//맴버 이름 저장 txtarea 설정
	
	public MultiChatUI()
	{
		super("JUNHYUK TALK");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(1000,700);								//전체 크기 설정
		setResizable(false);
		
		
		//로그인 , 로그아웃 관련 요소들 생성-------------------------------------------------------------
		//로그인
		loginPanel = new JPanel();								//로그인 패널 선언
		loginPanel.setLayout(new BorderLayout());
		
		idInput = new JTextField(15);							//아이디 입력란 선언
		loginButton = new JButton("로그인");						//아이디 입력 버튼 선언
		
		inLabel = new JLabel("대화명 ");							//대화명 메세지 출력
		loginPanel.add(inLabel, BorderLayout.WEST);				//대화명이 제일 좌측으로
		loginPanel.add(idInput, BorderLayout.CENTER);			//입력란이 중앙
		loginPanel.add(loginButton, BorderLayout.EAST);			//로그인 버튼이 제일 우측으로
		
		//로그아웃
		logoutPanel = new JPanel();								//로그아웃 패널 선언
		logoutPanel.setLayout(new BorderLayout());
		outLabel = new JLabel();								//id 라벨 선언
		logoutButton = new JButton("로그아웃");					//로그아웃 버튼 선언
		
		logoutPanel.add(outLabel, BorderLayout.CENTER);			//라벨을 중앙으로
		logoutPanel.add(logoutButton, BorderLayout.EAST);		//버튼을 제일 우측으로

		//로그인 로그아웃 모두 포함할 tab 선언 및 관련 요소들
		tab = new JPanel();
		cardLayout = new CardLayout();
		tab.setLayout(cardLayout);
		tab.add(loginPanel, "login");
		tab.add(logoutPanel, "logout");
		tab.setBounds(0,0,100,30);
		add(tab,BorderLayout.NORTH);
		
		//메인 채팅창 관련 요소들------------------------------------------------------------
		msgOut = new JTextArea("", 10, 30);						//메인 채팅창 선언
		msgOut.setFont(new Font("Gothic", Font.PLAIN, 40));
		msgOut.setBackground(new Color(219, 244, 255));
		msgOut.setEditable(false);								//편집 못하게 막음
		
		//스크롤바 붙이기
		JScrollPane jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(jsp, BorderLayout.CENTER);
		
		msgPanel = new JPanel();								//채팅 메세지 입력 패널 선언
		exitButton = new JButton("종료");						//종료 버튼 선언
		msgInput = new JTextField(80);
		msgPanel.add(msgInput, BorderLayout.WEST);				//메세지 입력 제일 좌측으로
		msgPanel.add(exitButton, BorderLayout.EAST);			//종료 버튼 제일 우측으로
		add(msgPanel, BorderLayout.SOUTH);						//메세지 입력 패널 제일 하단에 붙이기
				
		memberPanel = new JPanel();						//맴버 패널 선언
		memberPanel.setLayout(new BorderLayout());
		memberlbl = new JLabel("맴버");					//맴버 라벨 선언
		memberPanel.add(memberlbl, BorderLayout.NORTH);
		
		info = new JTextArea("", 10,30);
		info.setFont(new Font("Gothic", Font.BOLD,15));
		info.setBackground(new Color(185, 197, 236));
		info.setEditable(false);
		JScrollPane scroll = new JScrollPane(info,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		memberPanel.add(scroll);
		
		add(memberPanel, BorderLayout.EAST);
		
		setVisible(true);
	}
	
	public void addButtonActionListener(ActionListener listener)	//관련 ActionListener 선언 메소드
	{
		loginButton.addActionListener(listener);
		logoutButton.addActionListener(listener);
		exitButton.addActionListener(listener);
		msgInput.addActionListener(listener);
	}
}
