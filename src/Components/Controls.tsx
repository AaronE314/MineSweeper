import React, { useState } from "react";
import "../Styles/Controls.css";

interface Props {
  newGame: Function;
}

const Controls = ({ newGame }: Props) => {
  const [x, setX] = useState<number>(10);
  const [y, setY] = useState<number>(10);
  const [mines, setMines] = useState<number>(10);

  return (
    <div>
      <span>
        Width:
        <input
          type="number"
          value={x}
          onChange={(e) => setX(+e.target.value)}
        />
      </span>
      <span>
        Height:
        <input
          type="number"
          value={y}
          onChange={(e) => setY(+e.target.value)}
        />
      </span>
      <span>
        Number of Mines:
        <input
          type="number"
          value={mines}
          onChange={(e) => setMines(+e.target.value)}
        />
      </span>
      <button onClick={() => newGame(x, y, mines)}>New Game</button>
    </div>
  );
};

export default Controls;
