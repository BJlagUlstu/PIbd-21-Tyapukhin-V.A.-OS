package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Process {

	private int pid, all_time = 0;

	private ArrayList<Stream> streams = new ArrayList<Stream>();

	private Random rand = new Random();

	public Process(int pid) {
		this.pid = pid;
	}

	public int get_pid() {
		return pid;
	}

	public ArrayList<Stream> get_streams(){
		return streams;
	}

	public void create_streams() {
		for (int i = 0; i < rand.nextInt(10) + 1; i++) {
			int time = rand.nextInt(10) + 1;
			streams.add(new Stream(i, time));
			all_time += time;
		}
	}

	public void perform_process() {
		System.out.println(String.format("   Process " + pid + " was successful in %d ms", all_time));
	}
}