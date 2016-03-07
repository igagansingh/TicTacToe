package com.developer.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Gagandeep on 02/27/16.
 */
public class MainActivity extends Activity {

    // Representing the game state:
    private boolean is_O_turn = false; // Whose turn is it? false=X true=O
    // for now we will represent the gameBoard as an array of characters
    private char gameBoard[][] = new char[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupOnClickListeners();
        resetButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when you press new game.
     */
    public void newGame(View view) {
        is_O_turn = false;
        gameBoard = new char[3][3];
        resetButtons();
    }

    /**
     * Reset each button in the grid to be blank and enabled.
     */
    private void resetButtons() {
        TableLayout T = (TableLayout) findViewById(R.id.tableLayout);
        for (int y = 0; y < T.getChildCount(); y++) {
            if (T.getChildAt(y) instanceof TableRow) {
                TableRow R = (TableRow) T.getChildAt(y);
                for (int x = 0; x < R.getChildCount(); x++) {
                    if (R.getChildAt(x) instanceof Button) {
                        Button B = (Button) R.getChildAt(x);
                        B.setText("");
                        B.setEnabled(true);
                    }
                }
            }
        }
        TextView t = (TextView) findViewById(R.id.titleText);
        t.setText(R.string.title);

        TextView nextTurnView = (TextView) findViewById(R.id.nextTurn);
        nextTurnView.setText("X's Turn");
    }

    /**
     * This method that returns true when someone has won and false when nobody has
     * If someone has won the game, it will also displays that in form of Toast.
     *
     * @return
     */
    private boolean checkWin() {
        char winner = '\0';
        if (checkWinner(gameBoard, 3, 'X')) {
            winner = 'X';
        } else if (checkWinner(gameBoard, 3, 'O')) {
            winner = 'O';
        }

        if (winner == '\0') {
            return false; // nobody won
        } else {
            // display winner
            Toast.makeText(this, winner + " wins", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    /**
     * This is a main algorithm for checking if a specific player has won.
     * @return true if the specified player has won
     */
    private boolean checkWinner(char[][] board, int size, char player) {
        //First we check all rows
        for (int x = 0; x < size; x++) {
            int total = 0;
            for (int y = 0; y < size; y++) {
                if (board[x][y] == player) {
                    total++;
                }
            }
            if (total >= size) {
                return true;
            }
        }

        //Then we check all the columns
        for (int y = 0; y < size; y++) {
            int total = 0;
            for (int x = 0; x < size; x++) {
                if (board[x][y] == player) {
                    total++;
                }
            }
            if (total >= size) {
                return true;
            }
        }

        /*
        * This part is checking diagonals (forward)
        * i.e. starting x & y from 0 and compare when x & y are same.
        */
        int total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == y && board[x][y] == player) {
                    total++;
                }
            }
        }
        if (total >= size) {
            return true;
        }

        /*
        * This part is checking diagonals (forward)
        * i.e. starting x & y from 0 and compare when x + y = boardsize - 1 (x=0, y=2 | x=0, y=1 | x=2, y=0).
        */
        total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x + y == size - 1 && board[x][y] == player) {
                    total++;
                }
            }
        }
        if (total >= size) {
            return true;
        }

        return false;
    }

    /**
     * Disables all the buttons.
     */
    private void disableGameButtons() {
        TableLayout T = (TableLayout) findViewById(R.id.tableLayout);
        for (int y = 0; y < T.getChildCount(); y++) {
            if (T.getChildAt(y) instanceof TableRow) {
                TableRow R = (TableRow) T.getChildAt(y);
                for (int x = 0; x < R.getChildCount(); x++) {
                    if (R.getChildAt(x) instanceof Button) {
                        Button B = (Button) R.getChildAt(x);
                        B.setEnabled(false);
                    }
                }
            }
        }
    }

    private void setupOnClickListeners() {
        TableLayout T = (TableLayout) findViewById(R.id.tableLayout);
        for (int y = 0; y < T.getChildCount(); y++) {
            if (T.getChildAt(y) instanceof TableRow) {
                TableRow R = (TableRow) T.getChildAt(y);
                for (int x = 0; x < R.getChildCount(); x++) {
                    View V = R.getChildAt(x); // In our case this will be each button on the grid
                    V.setOnClickListener(new PlayButtonOnClick(x, y));
                }
            }
        }
    }

    /**
     * Handles click event on the buttons
     */
    private class PlayButtonOnClick implements View.OnClickListener {

        private int x = 0;
        private int y = 0;

        public PlayButtonOnClick(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof Button) {
                Button B = (Button) view;
                gameBoard[x][y] = is_O_turn ? 'O' : 'X';
                B.setText(is_O_turn ? "O" : "X");
                B.setTextColor(is_O_turn ?
                        getResources().getColor(R.color.colorPrimary) :
                        getResources().getColor(R.color.colorRed));
                B.setTextSize(20);
                B.setEnabled(false);
                is_O_turn = !is_O_turn;

                TextView nextTurnView = (TextView) findViewById(R.id.nextTurn);
                nextTurnView.setText(is_O_turn ? "O's Turn" : "X's Turn");

                // check if anyone has won
                if (checkWin()) {
                    disableGameButtons();
                    nextTurnView.setText("X's Turn");
                }
            }
        }
    }
}
