import java.util.Arrays; //for sorting an array

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

    // for every tile we store how many times we have accessed it for wall placing
    //we will put 1 or 2
    //0 means no walls placed
    //1 means checked once
    //2 twice
    //if checked twice continue
    //if count is close to W = N*N-2N-2 then find only those who have been visited 0 or 1 time
    int[] visited = new int[N*N];

    for(int i = 0; i < N*N; i++){
      tiles[i].setTileId(i);

      tiles[i].setX(Game.div(i,N));
      tiles[i].setY(Game.mod(i,N));

      //Now the only task left to complete the board is to set the walls
      //start_wall_strategy
      visited[i] = 0;


      //the 4 successive if-statements set a wall around the maze
      if(tiles[i].getX() == 0){
        tiles[i].setDown(true);
        visited[i] = 1;
        count++;
        //Special case::The entrance of the maze according to image1
        if(i == 0){
          tiles[i].setDown(false);
          visited[i] = 1;
          count--;
        }

      }
      if(tiles[i].getX() == (N -1)){
        tiles[i].setUp(true);
        visited[i] = 1;
        count++;
      }
      if(tiles[i].getY() == 0){
        tiles[i].setLeft(true);
        visited[i] = 1;
        if(tiles[i].getX() == N -1){
          visited[i] = 2;
        }
        count++;
      }
      if(tiles[i].getY() == (N-1)){
        tiles[i].setRight(true);
        visited[i] = 1;
        if(tiles[i].getX() == (N-1) || tiles[i].getX() == 0){
          visited[i] = 2;
        }
        count++;
      }
    }
    for(int i = 0; i < N; i++){
      for(int j = 0; j < N; j++){
        System.out.print(visited[(N-i-1)*N+j]+" ");
      }
      System.out.println();
    }

    //Here comes the "randomness"



    //I must be careful with the edges which already have 2 walls
    //That's why I added the method has2Walls() in Tile class
    //Moreover we must check if any of the neighbooring tiles has 2 walls
    while(count < W){
      int temp = (int) (Math.random()*101*N*N);
      int randomId = Game.mod(temp, N*N);
      int randX = tiles[randomId].getX(); // x coord of tile with randomId
      int randY = tiles[randomId].getY(); //y coord of tile with randomId
      //Every tile has a maximum of 4 neighboor tiles(up, down, left, right)

      //follow this strategy only after placing lots of walls
      if(count >= N*N){ //because chances that we hit an available tile are small

        int[] seq = findAvailableTiles(visited); //holds all tileIds with less than 2 walls

        int[] order = randomSeq(seq.length);  //the order in which I will try to place walls in the tiles that are available

        //loop over availableTiles could be done with a random permutation of the set {0, 1,...,seq.length}
        for(int i = 0; i < seq.length; i++){

          //for the definition-implementation of randomSeq watch below(extra methods of class Board)
          int[] dice = randomSeq(4); //array holding a random permutation of the set {0,1,2,3}
          for(int j = 0; j < 4; j++){
            if(tryWallPos(seq[order[i]], dice[j])){
              int tempX = tiles[seq[order[i]]].getX();
              int tempY = tiles[seq[order[i]]].getY();
              visited[seq[order[i]]]++;
              switch(dice[j]){
                case 0: visited[(tempX+1)*N + tempY]++; break;
                case 1: visited[(tempX-1)*N + tempY]++; break;
                case 2: visited[tempX*N + tempY-1]++; break;
                case 3: visited[tempX*N + tempY+1]++; break;
              }
              //increment counter
              count++;
              break;

            }
          }
        }
      }
      if(!tiles[randomId].has2Walls()){
        int[] dice = randomSeq(4);
        for(int i = 0; i < 4; i++){
          if(tryWallPos(randomId, dice[i])){
            //if the tile with randomid had more than 2 walls the following would not have been executed
            //because tryWallPos would return false

            //update the times we have visited-placed a wall in this tile with randomId
            visited[randomId]++;
            //also update the neighbooring tile according to the position we place the wall
            switch(dice[i]){
              //note that there is no fear of gooing out of bounds
              //because tryWallPos would return false if closeId was not valid.
              case 0: visited[(randX+1)*N + randY]++; break;  //up
              case 1: visited[(randX-1)*N + randY]++; break;  //down
              case 2: visited[randX*N + randY-1]++; break;  //left
              case 3: visited[randX*N+ randY+1]++; break;   //right
            }
            //increment counter
            count++;
            break;
          }
        }
      }
    }
    //end of createTile()
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

  /**
  @param len  the number of elements of the permutation set {0,1,...,len-1}
  returns 0,1,...,len-1 in one of the possible ways (len! ways in total)
  utilizing Math.random()
  */
  public int[] randomSeq(int len){
    int[] seq = new int[len];
    for(int i = 0; i < len; i++){
      seq[i] = -1;
    }
    int dice;
    do{
      dice = (int)(Math.random()*101*len);
      dice = Game.mod(dice, len);
      //checks if dice exists as a value in randomSeq
      //and if it does not it places dice in the next position
      // that has not yet been occupied by a number in {0,1,2,3}
      int i;
      for(i = 0; i < len; i ++){
        if(seq[i] == dice){
          break;
        }
        else if(seq[i] == -1){
          seq[i] = dice;
          break;
        }
      }
      if(seq[len-1] != -1)
        break;
    }while(true);
    return seq;
  }

  /**
  @return was the wall placing succesful
  */

  public boolean tryWallPos(int randomId, int dice){
    int randX = tiles[randomId].getX();
    int randY = tiles[randomId].getY();
    if(!tiles[randomId].has2Walls()){

      if(dice == 0 && !tiles[randomId].getUp() && tiles[randomId].closeIsValid(randX + 1, N)){
        int closeId = (randX + 1)*N + randY;
        //if the close Tile has less than 2 walls
        if(!tiles[closeId].has2Walls()){
          tiles[randomId].setUp(true);
          tiles[closeId].setDown(true);
          return true;
        }
      }
      else if(dice == 1 && !tiles[randomId].getDown() && tiles[randomId].closeIsValid(randX - 1, N)){
        int closeId = (randX - 1)*N + randY;
        //if the close Tile has less than 2 walls
        if(!tiles[closeId].has2Walls()){
          tiles[randomId].setDown(true);
          tiles[closeId].setUp(true);
          return true;
        }
      }
      else if(dice == 2 && !tiles[randomId].getLeft() && tiles[randomId].closeIsValid(randY - 1, N)){
      int closeId = randX * N + randY - 1;
        //if the close Tile has less than 2 walls
        if(!tiles[closeId].has2Walls()){
          tiles[randomId].setLeft(true);
          tiles[closeId].setRight(true);
          return true;
        }
      }
      else if(dice == 3 && !tiles[randomId].getRight() && tiles[randomId].closeIsValid(randY + 1, N)){
        int closeId = randX * N + randY + 1;
        //if the close Tile has less than 2 walls
        if(!tiles[closeId].has2Walls()){
          tiles[randomId].setRight(true);
          tiles[closeId].setLeft(true);
          return true;
        }
      }
    }
    return false;
  }

  /**
  @return an int array with ids of all available tiles
  */
  public int[] findAvailableTiles(int[] visited) {
    int[] temp = new int[N*N];
    int count = 0;
    for(int i = 0; i < N*N; i++){
      if(visited[i] == 2)
        continue;
      temp[i] = i;
      count++;
    }
    //Now we need to copy those elements of temp to the array that will be returned
    // which will have size count

    //this will be the index of seq
    int end = 0;
    int[] seq = new int[count];

    //we collect all initialized elements of temp from the previous loop
    for(int i = 0; i < N*N; i++){
      //leave out uninitialized elements of temp
      if(visited[i] == 2)
        continue;

      //copy the elements one by one to the new array
      seq[end] = temp[i];
      //increment the index in order to give more elements in the next iteration
      end++;
      //if we have filled seq with all the ids that we desired exit
      if(end == count)
        break;
    }
    return seq;
  }

//end of class Board
}
