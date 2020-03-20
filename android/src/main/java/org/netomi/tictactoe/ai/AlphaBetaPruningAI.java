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

public class AlphaBetaPruningAI implements AIPlayer {
    private static final int WIN  = 1000;
    private static final int LOSS = -1000;
    private static final int DRAW = 0;

    @Override
    public Move getMove(Board board, Player player) {
        int cellIndex = minimax(board, player, player, 0, LOSS, WIN);

        if (cellIndex != -1) {
            return Move.of((cellIndex / 3) + 1, (cellIndex % 3) + 1);
        }

        return null;
    }

    private int minimax(Board board, Player player, Player marker, int depth, int alpha, int beta) {
        Player winner = board.evaluateWinner();
        if (winner != null || board.isFull()) {
            return winner == null   ? DRAW :
                   winner == player ? WIN  :
                                      LOSS;
        }

        int bestScore = marker == player ? LOSS : WIN;
        int bestMove  = -1;

        for (Cell cell : board.emptyCells()) {
            board.makeMove(cell, marker);

            int score = minimax(board, player, marker.nextPlayer(), depth + 1, alpha, beta);

            if (marker == player)
            {
                if (score > bestScore) {
                    bestScore = score - depth * 10;
                    bestMove  = cell.getCellIndex();

                    alpha = Math.max(alpha, bestScore);
                    board.clearCell(cell);
                    if (beta <= alpha)
                    {
                        break;
                    }
                }
            } else {
                if (score < bestScore)
                {
                    bestScore = score + depth * 10;
                    bestMove  = cell.getCellIndex();

                    beta = Math.min(beta, bestScore);
                    board.clearCell(cell);
                    if (beta <= alpha)
                    {
                        break;
                    }
                }
            }

            board.clearCell(cell);
        }

        return depth == 0 ? bestMove : bestScore;
    }
}
