package com.dsav.datastructuresvisualised;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainApplication extends Application {
    public int DEFAULT_ARRAY_SIZE = 50;
    private GraphicsContext gc;
    private static int[] array;
    private int width;
    private static int height;

    private static void createArray(int size) {
        array = new int[size];
        for(int i = 0; i < size; i++) {
            array[i] = (int)(Math.random() * height);
        }
    }

    private void drawArray(int current) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
        for (int i = 0; i < array.length; i++) {
            int x = i * (width / array.length);
            int y = height - array[i];
            int w = width / array.length;
            int h = array[i];
            if (i == current) {
                gc.setFill(Color.RED);
            } else {
                gc.setFill(Color.BLACK);
            }
            gc.fillRect(x, y, w, h);
        }
    }

    private void bubbleSort(int[] array) {
        int n = array.length;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 0;
            boolean swapped = true;

            @Override
            public void run() {
                if (i < n - 1 && swapped) {
                    swapped = false;
                    for (int j = 0; j < n - i - 1; j++) {
                        if (array[j] > array[j + 1]) {
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                            swapped = true;
                            drawArray(j);
                        }
                    }
                    i++;
                } else {
                    timer.cancel();
                }
            }
        }, 0, 50);
    }

    @Override
    public void start(Stage primaryStage) {
        Label algorithmLabel = new Label("Selection:");
        ComboBox<String> algorithmComboBox = new ComboBox<>();
        algorithmComboBox.getItems().addAll("Searching", "Sorting", "Pathfinding");
        algorithmComboBox.setValue("Sorting");

        ComboBox<String> sortingComboBox = new ComboBox<>();
        sortingComboBox.getItems().addAll("Bubble Sort");

        ComboBox<String> searchingComboBox = new ComboBox<>();
        searchingComboBox.getItems().addAll("Binary Search");

        ComboBox<String> pathfindingComboBox = new ComboBox<>();
        pathfindingComboBox.getItems().addAll("A*");

        sortingComboBox.setVisible(true);
        searchingComboBox.setVisible(false);
        pathfindingComboBox.setVisible(false);

        VBox comboBoxes = new VBox(10, sortingComboBox, searchingComboBox, pathfindingComboBox);
        comboBoxes.setPadding(new Insets(10));

        algorithmComboBox.setOnAction(e -> {
            String selectedOption = algorithmComboBox.getValue();
            switch (selectedOption) {
                case "Sorting" -> {
                    sortingComboBox.setVisible(true);
                    searchingComboBox.setVisible(false);
                    pathfindingComboBox.setVisible(false);
                }
                case "Searching" -> {
                    searchingComboBox.setVisible(true);
                    sortingComboBox.setVisible(false);
                    pathfindingComboBox.setVisible(false);
                }
                case "Pathfinding" -> {
                    pathfindingComboBox.setVisible(true);
                    sortingComboBox.setVisible(false);
                    searchingComboBox.setVisible(false);
                }
                default -> {
                    sortingComboBox.setVisible(false);
                    searchingComboBox.setVisible(false);
                    pathfindingComboBox.setVisible(false);
                }
            }
        });

        Label sizeLabel = new Label("Size:");
        Slider sizeSlider = new Slider(10, 200, 50);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setMajorTickUnit(50);
        sizeSlider.setBlockIncrement(10);
        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            createArray(newValue.intValue());
            if(array != null) drawArray(sizeSlider.getMinorTickCount());
        });

        Canvas canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();

        HBox controls = new HBox(10, algorithmLabel, algorithmComboBox, sizeLabel, sizeSlider);
        controls.setPadding(new Insets(10));

        Button startButton = new Button("Start");

        HashMap<String, Runnable> sortingFunctions = new HashMap<>();
        sortingFunctions.put("Bubble Sort", () -> bubbleSort(array));

        startButton.setOnAction(e -> {
            String selectedAlgorithm = sortingComboBox.getValue();

            if (selectedAlgorithm != null) {
                Runnable sortingFunction = sortingFunctions.get(selectedAlgorithm);
                if (sortingFunction != null) {
                    sortingFunction.run();
                }
            }
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            createArray(DEFAULT_ARRAY_SIZE);
            drawArray(sizeSlider.getMinorTickCount());
        });

        VBox vbox = new VBox(10, controls, startButton, resetButton, comboBoxes);
        vbox.setPadding(new Insets(10));

        BorderPane root = new BorderPane(canvas);
        root.setBottom(vbox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sorting Visualizer");
        primaryStage.show();

        createArray((int)sizeSlider.getValue());
        drawArray(sizeSlider.getMinorTickCount());
    }

    public static void main(String[] args) {
        launch(args);
    }
}