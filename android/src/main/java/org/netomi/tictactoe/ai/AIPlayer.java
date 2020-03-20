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
import org.netomi.tictactoe.model.Player;

public interface AIPlayer {

    class Move {
        private final int row;
        private final int column;

        static Move of(int row, int column) {
            return new Move(row, column);
        }

        private Move(int row, int column) {
            this.row    = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }
    }

    Move getMove(Board board, Player player);
}
