package main;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private int isFlagged = 0;
    private Text text;
    private Panel[][] grid;
    private int bombCount = 0;
    private ArrayList<Panel> neighbours = new ArrayList<>();
    private Game game;
    private ImageView imageView = null;

    public Panel(int i, int j) {
        super();
        this.i = i;
        this.j = j;
        this.setOnMouseClicked(this);
        this.setColour();

        //this.setMaxSize(getWidth(),getHeight());

    }

    @Override
    public void handle(MouseEvent event) {
        if (!game.isDone()) {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!game.isGameInProgress()) {
                   game.setGameInProgress(true);
                }
                if (!isRevealed && !isFlagged()) {
                    if (isBomb) {
                        if (game.getFirstClick()) {
                            game.resetBomb(this);
                            game.playSound(Constants.CLICK_SOUND);
                            reveal();
                        } else {
                            game.setHitBomb(true);
                            game.playSound(Constants.LONG_BOMB_SOUND);
                            game.gameOver();
                        }
                    } else {
                        game.playSound(Constants.CLICK_SOUND);
                        reveal();
                    }
                    if (game.getFirstClick()) {
                        game.setFirstClick(false);
                    }
                } else {
                    if(event.getClickCount() == 2) {
                        game.playSound(Constants.CLICK_SOUND);
                        revealNeighbours(true);
                    }
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (!this.isRevealed) {

                    this.isFlagged ++;
                    if (game.getQuestion()) {
                        switch (this.isFlagged) {
                            case 1:
                                game.lowerBombCount(-1);
                                break;
                            case 2:
                                game.lowerBombCount(1);
                                break;
                            case 3:
                                this.isFlagged = 0;
                                break;
                        }
                    } else {
                        switch (this.isFlagged) {
                            case 1:
                                game.lowerBombCount(-1);
                                break;
                            case 2:
                                game.lowerBombCount(1);
                                this.isFlagged = 0;
                                break;
                        }
                    }
                    update();
                }

            }
        }
    }

    public void reveal() {
        isRevealed = true;


        if (!game.getHitBomb()) {
            if (this.isBomb) {
                game.setHitBomb(true);
                game.gameOver();
            }
        }
        game.checkWin();

        if (bombCount == 0 && !isBomb) {
            revealNeighbours();
        }

        update();
    }

    public void revealNeighbours(boolean notSafe) {
        if(!notSafe) {
            neighbours.stream().filter(neighbour -> (!neighbour.isBomb()) && !neighbour.isRevealed() && !neighbour.isFlagged()).forEach(Panel::reveal);
        } else {

            int flagCount = 0;
            for (Panel neighbour: neighbours){
                if (neighbour.isFlagged()) {
                    flagCount++;
                }
            }

            if(flagCount == this.bombCount) {
                neighbours.stream().filter(neighbour -> !neighbour.isRevealed() && !neighbour.isFlagged()).forEach(Panel::reveal);
            }
        }
    }

    public void revealNeighbours() {

        revealNeighbours(false);
    }

    public void calculateNeighbours() {

        this.bombCount = 0;

        if (!this.isBomb) {
            neighbours.stream().filter(Panel::isBomb).forEach(n -> bombCount++);
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

        removeImage(imageView);

        if (!this.isRevealed) {
            if (this.isFlagged == 1) {
                c = Color.YELLOW;
                imageView = addImage(Constants.FLAG_IMG);
            } else if (this.isFlagged == 2) {
                c = Color.LIGHTGREEN;
                imageView = addImage(Constants.QUESTION_IMG);
            } else {
                if (game != null) {
                    c = game.getBackColor();
                } else {
                    c=Color.LIGHTGRAY;
                }
            }
        } else {
            if (this.isBomb) {
                c = Color.RED;
                imageView = addImage(Constants.MINE_IMG);
            } else {
                c = Color.WHITE;
            }
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
        return isFlagged != 0;
    }

    public void setGrid(Panel[][] g) {
        this.grid = g;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setIsRevealed(boolean revealed) {isRevealed = revealed;}

    public void setIsFlagged(boolean flagged) {isFlagged = (flagged) ? 1 : 0;}

    public ImageView addImage(final String url) {
        Image img = new Image(url);
        ImageView imgView = new ImageView(img);
        //imgView.fitWidthProperty().bind(this.widthProperty().divide(2));
        //imgView.fitHeightProperty().bind(this.heightProperty().divide(2));
        imgView.setFitWidth(getWidth());
        imgView.setFitHeight(getHeight());
        this.getChildren().add(imgView);

        return imgView;
    }

    public void removeImage(ImageView iv) {
        this.getChildren().remove(iv);
        imageView = null;
    }



}
