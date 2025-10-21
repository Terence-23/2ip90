# Topics:
- git (we use git to track all the code for this project, each feature in backlog gets a commit)
- collision algorithms

# Player capability:
- [x] Moving \[user input\](stuff moves around the player; player always centered)
- [x] Shooting (bullets appearing on screen and moving towards mouse position)
- [x] track health (health counter on the screen)
- [x] end game on no health (Game over when health = 0)
- [x] Picking up items (items disappearing when player is on them)

# Bullet capability:
- [x] linear movement (moves on screen)
- [x] hit enemy (bullet disappears on hit)
- [x] deal damage to hit enemy (enemies die after some bullets)

# Enemy capability
- [x] random spawn around player \[maybe in groups\] (enemies appear on the screen)
- [x] track player (go towards player on screen)
- [x] attack player if in range (player takes damage if close enough)
- [x] die on no health (disappear from screen)
- [x] randomly drop item on death (item maybe appears after death)

# General capability
 - [x] draw things on screen (things can be seeen on screen)
 - [x] start game (player appears in the middle)
 - [x] detect collision of objects (unit test passes) (collision algorithms)
 - [x] restart game after end (same as start but after Game Over)

