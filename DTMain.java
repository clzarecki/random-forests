import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class DTMain {
	
	public static List<Attribute> attrs = new ArrayList<Attribute>();
	public static Attribute classes;
	private static Integer[] nominalAttrs = {}; //TODO change 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39

	public static void main(String[] args) {
		
		// parse training file
		List<String> files = new ArrayList<String>();
		files.add("041sonar.all-data"); //TODO change
		List<Instance> insts = new ArrayList<Instance>();
		for(String s : files) {
			List<Instance> fileInsts = new ArrayList<Instance>();
			parseFile(s,fileInsts);
			insts.addAll(fileInsts);
		}
		
		for(Instance i : insts) {
			for(String s : i.attributes) {
				System.out.print(s + " ");
			}
			System.out.println(i.label);
//			if(i.attributes.size() != 17) {
//				System.out.println("NO");
//			}
		}

		int numFolds = 10;
		addAttrs(insts);
		List<List<Instance>> folds = crossValFolds(insts, numFolds);
		
		int totalSum = 0;
		int dtSum = 0;
		double rf3Sum = 0;
		double rf5Sum = 0;
		double rf9Sum = 0;
		double rf15Sum = 0;
		double rf19Sum = 0;
		for(int i = 0; i < numFolds; i++) {
			
			// create training and test sets
			List<Instance> testSet = folds.get(i);
			List<Instance> trainSet = new ArrayList<Instance>();
			
			// add other instances to training set
			for(int j = 0; j < numFolds; j++) {
				if(j != i) {
					trainSet.addAll(folds.get(j));
				}
			}
			
			// get tuning set
			double percentageTune = 0.3;
			int numTest = (int) (trainSet.size() * percentageTune);
			List<Instance> tuneSet = new ArrayList<Instance>();
			List<Instance> newTrain = new ArrayList<Instance>();
			newTrain.addAll(trainSet);
			Random rand = new Random();
			for(int j = 0; j < numTest; j++) {
				tuneSet.add(newTrain.remove(rand.nextInt(newTrain.size())));
			}
			
			// train tree
			DecisionTree dc = new DecisionTreeImpl(newTrain, tuneSet, false);
			
			// train random forests
			int trials = 10;
			
			// 3 trees
			int sum3 = 0;
			for(int j = 0; j < trials; j++) {
				RandomForest rf = new RandomForest(trainSet, 3);
				sum3 += rf.numCorrect(testSet);
			}
			
			// 5 trees
			int sum5 = 0;
			for(int j = 0; j < trials; j++) {
				RandomForest rf = new RandomForest(trainSet, 5);
				sum5 += rf.numCorrect(testSet);
			}

			// 9 trees
			int sum9 = 0;
			for(int j = 0; j < trials; j++) {
				RandomForest rf = new RandomForest(trainSet, 9);
				sum9 += rf.numCorrect(testSet);
			}

			// 15 trees
			int sum15 = 0;
			for(int j = 0; j < trials; j++) {
				RandomForest rf = new RandomForest(trainSet, 15);
				sum15 += rf.numCorrect(testSet);
			}
			
			// 19 trees
			int sum19 = 0;
			for(int j = 0; j < trials; j++) {
				RandomForest rf = new RandomForest(trainSet, 19);
				sum19 += rf.numCorrect(testSet);
			}
			
//			System.out.println("Total: " + testSet.size() + "\t\tDT: " + dc.numCorrect(testSet) + "\t\tRF: " + (sum / trials));
			System.out.print(i);
			totalSum += testSet.size();
			dtSum += dc.numCorrect(testSet);
			rf3Sum += (double) sum3 / (double) trials;
			rf5Sum += (double) sum5 / (double) trials;
			rf9Sum += (double) sum9 / (double) trials;
			rf15Sum += (double) sum15 / (double) trials;
			rf19Sum += (double) sum19 / (double) trials;
		}
		System.out.println();
		System.out.println("Total: " + totalSum + "\nDT: " + dtSum + "\n3RF: " + rf3Sum + "\n5RF: " + rf5Sum + "\n9RF: " + rf9Sum + "\n15RF: " + rf15Sum + "\n19RF: " + rf19Sum);
		System.out.println("# Attributes: " + attrs.size());
		System.out.println("# Labels: " + classes.values.size());
	}
	
	private static void parseFile(String file, List<Instance> instances) {
		
		// check file
		File in = new File(file); // file to be read
		Scanner scan; // scanner for file reading
		try {
			scan = new Scanner(in);
		} catch (FileNotFoundException e) {
			System.out.println("Error: Cannot access input file");
			return;
		}
		
		String line = null;
		
		// read instances
		while(scan.hasNext()) {
			line = scan.nextLine();
			List<String> attributes = new ArrayList<String>(Arrays.asList(line.split(",|'| |\t")));
			
			// remove empty strings
			List<String> toRemove = new ArrayList<String>();
			toRemove.add("");
			attributes.removeAll(toRemove);
			
			//TODO choose which index of the file the class label is in
			String label = attributes.get(attributes.size() - 1);
			attributes.remove(attributes.size() - 1);

//			String label = attributes.get(0);
//			attributes.remove(0);
			
//			if(!label.equals("0")) {
//				label = "1";
//			}
			
//			String label = null;
//			if(attributes.get(attributes.size() - 7).equals("1")) {
//				label = "0";
//			} else if(attributes.get(attributes.size() - 6).equals("1")) {
//				label = "1";
//			} else if(attributes.get(attributes.size() - 5).equals("1")) {
//				label = "2";
//			} else if(attributes.get(attributes.size() - 4).equals("1")) {
//				label = "3";
//			} else if(attributes.get(attributes.size() - 3).equals("1")) {
//				label = "4";
//			} else if(attributes.get(attributes.size() - 2).equals("1")) {
//				label = "5";
//			} else if(attributes.get(attributes.size() - 1).equals("1")) {
//				label = "6";
//			} else {
//				System.out.println("Didn't find class");
//			}
//			for(int i = 0; i < 7; i++) {
//				attributes.remove(attributes.size() - 1);
//			}
			
			Instance inst = new Instance(label, attributes);
			instances.add(inst);
		}
		
	}
	
	public static void addAttrs(List<Instance> instances) {
		// add attributes
		Instance temp = instances.get(0);
		for(int i = 0; i < temp.attributes.size(); i++) {
			
			// find all values
			List<String> attrVals = new ArrayList<String>();
			for(Instance inst : instances) {
				String instAttrVal = inst.attributes.get(i);
				if(!attrVals.contains(instAttrVal)) {
					attrVals.add(instAttrVal);
				}
			}
			
			// create attribute
			Attribute newAttr = null;
			if(Arrays.asList(nominalAttrs).contains(i)) {
				// add attribute with categorical values
				newAttr = new Attribute("Attribute " + String.valueOf(i), attrVals);
			} else {
				// add attribute with real values
				newAttr = new Attribute("Attribute " + String.valueOf(i));
			}
			attrs.add(newAttr);
		}
		
		// add classes
		// find all values
		List<String> labelVals = new ArrayList<String>();
		for(Instance inst : instances) {
			if(!labelVals.contains(inst.label)) {
				labelVals.add(inst.label);
			}
		}
		// create attribute
		classes = new Attribute("Class", labelVals);
	}
	
	public static List<List<Instance>> crossValFolds(List<Instance> insts, int n) {
		List<List<Instance>> folds = new ArrayList<List<Instance>>(n);
		
		// put each kind of class label in its own list
		List<List<Instance>> takeFromInstsByClass = new ArrayList<List<Instance>>();
		for(String s : classes.values) {
			List<Instance> toAdd = new ArrayList<Instance>();
			for(Instance i : insts) {
				if(i.label.equals(s)) toAdd.add(i);
			}
			takeFromInstsByClass.add(toAdd);
		}
		
		// create folds
		for(int i = 0; i < n; i++) {
			List<Instance> fold = new ArrayList<Instance>();
			folds.add(fold);
		}
		
		// keep adding instances to folds until there are no more
		// add in order of class for stratification
		int foldIndexToAdd = 0;
		Random rand = new Random();
		for(List<Instance> list : takeFromInstsByClass) {
			while(!list.isEmpty()) {
				Instance i = list.remove(rand.nextInt(list.size()));
				folds.get(foldIndexToAdd % folds.size()).add(i);
				foldIndexToAdd++;
			}
		}
		
		return folds;
	}
	
}
