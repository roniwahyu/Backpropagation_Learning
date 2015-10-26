package EECEBP;

public class ErrorBackPropagation {
	   
		//Constructor
		public ErrorBackPropagation(int numOfUnitsInEachLayer[],
  				double totalTrainingInput[][],
  				double outputTrainingSets[][],
  				double learnRate,
  				double moment,
  				double minimumError,
  				long maxIteration)
	   			{
		   			numberOfTrainingSets = totalTrainingInput.length;
		   			minError=minimumError;
		   			learningRate = learnRate;
		   			momentum = moment;
		   			numberOfLayers = numOfUnitsInEachLayer.length;
		   			maxIter = maxIteration;
		   			layer = new Layer[numberOfLayers];
		   			

		   			//assign the number of units to the input layer
		   			layer[0] = new Layer(numOfUnitsInEachLayer[0], numOfUnitsInEachLayer[0]);

		   			//assign the number of units to each layer
		   			for(int i =1; i<numberOfLayers; i++)
		   			{
		   				layer[i] = new Layer(numOfUnitsInEachLayer[i],numOfUnitsInEachLayer[i-1]);
		   			}

		   			trainingInput = new double[numberOfTrainingSets][layer[0].unitVec.length];
		   			//Assign training set input to the trainingInput
		   			for(int i=0; i<numberOfTrainingSets; i++)
		   			{
		   				for(int j=0; j<layer[0].unitVec.length; j++)
		   				{
		   					trainingInput[i][j]=totalTrainingInput[i][j];
		   				}
		   			}
		   			
		   			
		   			expectedOutput = new double[numberOfTrainingSets][layer[numberOfLayers-1].unitVec.length];
		   		
		   			//Assign training set output to the expectedOutput
		   			for(int i=0; i<numberOfTrainingSets; i++)
		   			{
		   				for(int j=0; j< layer[numberOfLayers-1].unitVec.length; j++)
		   				{
		   					expectedOutput[i][j]=outputTrainingSets[i][j];
		   				}
		   			}
                   
		   			actualOutput = new double[numberOfTrainingSets][layer[numberOfLayers-1].unitVec.length];
		   			
	   			} 
	
		    // Total error 
			public double	totalError=0;

			//The minimum error defined by user. In this assignment, it's 0.05
			public double	minError;
			
			// User defined learning rate - used for updating the network weights
			private double	learningRate;

			// momentum - used for updating the network weights,  in assignment 1)a), momentum is 0, and the 1)c) momentum is 0.9
			private double	momentum;

			// Number of layers in the network - includes the input, output and hidden layers, in this assignment, 1 input layer, 1-hidden layer and 1 output layer
			private  int	numberOfLayers;
			
			// Public Variables
			public  Layer layer[];

			// The input for each neurons bias weight
			final double bias = 1.0;  

			// Number of training sets
			private  int numberOfTrainingSets;
			
			//training set Input
			private double[][] trainingInput;
			
			//training set Outputs
			private  double[][] expectedOutput;
			
			//Output Initialization
			private double[][] actualOutput;
			private double output[];
			
			boolean stopTraining = false;
			long maxIter;

			// Calculate all the units feedFroward Propagation
			public void feedForwardPropagation()
			{
				//for the layer 0, the input value is equal to output value
				for(int j=0; j<layer[0].unitVec.length; j++)
				{
					layer[0].unitVec[j].output = layer[0].inputVec[j];
				}
				
			   layer[0].outputVec=layer[0].inputVec; 
				
				for(int i=1; i< numberOfLayers; i++)
				{
					layer[i].feedForward();
					
					if(i!=(numberOfLayers-1))
					{
						layer[i+1].inputVec = layer[i].outputVector();
					}
				}
				
			} 
			
			//update the weights 
			public void updateWeights()
			{
				calculateSignalErrors();
				backPropagationError();
			}
			
			
			//calculate signal errors
			private void calculateSignalErrors()
			{
				int i, j, k;
				double sum;
				
				int outputLayer = numberOfLayers - 1;
				
				//calculate output signal error
				for(i=0; i<layer[outputLayer].unitVec.length; i++)
				{
					layer[outputLayer].unitVec[i].signalError=layer[outputLayer].unitVec[i].output*(1-layer[outputLayer].unitVec[i].output)*(expectedOutput[outputLayer][i]-layer[outputLayer].unitVec[i].output);
				}
				
			    //calculate signal error for all units in the hidden layer
				for(i=numberOfLayers-2; i>0; i--)
				{
					for(j=0; j<layer[i].unitVec.length; j++)
					{
						sum = 0;
						
						for(k=0; k<layer[i+1].unitVec.length; k++)    // i=0
						{
							sum = sum + layer[i+1].unitVec[k].weight[j]*layer[i+1].unitVec[k].signalError;
						}
						
						//for hidden layer units, the error signal is the weighted sum of the errors at the units above
						layer[i].unitVec[j].signalError = layer[i].unitVec[j].output*(1-layer[i].unitVec[j].output)*sum;
					}
				}
				
			}
			
			//calculate backPropoagationError
			private void backPropagationError()
			{
				int i, j, k;
				
				//update weights
				for(i = numberOfLayers - 1; i > 0; i--)
				{
					for(j=0; j<layer[i].unitVec.length; j++)
					{
						// Calculate Bias difference to unitVec j
						layer[i].unitVec[j].biasDiff 
							= momentum*layer[i].unitVec[j].biasDiff+learningRate * layer[i].unitVec[j].signalError;
						
						//update bias weight to unitVec j
						layer[i].unitVec[j].bias
						=layer[i].unitVec[j].bias+layer[i].unitVec[j].biasDiff;

						//update weights
						
						for(k=0; k<layer[i].inputVec.length; k++)
						{
							layer[i].unitVec[j].weightDiff[k] = momentum*layer[i].unitVec[j].weightDiff[k]+learningRate*layer[i].unitVec[j].signalError*layer[i-1].unitVec[k].output;
								
							layer[i].unitVec[j].weight[k]=layer[i].unitVec[j].weight[k]+layer[i].unitVec[j].weightDiff[k];
						}
					}
				}
			}
			
			//calculateTotalError
			private void calculateTotalError()
			{
				for(int i=0; i<numberOfTrainingSets; i++)
				{
					for(int j=0; j<layer[numberOfLayers-1].unitVec.length; j++)
					{
						totalError = totalError + 0.5*(Math.pow(expectedOutput[i][j]-actualOutput[i][j], 2));
					}
				}
			}
			
			public void trainNeuralNetwork()
			{
				long iter;
				
				for(iter=0; iter<maxIter&&totalError>minError; iter++)
				{//Assign training set input to the trainingInput
	   			for(int trainingsetNum=0; trainingsetNum<numberOfTrainingSets; trainingsetNum++)
	   			{
	   				for(int i=0; i<layer[0].unitVec.length; i++)
	   				{
	   					layer[0].inputVec[i]=trainingInput[trainingsetNum][i];
	   				}
	   			
	   			    feedForwardPropagation();
	   			
	   			    //Assign calculated output vector from neural network to actualOutput
	   			    for(int i=0; i<layer[numberOfLayers-1].unitVec.length; i++)
	   			    {
	   			    	actualOutput[trainingsetNum][i]=layer[numberOfLayers-1].unitVec[i].output;
	   			    }
	   			    
	   			    updateWeights();
	   			    
	   			}
	  
	   			calculateTotalError();
	   			
	   			iter++;
	   			
				}
				
				System.out.println("When we have reached the minError, the iteration is " + iter);
				
			}
			
			// Run BP
			public void runBP() {
				trainNeuralNetwork();
			} 

			// to notify the network to stop training.
			public void kill() 
			{ 
				stopTraining = true; 
			}
			
			public static void main(String[] args)
			{
				int numOfUnitsInEachLayer[]= {2,4,1};
				
				//XOR training input
				double totalTrainingInput[][] = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
				
				//XOR training output
				double outputTrainingSets[][] ={{0},{1},{1},{0}};
				
				double learningRate= 0.2;
				
				double momentum= 0.9;
				
				double minimumError = 0.05;
				
				long maxIteration=10000;
			 
			    ErrorBackPropagation  BP = new ErrorBackPropagation(numOfUnitsInEachLayer, totalTrainingInput, outputTrainingSets,learningRate, momentum, minimumError, maxIteration);
			    BP.runBP();
			    
			}

}


