package com.gutenbook.gutenbook.Regex.Tools;

public class Transition {

	public int from;
	public int to;
	public String value;
	
	public Transition(int from, String value, int to) {
		this.from=from;
		this.value=value;
		this.to=to;
	}
	
	@Override
	public String toString() {
		return from + " -" + value +"-> " + to;
	}
	
}
