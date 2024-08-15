import React, { useEffect, useState } from "react";
import Cell from "./Cell";
import State from "../enums/State";
import GameState from "../enums/GameState";

interface CellData {
  isMine: boolean;
  value: number;
  x: number;
  y: number;
  state: State;
}

interface Props {
  boardX: number;
  boardY: number;
  numBombs: number;
  endGame: Function;
  gameId: string;
  gameState: GameState;
}

const Board = ({
  boardX,
  boardY,
  numBombs,
  endGame,
  gameId,
  gameState,
}: Props) => {
  const [board, setBoard] = useState<CellData[][]>([]);

  const generateBoard = () => {
    const newBoard: CellData[][] = [];

    // Generates empty board
    for (let x = 0; x < boardX; x++) {
      const cellRow: CellData[] = [];
      for (let y = 0; y < boardY; y++) {
        cellRow.push({ isMine: false, value: 0, x, y, state: State.HIDDEN });
      }
      newBoard.push(cellRow);
    }

    // Add Bombs
    for (let i = 0; i < numBombs; i++) {
      let x = Math.floor(Math.random() * boardX);
      let y = Math.floor(Math.random() * boardY);

      while (newBoard[x][y].isMine) {
        x = Math.floor(Math.random() * boardX);
        y = Math.floor(Math.random() * boardY);
      }

      newBoard[x][y].isMine = true;
    }

    // Add Numbers
    for (let i = 0; i < boardX; i++) {
      for (let j = 0; j < boardY; j++) {
        let mineCount = 0;
        for (let n1 = -1; n1 < 2; n1++) {
          for (let n2 = -1; n2 < 2; n2++) {
            if (newBoard?.[i + n1]?.[j + n2]?.isMine) {
              mineCount += 1;
            }
          }
        }
        newBoard[i][j].value = mineCount;
      }
    }

    setBoard(newBoard);
  };

  useEffect(() => {
    generateBoard();
  }, [boardX, boardY, numBombs, gameId]);

  const checkWinOrLoss = (x: number, y: number, newState: State): boolean => {
    // Check Loss
    if (newState === State.REVEALED && board[x][y].isMine) {
      endGame(false);

      let newBoard = [...board];
      for (let i = 0; i < boardX; i++) {
        for (let j = 0; j < boardY; j++) {
          if (newBoard[i][j].isMine) {
            newBoard[i][j].state = State.REVEALED;
          }
        }
      }
      setBoard(newBoard);

      return true;
    }

    // Check win
    for (let i = 0; i < boardX; i++) {
      for (let j = 0; j < boardY; j++) {
        if (board[i][j].state !== State.REVEALED && !board[i][j].isMine) {
          return false;
        }
      }
    }

    endGame(true);

    return true;
  };

  const updateState = (
    x: number,
    y: number,
    newState: State,
    safe: boolean = true
  ) => {
    // TODO: Play sound

    if (gameState !== GameState.RUNNING) {
      return;
    }

    if (checkWinOrLoss(x, y, newState)) {
      return;
    }

    if (newState === State.REVEALED) {
      updateRevealed(x, y, safe);
      return;
    }

    let newBoard = [...board];

    const cell = newBoard[x][y];
    cell.state = newState;

    setBoard(newBoard);
  };

  const updateRevealed = (x: number, y: number, safe: boolean = true) => {
    let newBoard = [...board];

    const cell = newBoard[x][y];
    cell.state = State.REVEALED;

    if (checkWinOrLoss(x, y, State.REVEALED)) {
      return;
    }

    if (!safe || (cell.value === 0 && !cell.isMine)) {
      if (!safe) {
        let flagCount = 0;
        for (let n1 = -1; n1 < 2; n1++) {
          for (let n2 = -1; n2 < 2; n2++) {
            if (newBoard?.[x + n1]?.[y + n2]?.state === State.FLAGGED) {
              flagCount += 1;
            }
          }
        }

        if (flagCount !== cell.value) {
          return;
        }
      }

      for (let n1 = -1; n1 < 2; n1++) {
        for (let n2 = -1; n2 < 2; n2++) {
          if (n1 === 0 && n2 === 0) {
            continue;
          }
          const neighbourCell = newBoard?.[x + n1]?.[y + n2];

          if (!neighbourCell) {
            continue;
          }

          if (
            neighbourCell.state === State.FLAGGED ||
            neighbourCell.state === State.REVEALED ||
            (neighbourCell.isMine && safe)
          ) {
            continue;
          }

          updateRevealed(x + n1, y + n2, safe);
        }
      }
    }

    setBoard(newBoard);
  };

  return (
    <div className="board">
      {board.map((row, x) => {
        return (
          <div key={x} style={{ display: "flex", flexDirection: "row" }}>
            {row.map((cell, y) => {
              return (
                <Cell {...cell} key={`${x},${y}`} updateState={updateState} />
              );
            })}
          </div>
        );
      })}
    </div>
  );
};

export default Board;
