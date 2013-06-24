package com.blstream.urbangame.datastructures.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.blstream.urbangame.datastructures.ExpandableListHeader;

public class ExpandableListHeaderTest extends AndroidTestCase {
	
	private final String TITLE = "Header number one";
	private final ArrayList<String> CHILDREN;
	
	private ExpandableListHeader<String> expandableListHeader;
	
	public ExpandableListHeaderTest() {
		CHILDREN = new ArrayList<String>();
		CHILDREN.add("A");
		CHILDREN.add("B");
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		expandableListHeader = new ExpandableListHeader<String>();
	}
	
	public void testGetTitle() {
		assertNull(expandableListHeader.getTitle());
		expandableListHeader.setTitle(TITLE);
		assertEquals(TITLE, expandableListHeader.getTitle());
	}
	
	public void testSetTitle() {
		expandableListHeader.setTitle(TITLE);
		assertEquals(TITLE, expandableListHeader.getTitle());
	}
	
	public void testGetArrayChildren() {
		assertNull(expandableListHeader.getArrayChildren());
		expandableListHeader.setArrayChildren(CHILDREN);
		assertEquals(CHILDREN, expandableListHeader.getArrayChildren());
	}
	
	public void testSetArrayChildren() {
		expandableListHeader.setArrayChildren(CHILDREN);
		assertEquals(CHILDREN, expandableListHeader.getArrayChildren());
	}
	
}
