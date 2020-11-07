public class Board{
  private int N;
  private int S;
  private int W;
  private Tile[] tiles;
  private Supply[] supplies;

  public Board(){
    N = 0; ///we have not yet created the grid of the maze
    S = 0;  //no supplies on the board yet
    W = 0; // 0 walls are placed in the beginning

    tiles = new Tile[N*N];
    supplies = new Supply[S];

  }

  public Board(int N, int S, int W){
    this.N = N;
    this.S = S;
    this.W = W;

    tiles = new Tile[N*N];
    for(int i = 0; i < N*N; i++){
      tiles[i] = new Tile();
    }

    supplies = new Supply[S];
    for(int i = 0; i < S; i++){
      supplies[i] = new Supply();
    }
    //I assume that initialization happens at createTile() and at createSupply()
    //which are called at createBoard()
  }

  /**
  acts partly as a copy constructor(for N,S,W).
  but we have common tiles, supplies matrices with the object @param b
  We want to have the "same" board in our Game fot every player!
  */
  public Board(Board b){

    N = b.N;
    S = b.S;
    W = b.W;

    this.tiles = b.getTiles();  //assigns the address of b.getTiles(), so this.tiles is a reference of b.getTiles()
    this.supplies = b.getSupplies();  ////same as above this.supplies is now a reference of b.getSupplies()

  }

  // Setters and Getters
  public void setN(int N){
    this.N = N;
  }
  public int getN(){
    return N;
  }

  public void setS(int S){
    this.S = S;
  }
  public int getS(){
    return S;
  }

  public void setW(int W){
    this.W = W;
  }
  public int getW(){
    return W;
  }

  public Tile[] getTiles(){
    return tiles;
  }
  public Tile getTile(int i){
    if(i >= 0 && i < N*N)
      return tiles[i];
    else {
      System.out.println("Error in getTile. Index out of bounds!");
      System.out.println("i = "+i);
      System.exit(1);
      return tiles[i];  //this would give an error but we exit!
      //we just persuade the compiler that we return a Tile object
    }
  }

  public void setTile(Tile t, int i){
    tiles[i] = t;
  }

  public Supply[] getSupplies(){
    return supplies;
  }

  public Supply getSupply(int i){
    if(i >= 0 && i < S)
      return supplies[i];
      else {
        System.out.println("Error in getSupply. Index out of bounds!");
        System.out.println("i = "+i);
        System.exit(1);
        return supplies[i]; //this would give an error but we exit!
        //we just persuade the compiler that we return a Supply object
      }
  }

  public void setSupply(Supply s, int i){
    supplies[i] = s;
  }



  public void createTile(){
    int count = 0;  //how many walls have I placed?
    // count <= W

    for(int i = 0; i < N*N; i++){
      tiles[i].setTileId(i);

      tiles[i].setX(Game.div(i,N));
      tiles[i].setY(Game.mod(i,N));

      //Now the only task left to complete the board is to set the walls
      //start_wall_strategy



      //the 4 successive if-statements set a wall around the maze
      if(tiles[i].getX() == 0){
        tiles[i].setDown(true);
        count++;
        //Special case::The entrance of the maze according to image1
        if(i == 0){
          tiles[i].setDown(false);
          count--;
        }

      }
      if(tiles[i].getX() == (N -1)){
        tiles[i].setUp(true);
        count++;
      }
      if(tiles[i].getY() == 0){
        tiles[i].setLeft(true);
        count++;
      }
      if(tiles[i].getY() == (N-1)){
        tiles[i].setRight(true);
        count++;
      }
    }

    //Here comes the "randomness"


    //I must be careful with the edges which already have 2 walls
    //That's why I added the method has2Walls() in Tile class
    //Moreover we must check if any of the neighbooring tiles has 2 walls
    while(count < W){
      int temp = (int) (Math.random()*101*N*N);
      int randomId = Game.mod(temp, N*N);
      int randX = tiles[randomId].getX();
      int randY = tiles[randomId].getY();
      //Every tile has a maximum of 4 neighboor tiles(up, down, left, right)

      if(!tiles[randomId].has2Walls()){

        int dice = (int)(Math.random()*101*4);
        dice = Game.mod(dice, 4);


        if(dice == 0 && !tiles[randomId].getUp() && tiles[randomId].closeIsValid(randX + 1, N)){
          int closeId = (randX + 1)*N + randY;
          //if the close Tile has less than 2 walls
          if(!tiles[closeId].has2Walls()){
            tiles[randomId].setUp(true);
            tiles[closeId].setDown(true);
            count++;
          }
        }
        else if(dice == 1 && !tiles[randomId].getDown() && tiles[randomId].closeIsValid(randX - 1, N)){
          int closeId = (randX - 1)*N + randY;
          //if the close Tile has less than 2 walls
          if(!tiles[closeId].has2Walls()){
            tiles[randomId].setDown(true);
            tiles[closeId].setUp(true);
            count++;
          }
        }
        else if(dice == 2 && !tiles[randomId].getLeft() && tiles[randomId].closeIsValid(randY - 1, N)){
          int closeId = randX * N + randY - 1;
          //if the close Tile has less than 2 walls
          if(!tiles[closeId].has2Walls()){
            tiles[randomId].setLeft(true);
            tiles[closeId].setRight(true);
            count++;
          }
        }
        else if(dice == 3 && !tiles[randomId].getRight() && tiles[randomId].closeIsValid(randY + 1, N)){
          int closeId = randX * N + randY + 1;
          //if the close Tile has less than 2 walls
          if(!tiles[closeId].has2Walls()){
            tiles[randomId].setRight(true);
            tiles[closeId].setLeft(true);
            count++;
          }
        }

      }
    }

  }

  public void createSupply(){
    //supply id is set for each objecτ of the matrix
    for(int i = 0; i < S; i++){
      supplies[i].setSupplyId(i);
    }

    int count = 0;  // condition: count <= S
    //So when count exceeds or equals to S we are finished with our job
    int randomTile,randomId;
    int temp1,temp2;
    while(count < S){
      temp1 = (int) (Math.random()*101*S);
      randomId = Game.mod(temp1, S);  //this index refers to an object of the supplies matrix
      temp2 = (int) (Math.random()*101*N*N);
      randomTile = Game.mod(temp2, N*N);//this refers to the tileId in which we will place the object


      //Initially Theseus is located at (0,0)<->0
      // and Minotaurus at (Ndiv2, Ndiv2) <-> N*Ndiv2 + Ndiv2
      //Thus we need to avoid placing supplies at those tiles
      if(randomTile != 0 && randomTile != (N*Game.div(N, 2) + Game.div(N, 2))) {
        //We must also check if the tile has already a supply in it
        boolean flag = true;
        for(int i = 0; i < S; i++){
          if(supplies[i].getSupplyTileId() == randomTile){
            flag = false;
            break;
          }
        }
        if(supplies[randomId].getSupplyTileId() < 0 && flag){

          supplies[randomId].setSupplyTileId(randomTile);//Now the supply is placed in the board
          supplies[randomId].setX(Game.div(randomTile, N));//x-coord of supplyTileId of the supply above
          supplies[randomId].setY(Game.mod(randomTile, N));//y-coord of supplyTileId of the supply above
          count++;
        }
      }
    }


  }

  public void createBoard(){
    createTile(); //initialize properly the tiles matrix i.e. every tile of the maze

    createSupply(); //place the supplies on the board
  }

  public String[][] getStringRepresentation(int theseusTile, int minotaurTile){
    String[][] table = new String[2*N + 1][N];

    /*Every tile needs 2 rows in order to be correctly represented
    We loop twice throughout the whole String matrix (that Represents the board)
    taking care of the 1st row in the first loop
    as well as the 2nd row in the second double-for loop */

    //Note that in the first loop we also consider the case of the tiles that require 3 rows to be represented

    //Both double-for loops start from top left

    //1st double-for loop
    for(int i = N - 1; i >= 0; i--){
      for(int j = 0; j < N; j++){
        //1st row of tile with tempId
        int tempId = i*N+j;
        String temp = "+";
        if(tiles[tempId].getUp()){
          temp += " - - - ";
        }
        else{
          temp += "       ";
        }
        if(j == (N - 1)){
          temp += "+\n";
        }
        table[2*N-2*(i+1)][j] = temp;

        //Bottom row with walls i.e. i = 0
        if(i == 0){
          temp = "+";
          if(tiles[tempId].getDown()){
            temp += " - - - ";
          }
          else{
            temp += "       ";
          }
          if(j == N-1){
            temp += "+\n";
          }
          table[2*N][j] = temp;
        }

      }
    }

    //2nd double-for loop
    for(int i = N - 1; i >= 0; i--){
      for(int j = 0; j < N; j++){
        int tempId = i*N+j;
        String temp = "";
        //2nd row of tile with tempId
        if(j == 0){
          temp = "|";
        }

        /*If there exists a supply with supplyTileId == tempId*/
        int supId = -1;
        for(int k = 0; k < S; k++){
          //Does there exist a supply that is placed in tile with id := tempId
          if(supplies[k].getSupplyTileId() == tempId){
            //If so, make supId be the index of this supply
            supId = supplies[k].getSupplyId();
            break;
          }
        }

        if(minotaurTile == theseusTile && minotaurTile == tempId){
          //if theseus collected all supplies he wins the game
          if(countSupplies() == 0){
            temp += "   T   ";
          }
          else{
            temp += "   M   ";
          }
        }
        else if(minotaurTile == tempId && supId!=-1){
          temp += (" M, s" + (supId + 1) + " ");
        }
        else if(minotaurTile == tempId){
          temp += "   M   ";
        }
        else if(theseusTile == tempId){
          temp += "   T   ";
        }
        else if(supId != -1){
          temp += ("   s" + (supId + 1) + "  ");
        }
        else{
          temp += "       ";
        }
        /*Now we have to check if the tile has a right wall*/
        if(tiles[tempId].getRight()){
          temp += "|";
        }
        else{
          temp += " ";
        }
        if(j == N-1){
          temp += "\n";
        }
        table[2*N-2*(i+1) + 1][j] = temp;

      }
    }
    return table;
  }

  //extra method

  /**
  *@param tileId :possible location of the supply
  *@return
  the index of the supplies matrix if the given tileId holds a supply
  or
  -1 if no supply is positioned in the given tileId
  */
  public int searchSupply(int tileId){
    int index = -1;
    for(int i = 0; i < S; i++){
      if(supplies[i].getSupplyTileId() == tileId){
        index = i;
        return index;
      }
    }
    return index;
  }


  /**This method counts the total number of supplies
  that are currently placed in the maze
  @return the number of supplies currently on the maze
  */
  public int countSupplies(){
    int count = 0;
    for(int i = 0; i < S; i++){
      if(getSupply(i).getSupplyTileId() >= 0 && getSupply(i).getSupplyTileId() <= (N*N - 1)){
        count++;
      }
    }
    return count;
  }


}
