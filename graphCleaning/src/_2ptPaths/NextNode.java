package _2ptPaths;
import java.util.*;
@SuppressWarnings("unchecked")

public class NextNode 
{
	Vector nxt_nd_set;
	public int iterator=0;
	public NextNode() 
	{
		nxt_nd_set=new Vector();
	}
	
	void add(double node)
	{
		nxt_nd_set.add(node);
	}
	double get(int index)
	{
		return (double)nxt_nd_set.get(index);
	}
	
	int getSize()
	{
		return nxt_nd_set.size();
	}
}
