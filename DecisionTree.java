import java.util.List;

/**
 * This class provides a framework for accessing a decision tree.
 * Do not modify or place code here, instead create an
 * implementation in a file DecisionTreeImpl. 
 * 
 */
abstract class DecisionTree {
	
	/**
	 * Evaluates the learned decision tree on a test set
	 * @return the classification accuracy of the test set
	 */
	public abstract String[] classify(List<Instance> testSet);
	
	public abstract int numCorrect(List<Instance> toTest);
	
	public abstract void print();

}
