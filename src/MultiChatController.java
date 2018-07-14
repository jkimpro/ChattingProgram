/*
 * Sejong University Digital Contents
 * 객체지향 설계기술 과제#7
 * 
 * MultiChatController.class
 * 작성자: 122235 김준혁
 * 작성일: 2017년 12월 28일
 * 
 * */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import com.google.gson.Gson;


public class MultiChatController implements Runnable{			//Controller 자체를 쓰래드로 사용할수 있도록 Runnable implement 하기
	
	public static MultiChatServer server;						//서버 객체 선언
	
	public boolean status;										//로그인인지 아닌지에 대한 유무 결정 boolean 생성
	private final MultiChatUI v;								//MultiChatUI 객체 생성
	private final MultiChatData chatData;						//MultiChatData 객체 생성
	
	private Logger logger;										//Logger 생성
	
	private Gson gson = new Gson();								//Gson 객체 생성 및 선언
	private Socket socket;										//Socket 객체 생성
	
	private BufferedReader inMsg =null;							//입력 버퍼리더 생성
	private PrintWriter outMsg =null;							//출력 객체 생성
	
	private Message m = null;									//메세지 객체 한개 생성
	private Thread thread;										//차후 Runnable 에 붙일 쓰레드 생성
	
	public MultiChatController(MultiChatData chatData, MultiChatUI v)
	{
		logger = Logger.getLogger(this.getClass().getName());	//Logger 선언
		this.chatData = chatData;								//chatData 단독 객체 현재 chatData에 붙이기
		this.v = v;												//MultiChatUI 단독 객체 현재 v 에 붙이기 
	}
	
	public void appMain()
	{
		chatData.addObj(v.msgOut);								//chatData 에서 UI에 채팅을 기록할수 있도록 인자 전달
		chatData.connectMember(v.info);							//chatData 에서 UI에 맴버를 기록할수 있도록 인자 전달
		
		v.addButtonActionListener(new ActionListener(){			//ActionListener 메소드 정의 및 전달
			
			public void actionPerformed(ActionEvent e)
			{
				Object obj = e.getSource();
				if(obj == v.exitButton)							//종료버튼 클릭시 프로그램 종료
				{
					System.exit(0);
				}
				else if(obj == v.loginButton)					//로그인 버튼 클릭시 
				{
					v.id = v.idInput.getText();					//아이디 가져오기
					v.outLabel.setText(" 대화명 : "+v.id);		//대화명 + id 라벨 붙이기
					v.cardLayout.show(v.tab, "logout");			//logOut 패널로 UI 상단 패널 설정
					connectServer();							//서버 연결
					
					status = true;								//채팅 가능토록 만듦
				}
				else if(obj == v.logoutButton)					//로그아웃 버튼 클릭시
				{
					outMsg.println(gson.toJson(new Message(v.id, "", "", "logout" , "")));	//Message logout 상태로 변환
					v.msgOut.setText("");
					v.info.setText("");							//맴버 저장 목록 빈공간으로 초기화
					v.cardLayout.show(v.tab ,"login");			//login 패널로 UI 상단 패널 설정
					outMsg.close();								//출력 끄기
					
					try
					{
						inMsg.close();							//버퍼리더 끄기
						socket.close();							//소켓 종료 (끊기)
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
					
				}
				else if(obj == v.msgInput)						//enter를 눌렀을 경우
				{
					outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg" , "")));	//출력스트림에 메세지 설정
					v.msgInput.setText("");						//기존 UI 입력란 초기화
				}
			}
			
		});
		
	}
	public void connectServer()									//서버 연결
	{
		try
		{
			socket = new Socket("localhost", 5000);			//서버 연결
			logger.info("[Client]Sever 연결 성공!!");
			
			//입출력 스트림 생성
			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//입력 스트림 선언
			outMsg = new PrintWriter(socket.getOutputStream(), true);					//출력 스트림 선언
			
			//서버에 로그인 메시지 전달
			m = new Message(v.id, "","","login", "");
			
			outMsg.println(gson.toJson(m));						//출력 스트림 객체에 메세지를 Json 코드로 파싱
			System.out.println(gson.toJson(m));
			
			thread = new Thread(this);							//Runnable 에 쓰래드 붙이기
			thread.start();										//쓰래드 시작
		}
		catch(Exception e)
		{
			logger.warning("[MultiChatUI]connectServer() Exception 발생!!");
			e.printStackTrace();
		}
	}
	
	
	public void run()
	{
		String msg;															//메세지 선언
		
		while(status)
		{
			try
			{
				msg = inMsg.readLine();										//입력 스트림으로 부터 전달 받은 메세지 저장
				m = gson.fromJson(msg ,Message.class);						//Json 코드로 파싱
				
				chatData.refreshData(m.getId() + ">" + m.getMsg() + "\n");	//UI 채팅창에 입력
				System.out.println(m.getId() + ">" + m.getMsg() + "\n");
				chatData.updateMember(m.getMembers());						//UI 맴버리스트 초기화 및 재설정
				
				v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength()); //커서를 마지막으로 옮김
			}
			catch(IOException e)
			{
				logger.warning("[MultiChatUI]메시지 스트림 종료!!");
				status = false;
			}
		}
		
		logger.info("[MultiChatUI]" + thread.getName() + "메시지 수신 스레드 종료됨!!");
	}
	
	
	/*
	 * 추가기능
	 * -채팅방에 참여하고 있는 유저들 표시하는 기능 부여
	 * -우측 TextArea 에 현재 참여하고 있는 유저들 표시
	 * -프로그램 구동 이상무
	 * */
	public static void main(String [] args)
	{


		MultiChatServer server = new MultiChatServer();								//서버 실행
		server.start();
		
		MultiChatController app = new MultiChatController(new MultiChatData(), 		//Client 부분 실행
				new MultiChatUI());
		app.appMain();
		
		MultiChatController app1 = new MultiChatController(new MultiChatData(), 		//Client 부분 실행
				new MultiChatUI());
		app1.appMain();
		MultiChatController app2 = new MultiChatController(new MultiChatData(), 		//Client 부분 실행
				new MultiChatUI());
		app2.appMain();
		MultiChatController app3 = new MultiChatController(new MultiChatData(), 		//Client 부분 실행
				new MultiChatUI());
		app3.appMain();
		
		
	}
	
	
}
