package com.sadakhata.ml;

import java.io.*;

public class SKNeuralNetwork{
	NeuralNetwork neuralnetwork;
	String dataFileName;
	public void saveData()
	{
		try{
			
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(new FileOutputStream(dataFileName));
			
			objectoutputstream.writeObject(neuralnetwork);
		
		}catch(Exception ex)
		{
			System.out.println("<<<<<<<<< COULDN'T WRITE OBJECT >>>>>>>>>>>");
		}
	}

	public SKNeuralNetwork(int _inputUnit, int _outputUnit , int _hiddenLayer , int _neuronPerHiddenLayer, String _dataFileName )
	{
		dataFileName = _dataFileName;

		try{
			
			ObjectInputStream objectinputstream = new ObjectInputStream(
				new FileInputStream(dataFileName)
					);

			try{
				neuralnetwork = (NeuralNetwork) objectinputstream.readObject();
			}catch(Exception ex)
			{
				System.out.println("<<< COULDN'T READ OBJECT >>>");
				neuralnetwork = new NeuralNetwork(_inputUnit, _outputUnit, _hiddenLayer, _neuronPerHiddenLayer);
			}
		}catch(Exception ex)
		{
				System.out.println("<<< COULDN'T READ OBJECT >>>");
				neuralnetwork = new NeuralNetwork(_inputUnit, _outputUnit, _hiddenLayer, _neuronPerHiddenLayer);
		}
	}

	public int feedForward(double x[])
	{
		return neuralnetwork.feedForward(x);
	}

	public void backPropagate(double x[][], double t[][], int m)
	{
		neuralnetwork.backPropagate(x, t, m);
	}

	public int getNumTrained()
	{
		return neuralnetwork.numTrained;
	}


	public void printCostFunctionJ(double x[][], double t[][], int m)
	{
		neuralnetwork.calcError(x, t, m);
	}
}