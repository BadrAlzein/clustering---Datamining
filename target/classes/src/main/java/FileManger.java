import java.awt.image.BufferedImage;
import java.io.File;
import javafx.scene.image.*;
import javax.imageio.ImageIO;

class FileManger {

  /** this will load Image the from the repo */
  public BufferedImage loadImage(String imageName) {
    BufferedImage loadedImage = null;
    try {
      loadedImage = ImageIO.read(new File(imageName));
    } catch (Exception e) {
      System.out.println("failed to load!");
    }
    return loadedImage;
  }

  /** this will save the image somewhere in the repo */
  public void save(String imageName, BufferedImage image) {

    File file = new File(imageName);
    try {
      ImageIO.write(image, "png", file);
    } catch (Exception e) {
      System.out.println("failed to save");
    }
  }

  // convert a buffered image to a normal image
  public Image convertToFxImage(BufferedImage image) {
    WritableImage wr = null;
    if (image != null) {
      wr = new WritableImage(image.getWidth(), image.getHeight());
      PixelWriter pw = wr.getPixelWriter();
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          pw.setArgb(x, y, image.getRGB(x, y));
        }
      }
    }
    return new ImageView(wr).getImage();
  }
}
