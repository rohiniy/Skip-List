package skipList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import exception.ElementAllreadyExistsException;

/**
 * @author Rohini Yadav
 *This class implements the functions provided by the SkipList interface.
 *Functions include add, remove, contains, first, last, floor, ceiling, randomLevel and rebuild of skip list.
 * 
 */

public class SkipListImpl<T extends Comparable<? super T>> implements SkipList<T> {

	private int MAXLEVEL = 2;	//Default maximum level of the node
	private int size = 0;
	private SkipNode<T> header;

	public SkipListImpl() {
		size = 0;
		header = new SkipNode<T>(null,MAXLEVEL);	//Creating header of the list with 											
													//null data and MAXLEVEL	
	}
	
	// --------------------Override methods----------------------------------
	
	/*
	 * Random Level Function
	 */
	public int randomLevel() {
		int l=0;
		Random rand = new Random();
		while(l<MAXLEVEL){
			int b = rand.nextInt(2);	//This will give either 0 or 1

			if(b==0){
				return l;			//return the level as random number generated = 0
			}else{
				l++;				//increment level as random number generated = 1
			}
		}
		return l;
	} 
	
	/*
	 * Function to print the skip List 
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		SkipNode<T> x = header.getNext()[0];	
		while (x != null) {
			sb.append(x.getData());		//Traversing the 1st level of the list(0) where all are sequential
			x = x.getNext()[0];
			if (x != null)
				sb.append(",");
		}   
		sb.append("}");
		return sb.toString();
	}


	// --------------------Override methods----------------------------------
	/*
	 * Method to find the data x in the list
	 * param x : The element that needs to be searched in the list
	 * return FindResult object: return the node if the data x is found at node or else return null as node and 
	 * 	array of the visited nodes to find the node with data x. 
	 */
	@SuppressWarnings("unchecked")
	public FindResult<T> find(T x){
		SkipNode<T> p = header;
		SkipNode<T>[] visited = (SkipNode<T>[]) new SkipNode[MAXLEVEL+1];

		for(int i=MAXLEVEL;i>=0;i--){
			while((p.getNext()!=null && p.getNext()[0]!=null) && (p.getNext().length > i) && p.getNext()[i]!=null 
					&& x.compareTo(p.getNext()[i].getData())>0){		//Check if x is bigger than node's data
				p = p.getNext()[i];													
			}
			visited[i]=p;			//Save the visited nodes in the array for using in add and remove function 
		}
		if(p.getNext()[0]!= null && x.equals(p.getNext()[0].getData())){	//The element is found
			return new FindResult<T>(p.getNext()[0],visited);	//Return the node where x is found and array of visited nodes 
																	
		}else{					//Element not found
			return new FindResult<T>(null,visited);	//Return null node and visited array(used in adding the element)
		}
	}

	/*
	 * Function to add an element in the skip list
	 * Adds an element in the sorted order if it is not present in the list, 
	 * 	otherwise raises an exception of element all ready present
	 */
	@Override
	public void add(T x) {
		FindResult<T> prev;
		SkipNode<T> n;

		prev = find(x);

		if(prev.getNode()!=null){
			try {
				throw new ElementAllreadyExistsException("Element Allready Exists!!");	//Raise exception if element is all ready present in the list
			} catch (ElementAllreadyExistsException e) {
				System.out.println(e);
			}
		}else{
			if(MAXLEVEL < (int)Math.ceil((Math.log(size+1) / Math.log(2)))){	//Check if the size of list has become more than 2^MAXLEVEL
				rebuild();						//If the size is more than 2^MAXLEVEL, then call rebuild for new MAXLEVEL and build perfect list
			}
			prev = find(x);
			
			int l = randomLevel();			//Get random level for the new node
			n = new SkipNode<T>(x, l);		//Create a node with data x and level as generated randomly

			for(int i=0;i<=l;i++){
				if(prev.getVisited()[i].getNext().length  > i){
					n.getNext()[i] = prev.getVisited()[i].getNext()[i];	//Set the pointers of the previous node in the newly created node
					prev.getVisited()[i].getNext()[i] = n;				//Set the node in the previous node's next pointer
				}
			}
			size = size+1;	//increment the size of the list
		}
	}

	/* 
	 * Function ceiling to find least element that is >= x, or null if no such element
	 * @param x : The node x to find the element with value >= x
	 * @return : Node that is >= x or null if no such element is present
	 */
	@Override
	public T ceiling(T x) {
		if(isEmpty()){	//If list is empty then return null
			return null;
		}else{	
			FindResult<T> result = find(x);	//Find the element in the node
			if(result.getNode()!=null ){	//Element is present in the list, then return the same element 
				return result.getNode().getData();
			}	//If x is not in the list get the next element from the visited nodes array returned from Find
			else if(result.getVisited()[0].getNext()[0]!=null){
				return result.getVisited()[0].getNext()[0].getData();
			}
			return null;	//return null if the element x is less than the first element in the list
		}
	}

	/*
	 * Fucntion to check if  an element is in the list
	 * @param x: Element to check of present in the list
	 * @return : true if the element is found in the list, otherwise return false
	 */
	@Override
	public boolean contains(T x) {

		if(isEmpty()){	//If list is empty then return null
			return false;
		}else{
			FindResult<T> prev;	//Find the element
			prev = find(x);

			if(prev.getNode()!=null){	//Check if element is found, return true else false
				return true;
			}else{
				return false;
			}
		}
	}

	/*
	 * Function to find index of the element using iterator
	 * @param n: index to find the element on that index
	 * @return T object: return the node found at that index or else null if list is empty or index > size of list
	 */
	@Override
	public T findIndex(int n) {
		if(isEmpty()){	//If list is empty then return null
			return null;
		}else{
			if(size()>=n){
				Iterator<T> it =  iterator();	
				T x = null ;	
				for(int i=0;i<=n;i++){
					if(it.hasNext()){	//Iterate over the list till there are elelments
						x = it.next();
					}
				}
				return x;
		
			}else{
				return null;
			}
		}
	}

	/*
	 * Function to find the first element of the list
	 * @return : the first node of the list
	 */
	@Override
	public T first() {
		if(isEmpty()){	//If list is empty then return null
			return null;
		}else{	
			SkipNode<T> p = header;
			if(p.getNext()!=null){	
				return p.getNext()[0].getData();	//Return the first element pointed by the header's level 0 
			}
		}
		return null;
	}
	
	/*
	 * Function to get the greatest element that is <= x, or null if no such element exists in the list
	 * @param x: Node whose greatest element that is <= x is to be returned
	 * @return : node that is greatest element and <=x
	 */
	@Override
	public T floor(T x) {
		if(isEmpty()){	//If list is empty then return null
			return null;
		}else{
			FindResult<T> result = find(x);	//Find if x is present in the list
			
			if(result.getNode()!=null){		//If x is present in the list return that element 
				return result.getNode().getData();
			}else{
				if(result.getVisited()!=null){	//If the element is not present in the list then return its next greater element using visited array from find result
					return result.getVisited()[0].getData();
				}
			}
		}
		return null;	//Return null if element is greater than the last element of the list
	}

	/*
	 * Function to check if the list is empty by checking its size
	 * 
	 */		
	@Override
	public boolean isEmpty() {
		if(size==0){	//if size is 0 then list is empty, it contains header
			return true;
		}else{
			return false;
		}
	}

	/*
	 * Function to get the iterator for the list
	 * @return : iterator to iterate over the list
	 */
	@Override
	public Iterator<T> iterator() {
		
		ListIterator<T> it = new ListIterator<T>(header);
		return it;
	}

	/*
	 * Function to get the last element of the list
	 * @return : last node of the list 
	 */
	@Override
	public T last() {
	    if (isEmpty()) {	//If list is empty then return null
            return null;
        }else{
        	SkipNode<T>p = header;

            for (int level = MAXLEVEL; level >= 0; level--) {	//Run from MAXLEVEL till the 1st level (0) till the element is found
                while (p.getNext()[level] != null
                        && p.getNext()[level].getNext()[0] != null) {
                    p = p.getNext()[level];		//Move to the next elelment pointed by current element's level
                }
            }
    		return p.getNext()[0].getData();	//return the data
        }
  	}


	/*
	 * Function to rebuild the list
	 * This rebuilds the current list to perfect skip list with MAXLEVEL = log(size)
	 */
	@Override
	public void rebuild() {
		int n = size();
		int maxlevel = (int) Math.ceil((Math.log(n+1) / Math.log(2)));	//Change the MAXLEVEL to log(size)
		SkipNode<T> p = header;
		SkipNode<T> [] node;
	
		//Get from the 1st level
		//Starting from the level = 1 as at level 0 all are connected.
		//Point to its own's lower level's - next node level array, and then point to this level's next node
		for(int i=1 ; i<=maxlevel ; i++){
			p = header;
			while(p.getNext()!=null && p.getNext()[0]!=null && p.getNext()[i-1]!=null){
				node = p.getNext()[i-1].getNext();	//Get the Level-Array pointed from its lower level's next
				if(node[0]== null){	
					p.setNextIndex(i, p.getNext()[i-1]);	
				}else{
					p.setNextIndex(i, node[i-1]);
				}
				p= p.getNext()[i];
			}
		}
		//Set the MAXLEVEL to the new level as calculated
		MAXLEVEL = maxlevel;
	}

	/*
	 * Function to remove the element if it is present 
	 * @param x: the element to be removed from the list
	 * @return : return true if element is not present or else return false
	 */
	@Override
	public boolean remove(T x) {
		if(isEmpty()){
			return false;
		}else{
			FindResult<T> prev = find(x);
			if(prev.getNode() == null){	//if element not found then return null
				return false;
			}else{	//if element is present then remove
				size = size - 1;	//Decrement the size by 1
				for(int i=0;i<=MAXLEVEL;i++){
					if((prev.getVisited()[i].getNext().length > i) && prev.getVisited()[i].getNext()[i]!=null){	
						if(prev.getNode().getData().equals(prev.getVisited()[i].getNext()[i].getData())){
							if(prev.getNode().getNext().length > i){
								prev.getVisited()[i].getNext()[i] = prev.getNode().getNext()[i];
							}
						}else{
							break;
						}
					}
				}
			}
			return true;
		}
	}
	/*
	 * Function to return size of the list
	 * @return size of the list 
	 */
	@Override
	public int size() {
		return size;
	}
	

	public static void main(String[] args) {

		Scanner sc = null;

		if (args.length > 0) {
			File file = new File(args[0]);
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			sc = new Scanner(System.in);
		}
		String operation = "";
		long operand = 0;
		int modValue = 997;
		long result = 0;
		Long returnValue = null;
		SkipListImpl<Long> skipList = new SkipListImpl<Long>();
		// Initialize the timer
		long startTime = System.currentTimeMillis();

		while (!((operation = sc.next()).equals("End"))) {
			switch (operation) {
			case "Add": {
				operand = sc.nextLong();
				skipList.add(operand);
				result = (result + 1) % modValue;
				break;
			}
			case "Ceiling": {
				operand = sc.nextLong();
				returnValue = skipList.ceiling(operand);
				if (returnValue != null) {
					result = (result + returnValue) % modValue;
				}
				
				break;
			}
			case "FindIndex": {
				operand = sc.nextLong();
				returnValue = skipList.findIndex((int)operand);
				if (returnValue != null) {
					result = (result + returnValue) % modValue;
				}
				
				break;
			}
			case "First": {
				returnValue = skipList.first();
				if (returnValue != null) {
					result = (result + returnValue) % modValue;
				}
				
				break;
			}
			case "Last": {
				returnValue = skipList.last();
				if (returnValue != null) {
					result = (result + returnValue) % modValue;
				}
				break;
			}
			case "Floor": {
				operand = sc.nextLong();
				returnValue = skipList.floor(operand);
				if (returnValue != null) {
					result = (result + returnValue) % modValue;
				}
				System.out.println("Floor ="+returnValue);
				break;
			}
			case "Remove": {
				operand = sc.nextLong();
				if (skipList.remove(operand)) {
					result = (result + 1) % modValue;
				}
				break;
			}
			case "Contains": {
				operand = sc.nextLong();
				if(skipList.contains(operand)){	//If element is present in the skiplist
					result = (result + 1) % modValue;
					
				}
				break;
			}
			
			case "Rebuild": {
				String sb = skipList.toString();
				System.out.println(sb);
				skipList.rebuild();	//If element is present in the skiplist
				break;
			}
			}
		}

		// End Time
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;

		System.out.println(result + " " + elapsedTime);

	}
}