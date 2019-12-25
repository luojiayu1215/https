package com.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame{
	/*
	 * �����ҿͻ���
	 * */
	 private static final long serialVersionUID = 1L;
	    Socket socket;
	    PrintWriter pWriter;
	    BufferedReader bReader;
	    JPanel panel;
	    JScrollPane sPane;
	    JTextArea txtContent;
	    JLabel lblName,lblSend;
	    JTextField txtName,txtSend;
	    JButton btnSend;

	    public ChatClient() {
	        super("QQ ������");
	        txtContent = new JTextArea();
	        //�����ı���ֻ��
	        txtContent.setEditable(false);
	        sPane = new JScrollPane(txtContent);

	        lblName = new JLabel("�ǳ�:");
	        txtName = new JTextField(5);
	        lblSend = new JLabel("����:");
	        txtSend = new JTextField(20);
	        btnSend = new JButton("����");

	        panel = new JPanel();
	        panel.add(lblName);
	        panel.add(txtName);
	        panel.add(lblSend);
	        panel.add(txtSend);
	        panel.add(btnSend);
	        this.add(panel, BorderLayout.SOUTH);

	        this.add(sPane);
	        this.setSize(500,300);
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        try {
	            //����һ���׽���
	            socket = new Socket("127.0.0.1",8088);
	            //����һ�����׽�����д���ݵĹܵ��������������������������Ϣ
	            pWriter = new PrintWriter(socket.getOutputStream());
	            //����һ�����׽��ֶ����ݵĹܵ�����������������������������Ϣ
	            bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        } catch (UnknownHostException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        //ע�����
	        btnSend.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // ��ȡ�û�������ı�
	                String strName = txtName.getText();
	                String strMsg = txtSend.getText();
	                if(!strMsg.equals("")) {
	                    //ͨ������������ݷ��͸�������
	                    pWriter.println(strName+"˵��"+strMsg);
	                    pWriter.flush();
	                    //����ı���
	                    txtSend.setText("");
	                }
	            }
	        });
	        //�����߳�
	        new GetMsgFromServer().start();
	    }
	    //���ܷ�������������Ϣ���߳�
	    class GetMsgFromServer extends Thread{
	        @Override
	        public void run() {
	            // TODO Auto-generated method stub
	            while (this.isAlive()) {
	                try {
	                    String strMsg = bReader.readLine();
	                    if(strMsg != null) {
	                        //���ı�������ʾ������Ϣ
	                        txtContent.append(strMsg + "\n");
	                    }
	                    Thread.sleep(50);
	                } catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	    public static void main(String[] args) {
	        //���������ҿͻ��˴���ʵ��������ʾ
	        new ChatClient().setVisible(true);
	    }
	}
