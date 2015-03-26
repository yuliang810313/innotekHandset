package com.innotek.handset.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class RFIDReader {
	
	/**
	 * ��ͷ
	 */
	private final static byte CMD_HEAD = (byte)0xAA ; 
	/**
	 * ��β
	 */
	private final static byte CMD_END = (byte)0xBB ;
	/**
	 * ��д����ַ
	 */
	private final static byte CMD_ADDR = (byte)0x00 ;
	/**
	 * ��ȡ��д���汾
	 */
	private final static byte CMD_GET_VERSION = (byte)0x86 ;
	/**
	 * NFCѰ��
	 */
	private final static byte CMD_NFC_GET_SN = (byte)0x25 ; 
	/**
	 * ���߸���ֻ��һ�ſ�
	 */
	private final static byte SN_FLAG_ONLYCARD = 0x00;
	/**
	 * ���߸������������ſ�
	 */
	private final static byte SN_FLAG_NOT_ONLYCARD = 0x01;
	/**
	 * MF ����Ѱ��������ͻ��ѡ������֤���룬�����Ȳ�����
	 */
	private final static byte CMD_MF_READ = (byte)0x20 ; 
	/**
	 * MF ����Ѱ��������ͻ��ѡ������֤���룬д���Ȳ���
	 */
	private final static byte CMD_MF_WRITE = (byte)0x21 ;
	
	private final static int PORT = 12;
	
	private final static int BORDRATE = 9600;
	
	private SerialPort mserialport;//���ڲ�����
	
	private InputStream is; //����������
	
	private OutputStream os; //���������
	
	public RFIDReader(){
		try {
			this.mserialport = new SerialPort(PORT, BORDRATE, 0);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(mserialport != null){
			//��ȡ���������������
			this.is = mserialport.getInputStream();
			this.os = mserialport.getOutputStream();
			//�򿪶�д����Դ
			mserialport.poweOn();
		}
		
	}
	
	/**
	 * ���汾��
	 * @return
	 */
	public byte[] getVersion(){
		byte[] cmd = new byte[6];
		byte[] recv = null;
		cmd[0] = this.CMD_HEAD;
		cmd[1] = this.CMD_ADDR;
		cmd[2] = 0x01;
		cmd[3] = this.CMD_GET_VERSION;
		cmd[4] = getCRC(cmd);
		cmd[5] = this.CMD_END;
		Log.e("getVersion", Tools.Bytes2HexString(cmd, cmd.length));
		sendCMD(cmd);
		
		byte[] buffer = getRECV();
		recv = resolveData(buffer);
		return recv;
	}
	
	/****************************NFC ����************************************/
	
	/**
	 * NFCѰ��
	 */
	public byte[] getNFCuid(){
		byte[] cmd = new byte[8];
		byte[] recv = null;
		cmd[0] = this.CMD_HEAD;
		cmd[1] = CMD_ADDR;
		cmd[2] = 0x03;
		cmd[3] = CMD_NFC_GET_SN;
		cmd[4] = 0x52;
		cmd[5] = 0x00;
		cmd[6] = getCRC(cmd);
		cmd[7] = CMD_END;
		Log.e("getNFCuid", Tools.Bytes2HexString(cmd, cmd.length));
		sendCMD(cmd);
		
		byte[] buffer = getRECV();
		if(buffer != null){
			buffer = resolveData(buffer);
			if(buffer != null && buffer[0] == SN_FLAG_ONLYCARD){
				recv = new byte[buffer.length - 1];
				System.arraycopy(buffer, 1, recv, 0, buffer.length - 1);
			}
		}
		return recv;
	}
	
	/**
	 * NFC ��ҳ����
	 * @param page
	 */
	public byte[] readNFCpage(int page){
		byte[] cmd = new byte[9];
		byte[] recv = null;
		cmd[0] = CMD_HEAD;
		cmd[1] = CMD_ADDR;
		cmd[2] = 0x04;
		cmd[3] = CMD_MF_READ;
		cmd[4] = 0x00;
		cmd[5] = 0x01;
		cmd[6] = (byte)page;
		cmd[7] = getCRC(cmd);
		cmd[8] = CMD_END;
		Log.e("readNFCpage", Tools.Bytes2HexString(cmd, cmd.length));
		
		sendCMD(cmd);
		
		byte[] buffer = getRECV();
		if(buffer != null){
			recv = resolveData(buffer);
		}
		return recv;
	}
	
	/**
	 * дҳ����
	 * @param page  ҳ��
	 * @param bytes ����
	 * @return д�뿨��uid
	 */
	public byte[] writeNFCpage(int page, byte[] bytes){
		byte[] cmd = new byte[13];
		byte[] recv = null;
		cmd[0] = CMD_HEAD;
		cmd[1] = CMD_ADDR;
		cmd[2] = 0x08;
		cmd[3] = CMD_MF_WRITE;
		cmd[4] = 0x00;
		cmd[5] = 0x01;
		cmd[6] = (byte) page;
		cmd[7] = bytes[0];
		cmd[8] = bytes[1];
		cmd[9] = bytes[2];
		cmd[10] = bytes[3];
		cmd[11] = getCRC(cmd);
		cmd[12] = CMD_END;
		Log.e("writeNFCpage", Tools.Bytes2HexString(cmd, cmd.length));
		sendCMD(cmd);
		byte[] buffer = getRECV();
		if(buffer != null){
			recv = resolveData(buffer);
		}
		return recv;
	}
	
	/******************************************************/
	
	/****************************15693***************************AA 00 04 10 06 00 00 12 BB **/
	public List<byte[]> inventory15693(){
		List<byte[]> list = new ArrayList<byte[]>();
		byte[] cmd = {(byte)0xAA, (byte)0x00, (byte)0x04, (byte)0x10, (byte)0x06, (byte)0x00, (byte)0x00, (byte)0x12, (byte)0xBB};
		Log.e("inventory 15693", Tools.Bytes2HexString(cmd, cmd.length));
		sendCMD(cmd);
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		byte[] buffer = getRECV();
		if(buffer != null){
			byte[] recv = resolveData(buffer);
			
			if(recv != null && recv.length > 2){
				Log.e("15693 uid", Tools.Bytes2HexString(recv, recv.length));
				int cardCount = recv[0];
				int i = 0;
				if(recv.length > cardCount*10){
					while(i < cardCount){
						byte[] uid = new byte[8];
						System.arraycopy(recv, i*10 + 3, uid, 0, 8);
						Log.e("15693 uid", Tools.Bytes2HexString(uid, uid.length));
						list.add(uid);
						i++;
					}
				}
			}
			
		}
		
		return list ;
	}
	
	
	/**
	 * �����֤UID
	 * @return
	 */
	public byte[] readID(){
		byte[] cmd = {(byte)0xAA, (byte)0x00, (byte)0x01, (byte)0x09, (byte)0x08, (byte)0xBB};
		byte[] recv = null;
		Log.e("readID", Tools.Bytes2HexString(cmd, cmd.length));
		sendCMD(cmd);
		byte[] buffer = getRECV();
		if(buffer != null){
			recv = resolveData(buffer);
		}
		return recv;
	}
	
	/**
	 * ������������
	 * @param recv
	 * @return
	 */
	private byte[] resolveData(byte[] recv){
		byte[] data = null;
		if(recv[0] != CMD_HEAD){
			Log.e("ERROR CODE", "HEAR ERROR");
		}else
		if(recv[1] != CMD_ADDR){
			Log.e("ERROR CODE", "ADDR ERROR");
		}else
		if(recv[3] != 0){
			int errorcode = recv[3];
			Log.e("ERROR CODE", "ERROR :" + errorcode);
		}else if(recv[recv.length - 1] != CMD_END){
			Log.e("ERROR CODE", "ERROR END" );
		}
		else{
			int len = recv[2]&0xFF;
			data = new byte[len - 1];
			System.arraycopy(recv, 4, data, 0, len - 1);
		}
		return data;
	}
	
	/**
	 * ���շ�������
	 * @return
	 */
	private byte[] getRECV(){
		int count = 0;
		int index = 0;
		int allcount = 0;
		byte[] recv = null ;
		try{
			while(count < 3){
				  count = this.is.available();
				  if(index > 50){
				    return null;//��ʱ
				 }else{
					index++;
					try{
					  Thread.sleep(10);
					  }catch(InterruptedException e){
					   e.printStackTrace();
					 }
					}
				}
		allcount = this.is.available();
		recv = new byte[allcount];
		is.read(recv);
		}catch(Exception e){
			
		}
		if(recv != null){
			System.out.println(Tools.Bytes2HexString(recv, recv.length));
		}
		return recv;
	}
	
	//����ָ��
	private void sendCMD(byte[] cmd){
		try {
			os.write(cmd);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������
	 * @param cmd
	 * @return
	 */
	private byte getCRC(byte[] cmd){
		byte crc = 0x00 ;
		for(int i = 1; i < cmd.length - 2; i++){
			crc = (byte)(crc^cmd[i]);
		}
		return crc;
	}
	
	
	public void close(){
	
		
		if(is != null && os != null){
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mserialport != null){
			mserialport.powerOff();
			mserialport.close(PORT);
		}
	}
}
