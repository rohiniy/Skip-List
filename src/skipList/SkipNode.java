package skipList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Rohini Yadav
 *
 *This class is used for creating Node consisting of data element and the array of nodes which
 *will be the links of that element.
 *
 * @param <T> : it is the generic type of the element of which the list is to be created
 */
public class SkipNode <T> {
	private T data;
	private SkipNode<T>[] next;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public SkipNode<T>[] getNext() {
		return next;
	}

	public void setNext(SkipNode<T>[] next) {
		this.next = next;
	}
	
	@SuppressWarnings("unchecked")
	public void setNextIndex(int level , SkipNode<T> node) {
		
		if(this.next.length-1 < level){
			//ArrayList<SkipNode<T>> arrayList = new ArrayList<SkipNode<T>>(Arrays.asList(next));
			//sadsf
			
			//arrayList.add(node);
			
			SkipNode<T> [] rebuildArray = (SkipNode<T> []) Array.newInstance(SkipNode.class,level+1); 
			//rebuildArray = this.next.clone();
			System.arraycopy(this.next, 0, rebuildArray, 0, this.next.length);
			rebuildArray[level] = node;
			this.next = rebuildArray;
		}else{
			this.next[level] = node;
		}
		
	}

	@SuppressWarnings("unchecked")
	public SkipNode(T x, int level){
		data = x;
		next = (SkipNode<T>[]) new SkipNode[level+1];
	}

}
