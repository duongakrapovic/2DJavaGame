/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;

public class Chunk {
    public int chunkX, chunkY; // Chunk position in the map grid
    public int[][] mapTileNum; // 2D array storing tile indices for this chunk
    public int size;// Size of the chunk (number of tiles per side)

    public Chunk(int chunkX, int chunkY, int size){
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.size = size;
        mapTileNum = new int[size][size];// initialize tile indices
    }
}
