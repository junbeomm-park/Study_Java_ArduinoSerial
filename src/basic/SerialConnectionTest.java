package basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialConnectionTest {
	public void connect(String proName) {
		
		try {
			//COM포트가 실제 존재하고 사용가능한 상태인지 확인
			CommPortIdentifier comPortIdentifier = 
					CommPortIdentifier.getPortIdentifier(proName);
			//포트가 사용 중인지 체크
			if(comPortIdentifier.isCurrentlyOwned()) {
				System.out.println("포트를 사용할 수 없습니다");
			}else {
				System.out.println("포트 사용 가능");
				//port가 사용가능하면 포트를 열고 포트객체를 가져오기
				//=> 포트를 통해 통신할 수 있는 input/output객체를 가져올 수 있다.
				//=> 매개변수1 : 포트를 열고 사용하는 프로그램의 이름
				//=> 매개변수2 : 포트를 열고 통신하기 위해서 기다리는 시간(mills)
				CommPort commPort = 
						comPortIdentifier.open("basic_serial", 3000);
				System.out.println(commPort);
				//commPort는 종류가 두가지
				//=> Serial포트인지 Parallel포트인지 확인
				//SerialPort, ParallelPort
				if(commPort instanceof SerialPort) {
					//Serial포트객체를 가져오기
					SerialPort serialPort = (SerialPort)commPort;
					//SerialPort에 대한 설정
					//=> 통신속도, 데이터길이, stop bit, parity bit등을 설정
					serialPort.setSerialPortParams(
									9600, //Serial port통신속도
									SerialPort.DATABITS_8, //데이터길이, 한 번에 8bit씩 데이터가 전송
									SerialPort.STOPBITS_1, //stop bit(끌을 표시)
									//시리얼통신에서 송수신되는 데이터의 오류를 검출하기 위해 사용
									SerialPort.PARITY_NONE); 
					//시리얼포트와 통신 할 수 있도록 input/output스트림객체 구하기
					InputStream in = serialPort.getInputStream();
					OutputStream out = serialPort.getOutputStream();
					//시리얼통신을 하기 위해서 쓰레드를 start
					//=> 쓰레드 내부에서 시리얼포트와 통신을 할 수 있도록 스트림객체를
					//   매개변수로 전달
					new Thread(new SerialReaderThread(in)).start();
					new Thread(new SerialWriterThread(out)).start();
				}
			}
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
			new SerialConnectionTest().connect("COM5");
	}

}
