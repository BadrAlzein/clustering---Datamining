package dbscan;

public class DensityPoint {

    int pixel;
    int x, y; // position
    int red, green, blue;
    boolean isVisited, isNoise, isUndefinied;
    int id;

    public DensityPoint(int pixel, int x, int y) {
        extractColorChannels(pixel);
        this.pixel = pixel;
        this.isUndefinied = true; // at the beginning all points are undefined
        this.x = x;
        this.y = y;
    }

    /**
     * extract the three color channels from a given pixel and save it in the local
     * var the format is : RRRRRRRR GGGGGGGG BBBBBBBB (je 8 bit) 0x000000FF let only
     * first 9 bits
     * 
     * @param rgb
     */
    public void extractColorChannels(int rgb) {
        red = rgb >> 16 & 0x000000FF;
        green = rgb >> 8 & 0x000000FF;
        blue = rgb & 0x000000FF;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public boolean getVisited() {
        return this.isVisited;
    }

    public void setIsUndefinied(boolean isUndefinied) {
        this.isUndefinied = isUndefinied;
    }

    public boolean getIsUndefinied() {
        return this.isUndefinied;
    }

    public void setIsNois(boolean isNoise) {
        this.isNoise = isNoise;
    }

    public boolean getIsNois() {
        return this.isNoise;
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.blue;
    }

    public int getBlue() {
        return this.blue;
    }

    public int getpixel() {
        return this.pixel;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id=id;
    }

}
