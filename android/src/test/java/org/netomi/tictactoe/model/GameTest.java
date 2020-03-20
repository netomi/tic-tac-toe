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

import org.junit.Test;

import io.reactivex.disposables.CompositeDisposable;

public class GameTest {

    @Test
    public void firstTest() {
        Game game = new Game();

        CompositeDisposable disposables = new CompositeDisposable();

//        disposables.add(board.getGameEvents().subscribe(event -> {
//            switch (event) {
//                case INITIALIZED:
//                    System.out.println("board initialized");
//                    System.out.println(board);
//                    break;
//
//                case MOVE:
//                    System.out.println("Player made move");
//                    System.out.println(board);
//                    break;
//
//                case GAME_FINISHED:
//                    System.out.println("Game finished");
//                    break;
//            }
//        }));

//        board.getGameEvents()
//                .filter(GameEvent.only(GameEvent.EventType.BOARD_CLEARED))
//                .subscribe(BoardTest::clearBoard);
//
//        board.getGameEvents()
//                .filter(GameEvent.only(GameEvent.EventType.MADE_MOVE))
//                .cast(GameEvent.MadeMoveEvent.class)
//                .subscribe(BoardTest::madeMove);

//        game.getGameEvents().subscribe(GameTest::receiveEvents);

        game.reset();
        game.makeMove(1, 1);
        game.makeMove(2, 1);
        game.makeMove(3, 3);
        game.makeMove(1, 2);
        game.makeMove(2, 2);

        System.out.println(game);
        disposables.clear();
    }

//    @Test
//    public void actors() {
//        Game game = new Game();
//
//        AIPlayer AIPlayer1 = new MinimaxAI(Player.PLAYER_1);
//        AIPlayer AIPlayer2 = new AlphaBetaPruningAI(Player.PLAYER_2);
//
//        int cell = new Random().nextInt(9);
//        int row = (cell / 3) + 1;
//        int col = (cell % 3) + 1;
//
//        game.makeMove(row, col);
//
//        System.out.println(game);
//
//        AIPlayer currentAIPlayer = AIPlayer2;
//
//        while (!game.hasFinished()) {
//            Board board = game.getBoard();
//
//            long start = System.nanoTime();
//            AIPlayer.Move move = currentAIPlayer.getMove(board);
//            long end = System.nanoTime();
//
//            System.out.println("Making computer move too " + (end - start) / 1e6 + "ms");
//
//            game.makeMove(move.row, move.col);
//
//            System.out.println(game);
//
//            currentAIPlayer = currentAIPlayer == AIPlayer1 ? AIPlayer2 : AIPlayer1;
//        }
//    }

}
