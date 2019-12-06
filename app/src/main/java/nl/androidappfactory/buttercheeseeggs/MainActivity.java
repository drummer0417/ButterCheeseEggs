package nl.androidappfactory.buttercheeseeggs;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int KAAS = 1;
    private static final int BOTER = 2;
    private static final int FREE = 0;

    private int currentPlayer = KAAS;
    private int[] usedCells = {FREE, FREE, FREE, FREE, FREE, FREE, FREE, FREE, FREE};
    //there are only 8 possible winning combi's so lets put them in 8 arrays
    private int[][] winningCombinatios = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private int countersPlaced = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void placeCounter(View view) {

        Log.d("placeCounter", "currentPlayer" + currentPlayer);
        ImageView counter = (ImageView) view;
        // check if cell is already used
        int cell = Integer.parseInt(counter.getTag().toString());
        if (usedCells[cell] == FREE) {

            // apply counter image to counter
            if (currentPlayer == KAAS) {
                usedCells[cell] = KAAS;
                counter.setImageResource(R.drawable.kaas);
            } else {// BOTER
                usedCells[cell] = BOTER;
                counter.setImageResource(R.drawable.boter);
            }
            // move counter out of screen
            counter.setTranslationY(-500f);
            counter.setRotation(0f);
            // and back in
            counter.animate().translationY(0f).rotation(360f).setDuration(700);
            countersPlaced++;

            if (isWinning(currentPlayer)) {
                onWinner(currentPlayer);
            } else if (countersPlaced == 9) {
                onWinner(-1);
            }

            currentPlayer = (currentPlayer == KAAS) ? BOTER : KAAS;
        }
    }

    public void newGame(View view) {

        GridLayout g = (GridLayout) findViewById(R.id.gLayout);

        for (int i = 0; i < g.getChildCount(); i++) {
            ((ImageView) g.getChildAt(i)).setImageResource(0);
        }

        enDisableViews(true);
        for (int i = 0; i < usedCells.length; i++) {
            usedCells[i] = FREE;
        }
        countersPlaced = 0;
    }

    private void onWinner(int winner) {
        enDisableViews(false);
        if (winner == KAAS) {
            Toast.makeText(getApplicationContext(), R.string.kaas, Toast.LENGTH_LONG).show();
        } else if (winner == BOTER) {
            Toast.makeText(getApplicationContext(), R.string.boter, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.geen_winnaar, Toast.LENGTH_LONG).show();
            findViewById(R.id.bNewGame).setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isWinning(int currentPlayer) {
        boolean isWinner = false;

        // loop over all 8 combi's
        for (int[] winningcombi : winningCombinatios) {

            boolean okSoFarForAWinningCombi = false;
            // for each winning combi check if the numbers of that combi are equal to the numbers
            // in usedCombi for the ginven player
            for (int valueInWinningCombi : winningcombi) {
                boolean okForSomeValues = false;
                //
                for (int usedCell : usedCells) {
                    if (usedCells[valueInWinningCombi] == currentPlayer) {
                        okForSomeValues = true;
                        break;
                    }
                }
                if (okForSomeValues) {
                    okSoFarForAWinningCombi = true;
                } else {
                    okSoFarForAWinningCombi = false;
                    break;
                }
            }
            if (okSoFarForAWinningCombi)
                isWinner = true;
        }
        return isWinner;
    }

    private void enDisableViews(boolean enable) {

        GridLayout g = (GridLayout) findViewById(R.id.gLayout);
        for (int i = 0; i < g.getChildCount(); i++) {
            g.getChildAt(i).setClickable(enable);
        }
        findViewById(R.id.bNewGame).setEnabled(!enable);
    }
}