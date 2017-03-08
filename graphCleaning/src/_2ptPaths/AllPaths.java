package _2ptPaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
//@SuppressWarnings("rawtypes")

public class AllPaths
{
	//input parameters
	String m_filepath;			//store the graph
	int s,t;					//start vertex, terminal vertex
	int hop;
	
	//output result
	private Vector<StringBuffer> m_vctPaths=new Vector<StringBuffer>();
	
	//intermediate variables
	Map<Integer, NextNode> mp= new HashMap<Integer, NextNode>();
	
	public AllPaths(String file,int s,int t,int hop)
	{
		// TODO Auto-generated constructor stub
		m_filepath=file;
		this.s=s;
		this.t=t;
		this.hop=hop;
	}

	public void printAllPaths(String out2file) throws FileNotFoundException
	{
		PrintStream ps=new PrintStream(out2file);
		for(StringBuffer path: this.m_vctPaths)
			ps.println(path);
		ps.close();
	}
	
	public Vector<StringBuffer> getpaths()
	{
		return m_vctPaths;
	}
	
	public void listall() throws IOException
	{
		File files=new File(m_filepath);
		for(File file: files.listFiles())
		{	
			storeHash(file);
		}
		
		DFS();	
	}
	
	void storeHash(File file) throws IOException
	{
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		try
		{	
			String sLine;
			String[] sElmts=null;
			
			while((sLine=br.readLine())!=null)
			{
				sElmts=sLine.split("_");
				int iNode1=Integer.parseInt(sElmts[0]);			
				int iNode2=Integer.parseInt(sElmts[1]);
				double dProb=Double.parseDouble(sElmts[2]);
				
				NextNode nd;
				if((nd=(NextNode)mp.get(iNode1)) == null)
				{
					nd=new NextNode();
					nd.add(iNode2+dProb);
					mp.put(iNode1, nd);
				}
				else
					nd.add(iNode2+dProb);
				
				if((nd=(NextNode)mp.get(iNode2)) == null)
				{
					nd=new NextNode();
					nd.add(iNode1+dProb);
					mp.put(iNode2, nd);
				}
				else
					nd.add(iNode1+dProb);
			}						
		}
		catch(IOException ioe) 
		{
			System.out.println(ioe);
		}
		finally
		{
			br.close();
			fr.close();
		}
	}
	
	void DFS()
	{
		int iCurHop=0;
		Stack<Double> S = new Stack<Double>();
		S.push((double)s);			//For the stack element format: "node.prob"
		StringBuffer sPrePath=new StringBuffer();
			
		while(!S.isEmpty())
		{
			double dNd=(double)S.peek();
			int iNode=(int)Math.floor(dNd);
			
			if(sPrePath.indexOf(Integer.toString(iNode)) == -1)
				sPrePath.append("."+Double.toString(dNd));
			
			if(iNode==t)
			{
				StringBuffer sPath= new StringBuffer(sPrePath);
				m_vctPaths.add(sPath);
	
				S.pop();
				iCurHop--;
				sPrePath.delete(sPrePath.lastIndexOf("."), sPrePath.length());
				sPrePath.delete(sPrePath.lastIndexOf("."), sPrePath.length());
			}
			else
			{
				if(iCurHop == hop)
				{
					S.pop();
					iCurHop--;
					sPrePath.delete(sPrePath.lastIndexOf("."), sPrePath.length());
					sPrePath.delete(sPrePath.lastIndexOf("."), sPrePath.length());
				}
				else
				{	
					NextNode nxtnd=(NextNode)mp.get(iNode);
					if(nxtnd==null)
					{
						S.pop();
						iCurHop--;
						sPrePath.delete(sPrePath.lastIndexOf("."), sPrePath.length());
						sPrePath.delete(sPrePath.lastIndexOf("."), sPrePath.length());
					}
					else
					{
						boolean b=true;
						while(nxtnd.iterator < nxtnd.getSize())
						{
							double dNxNd=nxtnd.get(nxtnd.iterator);
							nxtnd.iterator++;
								
							if(sPrePath.lastIndexOf( Integer.toString( (int)Math.floor(dNxNd) ) ) == -1)
							{
								S.push(dNxNd);
								iCurHop++;	
								b=false;
								break;
							}
						}
						if(nxtnd.iterator == nxtnd.getSize() && b)
						{
							S.pop();
							iCurHop--;
							sPrePath.delete(sPrePath.lastIndexOf("."), sPrePath.length());
							sPrePath.delete(sPrePath.lastIndexOf("."), sPrePath.length());
							nxtnd.iterator=0;
						}
					}
				}
			}
		}
	}
}

