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

class NeuronLayer implements Serializable{
	int numNeurons;
	Neuron neurons[];

	public NeuronLayer(int neuronInThisLayer)
	{
		numNeurons = neuronInThisLayer + 1;
		neurons = new Neuron[numNeurons];
	}
}