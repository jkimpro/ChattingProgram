/*
 * Sejong University Digital Contents
 * 객체지향 설계기술 과제#7
 *
 * Message.class
 * 작성자: 122235 김준혁
 * 작성일: 2017년 12월 28일
 * 
 * */
public class Message {

	private String id;													//아이디 
	private String passwd;												//패스워드
	private String msg;													//메세지
	private String type;												//login logout 여부
	private String members;												//member 변수 추가
	
	public Message() {}
	public Message(String rId, String message1, String message2, String rType, String temp)	//parameter 생성자로 모든 요소를 입력받음
	{
		id = rId;
		passwd = message1;
		msg = message2;
		type = rType;
		members = temp;
	}
	
	public String getId() {								//id 반환
		return id;
	}
	public void setId(String id) {						//id 설정
		this.id = id;
	}
	public String getPasswd() {							//password 반환
		return passwd;
	}
	public void setPasswd(String passwd) {				//password 설정
		this.passwd = passwd;
	}
	public String getMsg() {							//메세지 반환
		return msg;
	}
	public void setMsg(String msg) {					//메세지 
		this.msg = msg;
	}
	public String getType() {							//타입 반환
		return type;
	}
	public void setType(String type) {					//타입 설정
		this.type = type;
	}
	public String getMembers() {						//전체 맴버 반환
		return members;
	}
	public void setMembers(String members) {			//전체 맴버 설정
		this.members = members;
	}

}
