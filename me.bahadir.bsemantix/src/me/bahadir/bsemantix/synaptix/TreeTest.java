package me.bahadir.bsemantix.synaptix;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class TreeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Question q = new Question("Whatta fuck?");
			Answer a = new Answer("No fuck!");
			a.setTarget(Leaf.PASS);
			q.addAnswer(a);
		
			Answer a1 = new Answer("Lotta fuck");
				Question q1 = new Question("how lotta?");
					Answer a11 = new Answer("2 times a week or more");
					a11.setTarget(Leaf.NOT_PASS);
					q1.addAnswer(a11);
					
					Answer a12 = new Answer("whole lotta");
					a12.setTarget(Leaf.PASS);
					q1.addAnswer(a12);
				a1.setTarget(q1);
			q.addAnswer(a1);
			
		ask(q);
		
		
	}
	
	public void ask(Question q) {

		System.out.println("Question: " + q.getText());
		int i = 0;
		for(Answer a : q.getAnswers()) { i++;
			System.out.println(String.format(
					"%d. %s",
					i, a.getText()
					));
		}

		Scanner scanIn = new Scanner(System.in);
		String choice = scanIn.nextLine();
		scanIn.close();
		
		int choiceIndex = Integer.parseInt(choice);
		
		Answer selAnswer = q.getAnswers().get(choiceIndex - 1);
		
		if(selAnswer.getTarget() instanceof Leaf) {
			Leaf leaf = (Leaf) selAnswer.getTarget();
			System.out.print("Clearance: ");
			System.out.println(leaf.getClearance());
		} else if(selAnswer.getTarget() instanceof Question) {
			ask((Question)selAnswer.getTarget());
		}
		

		
		
	}

}
