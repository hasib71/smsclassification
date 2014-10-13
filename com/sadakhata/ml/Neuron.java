/*
 * Author: Hasib Al Muhaimin.
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 * 
 */

package com.sadakhata.ml;


import java.io.*;

class Neuron implements Serializable{
	double weightVector[];
	int numInEdges;
	double y, z;
	double delta;
	double bigDelta[];

	public Neuron(int inputEdges)
	{
		numInEdges = inputEdges;
		weightVector = new double[numInEdges];
		bigDelta = new double[numInEdges];

		for(int i=0; i<weightVector.length; i++)
		{
			weightVector[i] = getRandomNumber(numInEdges);
		}
	}

	public double getRandomNumber(int n)
	{
		//return Math.random() - 0.5;
		return (2*Math.random() - 1.0) / Math.sqrt(n); // a random number between [-1/sqrt(n), 1/sqrt(n)]
	}

}