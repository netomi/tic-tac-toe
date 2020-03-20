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

import java.util.Objects;

public class Cell {
    private final int row;
    private final int column;

    private CellSymbol symbol;

    Cell(int row, int column) {
        this.row    = row;
        this.column = column;
        symbol = CellSymbol.EMPTY;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getCellIndex() {
        return Board.getCellIndex(row, column);
    }

    public boolean isEmpty() {
        return symbol == CellSymbol.EMPTY;
    }

    public CellSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(CellSymbol symbol) {
        Objects.requireNonNull(symbol);
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return String.format("%s", symbol.getSymbol());
    }
}
