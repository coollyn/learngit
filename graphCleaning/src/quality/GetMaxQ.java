package quality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;


import _2ptPaths.AllPaths;
import _2ptPaths.Transfer;

public class GetMaxQ
{
	//constant value
	double pc=0.7;		//crowd accuracy
	
	//input parameters
	String m_filepath;	//file representing an uncertain graph
	int m_s,m_t;			//start vertex, terminal vertex
	int m_hop;				//hop
	
	//intermediate values
	Vector<StringBuffer> m_allpaths;
	double m_R0,m_Q0;
	
	Map<String, Double> m_pstar51;
	Map<String, Double> m_pstar05;
	
	Map<String,Double> m_edges05;		//filter out edges with prob between 0 and 0.5
	Map<String,Double> m_edges51;
	
	List<Map.Entry<String,Double>> m_listProb;	//m_prob after sorted
	List<Map.Entry<String,Double>> m_listPstar;	//m_pstar after sorted
	short m_edgeProb[];											//indices for 3 parts of quality
	
	//output results
	double m_maxQ;		//the maximal graph quality corresponding to opt edge
	String m_optedge;		//the optimal edge
	
	public GetMaxQ(String file,int s,int t, int hop)
	{
		// TODO Auto-generated constructor stub
		m_filepath=file;
		m_s=s;
		m_t=t;
		m_hop=hop;
	}

	void getGraphProps() throws IOException
	{
		AllPaths ap=new AllPaths(this.m_filepath,m_s,m_t,m_hop);
		ap.listall();
		this.m_allpaths=ap.getpaths();
		
		GetInitQ init=new GetInitQ(m_allpaths);
		init.initQ();
		
		m_R0=init.getR0();
		m_Q0=init.getQ0();
		
		Transfer trans=new Transfer(this.m_allpaths);
		trans.tran2mp(m_R0);
		this.m_edges05=trans.get05();
		this.m_edges51=trans.get51();
		m_pstar05=trans.getPstar05();
		m_pstar51=trans.getPstar51();
		
		//sort map	
	}
	
	String accurateOptPStar(Map<String,Double> mpProb,Map<String,Double> mpPstar)		//compute pStar and then select the minimal pStar from edges 0<pe<0.5 (0.5<pe<1)
	{
		if(mpProb.isEmpty() || mpPstar.isEmpty())
			return null;
		String optE=null;
		
		List<Map.Entry<String, Double>> listProb=this.sortMap(mpProb, true);//new ArrayList<Map.Entry<String,Double>>(mpProb.entrySet());
		List<Map.Entry<String, Double>> listPstar=this.sortMap(mpPstar, false);//new ArrayList<Map.Entry<String,Double>>(mpPstar.entrySet());
		
		Iterator<Map.Entry<String,Double>> itProb=(Iterator<Map.Entry<String,Double>>) listProb.iterator();
		Iterator<Map.Entry<String,Double>> itPstar=(Iterator<Map.Entry<String,Double>>) listPstar.iterator();
		
		String edge1 =null,edge2=null;
		double prob1 =1,pstar2=0;
		double pstar_max= -100;
		PStar P=new PStar();
		boolean flag=false;
		
		while(true)
		{
			if(itProb.hasNext())
			{
				Map.Entry<String, Double> entry=itProb.next();
				edge1=entry.getKey();
				prob1=entry.getValue();
				flag=true;
			}
			
			if(itPstar.hasNext())
			{
				Map.Entry<String, Double> entry=itPstar.next();
				edge2=entry.getKey();
				pstar2=entry.getValue();
				flag=true;
			}
			
			if(flag==false)
				break;
			else
				flag=false;
			
			P.reset(edge1, m_allpaths);
			P.cacltPStar();
			if(P.getPStar() > pstar_max)
			{
				pstar_max=P.getPStar();
				optE=edge1;
			}
			
			P.reset(edge2, m_allpaths);
			P.cacltPStar();
			if(P.getPStar() > pstar_max)
			{
				pstar_max=P.getPStar();
				optE=edge2;
			}
			
			if(pstar_max > pstar2/prob1)
				break;
		}
		
		return optE;
	}
	
	List<Map.Entry<String,Double>> sortMap(Map<String,Double> mp,boolean order)  //	order=true: ascend ; order=false: descend
	{
		List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(mp.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Double>>()
        {
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) 
            {
            	if(order)
            		return o1.getValue().compareTo(o2.getValue());
            	else
            		return o2.getValue().compareTo(o1.getValue());
            }    
        });
		return list;
	}
	
	String obscureOptPStar(Map<String,Double> mpProb,Map<String,Double> mpPstar,double lower,double upper)		//form 0<pe<0.5 (0.5<pe<1), select es={0.5<R_q_T}, then compute and select the minimal pStar
	{
		/*String optE;
		String[] selmts=null;
		double min_pstar=100;
		String v1=null,v2=null,sPe=null;
		for(int i=0;i<m_allpaths.size();i++)
		{
			StringBuffer onepath=m_allpaths.get(i);
			selmts=onepath.toString().split("\\.");
			for(int j=4;j<selmts.length;j+=2)
			{
				double pe=Double.parseDouble("0."+selmts[j]);		//pe: edge probability
				if(pe<lower || pe>upper)
					continue;
				PStar P=new PStar(selmts[j-3]+"_"+selmts[j-1]+"_"+selmts[j],m_allpaths);
				P.cacltPStar();
				double pstar=P.getPStar();	
				double pet_minus_pe=pe*(1-pe)*(2*pc-1) / (pe*pc+(1-pe)*(1-pc));
				double R_q_T=m_R0+pstar*pet_minus_pe;								//compute R_q_T	
				if(R_q_T <lower || R_q_T>upper)
					continue;		
				if(pstar<min_pstar)
				{
					min_pstar=pstar;		
					v1=selmts[j-3];
					v2=selmts[j-1];
					sPe="0."+selmts[j];
				}
			}
		}
		 optE=v1+"_"+v2+"_"+sPe;
		return optE;*/
		
		String optE = null;
		
		Map<String,Double> obs_edges51=new HashMap<String,Double>();
		Map<String,Double> obs_pstar51=new HashMap<String,Double>();
		div_edges51(mpProb,mpPstar,obs_edges51,obs_pstar51,lower,upper);
		
		optE=this.accurateOptPStar(obs_edges51,obs_pstar51);
		
		return optE;
	}
	
	double newQ(String edge)			//assume cleaning an edge, the calculate the new quality
	{
		if(edge==null)
			return 0;
		String sPe=edge.substring(edge.indexOf("_")+1);
		double pe=Double.parseDouble( sPe );
		double pet_minus_pe=pe*(1-pe)*(2*pc-1) / (pe*pc+(1-pe)*(1-pc));
		double pef_minus_pe=pe*(1-pe)*(1-2*pc) / ((1-pe)*pc+pe*(1-pc));
		
		PStar P=new PStar(edge,this.m_allpaths);
		P.cacltPStar();
		double pstar=P.getPStar();
		double R_q_T=m_R0+pstar*pet_minus_pe;
		double R_q_F=m_R0+pstar*pef_minus_pe;
		
		double Q_q_T=-R_q_T*Math.log(R_q_T)-(1-R_q_T)*Math.log(1-R_q_T);
		double Q_q_F=-R_q_F*Math.log(R_q_F)-(1-R_q_F)*Math.log(1-R_q_F);
		
		double Q_q=pe*Q_q_T+(1-pe)*Q_q_F;
		
		return Q_q;
	}
	
	void div_edges51(Map<String,Double> edges51,Map<String,Double> pstar51,Map<String,Double> obs_edges51,Map<String,Double> obs_pstar51,double lower,double upper)	//divide edges with probs between 0.5 and 1 to get ES
	{
		PStar P=new PStar();
		
		for(Map.Entry<String, Double> entry: edges51.entrySet())
		{
			P.reset(entry.getKey(), this.m_allpaths);
			
			if(P.pstarNumeratorLB(this.m_R0)<lower || P.pstarNumeratorLB(m_R0)>upper)
				continue;
			obs_edges51.put(entry.getKey(), entry.getValue());
		}
		
		for(Map.Entry<String, Double> entry: pstar51.entrySet())
		{
			P.reset(entry.getKey(), this.m_allpaths);
			
			if(P.pstarNumeratorLB(this.m_R0)<lower || P.pstarNumeratorLB(m_R0)>upper)
				continue;
			obs_pstar51.put(entry.getKey(), entry.getValue());
		}
	}
	
	public void optEmaxQ() throws IOException
	{
		getGraphProps();
		String optE1,optE2;
		
		if(m_Q0<0.5)
		{
			optE1=this.accurateOptPStar(this.m_edges05,this.m_pstar05);
			optE2=obscureOptPStar(this.m_edges51,this.m_pstar51,0.5,1);
		}
		else
		{
			optE1=this.accurateOptPStar(this.m_edges51,this.m_pstar51);
			optE2=obscureOptPStar(this.m_edges05,this.m_pstar05,0,0.5);
		}
		
		if(this.newQ(optE1)>newQ(optE2))
		{
			m_optedge=optE1;
			m_maxQ=newQ(optE1);
		}
		else
		{
			m_optedge=optE2;
			m_maxQ=newQ(optE2);
		}
	}
	
	public String getOptE()	{
		return this.m_optedge;
	}
	
	public double getMaxQ()
	{
		return this.m_maxQ;
	}
}
