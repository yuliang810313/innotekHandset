package com.hdhe.rfid;

public interface RfidNfcInterface {
	
	/**
	 * Ϊ��д�������µĵ�ַ
	 * @param hexAddr
	 * @return
	 */
	int setAddress(byte hexAddr);
	/**
	 * ���ò�����
	 * @param buadrate
	 * @return
	 */
	int setBaudRate(int buadrate);

}
