package com.earl.nbyncheckers.base;

public class Container<T> {

	T value;

	public Container() {

	}

	public Container(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
