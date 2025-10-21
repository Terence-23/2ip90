# Requirements:
- java jdk>=17 with java executable on PATH
- sh shell or windows cmd to execute run scripts

# Running
## Linux/MacOs
Run the `game.sh` script in the root directory of the game. It should do everything on its own. (Note that Wayland is not supported by Swing and in conscequence incompatible with this game)

## Windows
Run the `game.bat` script in the root directory of the game. (Note that if running in powershell you may need to set the execution policy [https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.security/set-executionpolicy?view=powershell-7.5](https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.security/set-executionpolicy?view=powershell-7.5))

# Testing
Below There is a list of all features and how they should be tested:

| Feature | Testing steps |
| --- | --- |
| Game Start | Upon launching the game there should be a fullscreen button  described start. Clicking it should result in the player becoming visible in the middle of the screen and the background beckoming grey. |
| Walking | WASD keys are used for walking. As the background is monochrome the walking can be observed by bullets no longer following straight lines. |
| Shooting | Upon clicking the left mouse button there should be bullets flying from the player to the cursor position. The bullets may miss the cursor if either the player or the cursor move|
| Enemy spawn | Periodically enemies should appear around the player in groups. |
| Enemy damage | enemies should damage player if close enough|
| Shooting enemies | upon hitting an enemy the hitting bullet should disappear and damage the hit enemy | 
| Killing Enemies | Enemies should die upon being hit a few times. They sometimes drop upgrades (green boxes) |
| Upgrades | Upgrades dropped by enemies can be picked up. Upon pickup one of three things should happen: increase in speed, increase in damage or healing of the player (Health and damage are displayed in the top left corner). |
| Game over and restart | When player dies (Health reduced to zero) the game should end (Big game over text on the screen). It is then possible to restart the game by pressing restart. |

