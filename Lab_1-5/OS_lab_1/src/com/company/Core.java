package com.company;

import java.util.*;

public class Core {

	MyStack stack;
	private HashMap<Integer, SystemCall> calls;

	public Core(MyStack stack)
	{
		this.stack = stack;
		calls = new HashMap<>();
		calls.put(10, new SystemCall("int"));
		calls.put(20, new SystemCall("String"));
		calls.put(30, new SystemCall("int", "String"));
		calls.put(40, new SystemCall("String", "int", "String"));
		calls.put(50, new SystemCall("String", "int", "String", "int"));
	}

	public void run(int id) {
		if(!calls.containsKey(id)){
			System.out.println(String.format("System call #%d doesn't exist", id));
			return;
		}

		List<String> args = calls.get(id).getArgs();
		ArrayList<Object> str = new ArrayList<>();
		int lengthArgs = args.size();
		int lengthStr = stack.getSize();

		for(int i = 0; i < lengthStr; i++) {
			str.add(stack.pop());
		}

		if (lengthArgs != str.size()) {
			System.out.println("The number of arguments doesn't match");
			return;
		}

		HashMap<Integer, SystemCall> element = new HashMap<>();
		String variableType = "";

		for (int i = str.size() - 1; i >= 0; i--) {
			if(str.get(i) instanceof Integer) {
				variableType += "int ";
			} else {
				variableType += "String ";
			}
		}

		String variableTypeArray[] = variableType.split(" ");
		element.put(1, new SystemCall(variableTypeArray));

		if(element.get(1).getArgs().equals(calls.get(id).getArgs())) {
			System.out.println(String.format("System call #%d was successfully completed", id));
		} else {
			System.out.println("Arguments don't match");
		}
	}

	public void print() {
		for (Map.Entry<Integer, SystemCall> entry : calls.entrySet()) {
			System.out.println("id: " + entry.getKey() + "   arguments : " + entry.getValue().getArgs());
		}
	}
}