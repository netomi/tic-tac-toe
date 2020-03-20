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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    public enum RowType {
        ROW,
        COLUMN,
        DIAGONAL
    }

    public static class EvaluationResult {
        private final CellSymbol symbol;
        private final RowType    rowType;
        private final int        number;

        public static EvaluationResult of(CellSymbol symbol, RowType rowType, int number) {
            return new EvaluationResult(symbol, rowType, number);
        }

        private EvaluationResult(CellSymbol symbol, RowType rowType, int number) {
            this.symbol  = symbol;
            this.rowType = rowType;
            this.number  = number;
        }

        public boolean hasWinner() {
            return getSymbol() != null;
        }

        public CellSymbol getSymbol() {
            return symbol;
        }

        public RowType getRowType() {
            return rowType;
        }

        public int getNumber() {
            return number;
        }
    }

    private static final int GRID_SIZE = 3;

    private final Cell[] cells;

    public Board() {
        int cellCount = GRID_SIZE * GRID_SIZE;
        cells = new Cell[cellCount];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cells[i * GRID_SIZE + j] = new Cell(i + 1, j + 1);
            }
        }
    }

    private Board(Board other) {
        this();

        for (int i = 0; i < cells.length; i++) {
            cells[i].setSymbol(other.cells[i].getSymbol());
        }
    }

    public Board copy() {
        return new Board(this);
    }

    public int getCellCount() {
        return cells.length;
    }

    static int getCellIndex(int row, int column) {
        return (row - 1) * GRID_SIZE + (column - 1);
    }

    public boolean isFull() {
        return !hasEmptyCells();
    }

    public boolean hasEmptyCells() {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Iterable<Cell> emptyCells() {
        List<Cell> result = new ArrayList<>();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].isEmpty()) {
                result.add(cells[i]);
            }
        }
        return result;
    }

    public Iterable<Cell> cells() {
        return Arrays.asList(cells);
    }

    public boolean isCellEmpty(int row, int column) {
        if (row < 1 || row > GRID_SIZE) {
            throw new IllegalArgumentException(String.format("row must be in range [1, %d] but was %d", GRID_SIZE, row));
        }

        if (column < 1 || column > GRID_SIZE) {
            throw new IllegalArgumentException(String.format("column must be in range [1, %d] but was %d", GRID_SIZE, column));
        }

        return cells[getCellIndex(row, column)].isEmpty();
    }

    public void makeMove(Cell cell, Player player) {
        makeMove(cell.getRow(), cell.getColumn(), player);
    }

    public void makeMove(int row, int column, Player player) {
        if (!isCellEmpty(row, column)) {
            throw new IllegalArgumentException(String.format("cell at (%d,%d) is not empty", row, column));
        }

        // make move
        int cellIndex = getCellIndex(row, column);
        cells[cellIndex].setSymbol(player.getSymbol());
    }

    public void clearCell(Cell cell) {
        clearCell(cell.getRow(), cell.getColumn());
    }

    public void clearCell(int row, int column) {
        int cellIndex = getCellIndex(row, column);
        cells[cellIndex].setSymbol(CellSymbol.EMPTY);
    }

    public Player evaluateWinner() {
        EvaluationResult result = evaluate();
        return result.hasWinner() ? Player.of(result.getSymbol()) : null;
    }

    public EvaluationResult evaluate() {
        CellSymbol symbol;

        for (int row = 0; row < GRID_SIZE; row++) {
            symbol = checkRow(row);
            if (symbol != CellSymbol.EMPTY) {
                return EvaluationResult.of(symbol, RowType.ROW, row + 1);
            }
        }

        for (int col = 0; col < GRID_SIZE; col++) {
            symbol = checkColumn(col);
            if (symbol != CellSymbol.EMPTY) {
                return EvaluationResult.of(symbol, RowType.COLUMN, col + 1);
            }
        }

        symbol = checkDiagonal(0, 0, 1, 1);
        if (symbol != CellSymbol.EMPTY) {
            return EvaluationResult.of(symbol, RowType.DIAGONAL, 1);
        }

        symbol = checkDiagonal(GRID_SIZE - 1, 0, -1, 1);
        if (symbol != CellSymbol.EMPTY) {
            return EvaluationResult.of(symbol, RowType.DIAGONAL, 2);
        }

        return EvaluationResult.of(null, null, 0);
    }

    private CellSymbol checkRow(int rowIndex) {
        int sum = 0;
        for (int j = 0; j < GRID_SIZE; j++) {
            sum += cells[rowIndex * GRID_SIZE + j].getSymbol().getValue();
        }
        return checkSum(sum);
    }

    private CellSymbol checkColumn(int columnIndex) {
        int sum = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            sum += cells[columnIndex + i * GRID_SIZE].getSymbol().getValue();
        }
        return checkSum(sum);
    }

    private CellSymbol checkDiagonal(int startRow, int startColumn, int advanceRow, int advanceColumn) {
        int sum = 0;
        for (int i = startRow, j = startColumn, k = 0; k < GRID_SIZE; i += advanceRow, j += advanceColumn, k++) {
            sum += cells[i * GRID_SIZE + j].getSymbol().getValue();
        }
        return checkSum(sum);
    }

    private CellSymbol checkSum(int sum) {
        if (sum == GRID_SIZE) {
            return CellSymbol.X;
        } else if (sum == -GRID_SIZE) {
            return CellSymbol.O;
        } else {
            return CellSymbol.EMPTY;
        }
    }

    public void reset() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].setSymbol(CellSymbol.EMPTY);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                sb.append(cells[i * GRID_SIZE + j]);
                sb.append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}
