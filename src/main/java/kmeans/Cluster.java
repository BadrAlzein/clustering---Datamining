package kmeans;

class Cluster {

  // cluster Identifier
  int id;
  // number of elements
  int population;
  // centroid pixel
  int centroid_red, centroid_green, centroid_blue;
  // members pixels
  int elements_red, elements_green, elements_blue;
  // non-memeber-local pixel that used to not overwrite an existing member during
  // operations
  int localRed, localGreen, localBlue;

  public Cluster(int id, int initCentroid) {
    this.id = id;
    extractColorChannels(initCentroid);
    this.centroid_red = localRed;
    this.centroid_green = localGreen;
    this.centroid_blue = localBlue;
    addPixel(initCentroid);
  }

  /*****************************
   * add|remove memebers
   *****************************/

  /**
   * add the given pixel to the cluster
   * 
   * @param color the 18 Bit pixel
   */
  void addPixel(int p) {
    population++;
    extractColorChannels(p);
    elements_red += localRed;
    elements_green += localGreen;
    elements_blue += localBlue;
    updateCentroid();
  }

  /**
   * remove a member from the cluster
   * 
   * @param p the 16 bit pixel
   */
  void removePixel(int p) {
    population--;
    extractColorChannels(p);
    elements_red -= localRed;
    elements_green -= localGreen;
    elements_blue -= localBlue;
    updateCentroid();
  }

  /*****************************
   * centroid (distance to/update)
   *****************************/

  /**
   * find the distance between the centroid of the cluster and a given rgb pixel
   * use the euclidean distance function there is no y z so take only x for each
   * color chanel d(x2,x1) = sqrt(pow(x2-x1,2)) = x
   * 
   * @param color the given pixel
   * @return the distance between the average and
   */
  int distance(int color) {
    extractColorChannels(color);
    int r = Math.abs(centroid_red - localRed);
    int g = Math.abs(centroid_green - localGreen);
    int b = Math.abs(centroid_blue - localBlue);
    return (r + g + b) / 3;
  }

  /**
   * average of the Color channels of all elements of the cluster used in adding
   * or removing members
   */
  private void updateCentroid() {
    centroid_red = elements_red / population;
    centroid_green = elements_green / population;
    centroid_blue = elements_blue / population;
  }

  /*****************************
   * RGB<->R-G-B
   *****************************/

  /**
   * extract the three color channels from a given pixel and save it in the local
   * var the format is : RRRRRRRR GGGGGGGG BBBBBBBB (je 8 bit) 0x000000FF let only
   * first 9 bits
   * 
   * @param rgb
   */
  private void extractColorChannels(int rgb) {
    localRed = rgb >> 16 & 0x000000FF;
    localGreen = rgb >> 8 & 0x000000FF;
    localBlue = rgb & 0x000000FF;
  }

  /**
   * take the three color channels and convert them to a 16Bit pixel
   * 
   * @param r red
   * @param g green
   * @param b blue
   * @return
   */
  private int createPixel(int r, int g, int b) {
    return 0xff000000 | r << 16 | g << 8 | b;
  }

  /**
   * return the centroid (the average) as a pixel
   */
  int getRGB() {
    updateCentroid(); // just in case but dont do much diffrent -> can deleted later
    return createPixel(centroid_red, centroid_green, centroid_blue);
  }

}
