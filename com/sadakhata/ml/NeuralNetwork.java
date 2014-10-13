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

class NeuralNetwork implements Serializable{
	int numInputUnit;
	int numOutputUnit;
	int numHiddenLayer;
	int numNeuronPerHiddenLayer;
	int numTotalLayer;

	NeuronLayer neuronlayers[];

	int L; //id of last layer

	double learningRate, prevError, currentError, lamda;


	int numTrained;




	public NeuralNetwork(int _inputUnit, int _outputUnit, int _hiddenLayer, int _neuronPerHiddenLayer)
	{
		learningRate = 0.5;
		lamda = 0.001;
		prevError = Double.POSITIVE_INFINITY;
		numTrained = 0;


		numInputUnit = _inputUnit;
		numOutputUnit = _outputUnit;
		numHiddenLayer = _hiddenLayer;
		numNeuronPerHiddenLayer = _neuronPerHiddenLayer;

		numTotalLayer = 2 + numHiddenLayer;

		L = numTotalLayer - 1;

		neuronlayers = new NeuronLayer[numTotalLayer];

		neuronlayers[0] = new NeuronLayer(numInputUnit);
		neuronlayers[L] = new NeuronLayer(numOutputUnit);

		for(int i=1; i < L; i++)
		{
			neuronlayers[i] = new NeuronLayer(numNeuronPerHiddenLayer);
		}

		for(int i=0; i<=L; i++)
		{
			for(int j=0; j<neuronlayers[i].numNeurons; j++)
			{
				if(i == 0)
				{
					neuronlayers[i].neurons[j] = new Neuron(0);
				}
				else
				{
					if(j < neuronlayers[i].numNeurons-1)
					{
						neuronlayers[i].neurons[j] = new Neuron( neuronlayers[i-1].numNeurons );
					}
					else
					{
						neuronlayers[i].neurons[j] = new Neuron(0); //bias node don't have any input edges.
					}
				}
			}
			neuronlayers[i].neurons[ neuronlayers[i].numNeurons - 1].y = 1; //bias node's output value
		}
	}

	public int feedForward(double x[])
	{
		for(int i=0; i<x.length; i++)
		{
			neuronlayers[0].neurons[i].y = x[i];
		}

		for(int i=1; i<=L; i++)
		{
			feedALevel(neuronlayers[i-1], neuronlayers[i]);
		}
		

		double maxVal = -1;
		int idOfMaxVal = -1;
		
		for(int i=0; i<numOutputUnit; i++)
		{
			if(neuronlayers[L].neurons[i].y > maxVal)
			{
				maxVal = neuronlayers[L].neurons[i].y;
				idOfMaxVal = i;
			}
		}
		return idOfMaxVal;

	}

	public void feedALevel(NeuronLayer prevLayer, NeuronLayer curLayer)
	{
		for(int n=0; n < curLayer.numNeurons - 1; n++)
		{
			curLayer.neurons[n].z = 0;

			for(int i=0; i<prevLayer.numNeurons; i++)
			{
				curLayer.neurons[n].z += curLayer.neurons[n].weightVector[i] * prevLayer.neurons[i].y;
			}

			curLayer.neurons[n].y = sigmoid( curLayer.neurons[n].z );

		}
	}


	public double calcError(double x[][], double t[][], int m)
	{
		double error = 0;
		double tmp = 0;
		double hx;

		for(int i=0; i<m; i++)
		{
			feedForward(x[i]);

			for(int j = 0; j<numOutputUnit; j++)
			{
				hx = neuronlayers[L].neurons[j].y;
				error += ( -t[i][j]*Math.log(hx) - (1 - t[i][j])*Math.log(1 - hx) );
			}
		}




		for(int i=1; i<=L; i++)
		{
			for(int j=0; j<neuronlayers[i].numNeurons; j++)
			{
				for(int k=0; k<neuronlayers[i].neurons[j].numInEdges; k++)
				{
					tmp += ( neuronlayers[i].neurons[j].weightVector[k]*neuronlayers[i].neurons[j].weightVector[k] );
				}
			}
		}

		//error = (error + 0.5*lamda*tmp)/(double)m;
		
		error = error/(double)m + 0.5*lamda*tmp; //just testing

		System.out.printf("cost J = %.20f & learningRate = %.10f\n", error, learningRate);

		return error;
	}



	public void backPropagate(double x[][], double t[][], int m)
	{
		for(int kase = 0; kase<m; kase++)
		{
			feedForward(x[kase]);
			for(int i=0; i<numOutputUnit; i++)
			{
				neuronlayers[L].neurons[i].delta = neuronlayers[L].neurons[i].y - t[kase][i];
			}

			for(int i=L-1; i>0; i--)
			{
				calcDelta(neuronlayers[i], neuronlayers[i+1]);
			}

			for(int i=L; i>0; i--)
			{
				for(int j=0; j<neuronlayers[i].numNeurons; j++)
				{
					for(int k=0; k<neuronlayers[i].neurons[j].numInEdges; k++)
					{
						neuronlayers[i].neurons[j].bigDelta[k] += neuronlayers[i-1].neurons[k].y * neuronlayers[i].neurons[j].delta;
					}
				}
			}
		}



		for(int i=1; i<=L; i++)
		{
			for(int j=0; j<neuronlayers[i].numNeurons; j++)
			{
				for(int k=0; k<neuronlayers[i].neurons[j].numInEdges; k++)
				{

					neuronlayers[i].neurons[j].bigDelta[k] /= (double)m;

					if(k < neuronlayers[i].neurons[j].numInEdges - 1)
					{
						neuronlayers[i].neurons[j].bigDelta[k] += ( lamda*neuronlayers[i].neurons[j].weightVector[k] );
					}

					/*
					 * Now bigDelta[k] is derivative of Error Function J 
					 * wrt weightVector[k]. Here, the lamda
					 * factor resist from overfitting.
					*/

					neuronlayers[i].neurons[j].weightVector[k] -= (learningRate * neuronlayers[i].neurons[j].bigDelta[k]);
					neuronlayers[i].neurons[j].bigDelta[k] = 0;
				}
			}
		}



		/************************/
		/*
		 * error calculation and fixing learning rate depending on previous error
		 * and current error.
		*/

		currentError = calcError(x, t, m);
		if(currentError < prevError && learningRate < 0.9 )
		{
			learningRate = learningRate * 1.05;
		}
		else
		{
			learningRate = learningRate * 0.65;
		}

		prevError = currentError;

		numTrained++;

	}


	public void calcDelta(NeuronLayer curLayer, NeuronLayer forwardLayer)
	{
		for(int i=0; i < curLayer.numNeurons - 1; i++)
		{
			double delta = 0;
			for(int j=0; j<forwardLayer.numNeurons-1; j++)
			{
				delta += forwardLayer.neurons[j].weightVector[i] * forwardLayer.neurons[j].delta;
			}
			delta = delta * curLayer.neurons[i].y * (1.0 - curLayer.neurons[i].y);
			curLayer.neurons[i].delta = delta;
		}
	}


	public double sigmoid(double z)
	{
		return 1.0/(1.0 + Math.exp(-z));
	}



}