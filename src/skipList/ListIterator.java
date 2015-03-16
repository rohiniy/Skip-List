package skipList;

import java.util.Iterator;

/**
 * @author Rohini Yadav
 *
 * @param <T> - Generic type of the elements in the list
 * This class is used to iterate over the skiplist
 */
public class ListIterator <T extends Comparable <? super T>> implements Iterator<T> {

	private SkipNode<T> skipListNode;
	
	public ListIterator(SkipNode<T> node) {
		this.skipListNode = node;
	}
	
	/*
	 * 
	 *Function to check if there is next element
	 *@return : true if there is next element, else false
	 */
	@Override
	public boolean hasNext() {
		if(skipListNode.getNext()[0]!=null){
			return true;
		}else{
			return false;
		}
	}
	/*
	 * Function returning the next element
	 * 
	 */
	@Override
	public T next() {
		skipListNode = skipListNode.getNext()[0];
		return skipListNode.getData();
	}
	
	@Override
	public void remove() {
		
	}

}
