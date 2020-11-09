/*
	Group 100:
		Kallimanis Ioannis     10007  6945466230    ikallima@ece.auth.gr
		Melissaris Christos    9983   6907596710    cpmeliss@ece.auth.gr
*/
import java.util.Scanner;  // Import the Scanner class
import java.lang.*; //for Thread

/**
 * Game
 */
public class Game {
  //fields of the Game class
  int round;  //always less or equal to 2*n (n = 100)

  //Constructors
  public Game(){
    round = 0;
  }
  public Game(int round){
    this.round = round;
  }

  //Setters and Getters
  public void setRound(int round){
    this.round = round;
  }

  public int getRound(){
    return round;
  }

  public static int div(int x,int y){
    return x/y; ///works only for positive x,y
  }
  public static int mod(int x, int y){
    return x%y;
  }

  /**
  @return an integer in {-1,0,1}
  -1 means Minotaurus won
  0 means nobody won
  1 means Theseus won
  */
  public static int checkGameOver(int S, Player p1, Player p2){
    int p1Tile = p1.getPlayerTile();
    int p2Tile = p2.getPlayerTile();
    if(p1.getScore() == S || p2.getScore() == S){
      return 1; //  Theseus has won
    }
    if(p1Tile == p2Tile){
      return -1;  //Minotaurus has won
    }
    return 0; //nobody won
  }

  /**
  @param maze the maze to be printed
  @param N the columns
  the rows are 2*N+1
  */
  public static void printMaze(String[][] maze, int N){
    System.out.print("\n\n");
    for(int i = 0; i < 2*N+1; i++){
      for(int j = 0; j < N; j++){
        System.out.print(maze[i][j]);
      }
    }
    System.out.print("\n\n");
  }

  public void promptEnterKey(){
   System.out.println("Press \"ENTER\" to continue...");
   Scanner scanner = new Scanner(System.in);
   scanner.nextLine();
  }

  public static void removeSupply(Board b,int m){
    if(m < 0){
      return;
    }
    b.getSupply(m).setSupplyTileId(-10);
    b.getSupply(m).setX(-10);
    b.getSupply(m).setY(-10);

  }

  public void credits(){
    for (int i = 0; i < 16; i++) {
      if(i == 0){
        System.out.println("A long time ago in a galaxy\n");

      }
      else if(i == 1)
        System.out.println("far far away...\n");
      else if(i == 2){
          System.out.println("Team 100 welcomes you to...\n");
       }
      else if(i == 3){
         System.out.println("\"A night in the musuem!\"\n");
      }
      else if(i == 5){
       System.out.println("The content creators:");
      }
      else if(i == 6){
      System.out.println("Ioannis Kallimanis");
      System.out.println("Christos Melissaris");
      }
     try {
        // thread to sleep for x milliseconds
        if(i == 2 || i == 3)
          Thread.sleep(2000);
        else if(i == 0 || i == 1)
          Thread.sleep(1000);
        else if(i == 4 || i == 5)
          Thread.sleep(1000);
        else{
          Thread.sleep(200);
          System.out.println();
        }
     }
     catch (Exception e) {
        System.out.println(e);
     }
    }
  }

  public static void main(String[] args){
    //create a new Game
    Game g = new Game();
    //show credits
    g.credits();

    int n = 100;  //turns per player at max

    int N = 15;
    int S = 4;
    int W = 230; //always less than N*N + 2*N - 1 AND greater or equal to 4*N - 1

    //create a new Board
    Board b = new Board(N,S,W);
    b.createBoard();

    //Create the 2 players
    Player p1 = new Player(0, "Theseus", b, 0, 0, 0);

    Player p2 = new Player(1, "Minotaurus", b, 0, div(N,2), div(N,2));

    //randomly choose who plays First
    int playsFirst  = (int)(Math.random()*101);
    playsFirst = mod(playsFirst, 2);

    int gameOv = 0; //for every move we check the value of this variable which changes if someone wins

    //this variable will hold the representation of the maze in a (2N + 1)X( N ) array
    String[][] maze = b.getStringRepresentation(p1.getPlayerTile(),p2.getPlayerTile());

    //this will be updated after every move
    int[] moveInfo = new int[4];

    while(g.getRound() < n){
      System.out.println("Round #"+(g.getRound()+1));
      printMaze(maze, N);
      g.promptEnterKey();
      if(p1.getPlayerId() == playsFirst){
        //move
        moveInfo = p1.move(p1.getPlayerTile());
        //represent
        maze = b.getStringRepresentation(p1.getPlayerTile(),p2.getPlayerTile());
        //print
        printMaze(maze, N);

        //Is the game Over ?
        gameOv = checkGameOver(S,p1,p2);
        if(gameOv != 0){
          break;
        }

        //move
        moveInfo = p2.move(p2.getPlayerTile());
        //represent
        maze = b.getStringRepresentation(p1.getPlayerTile(),p2.getPlayerTile());
        //print
        printMaze(maze, N);
        //Is the game Over ?
        gameOv = checkGameOver(S,p1,p2);
        if(gameOv != 0){
          break;
        }
      }
      else{
        //move
        p2.move(p2.getPlayerTile());
        //represent
        maze = b.getStringRepresentation(p1.getPlayerTile(),p2.getPlayerTile());
        //print
        printMaze(maze, N);
        //Is the game Over ?
        gameOv = checkGameOver(S,p1,p2);
        if(gameOv != 0){
          break;
        }

        //move
        moveInfo = p1.move(p1.getPlayerTile());
        //represent
        maze = b.getStringRepresentation(p1.getPlayerTile(),p2.getPlayerTile());
        //print
        printMaze(maze, N);
        //Is the game Over ?
        gameOv = checkGameOver(S,p1,p2);
        if(gameOv != 0){
          break;
        }
      }
      //update round
      g.setRound(g.getRound() + 1);
    }

    //print score of Theseus
    System.out.println(p1.getName() + " has score: "+ p1.getScore());

    //print End Screen of the Game
    switch((gameOv + 2)){
      case 1: //Minotaurus wins
        System.out.println(p2.getName() + " is the winner!");
      break;
      case 2: //Nobody wins. It's a new day in the musuem!
        System.out.println("It's a draw!");
      break;
      case 3: //Theseus wins
        System.out.println(p1.getName() + " is the winner!");
      break;
    }

    //Goodbye ;)
    System.out.println("Thank you for participating!");
  }

}
