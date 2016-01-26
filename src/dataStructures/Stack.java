package dataStructures;

public class Stack<T> {
	int element;
	int size;
	T[] array;

	@SuppressWarnings("unchecked")
	public Stack(int size) {
		if (size > 0) {
			this.size = size;
			this.element = 0;
			array = (T[]) new Object[size];
		} else {
			throw new IllegalArgumentException("Size <= 0!");
		}
	}

	public void push(T o) throws ArrayFullException {
		if (element < size) {
			array[element] = o;
			element++;
		} else {
			throw new ArrayFullException("Array voll.");
		}

	}

	public boolean isEmpty() {
		if (element == 0) {
			return true;
		} else {
			return false;
		}
	}

	public T pop() throws ArrayEmptyException {
		if (element > 0) {
			element--;
			return array[element];
		} else {
			throw new ArrayEmptyException("Array leer.");
		}
	}

	public String toString() {
		String ausgabe = "Stackinhalt:\n";
		for (int i = 0; i < element; i++) {
			ausgabe += array[i];
			ausgabe += "\n";
		}
		return ausgabe;
	}
}



