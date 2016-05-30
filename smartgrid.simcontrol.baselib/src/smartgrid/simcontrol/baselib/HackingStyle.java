/**
 *
 */
package smartgrid.simcontrol.baselib;

/**
 * @author Christian
 *
 */
public enum HackingStyle {
    /**
     * With this Style the Attacker will hack the Node in an Breadth First Search manner
     */
    BFS_HACKING,
    /**
     * With this Style the Attacker will hack the Node in an Depth First Search manner
     */
    DFS_HACKING,

    /**
     * With this Style the Attacker will hack every Node in the his Cluster without respecting
     * logical Connections
     */
    FULLY_MESHED_HACKING

}
