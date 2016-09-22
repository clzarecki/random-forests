import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class RandomForest extends DecisionTree{
	
	public List<DecisionTree> dts = new LinkedList<DecisionTree>();

	/**
	 * RandomForest constructor
	 * @param train the training set
	 * @param numTrees number of trees to use
	 */
	public RandomForest(List<Instance> train, int numTrees) {
		
		// create new bagged test set and decision tree for each
		for(int i = 0; i < numTrees; i++) {
			this.dts.add(new DecisionTreeImpl(baggedSet(train), true));
//			System.out.print(".");
		}
	}
	
	public List<Instance> baggedSet(List<Instance> insts) {
		List<Instance> newList = new ArrayList<Instance>(insts.size());
		Random rand = new Random();
		for(int i = 0; i < insts.size(); i++) {
			int index = rand.nextInt(insts.size());
			newList.add(insts.get(index));
		}
		return newList;
	}

	@Override
	public String[] classify(List<Instance> testSet) {
		
		// get classification array for each decision tree
		List<String[]> classifications = new ArrayList<String[]>();
		for(DecisionTree dt : dts) {
			classifications.add(dt.classify(testSet));
		}
		
		// find majority vote for each instance
		String[] majority = new String[testSet.size()];
		for(int testInstIndex = 0; testInstIndex < majority.length; testInstIndex++) {
			
			// create count for each possible label
			List<LabelValuePair> labelCount = new ArrayList<LabelValuePair>();
			for(String curLabel : DTMain.classes.values) {
				labelCount.add(new LabelValuePair(curLabel, 0));
			}
			
			// cycle through classification for each decision tree and add to count
			for(String[] classification : classifications) {
				String prediction = classification[testInstIndex];
				
				// find correct count and increment
				for(LabelValuePair lvp : labelCount) {
					if(lvp.label.equals(prediction)) {
						lvp.value++;
					}
				}
			}
			
			// find which is majority, earlier class in input file will be
			// chosen if a tie occurs
			String maxVoteLabel = null;
			int maxVoteNum = -1;
			for(LabelValuePair lvp : labelCount) {
				if(lvp.value > maxVoteNum) {
					maxVoteNum = (int) lvp.value;
					maxVoteLabel = lvp.label;
				}
			}
			majority[testInstIndex] = maxVoteLabel;
		}
		
		return majority;
	}

	@Override
	public int numCorrect(List<Instance> toTest) {
		String[] classification = this.classify(toTest);
		int numCorrect = 0;
		for(int i = 0; i < toTest.size(); i++) {
			if(classification[i].equals(toTest.get(i).label)) numCorrect++;
		}
		return numCorrect;
	}

	@Override
	public void print() {
		for(int i = 0; i < dts.size(); i++) {
			System.out.println("Tree #" + i + "--------------------------------------------------------------------------------------------------");
			dts.get(i).print();
		}
	}
	
}
