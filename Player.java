public class Player{
  //fields of class Player
  private int playerId;
  private String name;
  private Board board;
  private int score;  //increments by 1 for each supply collected
  private int x;
  private int y;


  //Constructors
  public Player(){
    playerId = -1;  //means we have not yet given a valid id
    board = new Board();
    score = 0;
    x = -1; //means the player is not on the board
    y = -1; // same with x
  }
  public Player(int playerId, String name, Board board, int score, int x, int y){

    this.playerId = playerId;
    this.name = name;
    this.board = new Board(board);
    this.score = score;
    this.x = x;
    this.y = y;

  }
  //acts as a copy constructor
  public Player(Player p){
    this.playerId = p.getPlayerId();
    this.name = p.getName();
    this.board = new Board(p.getBoard());
    this.score = p.getScore();
    this.x = p.getX();
    this.y = p.getY();
  }

  //Setters & Getters
  public int getPlayerId(){
    return playerId;
  }
  public void setPlayerId(int playerId){
    this.playerId = playerId;
  }

  public String getName(){
    return name;
  }
  public void setName(String name){
    this.name = name;
  }

  public Board getBoard(){
    return board;
  }
  public void setBoard(Board board){
    this.board = board; //now both point in the same object
  }

  public int getScore(){
    return score;
  }
  public void setScore(int score){
    this.score = score;
  }

  public int getX(){
    return x;
  }
  public void setX(int x){
    this.x = x;
  }

  public int getY(){
    return y;
  }
  public void setY(int y){
    this.y = y;
  }

//extra getter
  public int getPlayerTile(){
    return x * board.getN() + y;
  }



  /*We assume that the parameter id in the move function is the current tileId
  in which the Player is located.
  */
  public int[] move(int id){
    int N = board.getN();
    int S = board.getS();
    int playerTile = getPlayerTile();
    //task #1 : Select randomly an available move

    int[] moveInfo = new int[4];

    int move = (int) (Math.random()*101);
    move = Game.mod(move,4);  //move belongs {0,1,2,3}
    move =  2*move + 1;       //move belongs {1,3,5,7}
    switch(move){
      case 1:
        if(!board.getTile(playerTile).getUp()){


          moveInfo[1] = x+1;
          moveInfo[2] = y;
          moveInfo[0] = (x+1)*N+y;
          x += 1; //we move up(change in row)
          //y(column index) is the same
          System.out.println(getName()+" now is in ("+moveInfo[1]+","+moveInfo[2]+") i.e. tile #"+moveInfo[0]);

          int supId = board.searchSupply(moveInfo[0]);
          //I make the assumption that Theseus always has id = 0
          if(getPlayerId() == 0){
            moveInfo[3] = supId;
          }
          else{
            moveInfo[3] = -2; //Minotaurus characteristic value
          }
          if(getPlayerId() == 0 && supId >= 0){
            score += 1;
            System.out.println(name + " collected supply with id " + (supId+1)+" !");
          }

        }
        else{
          moveInfo[0] = x*N + y;
          moveInfo[1] = x;
          moveInfo[2] = y;
          if(getPlayerId() == 0)
            moveInfo[3] = -1;
          else
            moveInfo[3] = -2;
          //inform about the move
          System.out.println(getName() + " cannot move " + "up!");
        }
      break;
      case 3:
        if(!board.getTile(playerTile).getRight()){


          moveInfo[1] = x;
          moveInfo[2] = y + 1;
          moveInfo[0] = x*N + (y + 1);
          y += 1; //we move right(change in column)
          //x(row index) is the same
          System.out.println(getName()+" now is in ("+moveInfo[1]+","+moveInfo[2]+") i.e. tile #"+moveInfo[0]);

          int supId = board.searchSupply(moveInfo[0]);
          //I make the assumption that Theseus always has id = 0
          if(getPlayerId() == 0){
            moveInfo[3] = supId;
          }
          else{
            moveInfo[3] = -2; //Minotaurus characteristic value
          }
          if(getPlayerId() == 0 && supId >= 0){
            score += 1;
            System.out.println(name + " collected supply with id " + (supId+1)+" !");
          }

        }
        else{
          moveInfo[0] = x*N + y;
          moveInfo[1] = x;
          moveInfo[2] = y;
          if(getPlayerId() == 0)
            moveInfo[3] = -1;
          else
            moveInfo[3] = -2;
          //inform about the move
          System.out.println(getName() + " cannot move " + "right!");
        }
      break;
      case 5:
        if(!board.getTile(playerTile).getDown() && x!=0){

          moveInfo[1] = x-1;
          moveInfo[2] = y;
          moveInfo[0] = (x - 1)*N + y;
          x -= 1; //we move down(change in row)
          //y(column index) is the same
          System.out.println(getName()+" now is in ("+moveInfo[1]+","+moveInfo[2]+") i.e. tile #"+moveInfo[0]);

          int supId = board.searchSupply(moveInfo[0]);
          //I make the assumption that Theseus always has id = 0
          if(getPlayerId() == 0){
            moveInfo[3] = supId;
          }
          else{
            moveInfo[3] = -2; //Minotaurus characteristic value
          }

          //if supId<0 then there is no supply in the tile that I will move
          if(getPlayerId() == 0 && supId >= 0){
            score += 1;
            System.out.println(name + " collected supply with id " + (supId+1)+" !");
          }

        }
        else{
          moveInfo[0] = x*N + y;
          moveInfo[1] = x;
          moveInfo[2] = y;
          if(getPlayerId() == 0)
            moveInfo[3] = -1;
          else
            moveInfo[3] = -2;
          //inform about the move
          System.out.println(getName() + " cannot move " + "down!");
        }
      break;
      case 7:
        if(!board.getTile(playerTile).getLeft()){


          moveInfo[1] = x;
          moveInfo[2] = y - 1;
          moveInfo[0] = x*N + (y - 1);
          y -= 1; //we move left(change in column)
          //x(row index) is the same
          System.out.println(getName()+" now is in (" + moveInfo[1] + "," + moveInfo[2] + ") i.e. tile #" + moveInfo[0]);


          int supId = board.searchSupply(moveInfo[0]);
          //I make the assumption that Theseus always has id = 0
          if(getPlayerId() == 0){
            moveInfo[3] = supId;
          }
          else{
            moveInfo[3] = -2; //Minotaurus characteristic value
          }
          if(getPlayerId() == 0 && supId >= 0){
            score += 1;
            System.out.println(name + " collected supply with id " + (supId+1)+" !");
          }

        }
        else{
          moveInfo[0] = x*N + y;
          moveInfo[1] = x;
          moveInfo[2] = y;
          if(getPlayerId() == 0)
            moveInfo[3] = -1;
          else
            moveInfo[3] = -2;
          //inform about the move
          System.out.println(getName() + " cannot move " + "left!");
        }
      break;
      default:
        System.out.println("Error in the code!");
    }


    Game.removeSupply(board, moveInfo[3]);

    return moveInfo;
  }

}
