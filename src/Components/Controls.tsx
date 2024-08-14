import React, { useState } from "react";

interface Props {
  newGame: Function;
}

const Controls = ({ newGame }: Props) => {
  const [x, setX] = useState<number>(10);
  const [y, setY] = useState<number>(10);
  const [mines, setMines] = useState<number>(10);

  return (
    <div>
      Width:
      <input type="number" value={x} onChange={(e) => setX(+e.target.value)} />
      Height:
      <input type="number" value={y} onChange={(e) => setY(+e.target.value)} />
      Number of Mines:
      <input
        type="number"
        value={mines}
        onChange={(e) => setMines(+e.target.value)}
      />
      <button onClick={() => newGame(x, y, mines)}>New Game</button>
    </div>
  );
};

export default Controls;
