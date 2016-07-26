package res.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.couphelper.krisitown.couphelper.MainActivity;
import com.couphelper.krisitown.couphelper.R;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    //used to display errors
    private String errors;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //text areas to update
        final TextView playerHeading = (TextView)findViewById(R.id.player_heading);
        final TextView coinsHeading = (TextView)findViewById(R.id.coins_heading);
        final TextView stats = (TextView)findViewById(R.id.stats);

        errors = "";

        names = getIntent().getStringArrayListExtra("names");
        Integer numberOfPlayers = names.size();
        final Game game = new Game(numberOfPlayers);

        updateText(game, playerHeading, coinsHeading, stats);

        Button getOneCoin = (Button)findViewById(R.id.oneCoinButton);
        getOneCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getCoin(1);
                updateText(game, playerHeading, coinsHeading, stats);
            }
        });

        Button getTwoCoins = (Button)findViewById(R.id.twoCoinButton);
        getTwoCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getCoin(2);
                updateText(game, playerHeading, coinsHeading, stats);
            }
        });

        Button getThreeCoins = (Button)findViewById(R.id.threeCoinButton);
        getThreeCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getCoin(3);
                updateText(game, playerHeading, coinsHeading, stats);
            }
        });

        Button assassinate = (Button)findViewById(R.id.assassinate);
        assassinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    game.removeCoins(3);
                } catch (IllegalArgumentException exc){
                    errors = exc.getMessage();
                }

                updateText(game, playerHeading, coinsHeading, stats);
            }
        });

        Button coup = (Button)findViewById(R.id.coup);
        coup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    game.removeCoins(7);
                } catch (IllegalArgumentException exc){
                    errors = exc.getMessage();
                }
                updateText(game, playerHeading, coinsHeading, stats);
            }
        });

        Button nextTurn = (Button)findViewById(R.id.next_turn);
        nextTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.nextTurn();
                updateText(game, playerHeading, coinsHeading, stats);
            }
        });

        Button steal = (Button)findViewById(R.id.stealTwoCoins);
        final EditText targetPlayer = (EditText)findViewById(R.id.playerToStealFrom);

        steal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo add name implementation
                Integer playerToStealFrom = playerToStealFrom(targetPlayer.getText().toString());
                if(playerToStealFrom != -1){
                    try {
                        game.stealCoins(playerToStealFrom + 1);
                    } catch (IllegalArgumentException exc) {
                        errors = exc.getMessage();
                    }
                } else {
                    errors = "Player not found.";
                }

                updateText(game, playerHeading, coinsHeading, stats);
            }
        });

        final Intent intent = new Intent(this, MainActivity.class);
        Button newGame = (Button)findViewById(R.id.new_game);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                updateText(game, playerHeading, coinsHeading, stats);
            }
        });
    }

    private int playerToStealFrom(String playerName){
        for (String name : names) {
            if(name.equals(playerName)){
                return names.indexOf(name);
            }
        }
        return -1;
    }

    private void updateText(Game game, TextView playerTurn, TextView currentPlayerCoins, TextView stats){
        playerTurn.setText(names.get(game.getCurrentPlayer()) + "'s turn:");
        currentPlayerCoins.setText("Coins: " + game.getPlayers()[game.getCurrentPlayer()]);
        stats.setText(createStatSheet(game, names));
    }

    private String createStatSheet(Game game, ArrayList<String> names){
        int[] playerStats = game.getPlayers();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < playerStats.length; i++) {
            sb.append(names.get(i));
            sb.append(": ");
            sb.append(playerStats[i]);
            sb.append("\n");
        }

        if(!errors.isEmpty()){
            sb.append("========\n");
            sb.append(errors);
            sb.append("\n========\n");
            errors = "";
        }

        return sb.toString();
    }
}
