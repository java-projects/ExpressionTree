import dataStructures.DList;

@SuppressWarnings({ "rawtypes" })
public class HashTable<T extends Comparable> implements IndexStructure<T> {
	
	private int size;
	private DList<T>[] hashtable;
	public static final String MSG_NEGATIVE_SIZE = 
			"Hashtabellengroesse kleiner oder gleich null.";
	
	@SuppressWarnings("unchecked")
	public HashTable(int size) throws HashTableSizeException {
		if (size <= 0) 
			throw new HashTableSizeException(MSG_NEGATIVE_SIZE);
		this.size = size;
		this.hashtable = (DList<T>[]) new DList[size];
	}
	
	private int getHashValue(Comparable c) {
		return Math.abs(c.hashCode()) % size;
	}
	
	public void add(T c) {
		int hashValue = getHashValue(c);
		if (hashtable[hashValue] == null) {
			hashtable[hashValue] = new DList<T>();
		}
		try {
			hashtable[hashValue].add(c);
		} catch (Exception e) {
		}
	}
	
	public void remove(T c) {
		int hashValue = getHashValue(c);
		if (hashtable[hashValue] != null) {
			hashtable[hashValue].remove(c);
		}
	}
	
	public T find(T c) {
		int hashValue = getHashValue(c);
		if(hashtable[hashValue] != null)
			return hashtable[hashValue].find(c);
		return null;
	}
	
	public int getSize() {
		return size;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			DList list = hashtable[i];
			if (list != null) {
				builder.append(i + ":\t" + list + "\n");
			}
		}
		return builder.toString();
	}
}
