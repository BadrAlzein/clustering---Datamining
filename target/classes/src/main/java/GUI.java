import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import kmeans.*;
import dbscan.*;

public class GUI {
  /****** CLUSTERINGH PARAMETERS */
  Clustering custering;
  dbScanClustering dbScanClustering;
  int k = 30; // number of clusters K-means
  int minPts = 20; //min. points in the Neighborhood
  double eps = 2;  //radius

  //image
  String selecetedImageurl = "src/main/resources/image2.jpg";
  FileManger filemanger = new FileManger();
  BufferedImage temp_imageKmeans, temp_imageDBSCAN;

  // GUI
  StackPane root = new StackPane();
  double scalling = 1.5; // scalling the image and window (keep this under 5)
  HBox controllHBox;
  HBox imageHBox;
  VBox allVBox, kmeanBox, dbScanBox, orginalBox;
  Stage primaryStage;
  Image background, selectedImage, convertedImageKmeans, convertedImageDBSCAN, originalImage;
  ImageView backgroundImageView, selectedImageView, dbScanImageView, orginalImageView;
  Label statusLabel;
  Label kmeanLabel, dbScanLabel, orginalLabel;

 

  GUI(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Clustering");
    primaryStage.setScene(setupMainScene());
    // load the image using the filemanger and cluster it
    custering = new Clustering(filemanger.loadImage(selecetedImageurl));
    dbScanClustering = new dbScanClustering(minPts, eps, filemanger.loadImage(selecetedImageurl));
  }

  /**
   * this will set the components in the start screen
   * 
   * @return
   */
  private Scene setupMainScene() {
    try {
      selectedImage = new Image(new FileInputStream(selecetedImageurl)); // the image before converting

      statusLabel = new Label(
          "states: picture is ready to be converted \n Warning: the picture size is 100x100 then DBSCAN will take a while to finish (about 1.5-2 minutes)\n see consule to check progress");
    } catch (Exception e) {
      showErrorAlert("the image has failed to Load!");
      statusLabel = new Label("states: error");
    }
    selectedImageView = new ImageView(selectedImage);
    setupBackground();
    allVBox.getChildren().addAll(imageHBox, controllHBox);
    imageHBox.getChildren().addAll(selectedImageView);
    controllHBox.getChildren().addAll(statusLabel, convertButton(allVBox));
    root.getChildren().add(allVBox);
    return new Scene(root, (selectedImage.getWidth() + 350) * scalling, (selectedImage.getHeight() + 120) * scalling);
  }

  /**
   * this Methode will controll the Action of the convertButton
   * 
   * @param allVBox use this parameter to hide it in preperation for the next
   *                Scene
   * @return
   */
  private Button convertButton(VBox allVBox) {
    Button btn = new Button();
    btn.setText("convert");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        temp_imageKmeans = custering.convertImage(k);
        temp_imageDBSCAN = dbScanClustering.convertImage();
        convertedImageKmeans = filemanger.convertToFxImage(temp_imageKmeans);
        convertedImageDBSCAN = filemanger.convertToFxImage(temp_imageDBSCAN);
        root.getChildren().remove(allVBox);
        getResultsScene();
      }
    });
    return btn;
  }

  /** this will set the scene to show the results */
  private void getResultsScene() {
    // set images
    selectedImageView = new ImageView(convertedImageKmeans);
    dbScanImageView = new ImageView(convertedImageDBSCAN);
    dbScanImageView.setFitHeight(selectedImage.getHeight() * scalling);
    dbScanImageView.setFitWidth(selectedImage.getWidth() * scalling);

    setupBackground();

    // set labels
    orginalLabel = new Label("orginal image\n ");
    dbScanLabel = new Label("DBSCAN \n minPts = " + minPts + ", eps= " + eps);
    kmeanLabel = new Label("k-mean \n k= " + k);
    statusLabel = new Label("states: picture is converted");

    // add elements

    dbScanBox.getChildren().addAll(dbScanImageView, dbScanLabel);
    kmeanBox.getChildren().addAll(selectedImageView, kmeanLabel);
    orginalBox.getChildren().addAll(orginalImageView, orginalLabel);

    imageHBox.getChildren().addAll(orginalBox, kmeanBox, dbScanBox);
    controllHBox.getChildren().addAll(statusLabel, saveButton());
    allVBox.getChildren().addAll(imageHBox, controllHBox);
    root.getChildren().add(allVBox);
  }

  /**
   * this will controll the Actions of the saveButton
   * 
   * @return
   */
  private Button saveButton() {
    Button btn = new Button();
    btn.setText("save");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        showSaveAlert();
      }
    });
    return btn;
  }

  /** this will set up the background and the general layout */
  private void setupBackground() {
    // init

    try {
      background = new Image(new FileInputStream("src/main/resources/image.JPG")); // bg
      selectedImage = new Image(new FileInputStream(selecetedImageurl));
      originalImage = new Image(new FileInputStream(selecetedImageurl)); // the image before converting
    } catch (FileNotFoundException e) {
      showErrorAlert("background image has failed to Load!");
    }
    orginalImageView = new ImageView(originalImage);
    backgroundImageView = new ImageView(background);
    orginalBox = new VBox();
    dbScanBox = new VBox();
    kmeanBox = new VBox();
    controllHBox = new HBox(18); // 8
    imageHBox = new HBox(10); // 8 for spacing
    allVBox = new VBox(10);

    root.getChildren().add(backgroundImageView);
    // position & padding
    selectedImageView.setFitHeight(selectedImage.getHeight() * scalling);
    selectedImageView.setFitWidth(selectedImage.getWidth() * scalling);
    orginalImageView.setFitHeight(selectedImage.getHeight() * scalling);
    orginalImageView.setFitWidth(selectedImage.getWidth() * scalling);
    imageHBox.setAlignment(Pos.CENTER);
    kmeanBox.setAlignment(Pos.CENTER);
    dbScanBox.setAlignment(Pos.CENTER);
    orginalBox.setAlignment(Pos.CENTER);
    controllHBox.setAlignment(Pos.CENTER);
    allVBox.setPadding(new Insets(50, 50, 50, 50));
  }

  /** this will show an alert dialog to a given String */
  private void showErrorAlert(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("There is something wrong");
    alert.setContentText(message);
    alert.showAndWait();
  }

  /** this will save and image based on a alert dialog */
  private void showSaveAlert() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Save");
    alert.setContentText("Do you want to save the converted pictures?");
    alert.setContentText("you can find the saved image somewhere in the repo file");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      filemanger.save("kmeans", temp_imageKmeans);
      filemanger.save("dbscan", temp_imageDBSCAN);
    }
  }
}
