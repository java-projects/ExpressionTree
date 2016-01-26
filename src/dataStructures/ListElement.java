package dataStructures;

public class ListElement<T> {
	private T data;
	private ListElement<T> next;
	private ListElement<T> previous;

	ListElement(T c, ListElement<T> n, ListElement<T> p) {
		data = c;
		next = n;
		previous = p;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ListElement<T> getNext() {
		return next;
	}

	public void setNext(ListElement<T> next) {
		this.next = next;
	}

	public ListElement<T> getPrevious() {
		return previous;
	}

	public void setPrevious(ListElement<T> previous) {
		this.previous = previous;
	}
}
