/**Given an uncertain graph, list all paths from point 1 to point 2
 * transfer paths to hashmap: (v1_v2,prob)  (v1_v2,pstar)
 * 
 */

//----------------------AllPaths------------------------------------------------------------------------
/*
 * input: a file representing a graph, start vertex, terminal vertex
 * output: a Vector<String> object containing all paths: first invoke listall(), then call getpaths()
 * 
 * 
 * 
 * ----------------------Transfer-----------------------------------------------------------------------
 * description: transfer paths to hashmap: (v1_v2,prob)  (v1_v2,pstar)
 * input: a vector<SrtingBuffer> object representing paths
 * output: two hashmap: (v1_v2,prob)  (v1_v2,pstar):  first invoke tran2mp(), then getMpProb or getMpPstar
 * 
 */

/**
 * @author coollyn
 *
 */
package _2ptPaths;