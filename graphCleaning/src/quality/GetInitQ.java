package quality;

import java.util.Vector;
import reachability.Montecarlo;

public class GetInitQ
{
	Vector<StringBuffer> m_allpaths;
	
	double m_R0;		//the initial reachability
	double m_Q0;		//the initial graph quality
	
	public GetInitQ(Vector<StringBuffer> m_allpaths2)
	{
		// TODO Auto-generated constructor stub
		this.m_allpaths=m_allpaths2;
	}

	public void initQ()
	{	
		Montecarlo mt=new Montecarlo(m_allpaths);
		mt.cacltReach();
		
		m_R0=mt.getReach();									//compute reachability R0
		
		m_Q0=-m_R0*Math.log(m_R0)-(1-m_R0)*Math.log(1-m_R0);		//compute quality
	}
	
	public double getR0()
	{
		return m_R0;
	}
	
	public double getQ0()
	{
		return m_Q0;
	}
}
