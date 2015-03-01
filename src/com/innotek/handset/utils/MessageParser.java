package com.innotek.handset.utils;

import android.util.Log;

public class MessageParser {
	
	public static String[] parseInformations(int[] info){
		float val = 0;
		int n = 0;
		int item = 0;

		int tmp = 0;
		String[] array = new String[10];
		
		String[] informations = {
				"����Ŀ���¶�",
				"ʪ��Ŀ���¶�",
				"����ʵ���¶�",
				"ʪ��ʵ���¶�",
				"װ����",
				"���Ŵ�",
				"ѭ�������",
				"��ȼ�����",
				"���Źر�",
				"ѭ������ر�",
				"��ȼ����ر�"
		};
		
		int[][] informationIndex = {
				{0x01, 5, 8},
				{0x02, 6, 9},
				{0x04, 7, 10}
		};
		
		for(int i = 0; i < 5; i++){
			tmp = info[n];
			tmp <<= 8;
			n++;
			tmp |= info[n];
			n++;
			
			val = i < 4 ? ((float)tmp / 10) : (float)tmp;
			//array[item] = informations[i] + ":" + val;
			array[item] = String.valueOf(val);
			item++;
		}
		
		tmp = info[n];
		for(int i = 0; i < 3; ++i){
			if(informationIndex[i][0] == (informationIndex[i][0] & tmp)){
				array[item] = informations[informationIndex[i][1]];
			}else
				array[item] = informations[informationIndex[i][2]];
			item++;
		}
		return array;
	}
	
	public static String[] parseAlert(int[] info){
		Log.i("MESSAGE_PARSE", "The info length is " + info.length);
		String[] array = new String[18];
		int alert_info = 0;
		
		int array_len = 0;
		int length = 16;
		
		
		int array_i = 0;
		int tmp = 0;
		
		int[] errorCode = {
				0x00000001,
				0x00000002,
				0x00000004,
				0x00000008,
				0x00000010,
				0x00000020,
				0x00000040,
				0x00000080,
				0x00000100,
				0x00000200,
				0x00000400,
				0x00000800,
				0x00001000,
				0x00002000,
				0x00004000,
				0x00008000
		};
		
		String[] errorContent = {
				"�ȶ��¶ȳ�ʱ����������������",
				"����������ʧ��",
				"��Ƶ��ͨѶʧ��",
				"�����¶�ƫ��",
				"�����¶�ƫ��",
				"ʪ���¶�ƫ��",
				"ʪ���¶�ƫ��",
				"����������",
				"����޵���",
				"���ȱ��",
				"�������",
				"��ѹƫ�ͣ���ر�ϵͳ��Դ",
				"��ѹƫ�ߣ���ر�ϵͳ��Դ",
				"����ʧЧ���뻻����",
				"���ϣ��洢�����ݴ�����ָ�����ֵ",
				"null"
		};
		
		array_len = info.length - 4;
		
		for(int i = 0; i < array_len; ++i){
			tmp = info[i];
			tmp <<= (i * 8);
			alert_info |= tmp;
			
		}
		
		tmp = info[4];
		tmp <<= 8;
		tmp |= info[5];
		
		//array[array_i++] = "����ʵ���¶�:" + (float)tmp /10;
		array[array_i++] = String.valueOf((float)tmp /10);
		
		tmp = info[6];
		tmp <<= 8;
		tmp |= info[7];
		//array[array_i++] = "ʪ��ʵ���¶�:" + (float)tmp /10;
		array[array_i++] = String.valueOf((float)tmp /10);
		
		for(int i = 0; i < length; ++i){
			if(errorCode[i] == (alert_info & errorCode[i])){
				array[array_i] = errorContent[i];
				array_i++;
			}
		}
		

		return array;
		
	}
}
