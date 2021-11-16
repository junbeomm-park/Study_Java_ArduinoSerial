package android.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SenderThread extends Thread{
	Socket client;
	BufferedReader clientIn;//클라이언트가 보내는 메시지를 읽는 스트림
	PrintWriter clientOut;//클라이언트에게 메시지를 보내는 스트림
	InputStream serialIn;//시리얼통신을 하기 위한 스트림
	public SenderThread(Socket client, InputStream serialIn) {
		super();
		this.client = client;
		this.serialIn = serialIn;
		//초기화
		try {
			clientIn = new BufferedReader(
						new InputStreamReader(
								this.client.getInputStream()));
			//클라이언트에게 메시지를 전송하기 위한 스트림생성
			clientOut = new PrintWriter(this.client.getOutputStream(),true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			//시리얼통신으로 input된 데이터를 읽어서
			while((len = serialIn.read(buffer))>-1) {
				String mydata = new String(buffer,0,len);
				System.out.print(mydata);
				//소켓객체를 통해 클라이언트로 내보내기 - 안드로이드로 나간다.
				if(mydata.trim().length()>0) {
					clientOut.println(mydata);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
