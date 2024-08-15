import React, { useState } from "react";
import State from "../enums/State";
import "../Styles/Cell.css";

interface CellProps {
  value: number;
  isMine: boolean;
  x: number;
  y: number;
  state: State;
  updateState: Function;
}

const numberColours = [
  "black",
  "blue",
  "rgb(21, 148, 8)",
  "red",
  "rgb(0, 0, 100)",
  "rgb(165, 102, 21)",
  "rgb(0, 185, 185)",
  "black",
  "gray",
];

const Cell = ({
  value,
  isMine = false,
  x,
  y,
  state,
  updateState,
}: CellProps) => {
  const reveal = () => {
    updateState(x, y, State.REVEALED);
  };

  const flag = (e: React.MouseEvent<HTMLElement>) => {
    e.preventDefault();

    if (state === State.REVEALED) {
      return;
    }

    updateState(x, y, state === State.FLAGGED ? State.HIDDEN : State.FLAGGED);
  };

  const specialReveal = () => {
    if (state !== State.REVEALED || value === 0) {
      return;
    }

    updateState(x, y, State.REVEALED, false);
  };

  return (
    <div
      className={`cell ${state === State.REVEALED ? "revealed" : ""}`}
      style={{ color: numberColours[value || 0] }}
      onClick={reveal}
      onContextMenu={flag}
      onDoubleClick={specialReveal}
    >
      {/* // TODO: Clean this up */}
      {state === State.REVEALED && !isMine && value !== 0 && value}
      {state === State.REVEALED && isMine && (
        <img src={process.env.PUBLIC_URL + "/resources/img/mine.png"}></img>
      )}
      {state === State.FLAGGED && (
        <img src={process.env.PUBLIC_URL + "/resources/img/flag.png"}></img>
      )}
    </div>
  );
};

export default Cell;
