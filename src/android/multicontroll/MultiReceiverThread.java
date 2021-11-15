package android.multicontroll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiReceiverThread extends Thread{
	Socket client;
	BufferedReader clientIn;//클라이언트가 보내는 메시지를 읽는 스트림
	PrintWriter clientOut;//클라이언트에게 메시지를 보내는 스트림
	SerialArduinoMultiLEDControl serialObj;//시리얼 통신을 위한 객체
	OutputStream serialOut;//시리얼통신에서 아두이노로 데이터를 내보내기 위한 스트림
	
	public MultiReceiverThread(Socket client) {
		super();
		this.client = client;
		//초기화
		try {
			//클라이언트가 보내오는 메시지를 읽기 위한 스트림생성
			clientIn = new BufferedReader(
						new InputStreamReader(
								this.client.getInputStream()));
			//클라이언트에게 메시지를 전송하기 위한 스트림생성
			clientOut = new PrintWriter(this.client.getOutputStream(),true);
			
			//아두이노와 시리얼통신을 하기 위한 객체
			serialObj = new SerialArduinoMultiLEDControl();
			serialObj.connect("COM5");
			serialOut = serialObj.getOutPut();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		//클라이언트의 메시지를 받아서 아두이노로 전송
		while (true) {
			try {
				String msg = clientIn.readLine();
				System.out.println("클라이언트가 보낸 메시지 : "+msg);
				if(msg.equals("led_on1")) {
					serialOut.write('o');
				}else if(msg.equals("led_off1")){
					serialOut.write('f');
				}
				else if(msg.equals("led_on2")) {
					serialOut.write('p');
				}else if(msg.equals("led_off2")){
					serialOut.write('g');
				}
				else if(msg.equals("led_on3")) {
					serialOut.write('i');
				}else if(msg.equals("led_off3")){
					serialOut.write('d');
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
