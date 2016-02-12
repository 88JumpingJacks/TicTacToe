package com.theboss.jackli.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    // 0 = yellow_chip, 1 = red_chip
    int activePlayer = 0;

    // Tracks whether a chip occupies a board slot
    // 2 means no chip is in that board slot
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    // Key = Player (0 = yellow, 1 = red)
    // Value = player score
    Map<Integer, Integer> playerScore = new HashMap<>();

    private TextView yellow_score;
    private TextView red_score;

    private GridLayout board;

    public void dropIn(View view)
    {
        ImageView counter = (ImageView) view;

        int tappedCounter = Integer.parseInt((String) counter.getTag());

        int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

        if (gameState[tappedCounter] == 2)
        {
            gameState[tappedCounter] = activePlayer;
            counter.setTranslationY(-1000f);


            if (activePlayer == 0)
            {
                counter.setImageResource(R.drawable.yellow_chip);
                activePlayer = 1;
            }
            else
            {
                counter.setImageResource(R.drawable.red_chip);
                activePlayer = 0;
            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration
                    (300);

            // Check if the player won
            for (int[] winningPosition : winningPositions)
            {
                if (gameState[winningPosition[0]] ==
                        gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] ==
                                gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2)
                {
                    // Display the opposite activePlayer as the winner as we
                    // changed the activePlayer already in the if-else
                    // statements above
                    if (activePlayer == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Red " +
                                "player wins!", Toast.LENGTH_LONG).show();
                        playerScore.put(1, playerScore.get(1) + 1);
                        Log.i("PlayerScore: ", "" + playerScore.entrySet());
                        red_score.setText(getString(R.string.red_score,
                                playerScore.get(1)));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Yellow " +
                                "player wins!", Toast.LENGTH_LONG).show();
                        playerScore.put(0, playerScore.get(0) + 1);
                        Log.i("PlayerScore: ", "" + playerScore.entrySet());
                        yellow_score.setText(getString(R.string.yellow_score,
                                playerScore.get(0)));
                    }

                    board = (GridLayout) findViewById(R.id
                            .game_board);

                    // Set all child elements on the game board to be
                    // unclickable after the current game has ended
                    setClickableRecursive(board, false);
                }
            }

            // todo need to check if the game ended in a tie
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Already occupied! Choose" +
                    " another board slot!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set all child elements of a view to clickable or unclickable
     *
     * @param aInViewGroup
     * @param aInIsClickable True - set all child elements to clickable
     *                       False - set all child elements to unclickable
     */
    public static void setClickableRecursive(ViewGroup aInViewGroup, boolean
            aInIsClickable)
    {
        for (int i = 0; i < aInViewGroup.getChildCount(); i++)
        {
            aInViewGroup.getChildAt(i).setClickable(aInIsClickable);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize TextView score fields
        yellow_score = (TextView) findViewById(R.id.yellow_score);
        red_score = (TextView) findViewById(R.id.red_score);

        yellow_score.setText(getString(R.string.yellow_score, 0));
        red_score.setText(getString(R.string.red_score, 0));

        // Initialize player scores to 0
        playerScore.put(0, 0);
        playerScore.put(1, 0);

        final Button lBtn_new_game = (Button) findViewById(R.id.btn_new_game);
        lBtn_new_game.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Reset the board to begin a new game
             *
             * @param aInView
             */
            public void onClick(View aInView)
            {
                View lView;

                // Make all ImageViews invisible (remove src's)
                for (int i = 0; i < board.getChildCount(); i++)
                {
                    lView = board.getChildAt(i);

                    if (lView instanceof ImageView)
                    {
                        // Clear the image resource
                        ((ImageView) lView).setImageResource(0);
                    }
                }

                // Reset all gameState elements to 2 (no chip occupies slot)
                for (int i = 0; i<gameState.length; i++)
                {
                    gameState[i] = 2;
                }

                // Reset ImageViews to be clickable
                setClickableRecursive(board, true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
