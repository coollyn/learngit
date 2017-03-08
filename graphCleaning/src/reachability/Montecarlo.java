package reachability;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class Montecarlo
{
	//input parameters
	Vector<StringBuffer> m_allpaths;

	double m_dErr=0.05,m_confidence=0.1;
	Random m_rdmPath=new Random();
	Random m_rdmState= new Random();
	
	//output result
	private double m_reachability; 
	
	public Montecarlo(Vector<StringBuffer> m_allpaths2)
	{
		// TODO Auto-generated constructor stub
		m_allpaths=m_allpaths2;
	}

	public double getReach()
	{
		return m_reachability;
	}

	public void cacltReach()
	{
		int alpha=0;
		int iSize=m_allpaths.size();
		int iTurns=(int)(iSize/(m_confidence*m_dErr*m_dErr));
		int iLoop=0;	
		while(iLoop++<iTurns)
		{
			int index=(int)(m_rdmPath.nextDouble()*iSize);
			StringBuffer sPath=(StringBuffer)m_allpaths.elementAt(index); 
			Map<String, String> mp=GetOneState(new String(sPath));
			
			int iStart,iEnd;
			if(m_allpaths.size()/2 > index)
			{
				iStart=0;
				iEnd=index-1;
			}
			else
			{
				iStart=index+1;
				iEnd=m_allpaths.size()-1;
			}
			
			for(int i=iStart;i<iEnd;i++)
				if(HaveState((StringBuffer)m_allpaths.get(i),mp))
				{
					alpha++;
					break;
				}
		}
		
		m_reachability=(double)alpha/iTurns;		
	}
	
	Map<String, String> GetOneState(String sPath)
	{
		Map<String, String> mp= new HashMap<String, String>();
		
		String sElmts[]=sPath.split("\\.");
		if(m_rdmState.nextDouble() < Double.valueOf("0."+sElmts[4]))
			mp.put(sElmts[1], sElmts[3]);
		
		for(int i=5;i<sElmts.length;i+=2)
			if(m_rdmState.nextDouble() < Double.valueOf("0."+sElmts[i+1]))
					mp.put(sElmts[i-2], sElmts[i]);
		
		return mp;
	}
	
	boolean HaveState(StringBuffer sPath,Map<String, String> mp)
	{
		boolean bb=true;
		for(Map.Entry<String, String> entry : mp.entrySet())
		{
			int i=sPath.indexOf(entry.getKey()),j;
			if(-1 != i)
			{
				i=sPath.indexOf(".", i);
				i=sPath.indexOf(".", i+1);
				j=sPath.indexOf(".", i+1);
				
				if(-1==j || !entry.getValue().equals(sPath.substring(i+1, j)))
				{
					bb=false;
					break;
				}
			}
			else
			{
				bb=false;
				break;
			}
		}

		return bb;
	}

	public void reset(Vector<StringBuffer> paths)
	{
		m_allpaths=paths;
	}
}
