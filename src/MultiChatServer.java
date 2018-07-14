/*
 * Sejong University Digital Contents
 * ��ü���� ������ ����#7
 *
 * MultiChatServer.class
 * �ۼ���: 122235 ������
 * �ۼ���: 2017�� 12�� 28��
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
	
	private ServerSocket ss = null;										//�������� ����
	private Socket s = null;											//�Ϲ� ���� ����
	private boolean status;												//ä�� ���� ���� Ȯ�� boolean
	
	ArrayList<ChatThread> chatThreads = new ArrayList<ChatThread>();	//�� Ŭ���̾�Ʈ�� ������� ����� ���⿡ �����ų ����
	ArrayList<String> memList = new ArrayList<String>();				//�ɹ�����Ʈ �������� �����ϱ� ���� ����
	
	private Logger logger;												//Logger ����
	
	public void start()
	{
		status = true;													//���� Ȱ��ȭ
		logger = Logger.getLogger(this.getClass().getName());			//�ΰ� ����
		
		try
		{
			ss = new ServerSocket(5000);								//�������� ��Ʈ ����
			logger.info("MultiChatServer start");
			
			while(status)
			{
				s = ss.accept();										//Ŭ���̾�Ʈ ������ ��������
				ChatThread chat = new ChatThread();						//������ ����
				chatThreads.add(chat);									//�ش� �����带 chatThreads�� �߰�
				chat.start();											//�ش� ������ Ȱ��ȭ
			}
		}catch(Exception e)
		{
			logger.info("[MultiChatServer]start() Exception �߻�!!");
			e.printStackTrace();
		}
	}
	
	class ChatThread extends Thread										//������ Ŭ���̾�Ʈ�� ������ȭ
	{
		String msg;														//�޼���
		Message m = new Message();										//�޼��� ��ü ����
		Gson gson = new Gson();											//Gson ��ü ����
		
		private BufferedReader inMsg = null;
		private PrintWriter outMsg = null;
		
		public void run()
		{
			//���� ������ true �̸� ������ ���鼭 ����ڿ��Լ� ���ŵ� �޽��� ó��
			try {
					inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
					outMsg = new PrintWriter(s.getOutputStream(), true);
					
				} catch (IOException e) {
					e.printStackTrace();
					status = false;
				}
				while(status)												//���� ä��â Ȱ��ȭ�Ͻ�
				{
					//���ŵ� �޽����� msg ������ ����
					try {
						msg = inMsg.readLine();								//�Է� ��Ʈ������ ���� �޼��� ���� ����
					} catch (IOException e) {
						e.printStackTrace();
					}
				
					m = gson.fromJson(msg,Message.class);					//�޼����� json ���� �Ľ��� Message ��ü�� ����
				
					if(m.getType().equals("logout"))						//�α׾ƿ��� ���
					{
						chatThreads.remove(this);							//�ش� Ŭ���̾�Ʈ ������ ����
						
						String temp="";										//�ӽ� String ���� ���� �� �ʱ�ȭ
						
						for(int i =0; i<memList.size(); i++)				//���̵� ��������� �˻� �ǽ�
						{
							if(memList.get(i).equals(m.getId()+"\n"))		//
							{
								memList.remove(i);							//�ɹ�����Ʈ���� ���̵� ����
								break;
							}
						}
						
						for(int i =0; i<memList.size(); i++)				//�ɹ�����Ʈ temp ������ ���ʷ� ����
						{
							temp += memList.get(i);
						}
						msgSendAll(gson.toJson(new Message(m.getId(),"", "���� �����߽��ϴ�.", "server", temp)));	//�α׾ƿ� �޼��� ���
					}
					else if(m.getType().equals("login"))					//�α����� ���
					{
						String temp ="";									//�ӽ� String ���� ���� �� �ʱ�ȭ
						memList.add(new String(m.getId()+"\n"));			//�������Ʈ ��� ���� 
						
						for(int i =0; i<memList.size(); i++)				//����� �������Ʈ ��������
						{
							temp += memList.get(i);							//temp �� �������Ʈ ����
						}
						
						msgSendAll(gson.toJson(new Message(m.getId(), "", "���� �α��� �߽��ϴ�.", "server", temp))); //�α��� �޼��� ���
					}
					else													//�̿��� ��� �޼��� �״�� ��� Ŭ���̾�Ʈ ä��â�� ���
					{
						String temp ="";
						
						for(int i =0; i<memList.size(); i++)				//����� �������Ʈ ��������
						{
							temp += memList.get(i);							//temp �� �������Ʈ ����
						}
						msgSendAll(gson.toJson(new Message(m.getId(),m.getPasswd(),m.getMsg(),m.getType(), temp)));
					}
				}
			
			this.interrupt();											//ä��â�� �ƹ��� ���� ��� ����
			logger.info(this.getName() + " �����!!");
			status = false;
		}

		void msgSendAll(String msg)	//����� ��� ����ڿ��� �޽����� �����ϴ� ����
		{
			
			for(ChatThread ct: chatThreads)								//�� Ŭ���̾�Ʈ�� �� ä�ù� ��� Ŭ���̾�Ʈ ä��â�� ����
			{
				System.out.println(msg);
				ct.outMsg.println(msg);
			}
			
			
		}
	}
}
