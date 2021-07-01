package dbscan;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import random.RandomColors;

public class dbScanClustering {

    // max radius
    private double eps;
    // min. members in the neighborhood whithin eps
    private int minPts;
    // the array of pixels
    public ArrayList<DensityPoint> dataset = new ArrayList<DensityPoint>();
    // clusters
    public ArrayList<dbScanCluster> dbScanClusters = new ArrayList<dbScanCluster>();

    // GUI
    BufferedImage oldImage; // the source image
    int width, height; // dimations of the source image
    RandomColors clustercolors = new RandomColors(); // color the clusters

    public dbScanClustering(int minPts, double eps, BufferedImage oldImage) {
        this.eps = eps;
        this.minPts = minPts;
        this.oldImage = oldImage;
        // GUI
        this.width = oldImage.getWidth();
        this.height = oldImage.getHeight();
        extractPoints();
    }

    /**
     * cluster the image using DBSCAN
     * 
     * @return the clustered image to the GUI
     */
    public BufferedImage convertImage() {
        int id_index = 0;
        for (DensityPoint point : dataset) {
            // if the point is not visited
            if (!point.getVisited()) {
                point.setVisited(true);
                // get the neighbors
                ArrayList<DensityPoint> neighbors = getNeighbors(point);
                // if the point is a core point
                if (neighbors.size() >= minPts) {
                    // create a cluster with id of the inital point
                    dbScanCluster cluster = new dbScanCluster(id_index, clustercolors.findColor(id_index));
                    dbScanClusters.add(expandCluster(cluster, point, neighbors));
                    id_index++;

                } else {
                    // set the point as a nois
                    point.setIsNois(true);
                }
            }

        }
        System.out.println("DBSCAN found " + dbScanClusters.size() + " clusters.");
        return convertToImage();
    }

    /**
     * expand the cluster here there is no need to update the cluster id of each
     * point because we can get the id's of all memebers just by calling the
     * clusters
     * 
     * @param cluster
     * @param point
     * @param neighbors
     * @return
     */
    private dbScanCluster expandCluster(dbScanCluster cluster, DensityPoint point, ArrayList<DensityPoint> neighbors) {
        ArrayList<DensityPoint> seeds = neighbors;
        int index = 0;
        while (index < seeds.size()) {
            System.out.println(" time left: " + index + "/" + dataset.size() + "s");
            // current neighbor
            DensityPoint curr = seeds.get(index);
            // if the neighbor is not visited
            if (!curr.getVisited()) {
                // get his neighborhood
                ArrayList<DensityPoint> currNeighbors = getNeighbors(curr);
                // use this to filter the noise from currNeighbors
                ArrayList<DensityPoint> filteredNeighbors = new ArrayList<DensityPoint>();
                // check if the current have min. points
                if (currNeighbors.size() >= minPts) {
                    // check if the neighbors are not visited and not noise
                    for (DensityPoint currNeighbor : currNeighbors) {
                        if (currNeighbor.getIsUndefinied() || currNeighbor.getIsNois()) {
                            if (currNeighbor.getIsUndefinied()) {
                                filteredNeighbors.add(currNeighbor);
                            }
                        }
                    }
                    // exapnd the seeds to inculde the curr neighbors without dublicates
                    seeds = expand(seeds, currNeighbors);
                }
            }
            // if the pixel is not yet in a cluster
            if (curr.getIsUndefinied()) {
                curr.setVisited(true);
                cluster.addpixel(curr);
                curr.setID(cluster.id);
            }
            index++;
        }

        return cluster;
    }

    /**
     * merg two arrays into one and avoid dublicates
     * 
     * @param seeds
     * @param currNeighbors
     * @return
     */
    public ArrayList<DensityPoint> expand(ArrayList<DensityPoint> seeds, ArrayList<DensityPoint> currNeighbors) {

        for (DensityPoint point : currNeighbors) {
            // avoid dublicates
            if (!seeds.contains(point)) {
                seeds.add(point);
            }

        }
        return seeds;
    }

    /**
     * get the neighbor points of a given point
     * 
     * @param point
     * @return
     */
    public ArrayList<DensityPoint> getNeighbors(DensityPoint point) {
        // neighbors array
        ArrayList<DensityPoint> neighbors = new ArrayList<DensityPoint>();
        // for each elm in the dataset
        for (DensityPoint neighbor : dataset) {
            // if its not himself and the distance is less that eps
            if (point != neighbor && calcDistance(point, neighbor) <= eps) {
                neighbors.add(neighbor);
            }

        }

        return neighbors;
    }

    /**
     * calculate the distance between two points using euclidean distance function
     * 
     * @param p1
     * @param p2
     * @return
     */
    private int calcDistance(DensityPoint p1, DensityPoint p2) {

        int r = Math.abs(p2.getRed() - p1.getRed());
        int g = Math.abs(p2.getGreen() - p1.getGreen());
        int b = Math.abs(p2.getBlue() - p1.getBlue());
        return (r + g + b) / 3;

    }

    /** extract from the image the rgb's and update the data set with them */
    public void extractPoints() {
        // this two loops are for every pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                dataset.add(new DensityPoint(oldImage.getRGB(x, y), x, y));
            }
        }
    }

    /*****************************
     * GUI RELATED
     *****************************/

    /**
     * For GUI take the dataset of clustersID and convert it into an image
     * Mark::TYPE_INT_RGB: Represents an image with 8-bit RGB color components
     * packed into integer pixels.
     * 
     * @return an image from dataset
     */
    private BufferedImage convertToImage() {

        // create result image
        BufferedImage convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // for all image dimantions
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // find the point
                for (DensityPoint p : dataset) {
                    if (p.getX() == x && p.getY() == y) {
                        // find the color of the cluster of that point
                        int pixelColor = dbScanClusters.get(p.getID()).clusterColor;
                        convertedImage.setRGB(x, y, pixelColor);
                    }

                }

            }
        }
        return convertedImage; // orginal -> return convertedImage;
    }

}
