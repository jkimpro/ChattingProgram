/*
 * Sejong University Digital Contents
 * ��ü���� ������ ����#7
 * 
 * MultiChatController.class
 * �ۼ���: 122235 ������
 * �ۼ���: 2017�� 12�� 28��
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


public class MultiChatController implements Runnable{			//Controller ��ü�� ������� ����Ҽ� �ֵ��� Runnable implement �ϱ�
	
	public static MultiChatServer server;						//���� ��ü ����
	
	public boolean status;										//�α������� �ƴ����� ���� ���� ���� boolean ����
	private final MultiChatUI v;								//MultiChatUI ��ü ����
	private final MultiChatData chatData;						//MultiChatData ��ü ����
	
	private Logger logger;										//Logger ����
	
	private Gson gson = new Gson();								//Gson ��ü ���� �� ����
	private Socket socket;										//Socket ��ü ����
	
	private BufferedReader inMsg =null;							//�Է� ���۸��� ����
	private PrintWriter outMsg =null;							//��� ��ü ����
	
	private Message m = null;									//�޼��� ��ü �Ѱ� ����
	private Thread thread;										//���� Runnable �� ���� ������ ����
	
	public MultiChatController(MultiChatData chatData, MultiChatUI v)
	{
		logger = Logger.getLogger(this.getClass().getName());	//Logger ����
		this.chatData = chatData;								//chatData �ܵ� ��ü ���� chatData�� ���̱�
		this.v = v;												//MultiChatUI �ܵ� ��ü ���� v �� ���̱� 
	}
	
	public void appMain()
	{
		chatData.addObj(v.msgOut);								//chatData ���� UI�� ä���� ����Ҽ� �ֵ��� ���� ����
		chatData.connectMember(v.info);							//chatData ���� UI�� �ɹ��� ����Ҽ� �ֵ��� ���� ����
		
		v.addButtonActionListener(new ActionListener(){			//ActionListener �޼ҵ� ���� �� ����
			
			public void actionPerformed(ActionEvent e)
			{
				Object obj = e.getSource();
				if(obj == v.exitButton)							//�����ư Ŭ���� ���α׷� ����
				{
					System.exit(0);
				}
				else if(obj == v.loginButton)					//�α��� ��ư Ŭ���� 
				{
					v.id = v.idInput.getText();					//���̵� ��������
					v.outLabel.setText(" ��ȭ�� : "+v.id);		//��ȭ�� + id �� ���̱�
					v.cardLayout.show(v.tab, "logout");			//logOut �гη� UI ��� �г� ����
					connectServer();							//���� ����
					
					status = true;								//ä�� ������� ����
				}
				else if(obj == v.logoutButton)					//�α׾ƿ� ��ư Ŭ����
				{
					outMsg.println(gson.toJson(new Message(v.id, "", "", "logout" , "")));	//Message logout ���·� ��ȯ
					v.msgOut.setText("");
					v.info.setText("");							//�ɹ� ���� ��� ��������� �ʱ�ȭ
					v.cardLayout.show(v.tab ,"login");			//login �гη� UI ��� �г� ����
					outMsg.close();								//��� ����
					
					try
					{
						inMsg.close();							//���۸��� ����
						socket.close();							//���� ���� (����)
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
					
				}
				else if(obj == v.msgInput)						//enter�� ������ ���
				{
					outMsg.println(gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg" , "")));	//��½�Ʈ���� �޼��� ����
					v.msgInput.setText("");						//���� UI �Է¶� �ʱ�ȭ
				}
			}
			
		});
		
	}
	public void connectServer()									//���� ����
	{
		try
		{
			socket = new Socket("localhost", 5000);			//���� ����
			logger.info("[Client]Sever ���� ����!!");
			
			//����� ��Ʈ�� ����
			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//�Է� ��Ʈ�� ����
			outMsg = new PrintWriter(socket.getOutputStream(), true);					//��� ��Ʈ�� ����
			
			//������ �α��� �޽��� ����
			m = new Message(v.id, "","","login", "");
			
			outMsg.println(gson.toJson(m));						//��� ��Ʈ�� ��ü�� �޼����� Json �ڵ�� �Ľ�
			System.out.println(gson.toJson(m));
			
			thread = new Thread(this);							//Runnable �� ������ ���̱�
			thread.start();										//������ ����
		}
		catch(Exception e)
		{
			logger.warning("[MultiChatUI]connectServer() Exception �߻�!!");
			e.printStackTrace();
		}
	}
	
	
	public void run()
	{
		String msg;															//�޼��� ����
		
		while(status)
		{
			try
			{
				msg = inMsg.readLine();										//�Է� ��Ʈ������ ���� ���� ���� �޼��� ����
				m = gson.fromJson(msg ,Message.class);						//Json �ڵ�� �Ľ�
				
				chatData.refreshData(m.getId() + ">" + m.getMsg() + "\n");	//UI ä��â�� �Է�
				System.out.println(m.getId() + ">" + m.getMsg() + "\n");
				chatData.updateMember(m.getMembers());						//UI �ɹ�����Ʈ �ʱ�ȭ �� �缳��
				
				v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength()); //Ŀ���� ���������� �ű�
			}
			catch(IOException e)
			{
				logger.warning("[MultiChatUI]�޽��� ��Ʈ�� ����!!");
				status = false;
			}
		}
		
		logger.info("[MultiChatUI]" + thread.getName() + "�޽��� ���� ������ �����!!");
	}
	
	
	/*
	 * �߰����
	 * -ä�ù濡 �����ϰ� �ִ� ������ ǥ���ϴ� ��� �ο�
	 * -���� TextArea �� ���� �����ϰ� �ִ� ������ ǥ��
	 * -���α׷� ���� �̻�
	 * */
	public static void main(String [] args)
	{


		MultiChatServer server = new MultiChatServer();								//���� ����
		server.start();
		
		MultiChatController app = new MultiChatController(new MultiChatData(), 		//Client �κ� ����
				new MultiChatUI());
		app.appMain();
		
		MultiChatController app1 = new MultiChatController(new MultiChatData(), 		//Client �κ� ����
				new MultiChatUI());
		app1.appMain();
		MultiChatController app2 = new MultiChatController(new MultiChatData(), 		//Client �κ� ����
				new MultiChatUI());
		app2.appMain();
		MultiChatController app3 = new MultiChatController(new MultiChatData(), 		//Client �κ� ����
				new MultiChatUI());
		app3.appMain();
		
		
	}
	
	
}
