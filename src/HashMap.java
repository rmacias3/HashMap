import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 *A paramaterized Hash map that supports the following operations
 *@author Ricardo Macias
 *@version 1.0
 */
public class HashMap<K, V> implements HashMapInterface<K, V>, Gradable<K, V> {
    // Do not make any new instance variables.
    private MapEntry<K, V>[] table;
    private int size;

    public HashMap() {
        table = (MapEntry<K, V>[]) new MapEntry[STARTING_SIZE];
    }

    @Override
    public V add(K key, V value) {
        if (size != 0) {
            double checker = (double) size / table.length;
            if (checker >= MAX_LOAD_FACTOR) {
                resizeMap();
            }
        }
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        int index = index(key);
        V oldValue = null;
        if (table[index] != null) {
            oldValue = table[index].getValue();
        }
        if (table[index] == null || table[index].isRemoved()) {
            table[index] = new MapEntry(key, value);
            size++;
        } else {
            if (!table[index].getKey().equals(key)) {
                if (findSlot(key) != 0) {
                    table[findSlot(key)] = new MapEntry(key, value);
                    size++;
                } else {
                    table[findSlot(key)] = new MapEntry(key, value);
                    size++;
                }
            } else {
                table[index] = new MapEntry<K, V>(key, value);
                size++;
            }
        }
        return oldValue;
    }
/**
* searches the array for
* an open slot and returns the index
* of it
* @param - the key that the user is wanting to place
* @return - index of the key, -1 if key wasn't found
*/
    private int findSlot(K key) {
        for (int i = index(key); i < table.length; i++) {
            if (table[i] == null || table[i].isRemoved()) {
                return i;
            }
        }
        for (int j = 0; j < index(key); j++) {
            if (table[j] == null || table[j].isRemoved()) {
                return j;
            }
        }
        return -1;
    }
/**
* helper method that doubles the size of our hashmap
* depending on how full the map is.
*/
    private void resizeMap() {
        int bleh = table.length * 2;
        MapEntry<K, V>[] table2 = table;
        size = 0;
        table = (MapEntry<K, V>[]) new MapEntry[bleh];
        for (int i = 0; i < table2.length; i++) {
            if (table2[i] != null && !table2[i].isRemoved()) {
                add(table2[i].getKey(), table2[i].getValue());
            }
        }
    }
/**
* searches the array for
* a specific key and returns the index
* of it
* @param - the key that the user is wanting to find
* @return - index of the given key, -1 if the key wasn't found
*/
    private int findSlot2(K key) {
        for (int i = index(key); i < table.length; i++) {
            if (table[i] != null && table[i].getKey().equals(key)
                && !table[i].isRemoved()) {
                return i;
            }
        }
        for (int j = 0; j < index(key); j++) {
            if (table[j] != null && table[j].getKey().equals(key)
                && !table[j].isRemoved()) {
                return j;
            }
        }
        return -1;
    }
/**
* the hashing function for the hashmap
* it takes in a hascode from a key and then mods it by
* the table length to get our index.
* @param - key
* @return - index to place the key in
*/
    private int index(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (table[index(key)].getKey().equals(key)) {
            table[index(key)].setRemoved(true);
        } else {
            if (findSlot2(key) != -1) {
                table[findSlot2(key)].setRemoved(true);
            } else {
                return null;
            }
        }
        size--;
        return table[index(key)].getValue();
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (table[index(key)].getKey().equals(key)) {
            return table[index(key)].getValue();
        } else {
            return table[findSlot2(key)].getValue();
        }
    }

    @Override
    public boolean contains(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (table[index(key)].getKey().equals(key)
            && !table[index(key)].isRemoved()) {
            return true;
        } else if (findSlot2(key) != -1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        table = (MapEntry<K, V>[]) new MapEntry[STARTING_SIZE];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MapEntry<K, V>[] toArray() {
        return table;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<K>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                set.add(table[i].getKey());
            }
        }
        size = set.size();
        return set;
    }

    @Override
    public List<V> values() {
        List<V> list = new ArrayList<V>();
        Set<K> set = keySet();
        K[] keys = (K[]) set.toArray();
        for (int i = 0; i < set.size(); i++) {
            list.add(get(keys[i]));
        }
        size = list.size();
        return list;
    }
}
