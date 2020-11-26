package com.company;

public class Stream {
	
    private int tid, required_time;

    public Stream (int tid, int required_time) {
        this.tid = tid;
        this.required_time = required_time;
    }

    public int get_tid() {
        return tid;
    }

    public int get_required_time() {
        return required_time;
    }

    public void change_time(int time){
    	required_time -= time;
    }

    public void perform_stream() {
    	System.out.println(String.format("   Stream "+ tid + " was successful in %d ms", required_time));
    }
}
