/*
 * Sejong University Digital Contents
 * 객체지향 설계기술 과제#7
 *
 * MultiChatServer.class
 * 작성자: 122235 김준혁
 * 작성일: 2017년 12월 28일
 * 
 * */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class MultiChatServer {
	
	private ServerSocket ss = null;										//서버소켓 생성
	private Socket s = null;											//일반 소켓 생성
	private boolean status;												//채팅 유무 상태 확인 boolean
	
	ArrayList<ChatThread> chatThreads = new ArrayList<ChatThread>();	//각 클라이언트를 쓰래드로 만들어 여기에 저장시킬 예정
	ArrayList<String> memList = new ArrayList<String>();				//맴버리스트 서버에서 관리하기 위해 선언
	
	private Logger logger;												//Logger 생성
	
	public void start()
	{
		status = true;													//서버 활성화
		logger = Logger.getLogger(this.getClass().getName());			//로거 선언
		
		try
		{
			ss = new ServerSocket(5000);								//서버소켓 포트 설정
			logger.info("MultiChatServer start");
			
			while(status)
			{
				s = ss.accept();										//클라이언트 소켓이 들어왔을때
				ChatThread chat = new ChatThread();						//쓰래드 생성
				chatThreads.add(chat);									//해당 쓰래드를 chatThreads에 추가
				chat.start();											//해당 쓰래드 활성화
			}
		}catch(Exception e)
		{
			logger.info("[MultiChatServer]start() Exception 발생!!");
			e.printStackTrace();
		}
	}
	
	class ChatThread extends Thread										//낱개의 클라이언트를 쓰래드화
	{
		String msg;														//메세지
		Message m = new Message();										//메세지 객체 생성
		Gson gson = new Gson();											//Gson 객체 생성
		
		private BufferedReader inMsg = null;
		private PrintWriter outMsg = null;
		
		public void run()
		{
			//상태 정보가 true 이면 루프를 돌면서 사용자에게서 수신된 메시지 처리
			try {
					inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
					outMsg = new PrintWriter(s.getOutputStream(), true);
					
				} catch (IOException e) {
					e.printStackTrace();
					status = false;
				}
				while(status)												//서버 채팅창 활성화일시
				{
					//수신된 메시지를 msg 변수에 저장
					try {
						msg = inMsg.readLine();								//입력 스트림으로 부터 메세지 전달 받음
					} catch (IOException e) {
						e.printStackTrace();
					}
				
					m = gson.fromJson(msg,Message.class);					//메세지를 json 으로 파싱후 Message 객체에 저장
				
					if(m.getType().equals("logout"))						//로그아웃일 경우
					{
						chatThreads.remove(this);							//해당 클라이언트 쓰래드 삭제
						
						String temp="";										//임시 String 변수 선언 및 초기화
						
						for(int i =0; i<memList.size(); i++)				//아이디를 지우기위한 검색 실시
						{
							if(memList.get(i).equals(m.getId()+"\n"))		//
							{
								memList.remove(i);							//맴버리스트에서 아이디 삭제
								break;
							}
						}
						
						for(int i =0; i<memList.size(); i++)				//맴버리스트 temp 변수에 차례로 저장
						{
							temp += memList.get(i);
						}
						msgSendAll(gson.toJson(new Message(m.getId(),"", "님이 종료했습니다.", "server", temp)));	//로그아웃 메세지 출력
					}
					else if(m.getType().equals("login"))					//로그인일 경우
					{
						String temp ="";									//임시 String 변수 선언 및 초기화
						memList.add(new String(m.getId()+"\n"));			//멤버리스트 멤버 저장 
						
						for(int i =0; i<memList.size(); i++)				//저장된 멤버리스트 가져오기
						{
							temp += memList.get(i);							//temp 에 멤버리스트 저장
						}
						
						msgSendAll(gson.toJson(new Message(m.getId(), "", "님이 로그인 했습니다.", "server", temp))); //로그인 메세지 출력
					}
					else													//이외의 경우 메세지 그대로 모든 클라이언트 채팅창에 출력
					{
						String temp ="";
						
						for(int i =0; i<memList.size(); i++)				//저장된 멤버리스트 가져오기
						{
							temp += memList.get(i);							//temp 에 멤버리스트 저장
						}
						msgSendAll(gson.toJson(new Message(m.getId(),m.getPasswd(),m.getMsg(),m.getType(), temp)));
					}
				}
			
			this.interrupt();											//채팅창에 아무도 없는 경우 종료
			logger.info(this.getName() + " 종료됨!!");
			status = false;
		}

		void msgSendAll(String msg)	//연결된 모든 사용자에게 메시지를 전달하는 역할
		{
			
			for(ChatThread ct: chatThreads)								//한 클라이언트가 한 채팅물 모든 클라이언트 채팅창에 전달
			{
				System.out.println(msg);
				ct.outMsg.println(msg);
			}
			
			
		}
	}
}
