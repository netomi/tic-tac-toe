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

import io.reactivex.functions.Predicate;

public abstract class GameEvent {

    public enum EventType {
        BOARD_CLEARED,
        MADE_MOVE,
        PLAYER_SWITCHED,
        GAME_FINISHED
    }

    public static GameEvent newBoardClearedEvent() {
        return new BoardClearedEvent();
    }

    public static GameEvent newMadeMoveEvent(Player player, int row, int column) {
        return new MadeMoveEvent(player, row, column);
    }

    public static GameEvent newPlayerSwitchedEvent(Player player) {
        return new PlayerSwitchedEvent(player);
    }

    public static GameEvent newGameFinishedEvent(GameResult result, Player winner) {
        return new GameFinishedEvent(result, winner);
    }

    public static Predicate<GameEvent> only(EventType type) {
        return (gameEvent) -> gameEvent.getEventType() == type;
    }

    public abstract EventType getEventType();

    public static class BoardClearedEvent extends GameEvent {

        private BoardClearedEvent() {}

        @Override
        public EventType getEventType() {
            return EventType.BOARD_CLEARED;
        }

        @Override
        public String toString() {
            return "Board cleared";
        }

    }

    public static class MadeMoveEvent extends GameEvent {

        private final Player player;
        private final int    row;
        private final int    column;

        private MadeMoveEvent(Player player, int row, int column) {
            this.player = player;
            this.row    = row;
            this.column = column;
        }

        @Override
        public EventType getEventType() {
            return EventType.MADE_MOVE;
        }

        public Player getPlayer() {
            return player;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        @Override
        public String toString() {
            return String.format("Player %s made move on cell (%d,%d)", player, row, column);
        }
    }

    public static class PlayerSwitchedEvent extends GameEvent {

        private final Player player;

        private PlayerSwitchedEvent(Player player) {
            this.player = player;
        }

        @Override
        public EventType getEventType() {
            return EventType.PLAYER_SWITCHED;
        }

        public Player getPlayer() {
            return player;
        }

        @Override
        public String toString() {
            return String.format("Player switched to %s", player);
        }
    }

    public static class GameFinishedEvent extends GameEvent {

        private final GameResult result;
        private final Player     winner;

        private GameFinishedEvent(GameResult result, Player winner) {
            this.result = result;
            this.winner = winner;
        }

        @Override
        public EventType getEventType() {
            return EventType.GAME_FINISHED;
        }

        public GameResult getResult() {
            return result;
        }

        public Player getWinner() {
            return winner;
        }

        @Override
        public String toString() {
            switch (result) {
                case GAME_WON:
                    return String.format("Game won by %s", winner);

                case GAME_DRAWN:
                    return "Game drawn.";

                case UNDECIDED:
                    return "Game not yet finished.";
            }

            return result.toString();
        }
    }
}
