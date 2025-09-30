/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;

public class Chunk {
    public int chunkX, chunkY; // chunk pos 
    public int[][] mapTileNum;// index tile
    public int size;
    
    public Chunk(int chunkX, int chunkY, int size){
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.size = size;
        mapTileNum = new int[size][size];
    }
}
