public class Tile{
  private int tileId;
  private int x;
  private int y;
  private boolean up;
  private boolean down;
  private boolean left;
  private boolean right;

  public Tile(){
    tileId = -1;
    x = -1;
    y = -1;
    up = false;
    down = false;
    left = false;
    right = false;
  }
  public Tile(int tileId, int x, int y, boolean up, boolean down, boolean left, boolean right){
    this.tileId = tileId;
    this.x = x;
    this.y = y;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
  }
  //acts as a copy constructor
  public Tile(Tile t){
    tileId = t.tileId;
    x = t.x;
    y = t.y;
    up = t.up;
    down = t.down;
    left = t.left;
    right = t.right;
  }

  public void setTileId(int tileId){
    this.tileId = tileId;
  }
  public int getTileId(){
    return tileId;
  }

  /** Set and Get x coordinate of the Tile object*/
  public void setX(int x){
    this.x = x;
  }
  public int getX(){
    return x;
  }


  public void setY(int y){
    this.y = y;
  }
  public int getY(){
    return y;
  }


  public void setUp(boolean up){
    this.up = up;
  }
  public boolean getUp(){
    return up;
  }


  public void setDown(boolean down){
    this.down = down;
  }
  public boolean getDown(){
    return down;
  }


  public void setLeft(boolean left){
    this.left = left;
  }
  public boolean getLeft(){
    return left;
  }


  public void setRight(boolean right){
    this.right = right;
  }
  public boolean getRight(){
    return right;
  }
  //extra methods

  /** @return we have
  either
  1) true: means the tile has 2 walls
  or
  2) false: means the tile has less than 2 walls
  */
  public boolean has2Walls(){
    int count = 0;
    if(up == true){
      count++;
    }
    if(down == true){
      count++;
    }
    if(left == true){
      count++;
    }
    if(right == true){
      count++;
    }
    //We finished counting the number of walls in all possible directions
    if(count < 2){
      return false;
    }
    else{
      return true;
    }
  }

  /** when looking info on neighbooring tiles we must ensure that
  we stay in BOUNDS */
  public boolean closeIsValid(int x, int N){
    if(x >= 0 && x < N)
      return true;
    return false;
  }

}
