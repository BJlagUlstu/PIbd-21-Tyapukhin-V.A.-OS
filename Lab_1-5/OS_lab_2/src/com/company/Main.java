package com.company;

public class Main {

	public static void main(String args[]) {

		Core core = new Core();
		
		core.setTime(7);
		core.setNumberOfProcesses(5);
		core.createProcesses();
		core.print();
		core.planner();
	}
}
