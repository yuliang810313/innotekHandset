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
				"干球目标温度",
				"湿球目标温度",
				"干球实际温度",
				"湿球实际温度",
				"装烟量",
				"风门打开",
				"循环风机打开",
				"助燃风机打开",
				"风门关闭",
				"循环风机关闭",
				"助燃风机关闭"
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
				"稳定温度超时，请重新设置数据",
				"物联网连接失败",
				"变频器通讯失败",
				"干球温度偏高",
				"干球温度偏低",
				"湿球温度偏高",
				"湿球温度偏低",
				"传感器故障",
				"风机无电流",
				"风机缺相",
				"风机过载",
				"电压偏低，请关闭系统电源",
				"电压偏高，请关闭系统电源",
				"晶体失效，请换主板",
				"故障：存储器数据错误，请恢复出厂值",
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
		
		//array[array_i++] = "干球实际温度:" + (float)tmp /10;
		array[array_i++] = String.valueOf((float)tmp /10);
		
		tmp = info[6];
		tmp <<= 8;
		tmp |= info[7];
		//array[array_i++] = "湿球实际温度:" + (float)tmp /10;
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
