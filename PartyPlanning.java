package practice.rahul;

import java.util.ArrayList;
import java.util.List;

class TreeNode {
	int value;
	List<TreeNode> children = new ArrayList<>();

	public TreeNode(int value) {
		this.value = value;
	}

	public void addChild(TreeNode child) {
		children.add(child);
	}
}

class NodeResponse {
	final int maxSumConsideringNode;
	final int maxSumNotConsideringNode;
	final List<Integer> nodeListConsideringNode;
	final List<Integer> nodeListNotConsideringNode;

	public NodeResponse(int maxSumConsideringNode, int maxSumNotConsideringNode, List<Integer> nodeListConsideringNode,
			List<Integer> nodeListNotConsideringNode) {
		this.maxSumConsideringNode = maxSumConsideringNode;
		this.maxSumNotConsideringNode = maxSumNotConsideringNode;
		this.nodeListConsideringNode = nodeListConsideringNode;
		this.nodeListNotConsideringNode = nodeListNotConsideringNode;
	}
}

public class PartyPlanning {

	// this is the response class sent from each node.
	// parent node will process the response from each child node and choose the one
	// that maximizes the value in both cases

	public NodeResponse getMaximum(TreeNode root) {

		// base case
		if (root == null) {
			return new NodeResponse(0, 0, null, null);
		}

		// This is where you store the responses from your children.
		NodeResponse[] childrenResponse = new NodeResponse[root.children.size()];

		// get the sums from all your children.
		for (int i = 0; i < root.children.size(); i++) {
			childrenResponse[i] = getMaximum(root.children.get(i));
		}

		// after retrieving values from your children, you have 2 options.
		// 1. either consider yourself in the final answer. that means you cannot
		// consider
		// the best sum returned from any of your children containing those children
		// themselves.
		//
		// 2. don't consider yourself in the final answer. that means you consider
		// the best sum returned by your child which may or may not contain that child
		// itself.

		int maxSumConsideringNode = root.value;
		int maxSumNotConsideringNode = 0;
		List<Integer> nodeListConsideringNode = new ArrayList<>();
		nodeListConsideringNode.add(root.value);
		
		List<Integer> nodeListNotConsideringNode = new ArrayList<>();

		for (int i = 0; i < root.children.size(); i++) {
			maxSumConsideringNode += childrenResponse[i].maxSumNotConsideringNode;
			nodeListConsideringNode.addAll(childrenResponse[i].nodeListNotConsideringNode);

			// this situation could be different for each child.
			// for one child, we may get the best sum by including it, whereas for other
			// child, we may get the best sum by ignoring it.
			// select the best out of the two, and append the child value list.
			if (childrenResponse[i].maxSumConsideringNode < childrenResponse[i].maxSumNotConsideringNode) {
				maxSumNotConsideringNode += childrenResponse[i].maxSumNotConsideringNode;
				nodeListNotConsideringNode.addAll(childrenResponse[i].nodeListNotConsideringNode);
			} else {
				maxSumNotConsideringNode += childrenResponse[i].maxSumConsideringNode;
				nodeListNotConsideringNode.addAll(childrenResponse[i].nodeListConsideringNode);
			}
		}

		return new NodeResponse(maxSumConsideringNode, maxSumNotConsideringNode, nodeListConsideringNode,
				nodeListNotConsideringNode);
	}
	
	private void printBestResponse(NodeResponse response) {
		if(response.maxSumConsideringNode > response.maxSumNotConsideringNode) {
			System.out.println("Maximum fun :" +response.maxSumConsideringNode);
			System.out.println("Nodes contributing to max fun are :" + response.nodeListConsideringNode);
		}
		else {
			System.out.println("Maximum fun :" +response.maxSumNotConsideringNode);
			System.out.println("Nodes contributing to max fun are :" + response.nodeListNotConsideringNode);
		}
	}

	public static void main(String[] args) {
		PartyPlanning problem = new PartyPlanning();
		TreeNode root = new TreeNode(4);
		TreeNode child1 = new TreeNode(20); 
		TreeNode child2= new TreeNode (15);
//		TreeNode child3= new TreeNode (12);
		root.addChild(child1);
		root.addChild(child2);
//		root.addChild(child3);
		
		child1.addChild(new TreeNode(15));
		child1.addChild(new TreeNode(5));
		child2.addChild(new TreeNode(2));
		child2.addChild(new TreeNode(10));
		
//				4
//			20		15	  12
//		15		5 2    10
		
		NodeResponse response = problem.getMaximum(root);
		problem.printBestResponse(response);
	}
}
