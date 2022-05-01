import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    @FXML private javafx.scene.control.TextField input_;
    @FXML private Label p0,p1,p2,p3,p4,p5,p6,p7,p8;
    @FXML private ComboBox comb_meth;
    @FXML private Slider slid;

    ObservableList<String> listo = FXCollections.observableArrayList(
            "1-A* (Eucledian)",
            "2-A* (Manhattan)",
            "3-BFS",
            "4-DFS");
    int[][] tiles;
    private Label[] tiles_array;

    public void initialize(){
        comb_meth.setItems(listo);
        tiles_array = new Label[]{p0, p1, p2, p3, p4, p5, p6, p7, p8};
    }

    public void generate(MouseEvent event) {
        Scanner in = new Scanner(input_.getText());
        int n = 3,t=0,temp=0;
        tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp=in.nextInt();
                tiles[i][j] = temp;
                tiles_array[t].setText(String.valueOf(temp));
                t++;
            }
        }
    }
    private void render_puzzle(String state) {
        Scanner in =new Scanner(state);
        int t=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles_array[t].setText(String.valueOf(in.nextInt()));
                t++;
            }
        }
    }
    Solver solver = null;
    Iterable<Board> solution;
    public void SOLVE_NOW(MouseEvent event) {
        int method = ((int) comb_meth.getSelectionModel().getSelectedItem().toString().charAt(0)-48);
        System.out.println(method);
        Board initial = new Board(tiles);

        switch (method) {
            case 1:
                solver = new Solver(initial, SearchTechnique.EUCLIDEAN);
                break;
            case 2:
                solver = new Solver(initial, SearchTechnique.MANHATTAN);
                break;
            case 3:
                solver = new Solver(initial, SearchTechnique.BFS);
                break;
            case 4:
                solver = new Solver(initial, SearchTechnique.DFS);
                break;
        }
        assert solver != null;
        solver.solve();
        solution = solver.solution();
        curState=0;
        animateSteps();
        timeline.play();

    }
    List<Board> result;
    int curState=0;
    private Timeline timeline;
    private int speed = 500;
    public void animateSteps() {
        result = new ArrayList<Board>();
        for (Board board : solution) {
            result.add(board);
        }
        timeline = new Timeline(new KeyFrame(Duration.millis((speed)), event -> {
            System.out.println(result.get(curState).toArray());
            render_puzzle(result.get(curState).toArray());
            if(result.size()-1==curState)
                timeline.stop();
            else
                curState++;
        }));
        timeline.setCycleCount(timeline.INDEFINITE);
    }

    public void changeSpeed(MouseEvent event) {
        speed= (int) slid.getValue();
    }
}


