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
package org.netomi.tictactoe.ai;

import org.netomi.tictactoe.model.Board;
import org.netomi.tictactoe.model.Cell;
import org.netomi.tictactoe.model.Player;

public class MinimaxAI implements AIPlayer {
    private static final int WIN  =  10;
    private static final int LOSS = -10;
    private static final int DRAW =   0;

    @Override
    public Move getMove(Board board, Player player) {
        int cellIndex = minimax(board, player, player,0);

        if (cellIndex != -1) {
            return Move.of((cellIndex / 3) + 1, (cellIndex % 3) + 1);
        }

        return null;
    }

    private int minimax(Board board, Player player, Player marker, int depth) {
        Player winner = board.evaluateWinner();
        if (winner != null || board.isFull()) {
            return winner == null   ? DRAW :
                   winner == player ? WIN  - depth :
                                      LOSS + depth;
        }

        int bestScore = marker == player ? LOSS : WIN;
        int bestMove  = -1;

        for (Cell cell : board.emptyCells()) {
            board.makeMove(cell, marker);

            int score = minimax(board, player, marker.nextPlayer(), depth + 1);

            if (marker == player)
            {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove  = cell.getCellIndex();
                }
            } else {
                if (score < bestScore)
                {
                    bestScore = score;
                    bestMove  = cell.getCellIndex();
                }

            }

            board.clearCell(cell);
        }

        return depth == 0 ? bestMove : bestScore;
    }
}
