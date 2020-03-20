# Tic Tac Toe

This app is my first Android application and used as a test bed for various things:

* data binding
* MVVM approach
* reactive programming using rxjava
* minimax AI

# Design

The design follows the MVVM approach, the main classes are as follows:

* GameFragment: the actual view class that displays the classical tic-tac-toe board
* GameViewModel: the view model implementation, it controls the actual model and observes game events
* Game / Board: the actual game model, it allows to observe game events with the use of rxjava

Computer players are implemented using an unbeatable minimax AI.