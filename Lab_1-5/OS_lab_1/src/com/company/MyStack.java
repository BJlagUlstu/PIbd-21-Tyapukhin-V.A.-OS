package com.company;

import java.util.ArrayList;

public class MyStack {

	ArrayList<Object> stack = new ArrayList<>();

	public void push(Object element) {
		stack.add(element);
	}

	public Object pop() {
		Object element = stack.get(stack.size() - 1);
		stack.remove(stack.size() - 1);
		return element;
	}

	public int getSize() {
		return stack.size();
	}
}