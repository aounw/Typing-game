# Typing-game

- Summary: 
  - This game challenges players to type words that appear on the screen as quickly as possible. Each word is represented by an `IWord` 
    object and moves down the screen at a pace that increases with each level. The game utilizes a world represented by the `ZTypeWorld` class, 
    where words (`enemyWords`) move down and must be typed before they reach the bottom of the screen.

- How the Game Ends: 
  - The game ends when any word reaches the bottom of the screen. The `tempEnd` flag becomes true, and the game displays a "Game Over" message 
    along with the final score and the option to restart.

- How the Score Works: 
  - The player's score increases by 10 points for each word successfully typed and removed from the screen. The score is displayed continuously, 
    and levels are determined based on the score, with new levels reached every 50 points.

- How Levels Work: 
  - Levels increase as the player's score reaches multiples of 50. Each new level increases the speed at which words move down the screen. 
    The level counter and the adjusted speed are displayed on the screen.

- Rate of Word Increase Per Level: 
  - As the player progresses through levels, the rate at which words move down the screen increases. This is managed by decreasing the 
    counter interval between new words being added, with a minimum speed to ensure the game remains playable.

- Option to Restart the Game: 
  - Players can restart the game at any time by pressing the TAB key. This resets the game world to its initial state with no words on 
    the screen, a score of zero, and the level counter reset.

- Variable Word Lengths: 
  - Words that appear in the game can vary in length. New words are generated randomly with lengths that can 
    change each time a word is added to the screen.

- Technical Implementation Details: 
  - The game is implemented in Java, using classes that extend `World` from the `javalib.funworld` library. It employs interfaces (`IWord`, `ILoWord`) 
    and classes (`ActiveWord`, `InactiveWord`, `ConsLoWord`, `MtLoWord`) to manage words and their behaviors. The game loop is driven by tick events (`onTick()`) that move words down the screen and respond to player input (`onKeyEvent(String)`).
