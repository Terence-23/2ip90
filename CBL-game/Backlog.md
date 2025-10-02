# Topics:
- git (we use git to track all the code for this project, each feature in backlog gets a commit)
- collision algorithms

# Player capability:
- Moving \[user input\](stuff moves around the player; player always centered)
- Shooting (bullets appearing on screen and moving towards mouse position)
- track health (health counter on the screen)
- end game on no health (Game over when health = 0)
- Picking up items (items disappearing when player is on them)

# Bullet capability:
- linear movement (moves on screen)
- hit enemy (bullet disappears on hit)
- deal damage to hit enemy (enemies die after some bullets)

# Enemy capability
- random spawn around player \[maybe in groups\] (enemies appear on the screen)
- track player (go towards player on screen)
- attack player if in range (player takes damage if close enough)
- die on no health (disappear from screen)
- randomly drop item on death (item maybe appears after death)

# General capability
 - draw things on screen (things can be seeen on screen)
 - start game (player appears in the middle)
 - detect collision of objects (unit test passes) (collision algorithms)
 - restart game after end (same as start but after Game Over)

