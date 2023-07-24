import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.lang.reflect.Array;
import java.lang.UnsupportedOperationException;

public class SkipListSet <T extends Comparable<T>> implements SortedSet<T> {

	ArrayList<SkipListSetItem> head = new ArrayList<SkipListSetItem>();
	public int maxHeight; // max height every skip list item can be
	public int elementCount; // tracks number of elements in skip list

	public SkipListSet () {
		int i;
		elementCount = 0;
		maxHeight = 3; // minimum height for skiplists will be 3
		
		// Initialize the head array
		for (i = 0; i < maxHeight; i++) 
			head.add(null);
		
	}
	
	public SkipListSet (Collection<? extends T> c) {
		// Initializes the variables to an empty skiplist, then add each collection val
		int i;
		elementCount = 0;
		maxHeight = 3;
		
		// Initialize the head array
		for (i = 0; i < maxHeight; i++) 
			head.add(null);

		// Add all the vals from the collection into our skiplist
		addAll(c);
		
	}
	
	// Additional print function, just prints all the vals in the skiplist line by line
	public void print() {
		Iterator<T> it = iterator();
		
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	// Returns the current size of the skiplist (number of items)
	@Override
	public int size() {
		return elementCount;
	}

	@Override
	public boolean isEmpty() {
		return (elementCount == 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {

		int i = maxHeight - 1;
		ArrayList<SkipListSetItem> temp = new ArrayList<SkipListSetItem>(); // temp pointer
		
		temp = head;
		
		// Traverse each level till you finished traversing the bottom level
		while (i >= 0) { // i will represent the height we are at in the skiplist
			
			// If the element to the right is null, traverse down
			if (temp.get(i) == null) {
				i--;
				continue;
			}
			
			// If the element to the right is the val we are looking for, return true
			if (temp.get(i).val.compareTo((T)o) == 0) {
				return true;
			} 
			
			// If the element to the right is greater than the passed val, traverse down from the current element position
			else if (temp.get(i).val.compareTo((T)o) > 0 || temp.get(i).val == null) {
				i--;
				continue;
			} 
			
			// If the element to the right is less than the passed val, keep traversing right
			else if (temp.get(i).val.compareTo((T)o) < 0){
				temp = temp.get(i).arr;
			}
		}
		
		// If you haven't found it in the skiplist return false
		return false;
	}

	@Override
	public Iterator<T> iterator() {

		SkipListSetIterator<T> listIterator = new SkipListSetIterator<T>();
		
		return listIterator;
	}

	@Override
	public Object[] toArray() {
		Object arr[] = new Object[elementCount];
		Iterator<T> iterator = iterator();

		int idx = 0;
		while (iterator.hasNext()) {
			arr[idx] = iterator.next();
			idx++;
		}
		return arr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E[] toArray(E[] arr) {
		
		Iterator<T> iterator = iterator();

		if (arr.length < elementCount) { 
			  arr = (E[]) Array.newInstance(arr.getClass().getComponentType(), elementCount);
		} else if (arr.length > elementCount) {
			  arr[elementCount] = null;
		}
		int idx = 0;		
		while (iterator.hasNext()) {
			arr[idx] = (E) iterator.next();
			idx++;
		}
		return arr;
	}

	
	@Override
	public boolean add(T e) {
		
		if (this.contains(e))
			return false;
		
		int idx = maxHeight - 1;
		ArrayList<SkipListSetItem> temp = new ArrayList<SkipListSetItem>(); // temp pointer
		
		SkipListSetItem item = new SkipListSetItem(e);
		
		temp = head;
		
		while (idx >= 0) { // i will represent the height we are at in the skiplist
			
			// If the element to the right is null, check to see if we can add at that height
			if (temp.get(idx) == null) {
				if (item.height - 1 >= idx) {
					item.arr.set(idx, temp.get(idx));
					temp.set(idx, item);
				}
				
				idx--;
				continue;
			}
			
			// If the element to the right is greater than the val passed, check to see if we can add at that height
			else if (temp.get(idx).val.compareTo(e) > 0 || temp.get(idx).val == null) {
				if (item.height - 1 >= idx) {
					item.arr.set(idx, temp.get(idx));
					temp.set(idx, item);
					idx--;
					continue;
					
				} else {
					idx--;
					continue;
				}
			} 
			
			// If the element to the right is less than the val passed, continue traversing to the right
			else if (temp.get(idx).val.compareTo(e) < 0){
				temp = temp.get(idx).arr;
			}
		}
		
		// Increment the element_count since we've successfully added
		elementCount++;
		
		// If the element_count reaches a power of 2 for max height, increment the max height and add to the head arraylist
		if (elementCount >= (Math.pow(2, maxHeight))) {
			maxHeight++;
			head.add(null);
		}
		
		return true;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		
		int idx = maxHeight - 1;
		int remove = 0; // tracker for checking if we actually change the skiplist
		ArrayList<SkipListSetItem> temp = new ArrayList<SkipListSetItem>(); // temp pointer
		
		temp = head;
		
		while (idx >= 0) { // i will represent the height we are at in the skiplist
			
			// If the element to the right is null, traverse down
			if (temp.get(idx) == null) {
				idx--;
				continue;
			}
			
			// If the element to the right is the val we are looking to remove, remove it and re-link the nodes to it's left to point to the right of the element we are removing
			if (temp.get(idx).val.compareTo((T)o) == 0) {
				remove = 1;
				temp.set(idx, temp.get(idx).arr.get(idx));
				idx--;
				continue;
			} 
			
			// If the element to the right is greater than the val we are looking for, traverse down
			else if (temp.get(idx).val.compareTo((T)o) > 0 || temp.get(idx).val == null) {
				idx--;
				continue;
			} 
			
			// If the element to the right is less than the val we are looking for, keep traversing right
			else if (temp.get(idx).val.compareTo((T)o) < 0){
				temp = temp.get(idx).arr;
			}
		}
		
		// If we've ever changed the skiplist (successfully removed something), decrement element_count and return true
		if (remove == 1) {
			elementCount--;
			
			// If the element_count is a power of 2 of the max_height - 1, that means we need to decrement max height and remove the last index of the head arraylist
			if (elementCount <= Math.pow(2, maxHeight - 1) && maxHeight != 3) {
				maxHeight--;
				head.remove(maxHeight);
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		
		for (Object o : c) {
			if(!contains(o))
				return false;
		} 
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean track = false;
		
		for (T e : c) {
			if(add(e)) {
				track = true;
			}
		}
		return track;
	}

	@Override
	public boolean retainAll(Collection<?> c) {

		boolean track = false;
		Iterator<T> iterator = iterator();
		T value;
		
		while (iterator.hasNext()) {
			value = iterator.next();
			if (!c.contains(value)) {
				remove(value);
				track = true;
			}
		}
		
		return track;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
	boolean track = false;
		
		for (Object o : c) {
			if(remove(o)) {
				track = true;
			}
		}
		return track;
	}

	@Override
	public void clear() {
		int i;
		while (maxHeight - 1 >= 0) {
			head.remove(maxHeight - 1);
			maxHeight--;
		}
		
		// Reset the initial vals of the skiplist
		maxHeight = 3;
		elementCount = 0;
		
		for (i = 0; i < maxHeight; i++) 
			head.add(null);
	}
	
	@Override
	public boolean equals(Object o) {
		return this.hashCode() == o.hashCode() ? true : false;
	}
	
	@Override
	public int hashCode() {
		Iterator<T> iterator = iterator();
		int sum = 0;
		
		while (iterator.hasNext()) {
			sum += iterator.next().hashCode();
		}
		return sum;
	}

	@Override
	public Comparator<? super T> comparator() {
		return null;
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		try {
	        throw new UnsupportedOperationException("Invalid operation for sorted list.");
	    } catch (java.lang.UnsupportedOperationException e) {
	        System.out.println("Invalid operation for sorted list.");
	    }
		return null;
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		try {
	        throw new UnsupportedOperationException("Invalid operation for sorted list.");
	    } catch (java.lang.UnsupportedOperationException e) {
	        System.out.println("Invalid operation for sorted list.");
	    }
		return null;
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		try {
	        throw new UnsupportedOperationException("Invalid operation for sorted list.");
	    } catch (java.lang.UnsupportedOperationException e) {
	        System.out.println("Invalid operation for sorted list.");
	    }
		return null;
	}

	@Override
	public T first() {
		
		return head.get(0) == null ? null : head.get(0).val;
	}

	@Override
	public T last() {
		int i = maxHeight - 1;
		T val = null;
		ArrayList<SkipListSetItem> temp = new ArrayList<SkipListSetItem>(); // temp pointer
		temp = head;
		
		while (i >= 0) { // i will represent the height we are at in the skiplist
			
			if (temp.get(i) == null) {
				i--;
				continue;
			}
			else {
				val = temp.get(i).val;
				temp = temp.get(i).arr;
			}
		}
		return val;
	}
	
	// Generates a height for a new item/node, 50% chance to be 1, 25% chance to be 2, etc.
	public int generate_height () {
		int height = 1;
		Random rand = new Random();
		boolean tf = rand.nextBoolean();
		
		while (tf == true && height < maxHeight) {
			height++;
			tf = rand.nextBoolean();
		}
		return height;
	}
	
	public void reBalance() {

		Iterator<T> it = iterator();
		T val;
		
		while (it.hasNext()) {
			val = it.next();
			this.remove(val);
			this.add(val);
		}
	}
	
	private class SkipListSetIterator<E extends Comparable<T>> implements Iterator<T> {

		SkipListSetItem temp;
		T val;
		
		public SkipListSetIterator () {
			temp = head.get(0);
		}
		
		@Override
		public boolean hasNext() {
			if (temp == null)
				return false;
			
			return true;
		}

		@Override
		public T next() {
			
			val = temp.val;
			temp = temp.arr.get(0);
			return val;
		}
		
		@Override
		public void remove() {
			if (val == null)
				return;
			else {
				SkipListSet.this.remove(val);
				val = null;
			}
		}
	}
	
	class SkipListSetItem {
		
		ArrayList<SkipListSetItem> arr = new ArrayList<SkipListSetItem>();
		
		private T val;
		private int height;
		
		public SkipListSetItem (T e) {
			int i;
			val = e;
			height = generate_height();
			
			for (i = 0; i < height; i++) 
				arr.add(null);
			
		}
	}

}