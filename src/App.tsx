import React, { useState } from "react";
import uuid from "react-uuid";
import Board from "./Components/Board";
import Controls from "./Components/Controls";
import "./App.css";
import GameState from "./enums/GameState";

// TODO: Add difficulty Setting
// TODO: Add ability to start a new game
// TODO: Add UI in general, timer, bomb count, settings
// TODO: Update Readme

const App = () => {
  const [boardX, setBoardX] = useState<number>(10);
  const [boardY, setBoardY] = useState<number>(10);
  const [mines, setMines] = useState<number>(10);
  const [gameID, setGameID] = useState<string>(uuid());
  const [gameState, setGameState] = useState<GameState>(GameState.RUNNING);

  const newGame = (boardX: number, boardY: number, mines: number) => {
    console.log(
      `Starting new game with a ${boardX}x${boardY} board with ${mines} mines`
    );
    setBoardX(boardX);
    setBoardY(boardY);
    setMines(mines);
    setGameID(uuid());
    setGameState(GameState.RUNNING);
  };

  return (
    <div className="App">
      <Board
        boardX={boardX}
        boardY={boardY}
        numBombs={mines}
        endGame={(won: boolean) => {
          console.log(`Game Over you ${won ? "won!" : "lost :("}`);
          setGameState(won ? GameState.WIN : GameState.LOSS);
        }}
        gameId={gameID}
        gameState={gameState}
      />
      <Controls newGame={newGame} />
    </div>
  );
};

export default App;
