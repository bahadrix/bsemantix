package me.bahadir.bsemantix.ngraph.dtree;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import me.bahadir.bsemantix.ngraph.dtree.DecisionTree.DecisionTreeData;

@XmlRootElement(namespace="me.bahadir.bsemantix.decisiontree")
public class SerialGraph {

	
	private DecisionTreeData decisionTreeData;
	

	@XmlElementWrapper(name="answers")
	@XmlElement(name="answer")
	private List<Answer> answers;
		

}
