package com.blstream.urbangame.test;

import junit.framework.TestCase;

import com.blstream.urbangame.database.entity.Answer;

public class AnswerTest extends TestCase {
	
	private final String ANSWER_1 = "frist answer";
	private final String ANSWER_2 = "second_answer";
	
	private Answer answer;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		answer = new Answer(ANSWER_1);
	}
	
	public void testSetAnswer() {
		answer.setAnswer(ANSWER_2);
		assertEquals(ANSWER_2, answer.getAnswer());
	}
	
	public void testGetAnswer() {
		assertEquals(ANSWER_1, answer.getAnswer());
	}
	
	public void testSetIsTrue() {
		answer.setIsTrue(true);
		assertTrue(answer.isTrue());
	}
	
	public void testIsTrue() {
		assertFalse(answer.isTrue());
	}
	
}
