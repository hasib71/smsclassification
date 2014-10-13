/*
 * Author: Hasib Al Muhaimin.
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 * 
 */


package com.sadakhata.spamsmsblocker;

import com.sadakhata.ml.*;
import java.io.*;
import java.util.Locale;


 /**
	*These imports should not be
 	*there when deploying. These imports
 	*are only for training dataset.
**/

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class SKSpamBlocker{
	SKNeuralNetwork skneuralnetwork;
	int numInputUnit;
	int numOutputUnit;
	boolean initiated = false;


	//101 prime number
	double primes[] = {1, 1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523};

	public void init()
	{
		if(!initiated)
		{
			numInputUnit = 500;
			numOutputUnit = 2;

			skneuralnetwork = new SKNeuralNetwork(numInputUnit, numOutputUnit, 1, 10, "SPAMBLOCKER.dat" );

			initiated = true;
		}

		train("sms.xls", 200);
	
	}


	
	public boolean isSpam(String mobileNo, String msg)
	{
		boolean spam = false;
		char ch;
		int len = mobileNo.length();
		for(int i=0; i<len; i++)
		{
			ch = mobileNo.charAt(i);
			if( !(ch == '+' || ('0' <= ch && ch <='9') ) )
			{
				spam = true;
				break;
			}
		}

		if(spam)
		{
			return true;
		}
		else
		{
			double x[] = new double[numInputUnit];
			hashFullString(x, msg);
			return (skneuralnetwork.feedForward( x ) == 1 );
		}




	}

	
	private void hashFullString(double x[], String msg)
	{
		msg = msg.toLowerCase(Locale.ENGLISH);
		String delims = "[^a-z]+";
		String msgtokens[] = msg.split(delims);
		
		int h;
		int k = 5;
		for(int i=0; i<msgtokens.length; i++)
		{
			for(int j=1; j<k; j++)
			{
				h = hash(msgtokens[i], (255.0*j)/(double)k);
				x[h] = Math.sqrt(x[h]) + 0.25;		
			}
		}
	}


	
	private int hash(String str, double reduce)
	{
		int len = Math.min(str.length(), 100);
	
		double sum = 0;

		for(int i=0; i<len; i++)
		{
			sum = sum + ( (str.charAt(i) - reduce)*primes[i] )/primes[i+1];
		}

		int ret = ((int)sum % numInputUnit);
		
		if(ret < 0) // precaution.
		{
			ret = ret + numInputUnit;
		}
		return ret;
	}


	private void train(String excelFileName, int times)
	{
		int numSms = 2916; //number of sms in the database(xl file)
		try{
			numSms = (new HSSFWorkbook(new FileInputStream(excelFileName)).getSheetAt(0)).getLastRowNum();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		double x[][] = new double[numSms][numInputUnit];
		double t[][] = new double[numSms][numOutputUnit];

		readExcelFile(excelFileName, x, t, numSms);
		
		for(int i=1; i<=times; i++)
		{
			System.out.print(i+".\t");
			skneuralnetwork.backPropagate(x, t, numSms);

			if((i%100) == 0)
			{
				skneuralnetwork.saveData();
			}
		}

		System.out.println("Total Trained: " + skneuralnetwork.getNumTrained() );
		skneuralnetwork.printCostFunctionJ(x, t, numSms);
		
		
		//checking how many classification error in the Training set.
		int totalError = 0;
		for(int i=0; i<numSms; i++)
		{
			if(t[i][ skneuralnetwork.feedForward(x[i]) ] == 0)
			{
				totalError++;
				System.out.println(i+" = "+skneuralnetwork.feedForward(x[i]));
			}
		}
		System.out.println("TotalError = " + totalError);
		

		

	}

	private void readExcelFile(String excelFileName, double x[][], double t[][], int numRows)
	{
		
		try{
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFileName));
			HSSFSheet worksheet = workbook.getSheetAt(0);
			for(int rowIdx = 0; rowIdx < numRows; rowIdx++ )
			{
				HSSFRow row = worksheet.getRow(rowIdx);

				String msgCellVal = row.getCell(4).getStringCellValue();
				int spamCellVal = (int)row.getCell(5).getNumericCellValue();

				hashFullString(x[rowIdx], msgCellVal);
				t[rowIdx][spamCellVal] = 1;

				//System.out.println(rowIdx);
			}
		
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}