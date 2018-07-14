/*
 * Sejong University Digital Contents
 * ��ü���� ������ ����#7
 *
 * MultiChatUI.class
 * �ۼ���: 122235 ������
 * �ۼ���: 2017�� 12�� 28��
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
	
	public String id;									//Client�� ���λ���� id�� ������ ��
	
	private JPanel loginPanel;							//loginPanel ����
	protected JButton loginButton;						//�α��� ��ư ����
	
	private JLabel inLabel;								//��ȭ�� ���� ����
	protected JLabel outLabel;							//��ȭ�� : id ���� ����
	protected JTextField idInput;						//id �Է�ĭ ����
	
	
	
	private JPanel logoutPanel;							//logoutPanel ����
	protected JButton logoutButton;						//logoutButton ����
	
	private JPanel msgPanel;							//ä��â �ϴ� �г� ����
	protected JTextField msgInput;						//�޼��� �Է¶� ����
	protected JButton exitButton;						//���� ��ư ����
	
	protected Container tab;							//�α���, �α� �ƿ� Panel�� ��� �����ҿ���
	protected CardLayout cardLayout;					//tab �� ���� Card Layout ����
	
	protected JTextArea msgOut;							//ä��â �κ� TextArea ����
	
	//���� �г�
	protected JPanel memberPanel;						//�ɹ� ��� ���� �г� ����
	protected JLabel memberlbl;							//�ɹ� ���� �� ����
	protected JTextArea info;							//�ɹ� �̸� ���� txtarea ����
	
	public MultiChatUI()
	{
		super("JUNHYUK TALK");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(1000,700);								//��ü ũ�� ����
		setResizable(false);
		
		
		//�α��� , �α׾ƿ� ���� ��ҵ� ����-------------------------------------------------------------
		//�α���
		loginPanel = new JPanel();								//�α��� �г� ����
		loginPanel.setLayout(new BorderLayout());
		
		idInput = new JTextField(15);							//���̵� �Է¶� ����
		loginButton = new JButton("�α���");						//���̵� �Է� ��ư ����
		
		inLabel = new JLabel("��ȭ�� ");							//��ȭ�� �޼��� ���
		loginPanel.add(inLabel, BorderLayout.WEST);				//��ȭ���� ���� ��������
		loginPanel.add(idInput, BorderLayout.CENTER);			//�Է¶��� �߾�
		loginPanel.add(loginButton, BorderLayout.EAST);			//�α��� ��ư�� ���� ��������
		
		//�α׾ƿ�
		logoutPanel = new JPanel();								//�α׾ƿ� �г� ����
		logoutPanel.setLayout(new BorderLayout());
		outLabel = new JLabel();								//id �� ����
		logoutButton = new JButton("�α׾ƿ�");					//�α׾ƿ� ��ư ����
		
		logoutPanel.add(outLabel, BorderLayout.CENTER);			//���� �߾�����
		logoutPanel.add(logoutButton, BorderLayout.EAST);		//��ư�� ���� ��������

		//�α��� �α׾ƿ� ��� ������ tab ���� �� ���� ��ҵ�
		tab = new JPanel();
		cardLayout = new CardLayout();
		tab.setLayout(cardLayout);
		tab.add(loginPanel, "login");
		tab.add(logoutPanel, "logout");
		tab.setBounds(0,0,100,30);
		add(tab,BorderLayout.NORTH);
		
		//���� ä��â ���� ��ҵ�------------------------------------------------------------
		msgOut = new JTextArea("", 10, 30);						//���� ä��â ����
		msgOut.setFont(new Font("Gothic", Font.PLAIN, 40));
		msgOut.setBackground(new Color(219, 244, 255));
		msgOut.setEditable(false);								//���� ���ϰ� ����
		
		//��ũ�ѹ� ���̱�
		JScrollPane jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(jsp, BorderLayout.CENTER);
		
		msgPanel = new JPanel();								//ä�� �޼��� �Է� �г� ����
		exitButton = new JButton("����");						//���� ��ư ����
		msgInput = new JTextField(80);
		msgPanel.add(msgInput, BorderLayout.WEST);				//�޼��� �Է� ���� ��������
		msgPanel.add(exitButton, BorderLayout.EAST);			//���� ��ư ���� ��������
		add(msgPanel, BorderLayout.SOUTH);						//�޼��� �Է� �г� ���� �ϴܿ� ���̱�
				
		memberPanel = new JPanel();						//�ɹ� �г� ����
		memberPanel.setLayout(new BorderLayout());
		memberlbl = new JLabel("�ɹ�");					//�ɹ� �� ����
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
	
	public void addButtonActionListener(ActionListener listener)	//���� ActionListener ���� �޼ҵ�
	{
		loginButton.addActionListener(listener);
		logoutButton.addActionListener(listener);
		exitButton.addActionListener(listener);
		msgInput.addActionListener(listener);
	}
}
