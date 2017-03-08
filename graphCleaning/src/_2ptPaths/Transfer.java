package _2ptPaths;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import quality.PStar;

public class Transfer
{
	//output
	Map<String, Double> m_pstar51=new HashMap<String, Double>();
	Map<String, Double> m_pstar05=new HashMap<String, Double>();
	
	Map<String,Double> m_edges05=new HashMap<String, Double>();		//filter out edges with prob between 0 and 0.5
	Map<String,Double> m_edges51=new HashMap<String, Double>();		//filter out edges with prob between 0.5 and 1.
	
	//input
	Vector<StringBuffer> m_allpaths;
	
	public Transfer(Vector<StringBuffer> paths)
	{
		// TODO Auto-generated constructor stub
		this.m_allpaths=paths;
	}

	public Map<String, Double> getPstar05()
	{
		return this.m_pstar05;
	}
	public Map<String, Double> getPstar51()
	{
		return this.m_pstar51;
	}
	public Map<String,Double> get05()
	{
		return m_edges05;
	}
	public Map<String,Double> get51()
	{
		return m_edges51;
	}
	
	public void tran2mp(double r0)
	{
		String []elmts = null;
		PStar P=new PStar();
		
		for(StringBuffer onepath: this.m_allpaths)
		{
			elmts=onepath.toString().split("\\.");
			for(int j=4;j<elmts.length;j+=2)
			{	
				double prob=Double.parseDouble("0."+elmts[j]);
				if(prob<0.5)
				{
					this.m_edges05.put(elmts[j-3]+"_"+elmts[j-1], prob);
				
					P.reset(elmts[j-3]+"_"+elmts[j-1]+elmts[j],this.m_allpaths);
					this.m_pstar05.put(elmts[j-3]+"_"+elmts[j-1], P.pstarNumeratorUB(r0));
				
				}
				else
				{
					this.m_edges51.put(elmts[j-3]+"_"+elmts[j-1], prob);
				
					P.reset(elmts[j-3]+"_"+elmts[j-1]+elmts[j],this.m_allpaths);
					this.m_pstar51.put(elmts[j-3]+"_"+elmts[j-1], P.pstarNumeratorUB(r0));
				
				}
			}
		}
	}
}
