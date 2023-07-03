import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.lang.reflect.Array;
import java.lang.UnsupportedOperationException;

public class SkipListSet <T extends Comparable<T>> implements SortedSet<T> {
    
	ArrayList<SkipListSetItem> arr = new ArrayList<SkipListSetItem>();
	public int heightMax;
	public int elementCount;
    
	public SkipListSet () {
		int i;
		elementCount = 0;
		heightMax = 3;
		
		for (i = 0; i < heightMax; i++) 
			arr.add(null);
		
	}
    private class SkipListSetItem {
		
		ArrayList<SkipListSetItem> array = new ArrayList<SkipListSetItem>();
		
		private T val;
		private int height;
		
		public SkipListSetItem (T e) {
			val = e;
			height = getHeight();
			
			for (int i = 0; i < height; i++) 
				array.add(null);
			
		}
	}
	public int getHeight () {
		int height = 1;
		Random rand = new Random();
		boolean bool = rand.nextBoolean();
		
		while (bool == true && height < heightMax) {
			height++;
			bool = rand.nextBoolean();
		}
		return height;
	}
	private class SkipListSetIterator<T extends Comparable<T>> implements Iterator<T>{

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
        }

        @Override
        public T next() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'next'");
        }

	}

    @Override
    public int size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'size'");
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEmpty'");
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toArray'");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toArray'");
    }

    @Override
    public boolean add(T e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'containsAll'");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAll'");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retainAll'");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAll'");
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clear'");
    }

    @Override
    public Comparator<? super T> comparator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'comparator'");
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subSet'");
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'headSet'");
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tailSet'");
    }

    @Override
    public T first() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'first'");
    }

    @Override
    public T last() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'last'");
    }

}