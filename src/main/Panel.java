package main;

import javafx.event.EventHandler;
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

/**
 * a Panel for each tile in the grid
 *
 */
public class Panel extends StackPane implements EventHandler<MouseEvent> {


    /**
     * class attributes
     */
    private int i;
    private int j;
    private boolean isBomb = false;
    private boolean isRevealed = false;
    private int isFlagged = 0;
    private Panel[][] grid;
    private int bombCount = 0;
    private ArrayList<Panel> neighbours = new ArrayList<>();
    private Game game;

    /**
     * constructor
     * @param i
     *      row
     * @param j
     *      col
     */
    public Panel(int i, int j) {
        super();
        this.i = i;
        this.j = j;
        this.setOnMouseClicked(this);
        this.setColour();

        //this.setMaxSize(getWidth(),getHeight());

    }

    /**
     *Handles click event on the panel, handles left click, right click, and double left click
     *
     * @param event
     *      event that calls the function
     */
    @Override
    public void handle(MouseEvent event) {
        if (!game.isDone()) {

            //Left click handler
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
                    //Double click handler
                    if(event.getClickCount() == 2) {
                        game.playSound(Constants.CLICK_SOUND);
                        revealNeighbours(true);
                    }
                }

            //Right Click handler
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

    /**
     * reveals the current tile, if the tile is blank will call revealNeighbours.
     * will end the game if the tile is a bomb
     */
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

    /**
     * will call the reveal function on the neighbours of the current tile. Avoiding a tile if it is a bomb, revealed, or flagged.
     *
     * @param notSafe
     *      if true the function will call reveal on bombs as well.
     */
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


    /**
     * will call the reveal function on the neighbours of the current tile. Avoiding a tile if it is a bomb, revealed, or flagged.
     */
    public void revealNeighbours() {

        revealNeighbours(false);
    }

    /**
     * will calculate the bomb count by checking the neighbours of the panel
     */
    public void calculateNeighbours() {

        this.bombCount = 0;

        if (!this.isBomb) {
            neighbours.stream().filter(Panel::isBomb).forEach(n -> bombCount++);
        }

        update();
    }

    /**
     * goes through the main grid and adds all of the neighbours that exist around the panel to the neighbours attribute
     */
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

    /**
     * updates the panel
     */
    public void update() {
        setColour();
        setText();
    }


    /**
     * sets the colour of the panel based on its state
     */
    public void setColour() {
        Color c;

        //removeImage(imageView);

        if (!this.isRevealed) {
            if (this.isFlagged == 1) {
                c = Color.YELLOW;
                //imageView = addImage(Constants.FLAG_IMG);
            } else if (this.isFlagged == 2) {
                c = Color.LIGHTGREEN;
                //imageView = addImage(Constants.QUESTION_IMG);
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
                //imageView = addImage(Constants.MINE_IMG);
            } else {
                c = Color.WHITE;
            }
        }

        this.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * sets the text of the tile to the bomb count of the panel, or to nothing if the panel is a bomb or is not revealed.
     */
    public void setText() {
        Text text = new Text((bombCount > 0 && this.isRevealed) ? String.valueOf(this.bombCount) : "");
        text.setFill(Constants.TEXT_COLOUR[bombCount]);
        this.getChildren().add(text);
        this.setAlignment(Pos.CENTER);
    }

    /**
     * determines whether or not the tile is a bomb
     * @return
     *      true if the tile is a bomb, false otherwise
     */
    public boolean isBomb() {
        return isBomb;
    }

    /**
     * sets the tile to a bomb or not
     * @param bomb
     *      true to set to a bomb, false otherwise.
     */
    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    /**
     * determines whether the panel is revealed
     * @return
     *      true if revealed, false otherwise.
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * determines whether the panel is flagged
     * @return
     *      true if flagged, false otherwise
     */
    public boolean isFlagged() {
        return isFlagged != 0;
    }

    /**
     * sets the grid attribute to a given grid
     * @param g
     *      a 2D array of panels that is the grid
     */
    public void setGrid(Panel[][] g) {
        this.grid = g;
    }

    /**
     * sets the game attribute to the given game object
     * @param game
     *      a game object
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * sets the panel to be revealed or not
     * @param revealed
     *      true to reveal, false otherwise
     */
    public void setIsRevealed(boolean revealed) {isRevealed = revealed;}

    /**
     * sets the panel to be flagged or not
     * @param flagged
     *      true to set flagged, false otherwise
     */
    public void setIsFlagged(boolean flagged) {isFlagged = (flagged) ? 1 : 0;}

    /**
     * adds an image to the panel by loading the image from a given url
     *
     * @param url
     *      the url path to the file
     * @return
     *      the imgView with the image added to it
     */
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

    /**
     * removes the Image from the panel
     *
     * @param iv
     *      the imageView to be removed
     */
    public void removeImage(ImageView iv) {
        this.getChildren().remove(iv);
    }



}
