package dataStructures;
import java.util.NoSuchElementException;

/**
 * Implementierung einer doppelt-verketteten linearen Liste
 * @author mkohn, nikleer
 *
 */
public class DList<T> {
	private int size;
	private ListElement<T> first;
	private ListElement<T> last;
	
	/**
	 *  Erzeugt leere Liste
	 */
	public DList() {
		first = null;
		last = null;
		size = 0;
	}
	
	/** Gibt Referenz auf das i-te Element zurück*/
	private ListElement<T> entry(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException(
					"index:" + index + ", size:" + size);
		ListElement<T> le = first;
		int i = 0;
		while (i < index) {
			le = le.getNext();
			i++;
		}
		return le;
	}
	
	/** Fuegt ein Element an die Liste an*/
	public void add(T c) {
		if (c == null) 
			throw new NullPointerException();
		if (first == null) {
			addFirst(c);
		}
		else {
			ListElement<T> current = first;
			ListElement<T> predecessor = null;
			ListElement<T> newElement = new ListElement<T> (c, null, null);
			while (current != null) {
				predecessor = current;
				current = current.getNext();
			}
			this.last = newElement;
			predecessor.setNext(newElement);
			newElement.setPrevious(predecessor);
			size++;
		}
	}
	
	/** Fuegt ein Element am Anfang der Liste ein */
	private void addFirst(T c) {
		ListElement<T> einfuegen = new ListElement<T>(c, null, null);
		this.setFirst(einfuegen);
		this.last = einfuegen;
		size++;
	}

	/** Komplette Liste loeschen */
	public void clear() {
		setFirst(null);
		last = null;
		size = 0;
	}

	/** Enthaelt die Liste das Objekt? */
	public boolean contains(T c) {
		return indexOf(c) != -1;
	}

	/** Objekt an der Stelle index zurueckgeben */
	public T get(int index) {
		if (entry(index) == null) {
			return null;
		}
		return entry(index).getData();
	}

	/** Gibt eine Referenz auf das erste Element zurueck */
	public T getFirst() {
		if (size == 0)
			throw new NoSuchElementException();
		return first.getData();
	}
	/** Gibt eine Referenz auf das letzte Element zurueck */
	public T getLast() {
		if (size == 0)
			throw new NoSuchElementException();
		return last.getData();
	}

	/** An Welcher Stelle steht das Objekt? */
	public int indexOf(T o) {
		int index = 0;
		ListElement<T> le = first;
		while (le != null) {
			if (le.getData().equals(o))
				return index;
			le = le.getNext();
			index++;
		}
		return -1;
	}

	/**
	 * Sucht nach dem Object in der Liste
	 * 
	 * @param c
	 *            gesuchtes Element
	 * @return null wenn nicht gefunden | ansonsten Element
	 */
	public T find(T c) {
		ListElement<T> current = first;
		while (current != null) {
			if (c.equals(current.getData())) {
				return current.getData();
			}
			current = current.getNext();
		}
		return null;
	}

	/** allgemeine private Methode zum Entfernen eines Elements */
	private void remove(ListElement<T> le) {
		if (le == null)
			throw new NoSuchElementException("remove(null)");
		// erstes Element ?
		if (le.getPrevious() == null) {
			setFirst(le.getNext());
			// letztes Element?
			if (le.getNext() == null)
				last = null;
			else
				le.getNext().setPrevious(null);
		} else {
			le.getPrevious().setNext(le.getNext());
			// letztes Element?
			if (le.getNext() == null)
				last = le.getPrevious();
			else
				le.getNext().setPrevious(le.getPrevious());
		}
		size--;
	}

	/**  Entfernt ein Element aus der Liste an einer bestimmten Stelle i*/
	public void remove(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("index:" + index + ", size:" + size);
		if (index == size - 1)
			removeLast();
		else if (index == 0)
			removeFirst();
		else {
			// beginne beim erstem Element
			ListElement<T> le = first;
			// gehe bis zum Element index-1
			for (int i = 0; i < index - 1; i++)
				le = le.getNext();
			// Das i-te Element einfach ueberspringen
			le.setNext(le.getNext().getNext());
			le.getNext().setPrevious(le);
			size--;
		}
	}

	/** Entferne Objekt */
	public void remove(T c) {
		if (size == 0 || c == null)
			return;
		ListElement<T> le = first;
		// gehe bis zum gesuchten Element
		while (le != null) {
			if (le.getData().equals(c)) {
				remove(le);
				break;
			}
			le = le.getNext();
		}
	}

	public void removeFirst() {
		if (size != 0)
			remove(getFirst());
	}

	public void removeLast() {
		if (size != 0)
			remove(last);
	}

	/** Wert eines Elementes veraendern */
	public T set(int index, T c) {
		ListElement<T> le = entry(index);
		T alterWert = le.getData();
		le.setData(c);
		return alterWert;
	}

	public int size() {
		return size;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		ListElement<T> le = first;
		while (le != null) {
			sb.append(" <-> ").append(le.getData().toString());
			le = le.getNext();
		}
		return sb.toString();
	}

	public void setFirst(ListElement<T> first) {
		this.first = first;
	}
}
	
