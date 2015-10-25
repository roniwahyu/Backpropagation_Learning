package EECEBP;

public class Main {

	public static void main()
	{
		int numberOfUnits[]= {2,3,3,3,3,1};
		double totalTrainingInput[][] = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		double outputTrainingSets[][] ={{0},{1},{1},{1}};
		double learningRate=0.2;
		double momentum=0.9;
		double minimumError = 0.05;
		long maxIterations = 10000;
	 
	    ErrorBackPropagation BP;
	    BP(numberOfUnits, totalTrainingInput, outputTrainingSets,learningRate, momentum, minimumError, maxIterations);
	    
	}
}