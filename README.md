# Skip-List
Problem: Implement the skip list data structure. Compare its performance with Java's TreeMap.
Description:  Implement algorithm for adding an element and assigning random levels to its node, removing of nodes in the list, and finding an element and getting the last and first element of the list.
A Skip List is a data structure that allows fast search within an ordered sequence of elements. Fast search is made possible by maintaining a linked hierarchy of subsequences, each skipping over fewer elements. [http://en.wikipedia.org/wiki/Skip_list]
Skip list consists of nodes linked only in forward direction. A node is made up of data and array of links which points to the next element on that level.
Classes in the Project:
1.	SkipListImpl
Class implementing the SkipNode interface
2.	SkipNode
Class for creating the node of the list with 
3.	ElementAllreadyExistsException
Class for throwing exception when already existing element is added to the list
4.	ListIterator
Class for creating iterator to iterate over the list
5.	Tree
Class implementing TreeSet for adding, removing and checking if an element is present in the list. This class is just for checking the performance of TreeSet with skip list.
6.	FindResult
Class used to create an object for returning the result of find function. It will return the node whose data is found and the array of visited nodes to reach to this node. This array is used in case of add and remove.
7.	SkipList
This is an interface which is implemented by SkipListImpl

Approach for Rebuild:
•	Rebuild function rebuilds the existing list to have perfect skip list.
•	MAXLEVEL of the list is calculated by the formula log(size of skip list)
•	Level 0 of all the nodes will be connected to their immediate next node. (forward direction)
•	Level 1 of all the nodes will be skipping 1 element and will be connected to alternate nodes
•	This way each higher level contains half of the elements of its lower level
Approach for changing MAXLEVEL: 
The MAXLEVEL is set to 2 initially. The value of MAXLEVEL is changed when following condition occurs: MAXLEVEL < (log(size+1)). Then rebuild function is called and the list is converted to perfect skip list with new MAXLEVEL.
Note: FindIndex function implemented is the traditional way taking n time.
Improvement Areas: FindIndex function.


