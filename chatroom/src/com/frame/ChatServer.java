package com.frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class ChatServer {
		//�������������׽���ServerSocket
	    ServerSocket serverSocket;
	    //�������б���
	    ArrayList<BufferedReader> bReaders = new ArrayList<BufferedReader>();
	    //�������б���
	    ArrayList<PrintWriter> pWriters = new ArrayList<PrintWriter>();
	    //������Ϣ������
	    LinkedList<String> msgList =new LinkedList<String>();
	
	    public ChatServer() {
	        try {
	            //�������������׽���ServerSocket,��8888�˿ڼ���
	            serverSocket = new ServerSocket(8088);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        //�������ܿͻ���Socket���߳�ʵ����������
	        new AcceptSocketThread().start();
	        //������ �ͻ��˷�����Ϣ���߳�ʵ����������
	        new SendMsgToClient().start();
	        System.out.println("�������Ѿ�����...");
	    }
	
	    //���տͻ���Socket�׽����߳�
	    class AcceptSocketThread extends Thread{
	        public void run() {
	            while(this.isAlive()) {
	                try {
	                    //����һ���ͻ���Socket����
	                    Socket socket = serverSocket.accept();
	                    //�����ÿͻ��˶�ͨ�Źܵ�
	                    if(socket != null) {
	                        //��ȡSocket�����������
	                        BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                        //����������ӵ��������б�����
	                        bReaders.add(bReader);
	                        //����һ���߳̽��տͻ��˶�������Ϣ
	                        new GetMsgFromClient(bReader).start();
	                        //��ȡSocket����������������ӵ��������б�����
	                        pWriters.add(new PrintWriter(socket.getOutputStream()));
	                    }
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                } 
	            }
	        }
	    }
	    //���տͻ��˶�������Ϣ���߳�
	    class GetMsgFromClient extends Thread{
	        BufferedReader bReader;
	
	        public GetMsgFromClient(BufferedReader bReader) {
	            // TODO Auto-generated constructor stub
	            this.bReader = bReader;
	        }
	
	        public void run() {
	            while(this.isAlive()) {
	                String strMsg;
	                try {
	                    strMsg = bReader.readLine();
	                    if(strMsg != null) {
	                        //SimpleDateFormat ���ڸ�ʽ���࣬�ƶ����ڸ�ʽ
	                        //"��-��-�� ʱ:��:��",����"2017-11-06 23:06:11"
	                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                        //��ȡ��ǰϵͳʱ�䣬��ʹ�����ڸ�ʽ���໪Ϊ�ƶ���ʽ���ַ���
	                        String strTime = dateFormat.format(new Date());
	                        //��ʱ�����Ϣ��ӵ���Ϣ��������
	                        msgList.addFirst("<==" + strTime + "==>\n" + strMsg);
	                    }
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }
	
	    }
	    //�����пͻ�����������Ϣ���߳�
	    class SendMsgToClient extends Thread{
	        public void run() {
	            while(this.isAlive()) {
	                try {
	                    //�����Ϣ�����ϲ��գ�����������Ϣδ���ͣ�
	                    if(!msgList.isEmpty()) {
	                        //ȡ��Ϣ�������ж����һ�������Ƴ�
	                        String msg = msgList.removeLast();
	                        //��������б��Ͻ��б�����ѭ��������Ϣ�����пͻ���
	                        for (int i = 0; i < pWriters.size(); i++) {
	                            pWriters.get(i).println(msg);
	                            pWriters.get(i).flush();
	                        }
	                    }
	                } catch (Exception e) {
	                    // TODO: handle exception
	                }
	            }
	        }
	    }
	    //����������
	    public static void main(String args[]) {
	        new ChatServer();
	    }
}

