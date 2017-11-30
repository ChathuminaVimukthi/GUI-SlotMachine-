package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static javafx.scene.media.AudioClip.INDEFINITE;

public class Reel implements Runnable {

    //array to store symbol pobjects
    volatile Symbol[] symbolArr = new Symbol[6];

    //static and instance variable declaration
    volatile ImageView view;
    Thread t1,t2,t3;
    static boolean spinning = false;
    AudioClip audio;
    static volatile int index;
    Reel r1,r2,r3;
    volatile int position;

    static int valueOfReel1,valueOfReel2,valueOfReel3;
    static double initialCredit = 10;
    static int winCount, lostCount,totalChances;
    static double nettedCredits,totalCredits;
    static int reelCheck=0;
    static double coinAverage=0;
    static int betmaxCount =0;
    double betAmount = 0;
    double creditAfterBet = 0;
    Stage stage = new Stage();

    //creating six symbol objects relevant to six images
    Symbol imageOne = new Symbol("res/bell.png", 6);
    Symbol imageTwo = new Symbol("res/cherry.png", 2);
    Symbol imageThree = new Symbol("res/lemon.png", 3);
    Symbol imageFour = new Symbol("res/plum.png", 4);
    Symbol imageFive = new Symbol("res/redseven.png", 7);
    Symbol imageSix = new Symbol("res/watermelon.png", 5);

    //default constructor
    public Reel() {

    }
    //overloaded constructor
    public Reel(ImageView view) {
        this.view = view;
        //populating the array with symbol objects
        symbolArr[0] = imageOne;
        symbolArr[1] = imageTwo;
        symbolArr[2] = imageThree;
        symbolArr[3] = imageFour;
        symbolArr[4] = imageFive;
        symbolArr[5] = imageSix;
        shuffleArray(symbolArr);
    }

    //array randomizing method
    public void shuffleArray(Symbol[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            //swap
            Symbol a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    //overidden run method for multithreading
    @Override
    public void run() {
        position = 0;

        //spin images in current ImageView until images are pressed again.
        try {
        while (spinning && !Thread.currentThread().isInterrupted()) {
            Thread.sleep(150);
            synchronized (Reel.class) {
                view.setImage(new Image(getClass().getResourceAsStream(symbolArr[position].getImage())));
                index = position;
                position = (position + 1) % symbolArr.length;
            }
        }
        } catch (InterruptedException e) {

        }
    }

    //spin method where the threads initialized
    public void spin(ImageView reel1,ImageView reel2,ImageView reel3,Label l1,Stage stage){

        if(betAmount==0) {
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Sorry");
            winAlert.setHeaderText("Sorry!! You need to bet some credit");
            winAlert.setContentText("Bet some credit first and then try to earn");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
        }else
        if(initialCredit==0){
            Alert cantplay = new Alert(Alert.AlertType.WARNING);
            cantplay.setTitle("Sorry");
            cantplay.setHeaderText("Seems like you are out of credit");
            cantplay.setContentText("Please recgarge the credits and try again..Thankyou!!");
            cantplay.setHeight(200);
            cantplay.setWidth(400);
            cantplay.initOwner(stage);
            cantplay.setGraphic(null);
            cantplay.show();
        }else{
            spinning = true;

            //Create 3 threads to spin images in 3 different Reels
            r1 = new Reel(reel1);
            t1 = new Thread(r1);
            t1.start();

            r2 = new Reel(reel2);
            t2 = new Thread(r2);
            t2.start();

            r3 = new Reel(reel3);
            t3 = new Thread(r3);
            t3.start();

            //reel spinning audio sound
            audio = new AudioClip(getClass().getResource("res/slotMachineSpin.mp3").toExternalForm());
            int s = INDEFINITE;
            audio.setVolume(0.5f);
            audio.setCycleCount(s);
            audio.play();

            creditAfterBet = initialCredit-betAmount;
            l1.setText("Credit Amount : "+creditAfterBet);
        }


    }
    //three methods to stop each reel by clicking on each of them
    public void stopSpinReel1() throws InterruptedException {
        t1.interrupt();
        valueOfReel1 = r1.symbolArr[index].getValue();
        reelCheck++;
        System.out.println(r1.symbolArr[index].getValue());

    }

    public void stopspinReel2() throws InterruptedException {
        t2.interrupt();
        valueOfReel2 = r2.symbolArr[index].getValue();
        reelCheck++;
        System.out.println(r2.symbolArr[index].getValue());

    }

    public void stopspinReel3() throws InterruptedException{
        t3.interrupt();
        valueOfReel3 = r3.symbolArr[index].getValue();
        reelCheck++;
        System.out.println(r3.symbolArr[index].getValue());


    }

    //method to reset bet amount and stop spinning sound
    public void matchReels(Stage stage, Label l1,Label l2,Label l3,Label l4,Label l5,Label l6){
        if(reelCheck==3){
            checkReels(stage,l1,l2,l3,l4,l6);
            resetBet(l5);
            stopspinSound();
            reelCheck =0;
        }
    }

    public void stopspinSound() {
        audio.stop();
    }

    //method to compare three reels and calculate the credit amount won or lost
    public void checkReels(Stage stage, Label l1,Label l2,Label l3,Label l4,Label l6) {
        int credit =0;

        if(valueOfReel1==valueOfReel2 && valueOfReel2==valueOfReel3 ){
            l1.getStylesheets().add(getClass().getResource("alphx.css").toExternalForm());
            l1.setId("l1");
            l2.setId("l1");
            l3.setId("l1");
            credit = (int) (valueOfReel1*betAmount);
            nettedCredits += credit;
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("JACKPOT");
            winAlert.setHeaderText("Congratz!! You won the JACKPOT");
            winAlert.setContentText("Your have won "+credit+" credits");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
            initialCredit = creditAfterBet+credit;
            l4.setText("Credit Amount : "+(creditAfterBet+credit));
            winCount++;
            totalChances++;
        }

        if(valueOfReel2==valueOfReel3 && valueOfReel1!= valueOfReel2){
            l2.getStylesheets().add(getClass().getResource("alphx.css").toExternalForm());
            l2.setId("l2");
            l3.setId("l2");
            credit = (int) (valueOfReel2*betAmount);
            nettedCredits += credit;
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Congratulations");
            winAlert.setHeaderText("Congratz!! You won the bet");
            winAlert.setContentText("Your have won "+credit+" credits");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
            initialCredit = creditAfterBet+credit;
            l4.setText("Credit Amount : "+(creditAfterBet+credit));
            winCount++;
            totalChances++;
        }

        if(valueOfReel1==valueOfReel2 && valueOfReel1!=valueOfReel3){
            l1.getStylesheets().add(getClass().getResource("alphx.css").toExternalForm());
            l1.setId("l1");
            l2.setId("l1");
            credit = (int) (valueOfReel1*betAmount);
            nettedCredits += credit;
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Congratulations");
            winAlert.setHeaderText("Congratz!! You won the bet");
            winAlert.setContentText("Your have won "+credit+" credits");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
            initialCredit = creditAfterBet+credit;
            l4.setText("Credit Amount : "+(creditAfterBet+credit));
            winCount++;
            totalChances++;
        }

        if(valueOfReel1==valueOfReel3 && valueOfReel1!=valueOfReel2){
            l1.getStylesheets().add(getClass().getResource("alphx.css").toExternalForm());
            l1.setId("l1");
            l3.setId("l1");
            credit = (int) (valueOfReel1*betAmount);
            nettedCredits += credit;
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Congratulations");
            winAlert.setHeaderText("Congratz!! You won the bet");
            winAlert.setContentText("Your have won "+credit+" credits");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
            initialCredit = creditAfterBet+credit;
            l4.setText("Credit Amount : "+(creditAfterBet+credit));
            winCount++;
            totalChances++;
        }

        if(valueOfReel1!=valueOfReel2 && valueOfReel2!=valueOfReel3 && valueOfReel1!=valueOfReel3){
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Sorry");
            winAlert.setHeaderText("Sorry!! You lost the bet");
            winAlert.setContentText("Your have lost "+betAmount+" credits");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
            initialCredit = creditAfterBet;
            l4.setText("Credit Amount : "+creditAfterBet);
            lostCount++;
            totalChances++;
        }

        if(initialCredit==0){
            Alert winAlert = new Alert(Alert.AlertType.WARNING);
            winAlert.setTitle("Sorry");
            winAlert.setHeaderText("Seems like you are out of credit");
            winAlert.setContentText("Please recgarge the credits and try again..Thankyou!!");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
        }

        l6.setText("Total Wins : "+ winCount +"\nTotal Losts : "+ lostCount);
        totalCredits = initialCredit;

    }

    //method to bet function
    public void bet(Label l1,Stage stage){

        if(betAmount<initialCredit) {
            betAmount++;
            l1.setText("Bet Amount : "+ betAmount);
        }else{
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Sorry");
            winAlert.setHeaderText("Maximum bet amount exceeded");
            winAlert.setContentText("You can bet only up to your current amont of credits at maximum");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
        }
    }

    //method for bet max function
    public void betMax(Label l1,Stage stage){
        if(betmaxCount ==0){
            betAmount=3;
            l1.setText("Bet Amount : "+ betAmount);
            betmaxCount++;
        }else{
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Sorry");
            winAlert.setHeaderText("You have already used betmax option");
            winAlert.setContentText("Bet Max can be used only once in a game play");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
        }
        if(initialCredit<3){
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Sorry");
            winAlert.setHeaderText("You don't have enough credits");
            winAlert.setContentText("Your credit amount is low.. Please try BET ONE function");
            winAlert.setHeight(200);
            winAlert.setWidth(400);
            winAlert.initOwner(stage);
            winAlert.setGraphic(null);
            winAlert.show();
        }
    }

    //method to reset bet label
    public void resetBet(Label l1){
        betAmount=0;
        l1.setText("Bet Amount : "+ betAmount);
    }

    //method to create the piechart and other statistics
    public void statistics(Stage stage2){
        if(totalChances>0){
            coinAverage = (nettedCredits)/totalChances;
            DecimalFormat df = new DecimalFormat("#.##");
            df.format(coinAverage);
            //Pie Chart
            PieChart pieChart = new PieChart();
            pieChart.setData(getChartData());
            Label lbl = new Label("You have won : "+winCount+" games \nYou have lost : "+lostCount+" games"+
                                    "\nYou have netted "+coinAverage+" credits per game \nYou have earned "+nettedCredits+" credits in all your plays");
            GridPane pane = new GridPane();
            pane.setPadding(new Insets(10,10,10,10));
            GridPane.setConstraints(pieChart,1,1);
            GridPane.setConstraints(lbl,1,2);
            Button button = new Button("SAVE STATS");
            button.setOnAction(event -> addPlayertoFile(stage));
            GridPane.setConstraints(button,1,3);
            pane.getChildren().addAll(pieChart,lbl,button);
            Scene scene = new Scene(pane,600,600);
            stage.setScene(scene);
            stage.resizableProperty().set(false);
            stage.setFullScreen(false);
            stage.show();

        }else{
            Alert al = new Alert(Alert.AlertType.WARNING);
            al.setTitle("Sorry");
            al.setHeaderText("No statistics to view");
            al.setContentText("Play to view Statistics");
            al.setHeight(300);
            al.setWidth(400);
            al.initOwner(stage2);
            al.setGraphic(null);
            al.show();
        }

    }

    //add credit method to add credits when needed
    public void addCredit(Label l1){
        initialCredit ++;
        l1.setText("Bet Amount : "+ initialCredit);
    }

    //piechart function
    private ObservableList<Data> getChartData() {
        ObservableList<Data> answer = FXCollections.observableArrayList();
        answer.addAll(new PieChart.Data("Wins", winCount),
                new PieChart.Data("Loses", lostCount));
        return answer;
    }

    //method to save satistics to a folder
    public void addPlayertoFile(Stage stage){
        String date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        File file = new File(date);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        printWriter.println("**********  Your statistics  *********");
        printWriter.println("You have won : "+winCount+" games \nYou have lost : "+lostCount+" games"+
        "\nYou have netted "+coinAverage+" credits per game \nYou have earned "+nettedCredits+" credits in all your plays");
        printWriter.close();

        Alert al = new Alert(Alert.AlertType.WARNING);
        al.setTitle("Data saved");
        al.setHeaderText("Statistics saved into the file "+date);
        al.setHeight(300);
        al.setWidth(400);
        al.initOwner(stage);
        al.setGraphic(null);
        al.show();

    }

}
