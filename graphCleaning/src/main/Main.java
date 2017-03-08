package main;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import _2ptPaths.AllPaths;
import _2ptPaths.Transfer;
import quality.GetMaxQ;
import quality.PStar;
import reachability.Montecarlo;

public class Main
{
	String m_filepath;		//the uncertain graph
	int m_s,m_t;			//start vertex and terminal vertex
	int m_hop;				//hop
	
	public Main(String file,int s,int t,int hop)
	{
		// TODO Auto-generated constructor stub
		m_filepath=file;
		m_s=s;
		m_t=t;
		m_hop=hop;
	}

	public static void main(String[] args) throws IOException
	{
		// TODO Auto-generated method stub
		String filepath="F:\\林欣\\实验数据\\作者关系网\\0.99保留0.01忽略";//"C:\\Users\\coollyn\\Desktop\\1";//args[0];
		int s=364861;//Integer.parseInt(args[1]);
		int t=890183;//Integer.parseInt(args[2]);
		int hop=3;//Integer.parseInt(args[3]);

		GetMaxQ max=new GetMaxQ(filepath,s,t,hop);
		max.optEmaxQ();
		String optE=max.getOptE();
		double maxQ=max.getMaxQ();
		System.out.println(optE+"\t"+maxQ);

/*		long t1=System.currentTimeMillis();
		
		AllPaths ap=new AllPaths(filepath,s,t,hop);
		ap.listall();
		Vector<StringBuffer> paths=ap.getpaths();
		PrintStream ps=new PrintStream("F:\\林欣\\实验数据\\s=364861,t=890183(2).txt");
		for(int i=0;i<paths.size();i++)
			ps.println(paths.get(i));
		ps.close();
		 
		long t2=System.currentTimeMillis();
		System.out.println(t2-t1);
		
		Montecarlo mt=new Montecarlo(paths);
		mt.cacltReach();
		System.out.println(mt.getReach());
		
		long t3=System.currentTimeMillis();
		System.out.println(t3-t2);*/	
	}
}
