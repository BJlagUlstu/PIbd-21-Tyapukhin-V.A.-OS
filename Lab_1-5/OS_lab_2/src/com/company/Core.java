package com.company;

import java.util.ArrayList;

public class Core {

	private ArrayList<Process> processes = new ArrayList<Process>();
	private int allocatedTime, numberOfProcesses;

	public void setTime(int time) {
		try {
			if (time > 0)
				allocatedTime = time;
		} catch (Exception ex) {
			System.out.println("Invalid input!");
		}
	}

	public void setNumberOfProcesses(int number) {
		try {
			if (number > 0)
				numberOfProcesses = number;
		} catch (Exception ex) {
			System.out.println("Invalid input!");
		}
	}
	
	public void createProcesses() {
		for (int i = 0; i < numberOfProcesses; i++) {
			processes.add(new Process(i));
			processes.get(i).create_streams();
		}
	}

	public void print() {
		for (int i = 0; i < numberOfProcesses; i++) {
			System.out.println(String.format("\nProcess %d {", processes.get(i).get_pid()));
			for (int j = 0; j < processes.get(i).get_streams().size(); j++) {
				System.out.println(String.format("   Stream %d - time: %d ms", processes.get(i).get_streams().get(j).get_tid(), processes.get(i).get_streams().get(j).get_required_time()));
			}
			System.out.println("}");
		}
		System.out.println();
	}

	public void planner() {
		System.out.println("Results:\n");
		while (!processes.isEmpty()) {
			for (int i = 0; i < numberOfProcesses; i++) {
				int pid = processes.get(i).get_pid();
				System.out.println(String.format("Process %d started running {", pid));
				for (int j = 0; j < processes.get(i).get_streams().size(); j++) {
					int tid = processes.get(i).get_streams().get(j).get_tid();
					int required_time = processes.get(i).get_streams().get(j).get_required_time();
					if (required_time > allocatedTime) {
						int remaining_time = required_time - allocatedTime;
						processes.get(i).get_streams().get(j).change_time(allocatedTime);
						System.out.println(String.format("   Stream %d was break (required time: %d ms - allocated time: %d ms - remaining time: %d ms)", tid, required_time, allocatedTime, remaining_time));
					} else {
						processes.get(i).get_streams().get(j).perform_stream();
						processes.get(i).get_streams().remove(j);
						j--;
					}
				}
				if (processes.get(i).get_streams().isEmpty()) {
					processes.get(i).perform_process();
					processes.remove(i);
					numberOfProcesses--; i--;
				}
				System.out.println("}\n");
			}
		}
	}
}