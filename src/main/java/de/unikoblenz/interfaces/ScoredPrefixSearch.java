package de.unikoblenz.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author rpickhardt Hartmann
 * @version 0.1
 */

public interface ScoredPrefixSearch<T> {
	class Tuple<T> {
		public String word;
		public T score;
	};

	/**
	 * serves as insert and update. If the key already exists the new score will
	 * be stored
	 * 
	 * @param key
	 * @param score
	 */
	public void insert(String key, T score);

	public List<Tuple<T>> retrieveTop(String prefix, Integer k);

	public void bulkImport(Map<String, T> map);

	public T get(String key);

	public void remove(String key);

	void printMe();
}
