/*
 * Sejong University Digital Contents
 * ��ü���� ������ ����#7
 *
 * Message.class
 * �ۼ���: 122235 ������
 * �ۼ���: 2017�� 12�� 28��
 * 
 * */
public class Message {

	private String id;													//���̵� 
	private String passwd;												//�н�����
	private String msg;													//�޼���
	private String type;												//login logout ����
	private String members;												//member ���� �߰�
	
	public Message() {}
	public Message(String rId, String message1, String message2, String rType, String temp)	//parameter �����ڷ� ��� ��Ҹ� �Է¹���
	{
		id = rId;
		passwd = message1;
		msg = message2;
		type = rType;
		members = temp;
	}
	
	public String getId() {								//id ��ȯ
		return id;
	}
	public void setId(String id) {						//id ����
		this.id = id;
	}
	public String getPasswd() {							//password ��ȯ
		return passwd;
	}
	public void setPasswd(String passwd) {				//password ����
		this.passwd = passwd;
	}
	public String getMsg() {							//�޼��� ��ȯ
		return msg;
	}
	public void setMsg(String msg) {					//�޼��� 
		this.msg = msg;
	}
	public String getType() {							//Ÿ�� ��ȯ
		return type;
	}
	public void setType(String type) {					//Ÿ�� ����
		this.type = type;
	}
	public String getMembers() {						//��ü �ɹ� ��ȯ
		return members;
	}
	public void setMembers(String members) {			//��ü �ɹ� ����
		this.members = members;
	}

}
