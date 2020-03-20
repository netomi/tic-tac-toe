/*
 * Copyright (c) 2020 Thomas Neidhart
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.netomi.tictactoe.model;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Game {

    private final Board board;

    private Player      currentPlayer;
    private int         numberOfMoves;
    private GameResult  gameResult;
    private Player      winner;

    private final PublishSubject<GameEvent> gameEvents;

    public Game() {
        board      = new Board();

        currentPlayer = Player.PLAYER_1;
        numberOfMoves = 0;
        gameResult    = GameResult.UNDECIDED;
        winner        = null;

        gameEvents = PublishSubject.create();
    }

    public Observable<GameEvent> getGameEvents() {
        return gameEvents.hide();
    }

    public Board getBoard() {
        return board.copy();
    }

    public boolean isCellEmpty(int row, int column) {
        return board.isCellEmpty(row, column);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean hasFinished() {
        return gameResult != GameResult.UNDECIDED;
    }

    public void makeMove(int row, int column) {
        if (gameResult.isGameFinished()) {
            throw new IllegalArgumentException(String.format("game is already finished"));
        }

        // make move
        board.makeMove(row, column, currentPlayer);
        gameEvents.onNext(GameEvent.newMadeMoveEvent(currentPlayer, row, column));

        // check game result status
        numberOfMoves++;
        updateGameResult();
        if (gameResult.isGameFinished()) {
            gameEvents.onNext(GameEvent.newGameFinishedEvent(gameResult, winner));
        } else {
            // switch players
            currentPlayer = currentPlayer.nextPlayer();
            gameEvents.onNext(GameEvent.newPlayerSwitchedEvent(currentPlayer));
        }
    }

    private void updateGameResult() {
        Board.EvaluationResult result = board.evaluate();

        Player winningPlayer = result.hasWinner() ? Player.of(result.getSymbol()) : null;

        if (winningPlayer != null) {
            gameResult = GameResult.GAME_WON;
            winner     = winningPlayer;
        } else if (numberOfMoves == board.getCellCount()) {
            gameResult = GameResult.GAME_DRAWN;
            winner     = null;
        } else {
            gameResult = GameResult.UNDECIDED;
            winner     = null;
        }
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public Player getWinner() {
        return winner;
    }

    public void reset() {
        board.reset();

        numberOfMoves = 0;
        gameResult    = GameResult.UNDECIDED;
        winner        = null;
        gameEvents.onNext(GameEvent.newBoardClearedEvent());

        currentPlayer = Player.PLAYER_1;

        gameEvents.onNext(GameEvent.newPlayerSwitchedEvent(currentPlayer));
    }

    @Override
    public String toString() {
        return board.toString();
    }
}
