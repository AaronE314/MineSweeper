package main;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Panel extends StackPane implements EventHandler<MouseEvent> {


    private final Color[] TEXT_COLOUR = new Color[]{Color.BLACK, Color.BLUE, Color.rgb(21, 148, 8), Color.RED, Color.rgb(0, 0, 100), Color.rgb(165, 102, 21), Color.rgb(0, 185, 185), Color.BLACK, Color.GRAY,};

    private int i;
    private int j;
    private boolean isBomb = false;
    private boolean isRevealed = false;
    private boolean isFlagged = false;
    private Text text;
    private Panel[][] grid;
    private int bombCount = 0;
    private ArrayList<Panel> neighbours = new ArrayList<>();
    private Game game;

    public Panel(int i, int j) {
        super();
        this.i = i;
        this.j = j;
        this.setOnMouseClicked(this);
        this.setColour();
    }

    @Override
    public void handle(MouseEvent event) {
        //System.out.println("Pos: " + i + " " + j + " : Bomb: " + isBomb + " : BombCount: " + bombCount + " : Flagged: " + isFlagged);

        if (event.getButton() == MouseButton.PRIMARY) {
            if (isBomb) {
                if (game.getFirstClick()) {
                    game.resetBomb(this);
                    reveal();
                } else {
                    game.gameOver();
                }
            } else {
                reveal();
            }
            if (game.getFirstClick()) {
                game.setFirstClick(false);
            }
        } else if (event.getButton() == MouseButton.SECONDARY) {
            this.isFlagged = !this.isFlagged;
            update();

        }
    }

    public void reveal() {
        isRevealed = true;

        game.checkWin();

        if (bombCount == 0 && !isBomb) {
            revealNeighbours();
        }

        update();
    }
    public void revealNeighbours() {
        neighbours.stream().filter(neighbour -> !neighbour.isBomb() && !neighbour.isRevealed() && !neighbour.isFlagged()).forEach(Panel::reveal);
    }

    public void calculateNeighbours() {

        this.bombCount = 0;

        if (!this.isBomb) {
            neighbours.stream().filter(Panel::isBomb).forEach(n -> {
                bombCount++;
            });
        }

        update();
    }

    public void addNeighbours() {
        int startPosX = (this.i - 1 < 0) ? this.i : this.i - 1;
        int startPosY = (this.j - 1 < 0) ? this.j : this.j - 1;
        int endPosX = (this.i + 1 > grid.length - 1) ? this.i : this.i + 1;
        int endPosY = (this.j + 1 > grid[0].length - 1) ? this.j : this.j + 1;

        for (int rowNum = startPosX; rowNum <= endPosX; rowNum++) {
            for (int colNum = startPosY; colNum <= endPosY; colNum++) {
                if (grid[rowNum][colNum] != this) {
                    this.neighbours.add(grid[rowNum][colNum]);
                }
            }
        }
    }

    public void update() {
        setColour();
        setText();
    }


    public void setColour() {
        Color c;

        if (this.isFlagged) {
            c = Color.YELLOW;
        } else if (!this.isRevealed) {
            c = Color.LIGHTGRAY;
        } else if (this.isBomb) {
            c = Color.RED;
        } else {
            c = Color.WHITE;
        }

        this.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setText() {
        this.text = new Text((bombCount > 0 && this.isRevealed) ? String.valueOf(this.bombCount) : "");
        this.text.setFill(TEXT_COLOUR[bombCount]);
        this.getChildren().add(text);
        this.setAlignment(Pos.CENTER);
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setGrid(Panel[][] g) {
        this.grid = g;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    //    public void test() {
//        final Animation animation = new Transition() {
//
//            {
//                setCycleDuration(Duration.millis(1000));
//                setInterpolator(Interpolator.EASE_OUT);
//            }
//
//            @Override
//            protected void interpolate(double frac) {
//                Color vColor = new Color(1, 0, 0, 1 - frac);
//                .setBackgroxund(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
//            }
//        };
//        animation.play();
//    }


}
