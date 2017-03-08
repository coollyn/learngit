package quality;

import java.util.Vector;

import reachability.Montecarlo;

public class PStar
{
	//input parameters
	String m_pe;
	Vector<StringBuffer> m_es;
	
	//output result
	private double m_pstar;
	
	public PStar(String pe,Vector<StringBuffer> allpaths)
	{
		// TODO Auto-generated constructor stub
		m_pe=pe;
		this.m_es=allpaths;
	}
	
	public PStar()
	{
	}

	public void reset(String pe,Vector<StringBuffer> allpaths)
	{
		m_pe=pe;
		this.m_es=allpaths;
	}
	
	public double getPStar()
	{
		return m_pstar;
	}
	
	boolean pathWithPe(StringBuffer path, String pe)			//judge if a path contains a edge pe: v1_v2
	{
		String v1=pe.substring(0, pe.indexOf('_'));
		String v2=pe.substring(pe.indexOf('_')+1);
		
		int forePoint1=-1, forePoint2=-1;
		int len1=v1.length(),len2=v2.length();
		
		boolean b=false;
			
		while(true)
		{	
			if(b == false)
			{
				forePoint1=path.indexOf(v1,forePoint1+1);
				if(-1==forePoint1)
					break;
				
				if(path.charAt(forePoint1-1) != '.')
					continue;
							
				if(path.charAt(forePoint1+len1) != '.')
					continue;
				forePoint2=path.indexOf(v2,forePoint1+len1+2);
			}
			else
				forePoint2=path.indexOf(v2,forePoint2+1);
			
			if(forePoint2==-1)
				break;
			b=false;
			
			if(path.charAt(forePoint2-1) != '.')
			{
				b=true;
				continue;
			}
			
			if(path.charAt(forePoint2+len2) != '.')
			{
				b=true;
				continue;
			}
			
			if(forePoint1+len1 >= forePoint2-1)
				continue;
			
			return true;	
		}
		
		return false;
	}
	
	public void cacltPStar()
	{
		//idea: according to the formula of pstar, the paths set containing m_pe is complete while that without m_pe is incomplete
		//so now separate the paths set into 2 parts and meanwhile compute their corresponding values in the formula.
		double rch1=1,rch2=1;	//	two sets
		Vector<StringBuffer> paths_pe =new Vector<StringBuffer>();	//paths set with pe
		Vector<StringBuffer> paths_no_pe =new Vector<StringBuffer>();	//paths set without pe
		
		for(int i=0;i<m_es.size();i++)
		{
			StringBuffer onepath=m_es.get(i);			//single path
			if( pathWithPe(onepath,m_pe) )
				paths_pe.add(onepath);
			else
				paths_no_pe.add(onepath);
		}
		
		Montecarlo mt=new Montecarlo(paths_pe);
		mt.cacltReach();
		rch1=mt.getReach();
		
		mt.reset(paths_no_pe);
		mt.cacltReach();
		rch2=mt.getReach();
		
		this.m_pstar=rch1*(1-rch2);
	}
	
	double cacltOnePathProb(String onepath)	//calculate one path probability
	{
		double prob=1;
		String elmts[]=onepath.split(".");
		for(int i=2;i<elmts.length;i+=3)
			prob*=Double.parseDouble(elmts[i]);
		
		return prob;
	}

	public double pstarNumeratorUB(double r0)	//the upper bound of numerator of patar
	{
		double pathProb=0;
		String elmts[]=null;
		for(StringBuffer onepath: this.m_es)
		{
			if(onepath.indexOf(this.m_pe) == -1)
				continue;
			
			double prob=1;
			elmts=onepath.toString().split("\\.");
			for(int i=4;i<elmts.length;i+=2)
				prob*=Double.parseDouble("0."+elmts[i]);
			
			pathProb+=prob;
		}
		
		return pathProb*(1+pathProb-r0);
	}
	
	public double pstarNumeratorLB(double r0)	//the lower bound of numerator of patar
	{
		double pathProb=0;
		String elmts[]=null;
		for(StringBuffer onepath: this.m_es)
		{
			if(onepath.indexOf(this.m_pe) != -1)
				continue;
			
			double prob=1;
			elmts=onepath.toString().split("\\.");
			for(int i=4;i<elmts.length;i+=2)
				prob*=Double.parseDouble("0."+elmts[i]);
			
			pathProb+=prob;
		}
		
		return (r0-pathProb) * (1-pathProb);
	}
}
