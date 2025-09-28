/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

public class GameStateManager {

    public GameState currentState = GameState.START;
    
    public GameState getState() {
        return currentState;
    }

    public void setState(GameState state) {
        currentState = state;
    }
}
