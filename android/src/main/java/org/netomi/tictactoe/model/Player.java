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

public enum Player {

    PLAYER_1(CellSymbol.X),
    PLAYER_2(CellSymbol.O);

    private final CellSymbol symbol;

    Player(CellSymbol symbol) {
        this.symbol = symbol;
    }

    public CellSymbol getSymbol() {
        return symbol;
    }

    public Player nextPlayer() {
        return this == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }

    public static Player of(CellSymbol symbol) {
        if (symbol != null) {
            for (Player player : values()) {
                if (player.getSymbol() == symbol) {
                    return player;
                }
            }
        }

        return null;
    }
}
