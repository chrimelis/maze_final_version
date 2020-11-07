public class Supply{
  //The fields of Supply class
  private int supplyId;
  private int x;
  private int y;
  private int supplyTileId;

  //Constructors
  public Supply(){
    supplyId = -1;
    x = -1;
    y = -1;
    supplyTileId = -1;  //this means that the supply is not present in a tile
  }
  public Supply(int supplyId, int x, int y, int supplyTileId){
    this.supplyId = supplyId;
    this.x = x;
    this.y = y;
    this.supplyTileId = supplyTileId;
  }
  //acts as a copy constructor
  public Supply(Supply s){
    this.supplyId = s.supplyId;
    this.x = s.x;
    this.y = s.y;
    this.supplyTileId = s.supplyTileId;
  }

  //Setters and Getters

  /**
   * @return the supplyId
   */
  public int getSupplyId() {
  	return supplyId;
  }
  /**
   * @param supplyId the supplyId to set
   */
  public void setSupplyId(int supplyId) {
  	this.supplyId = supplyId;
  }

  /**
   * @return the x coord of the supply
   */
  public int getX() {
  	return x;
  }
  /**
   * @param x the x coord of the supply
   */
  public void setX(int x) {
  	this.x = x;
  }

  /**
   * @return the y coord of the supply
   */
  public int getY() {
  	return y;
  }
  /**
   * @param y the y coord of the supply
   */
  public void setY(int y) {
  	this.y = y;
  }

  /**
   * @return the supplyTileId
   */
  public int getSupplyTileId() {
  	return supplyTileId;
  }
  /**
   * @param supplyTileId the supplyTileId to set
   */
  public void setSupplyTileId(int supplyTileId) {
  	this.supplyTileId = supplyTileId;
  }
}
