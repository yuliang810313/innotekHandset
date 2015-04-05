package com.hdhe.rfid;

public interface RfidNfcInterface {
	
	/**
	 * 为读写器设置新的地址
	 * @param hexAddr
	 * @return
	 */
	int setAddress(byte hexAddr);
	/**
	 * 设置波特率
	 * @param buadrate
	 * @return
	 */
	int setBaudRate(int buadrate);

}
