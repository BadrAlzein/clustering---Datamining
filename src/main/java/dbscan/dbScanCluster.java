package dbscan;

import java.util.ArrayList;

public class dbScanCluster {

    public int id; 
    public int clusterColor; 
    public ArrayList<DensityPoint> members = new ArrayList<DensityPoint>();

    public dbScanCluster(int id, int clusterColor) {
        this.id = id;
        this.clusterColor = clusterColor;
    }

    /**
     * add memeber to this cluster
     * 
     * @param curr a point
     */
    public void addpixel(DensityPoint curr) {
        members.add(curr);
    }

}
