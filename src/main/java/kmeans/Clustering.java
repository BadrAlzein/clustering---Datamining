
package kmeans;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Clustering {

  BufferedImage convertedImage;
  Cluster[] clusters; // all clusters in the pic
  BufferedImage oldImage; // the source image
  int width, height; // dimations of the source image
  int[] imageData; // the dataset (clustersIDs array)

  public Clustering(BufferedImage oldImage) {
    this.oldImage = oldImage;
    this.width = oldImage.getWidth();
    this.height = oldImage.getHeight();
    this.imageData = new int[this.width * this.height];
    System.out.println(" clustering " + this.width * this.height);
    Arrays.fill(this.imageData, -1); // by default fill it with anything <0
  }

  /**
   * convertImage:: K means Algo 1. create k inital clusters 2. for each pixel in
   * the image (widthXheight) do the following: a. get the RGB value for each
   * pixel b. finde the closest cluster to that pixel c. if the pixel is assigned
   * to a cluster (his index in the data set is not the neares cluster) then :
   * c.1. clear his index in the dataset from any old clusters -> also remove the
   * pixel from the old cluster (*(-1)) c.2. add the pixel to the new cluster (the
   * closest) -> also update his index in the dataset to be the new clusterId c.3.
   * finally let the loop looping until all pixels are stable
   */

  /**
   * cluster a giving image
   * 
   * @param k number of clusters
   * @return the clustered image
   */
  public BufferedImage convertImage(int k) {
    // create clusters
    clusters = createClusters(k);
    // at first loop all pixels will move their clusters
    boolean changeFlag = true;
    // loop until all clusters are stable!
    while (changeFlag) {
      changeFlag = false;
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) { // this two loops are for every pixel in the image
          int pixel = oldImage.getRGB(x, y); // the current pixel
          Cluster cluster = closestCluster(pixel); // find the nearst cluster to that pixel
          if (imageData[width * y + x] != cluster.id) { // if the pixel dont bellong to the nearst
            // cluster changed
            if (imageData[width * y + x] != -1) { // if its not the first cycle
              // remove from possible previous
              // cluster
              clusters[imageData[width * y + x]].removePixel(pixel);
            }
            // add pixel to cluster
            cluster.addPixel(pixel);
            // continue looping
            changeFlag = true;
            // update imageData
            imageData[width * y + x] = cluster.id; // assign the pixel to the cluster
          }
        }
      }
    }
    return extractImage();
  }

  /**
   * divide the image into k clusters
   * 
   * @param k clusters
   * @return a k clusters array with diffrent initial centroids
   */
  public Cluster[] createClusters(int k) {
    // create an array of clusters
    clusters = new Cluster[k];
    // start first centroid in 0,0 and next cluster shift to bottom right
    int centroid_x = 0, centroid_y = 0;
    int shift_centroid_x = width / k, shift_centroid_y = height / k;
    // for k clusters
    for (int i = 0; i < k; i++) {
      // create the cluster with its centroid coord.
      clusters[i] = new Cluster(i, oldImage.getRGB(centroid_x, centroid_y));
      // shift the centroid for the next cluster centroid
      centroid_x += shift_centroid_x;
      centroid_y += shift_centroid_y;
    }
    return clusters;
  }

  /**
   * find the nearest cluster to a given rgb value
   * 
   * @param rgb the given pixel on three color chanels
   * @return the closest cluster to that pixel
   */
  public Cluster closestCluster(int pixel) {
    int nearest_ClusterIndex = 0;
    int minDistance = Integer.MAX_VALUE; // in default set the min to be as high as the integer range
    // for all possible clusters
    for (int i = 0; i < clusters.length; i++) {
      // the distance between the current cluster and the pixel
      int newDistance = clusters[i].distance(pixel);
      // if the new cost is closer (sheaper) then update
      if (newDistance < minDistance) {
        minDistance = newDistance; // update the cost
        nearest_ClusterIndex = i; // the index of the closest
      }
    }
    // the cluster with the index of the minimal distance to the pixel
    return clusters[nearest_ClusterIndex];
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
  private BufferedImage extractImage() {
    // create result image
    convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // for all image dimantions
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int clusterId = imageData[width * y + x]; // get the id from the dataset
        convertedImage.setRGB(x, y, clusters[clusterId].getRGB());
      }
    }
    return convertedImage;
  }
}
