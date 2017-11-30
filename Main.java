package sample;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{

    //declaration of the instance variables
    BorderPane layout;
    GridPane gridLayout;
    Button spin,addCoin,betOne,betMax,statistics,resetBet;
    ImageView imgOne,imgTwo,imgThree;
    Label imageOneLabel,imageTwoLabel,imageThreeLabel,creditArea,betArea,statArea;

    //reel object from default constructor
    Reel spinReel = new Reel();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = null;

        //Grid Pane layout
        gridLayout = new GridPane();
        gridLayout.setPadding(new Insets(10,10,10,10));
        gridLayout.setVgap(8);
        gridLayout.setHgap(10);
        gridLayout.setVisible(true);

        //Buttons
        spin = new Button("SPIN");
        spin.setId("function");
        spin.setOnAction(event -> functions(imageOneLabel,imageTwoLabel,imageThreeLabel,primaryStage));
        addCoin = new Button(" ADD COIN ");
        addCoin.setId("function");
        addCoin.setOnAction(event -> spinReel.addCredit(creditArea));
        betOne = new Button("BET ONE");
        betOne.setOnAction(event -> spinReel.bet(betArea,primaryStage));
        betOne.setId("function");
        betMax = new Button("BET MAX");
        betMax.setId("function");
        betMax.setOnAction(event -> spinReel.betMax(betArea,primaryStage));
        statistics = new Button("STATISTICS");
        statistics.setId("function");
        statistics.setOnAction(event -> spinReel.statistics(primaryStage));
        resetBet = new Button("RESET BET");
        resetBet.setId("function");
        resetBet.setOnAction(event -> spinReel.resetBet(betArea));

        //adding and aligning the buttons in the layout
        GridPane.setConstraints(spin,24,  20,1,1);
        GridPane.setHalignment(spin, HPos.CENTER);
        GridPane.setConstraints(addCoin,25,  20);
        GridPane.setHalignment(addCoin, HPos.CENTER);
        GridPane.setConstraints(statistics,24,28);
        GridPane.setHalignment(statistics, HPos.CENTER);
        GridPane.setConstraints(betOne,26,20);
        GridPane.setHalignment(betOne, HPos.CENTER);
        GridPane.setConstraints(betMax,25,28);
        GridPane.setHalignment(betMax, HPos.CENTER);
        GridPane.setConstraints(resetBet,26,28);
        GridPane.setHalignment(resetBet, HPos.CENTER);
        gridLayout.getChildren().addAll(spin,addCoin,statistics,betOne,betMax,resetBet);

        //Setting the images
        imgOne = new ImageView(new Image(getClass().getResourceAsStream("res/chips.png")));
        imgOne.setFitHeight(300);
        imgOne.setFitWidth(300);
        imgOne.setPreserveRatio(true);
        imageOneLabel = new Label();
        imageOneLabel.setId("reels");
        imageOneLabel.setGraphic(imgOne);
        GridPane.setConstraints(imageOneLabel,24,3);

        imgTwo = new ImageView(new Image(getClass().getResourceAsStream("res/chips.png")));
        imgTwo.setFitHeight(300);
        imgTwo.setFitWidth(300);
        imgTwo.setPreserveRatio(true);
        imageTwoLabel = new Label();
        imageTwoLabel.setId("reels");
        imageTwoLabel.setGraphic(imgTwo);
        GridPane.setConstraints(imageTwoLabel,25,3);

        imgThree = new ImageView(new Image(getClass().getResourceAsStream("res/chips.png")));
        imgThree.setFitHeight(300);
        imgThree.setFitWidth(300);
        imgThree.setPreserveRatio(true);
        imageThreeLabel = new Label();
        imageThreeLabel.setId("reels");
        imageThreeLabel.setGraphic(imgThree);
        GridPane.setConstraints(imageThreeLabel,26,3);

        //adding event handlers to stop spinining the reels by clicking on the reels
        imageOneLabel.setOnMouseClicked(event -> {
            boolean ss = false;

            try {
                spinReel.stopSpinReel1();
                spinReel.matchReels(primaryStage,imageOneLabel,imageTwoLabel,imageThreeLabel,creditArea,betArea,statArea);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        imageTwoLabel.setOnMouseClicked(event -> {
            try {
                spinReel.stopspinReel2();
                spinReel.matchReels(primaryStage,imageOneLabel,imageTwoLabel,imageThreeLabel,creditArea,betArea,statArea);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        imageThreeLabel.setOnMouseClicked(event -> {
            try {
                spinReel.stopspinReel3();
                spinReel.matchReels(primaryStage,imageOneLabel,imageTwoLabel,imageThreeLabel,creditArea,betArea,statArea);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //adding image views to the grid layout
        gridLayout.getChildren().addAll(imageOneLabel,imageTwoLabel,imageThreeLabel);

        //Credit Area
        creditArea = new Label("Credit Amount : "+10);
        creditArea.setId("textArea");
        creditArea.setMaxSize(300,400);
        GridPane.setConstraints(creditArea,24,38,1,10);
        gridLayout.getChildren().add(creditArea);

        //Bet Area
        betArea = new Label("Bet Area");
        betArea.setId("textArea");
        betArea.setMaxSize(300,400);
        GridPane.setConstraints(betArea,26,38,1,10);
        gridLayout.getChildren().add(betArea);

        //Statistics area
        statArea = new Label("Stat Area");
        statArea.setId("textArea");
        statArea.setMaxSize(300,400);
        GridPane.setConstraints(statArea,25,38,1,10);
        gridLayout.getChildren().add(statArea);

        //setting the layout
        layout = new BorderPane();
        layout.setId("layout");
        layout.setCenter(gridLayout);

        //basic layout of the gui
        StackPane pane = new StackPane();
        pane.getChildren().add(layout);

        //primary scene of the gui
        Scene scene = new Scene(pane);
        //connecting the css file to the javafc code
        scene.getStylesheets().add(getClass().getResource("alphx.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Slot Machine v1.1.0");
        primaryStage.show();

    }


    public void functions(Label imageOneLabel,Label imageTwoLabel,Label imageThreeLabel,Stage stage){

        imageOneLabel.setId("reels");
        imageTwoLabel.setId("reels");
        imageThreeLabel.setId("reels");

        spinReel.spin(imgOne,imgTwo,imgThree,creditArea,stage);
    }


    //main method
    public static void main(String[] args) {
        launch(args);
    }
}
