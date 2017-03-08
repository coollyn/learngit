/**compute the quality of an uncertain graph
 * compute pstar
 * 
 * 
 * --------------------------------------GetInitQ----------------------------------------------
 * description: compute the initial quality initQ of an uncertain graph
 * 
 * input: a Vector<String> object reperesenting all paths
 * output: a double value indicating the initQ: first invoke initQ()
 * 
 * 
 * --------------------------------------GetMaxQ------------------------------------------------------
 * description: compute and select the maximal quality of an uncertain graph
 * input: a file representing the graph,start vertex, terminal vertex
 * output: an String representing the edge and a double value indicating the maxQ accordingly: first invoke maxQ(), then getOptE, getMaxQ()
 * 
 * -------------------------------------PStar--------------------------------------------------------
 * description: given the edges set: an edge probability; edges set description, then compute corresponding pstar
 * 
 * input: an edge with form "v1_v2_prob", a Vector<String> object indicating edges set
 * output: a double value indicating pstar: first invoke cacltPStar, then getPStar
 * 
 * 
 * ------------------------------------
 */
/**
 * @author coollyn
 *
 */
package quality;