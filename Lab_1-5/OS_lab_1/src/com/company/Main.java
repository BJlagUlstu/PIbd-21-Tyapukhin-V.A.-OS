package com.company;

public class Main {

	public static void main(String args[]) {

		MyStack stack = new MyStack();
		Core core = new Core(stack);
		core.print();

		stack.push("aaa");
		stack.push(666);
		stack.push("aaa");
		core.run(40);

		stack.push("Z");
		stack.push(0);
		core.run(10);

		stack.push("X");
		stack.push(0);
		stack.push("X");
		stack.push(0);
		core.run(50);

		stack.push("Zzz");
		core.run(20);

		stack.push(101);
		core.run(20);
	}
}