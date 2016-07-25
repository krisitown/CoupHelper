package res.layout;

public class Game {
    private int[] players;
    private int playerTurn;

    public Game(int numberOfPlayers){
        players = new int[numberOfPlayers];
        playerTurn = 0;
    }

    public void nextTurn(){
        playerTurn++;
        if(playerTurn == players.length){
            playerTurn = 0;
        }
    }

    public void getCoin(int numberOfCoins){
        players[playerTurn] += numberOfCoins;
        nextTurn();
    }

    public void removeCoins(int numberOfCoins){
        if(numberOfCoins > players[playerTurn]){
            throw new IllegalArgumentException("Not enough coins for operation.");
        }
        players[playerTurn] -= numberOfCoins;
        nextTurn();
    }

    public void stealCoins(int playerToStealFrom){
        playerToStealFrom--;
        if(playerToStealFrom == playerTurn){
            throw new IllegalArgumentException("Cannot steal from yourself.");
        }
        if (players[playerToStealFrom] < 2){
            throw new IllegalArgumentException("Player to steal from does not have sufficient coins");
        }
        players[playerToStealFrom] -= 2;
        players[playerTurn] += 2;
        nextTurn();
    }

    public int[] getPlayers() { return this.players; }
    public int getCurrentPlayer() { return this.playerTurn; }
}
