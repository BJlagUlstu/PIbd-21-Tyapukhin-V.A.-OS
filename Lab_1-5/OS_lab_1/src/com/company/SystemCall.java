package com.company;

import java.util.Arrays;
import java.util.List;

public class SystemCall {

	private List<String> Args;

	SystemCall(String... args) {
		Args = Arrays.asList(args);
	}

	public List<String> getArgs() {
		return Args;
	}
}