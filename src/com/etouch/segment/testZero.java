package com.etouch.segment;

import java.util.Arrays;


public class testZero {
public static void main(String args[]){
	String fieldsOfTypeDate="3|4|5";
		String[] splitOfFieldsTypeDate = fieldsOfTypeDate.split("\\|");
		for(int i=0;i<splitOfFieldsTypeDate.length;i++){
			int id=Integer.parseInt(splitOfFieldsTypeDate[i]);
			System.out.println("id:::"+id);
		}
    }
}
