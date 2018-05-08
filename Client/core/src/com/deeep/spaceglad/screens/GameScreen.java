package com.deeep.spaceglad.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deeep.spaceglad.Core;
import com.deeep.spaceglad.GameWorld;
import com.deeep.spaceglad.Settings;
import com.deeep.spaceglad.UI.GameUI;
import com.deeep.spaceglad.components.AvatarComponent;
import com.deeep.spaceglad.components.PlayerComponent;


/**
 * Created by scanevaro on 31/07/2015.
 */
public class GameScreen implements Screen {
	Core game;
	GameUI gameUI;
	GameWorld gameWorld;

	public GameScreen(Core game) {
		this.game = game;
		gameUI = new GameUI(game);
		gameWorld = new GameWorld(gameUI, game);
		Settings.Paused = false;
		Gdx.input.setInputProcessor(gameUI.stage);
		Gdx.input.setCursorCatched(true); // capture cursor for aiming -Paul
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {

		// Handle server messages
		if (game.client != null) { // Should only be null if offline
			while (game.client.getQueueLength() > 0) {
				String msg = game.client.getNextMessage();
				String params[] = msg.split(" ");
                
				// Remove \n
				params[params.length - 1] = params[params.length - 1].substring(0, params[params.length - 1].length() - 1);
                
				Entity entity;
				switch (params[0]) {
				
					// Chat Messages
					case "\\say":
						gameUI.messageWidget.addChatMessage(msg.substring(5));
						break;
					case "\\inform":
						gameUI.messageWidget.addChatMessage("Server: " + msg.substring(8));
						break;
						
					// Login
					case "\\avatar":
						// TODO params[5] = max(rotation.y, 1.5)
						if (params[1] == game.client.username) {
							// TODO restore player position on login
						} else {
							gameWorld.addPlayer(params[1], Float.parseFloat(params[2]),
									Float.parseFloat(params[3]),
									Float.parseFloat(params[4]), Float.parseFloat(params[5]));
						}
						break;
						
					// Logout
                    case "\\delavatar":
                        if ((entity = (Entity) gameWorld.avatarSystem.getPlayersList().get(params[1])) != null) {
                            gameWorld.avatarSystem.getPlayersList().remove(params[1]);
                            gameWorld.remove(entity);
                        }
                        break;
                        
                    // Damage
					case "\\damage":
						if ((entity = (Entity) gameWorld.avatarSystem.getPlayersList().get(params[1])) != null)
							entity.getComponent(AvatarComponent.class).health -= Integer.parseInt(params[2]);
						else
							gameWorld.playerSystem.getPlayer().getComponent(PlayerComponent.class).
									health -= Integer.parseInt(params[2]);
						break;
						
					// Move
					case "\\move":
						if ((entity = (Entity) gameWorld.avatarSystem.getPlayersList().get(params[1])) != null) {
							entity.getComponent(AvatarComponent.class).x = Float.parseFloat(params[2]);
							entity.getComponent(AvatarComponent.class).y = Float.parseFloat(params[3]);
							entity.getComponent(AvatarComponent.class).z = Float.parseFloat(params[4]);
							entity.getComponent(AvatarComponent.class).rotAngle = Float.parseFloat(params[5]);
							// TODO params[5] = max(rotation.y, 1.5)
						}
						break;

					// Empty Message
                    case "":
                    	break;
                    	
                	// Extraneous Messages
                    case "\\Whoami":
                    case "\\vgetip":
                    case "\\dat":
                    case "\\dynstate":
                    case "\\Addusers":
                    case "\\c":
                    case "\\Deleteuser":
                    	// TODO are any of these needed?
                    	break;
                    	
                	// TODO Handle more commands
					// Unhandled commands are pushed to chat window
					default:
						gameUI.messageWidget.addChatMessage(msg);
				}
			}
		}

		/** Updates */
		gameUI.update(delta);
		/** Draw */
		gameWorld.render(delta);
		gameUI.render();
	}

	@Override
	public void resize(int width, int height) {
		gameUI.resize(width, height);
		gameWorld.resize(width, height);
	}

	@Override
	public void dispose() {
		gameWorld.dispose();
		gameUI.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}
}
