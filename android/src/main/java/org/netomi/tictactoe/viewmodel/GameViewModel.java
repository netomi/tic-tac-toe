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
package org.netomi.tictactoe.viewmodel;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.netomi.tictactoe.R;
import org.netomi.tictactoe.ai.AIPlayer;
import org.netomi.tictactoe.ai.AlphaBetaPruningAI;
import org.netomi.tictactoe.model.Game;
import org.netomi.tictactoe.model.GameEvent;
import org.netomi.tictactoe.model.Player;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GameViewModel extends ViewModel {

    private static final String TAG = "GameViewModel";

    // observable map accessed by fragment_game.xml
    public final ObservableArrayMap<String, Integer> cells;

    private final Game     game;
    private final Player   human;
    private final AIPlayer computer;

    private final CompositeDisposable disposables;

    private final MutableLiveData<Boolean> gameFinished = new MutableLiveData<>(false);

    public final ObservableInt player1Score = new ObservableInt(0);
    public final ObservableInt player2Score = new ObservableInt(0);
    public final ObservableInt drawScore    = new ObservableInt(0);

    public GameViewModel() {
        cells    = new ObservableArrayMap<>();

        game     = new Game();
        human    = Player.PLAYER_1;
        computer = new AlphaBetaPruningAI();

        // subscribe to the different events.
        disposables = new CompositeDisposable();

        disposables.add(game.getGameEvents()
                            .filter(GameEvent.only(GameEvent.EventType.MADE_MOVE))
                            .cast(GameEvent.MadeMoveEvent.class)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::madeMove));

        disposables.add(game.getGameEvents()
                            .filter(GameEvent.only(GameEvent.EventType.BOARD_CLEARED))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::clearBoard));

        disposables.add(game.getGameEvents()
                            .filter(GameEvent.only(GameEvent.EventType.PLAYER_SWITCHED))
                            .cast(GameEvent.PlayerSwitchedEvent.class)
                            .observeOn(Schedulers.computation())
                            .subscribe(this::switchPlayer));

        disposables.add(game.getGameEvents()
                            .filter(GameEvent.only(GameEvent.EventType.GAME_FINISHED))
                            .cast(GameEvent.GameFinishedEvent.class)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::gameFinished));

        game.reset();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.clear();
    }

    public String getWinner() {
        Player winner = game.getWinner();

        if (winner == human) {
            return "Human";
        } else if (winner == Player.PLAYER_2) {
            return "Computer";
        } else {
            return null;
        }
    }

    public void resetGame() {
        game.reset();
        gameFinished.setValue(false);
    }

    public LiveData<Boolean> getGameFinished() {
        return gameFinished;
    }

    private void gameFinished(GameEvent.GameFinishedEvent event) {
        Log.d(TAG, "game finished, winner = " + event.getWinner());

        gameFinished.setValue(true);

        if (event.getWinner() == Player.PLAYER_1) {
            player1Score.set(player1Score.get() + 1);
        } else if (event.getWinner() == Player.PLAYER_2) {
            player2Score.set(player2Score.get() + 1);
        } else {
            drawScore.set(drawScore.get() + 1);
        }
    }

    private void switchPlayer(GameEvent.PlayerSwitchedEvent event) {
        Log.d(TAG, "switching player, new player = " + event.getPlayer());

        if (event.getPlayer() == Player.PLAYER_2) {
            AIPlayer.Move move = computer.getMove(game.getBoard(), Player.PLAYER_2);

            game.makeMove(move.getRow(), move.getColumn());
        }
    }

    private void clearBoard(GameEvent event) {
        Log.d(TAG, "board cleared");

        cells.clear();
    }

    private void madeMove(GameEvent.MadeMoveEvent event) {
        Log.d(TAG, "player " + event.getPlayer() + " made move (" + event.getRow() + ", " + event.getColumn() + ")");

        int drawableResource = event.getPlayer() == Player.PLAYER_1 ? R.drawable.x : R.drawable.o;
        cells.put(cellIdFromRowColumn(event.getRow(), event.getColumn()), drawableResource);
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    public void onClickedCellAt(int row, int column) {
        if (game.getCurrentPlayer() == human) {
            Log.i(TAG, "Clicked at cell (" + row + "/" + column + ")");
            if (!game.hasFinished() && game.isCellEmpty(row, column)) {
                game.makeMove(row, column);
            }
        }
    }

    private static String cellIdFromRowColumn(int row, int column) {
        StringBuilder sb = new StringBuilder();
        sb.append(row);
        sb.append(column);
        return sb.toString();
    }
}
