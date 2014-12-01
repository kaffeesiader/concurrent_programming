package nlprog;

import java.util.HashMap;

public class SyncMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 7114199161997339424L;

	public synchronized boolean containsKey(Object key) {
		return super.containsKey(key);
	}
	
	public synchronized V get(Object key) {
		return super.get(key);
	}
	
	public synchronized V put(K key, V value) {
		return super.put(key,  value);
	}
	
	public synchronized V remove(Object key) {
		return super.remove(key);
	}
}
