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
package org.netomi.tictactoe.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.netomi.tictactoe.databinding.FragmentGameBinding;
import org.netomi.tictactoe.viewmodel.GameViewModel;

public class GameFragment extends Fragment {

    private GameViewModel gameViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentGameBinding gameBinding = FragmentGameBinding.inflate(inflater, container, false);

        gameBinding.setLifecycleOwner(this);

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        gameBinding.setGameViewModel(gameViewModel);

        gameViewModel.getGameFinished().observe(getViewLifecycleOwner(), finished ->
            {
                if (finished) {
                    String winner = gameViewModel.getWinner();

                    String message = winner == null ? "Game has been drawn." : "Game has been won by " + winner;

                    new AlertDialog.Builder(getContext())
                            .setTitle("Game Finished")
                            .setMessage(message)

                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                gameViewModel.resetGame();
                            })

                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            });

        return gameBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
