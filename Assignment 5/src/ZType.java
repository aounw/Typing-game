import tester.*;             
import javalib.funworld.*;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import java.awt.Color;
import java.util.Random; 

//constants class used throughout
interface IConstants {
  int WIDTH = 400;
  int HEIGHT = 700;
  String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
  int UI_WIDTH = WIDTH * 2;
  int UI_HEIGHT = HEIGHT / 5;
  int MIN_COUNTER = 5;
}

//class for all of the world functions
class ZTypeWorld extends World implements IConstants {
  ILoWord enemyWords;
  int counter;
  int lvlCounter;
  int score;
  Random rand;

  ZTypeWorld(
      ILoWord enemyWords, int counter, int lvlCounter, int score, Random rand) {
    this.enemyWords = enemyWords;
    this.counter = counter;
    this.lvlCounter = lvlCounter;
    this.score = score;
    this.rand = rand;
  }

  /* fields:
   *  this.enemyWords ... ILoWord
   *  this.counter ... int
   *  this.lvlCounter ... int
   *  this.score ... int
   *  this.rand ... Random
   * methods:
   *  this.makeBackground() ... WorldScene
   *  this.drawMainScene() ... WorldScene
   *  this.drawEndScene() ... WorldScene
   *  this.makeScene() ... WorldScene
   *  this.onTick() ... ZTypeWorld
   *  this.onTickForTesting ... ZTypeWorld
   *  this.levelup() .. int
   *  this.adjustCounterBasedOnLevel() ... int
   *  this.onKeyEvent(String) ... ZTypeWorld
   */

  //creates the backdrop based on 2 constants for 
  //the width and height
  public WorldScene makeBackground() {
    return new WorldScene(IConstants.WIDTH, HEIGHT);
  }

  //draws the words onto the background as well as displays 
  //the restart instructions, current score, 
  //and level details
  public WorldScene drawMainScene() {
    return this.enemyWords.draw(this.makeBackground())
        .placeImageXY(new RectangleImage(
            UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
            0, 0)
        .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
        .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
        .placeImageXY(new TextImage("Score: " + this.score, Color.WHITE), 200, 55)
        .placeImageXY(new TextImage("Level:" + this.lvlCounter + " Speed:" 
            + adjustCounterBasedOnLevel() + " ticks per word", 10, Color.WHITE), 
            200, 30);
  }

  //draws the final scene of the game onto the world
  //displays a game over onto the world
  public WorldScene drawEndScene() {
    return this.drawMainScene()
        .placeImageXY(new TextImage("Game Over", 50, Color.BLACK), 
            WIDTH / 2, HEIGHT / 2);
  }

  //makes a scene for every tick of the clock depending
  //on if a word has reached the bottom
  public WorldScene makeScene() {
    if (this.enemyWords.anyReachedBottom()) {
      return this.drawEndScene();
    } else {
      return this.drawMainScene();
    }
  }

  //move a Word down on the scene by the tick of the clock
  //adds a new word by 20 ticks
  //removes an empty word from the list and adds 10 to the score
  //stops if a word reached the bottom
  public ZTypeWorld onTick() {  
    int newCounter = adjustCounterBasedOnLevel();
    //case to reset counter based on level
    ZTypeWorld resetCase = new ZTypeWorld(this.enemyWords
        .moveWords() //moves the words down by 3 and 5
        .addToEnd(new Utils( //adds a new random words to the list
            new Random()).wordWrap(this.rand.nextInt(10))) 
        .filterOutEmpties(), //removes the empty elements
        newCounter, this.levelUp(), this.score, this.rand);
    //case to add 10 to the score should an empty word be removed 
    //and determine if level should increase
    ZTypeWorld addScoreCase = new ZTypeWorld(enemyWords
        .moveWords() //adds a score of increments + 10
        .filterOutEmpties(), 
        this.counter, this.levelUp(), this.score + 10, this.rand);
    //case to reduce the counter by 1
    ZTypeWorld reduceCase = new ZTypeWorld(enemyWords.moveWords() //reduces the counter by 1
        .filterOutEmpties(), 
        this.counter - 1, this.levelUp(), this.score, this.rand);
    //if the clock is 0 then add a word
    if (this.counter == 0) {
      return resetCase; 
      //if a word has reached the bottom, return the current scene
    } else if (this.enemyWords.anyReachedBottom()) { 
      return this;
      //if a list has empty elements, add 10 to the score and calculate level
    } else if (this.enemyWords.finishedWord()) { 
      return addScoreCase;
    } else {
      return reduceCase;
    }
  }

  //move a Word down on the scene by the tick of the clock
  //uses a seeded random object for making the Utils to make it easier for testing
  public ZTypeWorld onTickForTesting() {
    int newCounter = adjustCounterBasedOnLevel();
    //case to reset counter based on level
    ZTypeWorld resetCase = new ZTypeWorld(this.enemyWords
        .moveWords() //moves the words down by 3 and 5
        .addToEnd(new Utils( //adds a new random words to the list
            new Random(2)).wordWrap(this.rand.nextInt(10))) 
        .filterOutEmpties(), //removes the empty elements
        newCounter, 0, this.score, new Random(2));
    //case to add 10 to the score should an empty word be removed 
    //and determine if level should increase
    ZTypeWorld addScoreCase = new ZTypeWorld(enemyWords
        .moveWords() //adds a score of increments + 10
        .filterOutEmpties(), 
        this.counter, 0, this.score + 10, new Random(2));
    //case to reduce the counter by 1
    ZTypeWorld reduceCase = new ZTypeWorld(enemyWords.moveWords() //reduces the counter by 1
        .filterOutEmpties(), 
        this.counter - 1, 0, this.score, new Random(2));
    //if the clock is 0 then add a word
    if (this.counter == 0) {
      return resetCase; 
      //if a word has reached the bottom, return the current scene
    } else if (this.enemyWords.anyReachedBottom()) { 
      return this;
      //if a list has empty elements, add 10 to the score and calculate level
    } else if (this.enemyWords.finishedWord()) { 
      return addScoreCase;
    } else {
      return reduceCase;
    }
  }

  //Calculate the expected level based on the current score
  public int levelUp() {
    int expectedLevel = this.score / 50 + 1;
    if (expectedLevel > this.lvlCounter) {
      this.lvlCounter = expectedLevel;
    }
    return this.lvlCounter;
  }

  //Adjusted method to account for level in counter and introduce a minimum counter
  public int adjustCounterBasedOnLevel() {
    int baseCounter = 20 - this.lvlCounter; 
    return Math.max(baseCounter, MIN_COUNTER);
  }

  //add a key event to change the length of all of the existing words in a list 
  //Active words change color to red and reduces by length 1
  //Inactive words keeps color as blue and stays the same length
  //if a word reaches the bottom, no changes can be made
  public ZTypeWorld onKeyEvent(String key) {
    if (key.equals("tab")) {
      return new ZTypeWorld(new MtLoWord(), 0, 0, 0, this.rand);
    } else if (this.enemyWords.anyReachedBottom()) {
      return this;
    } else {
      return new ZTypeWorld(this.enemyWords.checkAndReduce(key), 
          this.counter, this.lvlCounter, this.score, this.rand);
    }
  }
}

//utilities class for ZTypeWorld functions
class Utils implements IConstants {
  Random rand;

  Utils(Random rand) {
    this.rand = rand;
  }

  /* fields:
   *  this.rand ... Random
   * methods:
   *  this.wordWrap(int) ... IWord
   *  this.generateWord(int) ... String
   *  this.generateWordAcc(String, int) ... String
   */

  //wraps the new random word into an IWord
  IWord wordWrap(int comp) {
    return new InactiveWord(this.generateWord(comp), 
        rand.nextInt(IConstants.WIDTH), 0);
  }

  //constructs a random word of 6 characters
  String generateWord(int comp) {
    return this.generateWordAcc("", comp);
  }

  //helper for the method generateWord
  //keeps track of the word being constructed.
  String generateWordAcc(String acc, int comp) {
    if (acc.length() == comp) {
      return acc;
    } else {
      return this.generateWordAcc(acc 
          + ALPHABET.charAt(rand.nextInt(25)), comp);
    }
  }
}

//Examples class for testing 
class ExamplesZTypeWorld implements IConstants {

  IWord apple = new InactiveWord("apple", 280, 0);
  IWord banana = new InactiveWord("banana", 160, 0);
  IWord car = new InactiveWord("car", 30, 0);
  IWord cat = new ActiveWord("cat", 79, 0);
  IWord testBot = new ActiveWord("test", 30, 700);

  ILoWord mt = new MtLoWord();
  ILoWord list1 = new ConsLoWord(this.car, this.mt);
  ILoWord list2 = new ConsLoWord(this.banana, this.list1);
  ILoWord list3 = new ConsLoWord(this.apple, this.list2);
  ILoWord list4 = new ConsLoWord(this.cat, this.list3);
  ILoWord listBot = new ConsLoWord(this.testBot, this.mt);

  ZTypeWorld testWorld = new ZTypeWorld(this.list3, 0, 0, 0, new Random(10));
  ZTypeWorld testMtWorld = new ZTypeWorld(this.mt, 0, 0, 0, new Random(10));
  ZTypeWorld testAWorld = new ZTypeWorld(this.list4, 0, 0, 0, new Random(10));
  ZTypeWorld testBotWorld = new ZTypeWorld(this.listBot, 0, 0, 0, new Random(10));

  Utils u = new Utils(new Random(2));
  Utils u1 = new Utils(new Random(2));
  Utils u2 = new Utils(new Random(2));

  boolean testBigBang(Tester t) {

    ZTypeWorld world = testMtWorld;

    int worldWidth = WIDTH;

    int worldHeight = HEIGHT;

    double tickRate = .1;

    return world.bigBang(worldWidth, worldHeight, tickRate);
  }

  //tests for the method makeBackground
  boolean testMakeBackground(Tester t) {
    return 
        //test for an empty world
        t.checkExpect(new ZTypeWorld(this.mt, 0, 0, 0, new Random()).makeBackground(), 
            new WorldScene(IConstants.WIDTH, HEIGHT))
        //test for a full world
        && t.checkExpect(this.testMtWorld.makeBackground(),
            new WorldScene(IConstants.WIDTH, HEIGHT));
  }

  //tests for the method drawMainScene
  boolean testDrawMainScene(Tester t) {
    return 
        //test with a list of words
        t.checkExpect(this.testWorld.drawMainScene(), 
            new WorldScene(400, 700)
            .placeImageXY(new TextImage("apple", 20, Color.BLUE), 280, 0)
            .placeImageXY(new TextImage("banana", 20, Color.BLUE), 160, 0)
            .placeImageXY(new TextImage("car", 20, Color.BLUE), 30, 0)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30))
        //test with an empty list 
        && t.checkExpect(this.testMtWorld.drawMainScene(),
            new WorldScene(400, 700)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30))
        //test with a list of words and an active word
        && t.checkExpect(this.testAWorld.drawMainScene(), 
            new WorldScene(400, 700)
            .placeImageXY(new TextImage("apple", 20, Color.BLUE), 280, 0)
            .placeImageXY(new TextImage("banana", 20, Color.BLUE), 160, 0)
            .placeImageXY(new TextImage("car", 20, Color.BLUE), 30, 0)
            .placeImageXY(new TextImage("cat", 20, Color.RED), 79, 0)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30));
  }

  //tests for the method drawEndScene
  boolean testDrawEndScene(Tester t) {
    return 
        //test with a list of words
        t.checkExpect(this.testWorld.drawEndScene(), 
            new WorldScene(400, 700)
            .placeImageXY(new TextImage("apple", 20, Color.BLUE), 280, 0)
            .placeImageXY(new TextImage("banana", 20, Color.BLUE), 160, 0)
            .placeImageXY(new TextImage("car", 20, Color.BLUE), 30, 0)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30)
            .placeImageXY(new TextImage("Game Over", 50, Color.BLACK), 
                WIDTH / 2, HEIGHT / 2))
        //test with an empty list 
        && t.checkExpect(this.testMtWorld.drawEndScene(),
            new WorldScene(400, 700)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30)
            .placeImageXY(new TextImage("Game Over", 50, Color.BLACK), 
                WIDTH / 2, HEIGHT / 2))
        //test with a list of words and an active word
        && t.checkExpect(this.testAWorld.drawEndScene(), 
            new WorldScene(400, 700)
            .placeImageXY(new TextImage("apple", 20, Color.BLUE), 280, 0)
            .placeImageXY(new TextImage("banana", 20, Color.BLUE), 160, 0)
            .placeImageXY(new TextImage("car", 20, Color.BLUE), 30, 0)
            .placeImageXY(new TextImage("cat", 20, Color.RED), 79, 0)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30)
            .placeImageXY(new TextImage("Game Over", 50, Color.BLACK), 
                WIDTH / 2, HEIGHT / 2));
  }

  //tests for the method makeScene
  boolean testMakeScene(Tester t) {
    return
        //test with a list of words
        t.checkExpect(this.testWorld.drawMainScene(), 
            new WorldScene(400, 700)
            .placeImageXY(new TextImage("apple", 20, Color.BLUE), 280, 0)
            .placeImageXY(new TextImage("banana", 20, Color.BLUE), 160, 0)
            .placeImageXY(new TextImage("car", 20, Color.BLUE), 30, 0)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30))
        //test with an empty list 
        && t.checkExpect(this.testMtWorld.makeScene(),
            new WorldScene(400, 700)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                +  20 + " ticks per word", 10, Color.WHITE), 200, 30))
        //test with a list of words and an active word
        && t.checkExpect(this.testAWorld.makeScene(), 
            new WorldScene(400, 700)
            .placeImageXY(new TextImage("apple", 20, Color.BLUE), 280, 0)
            .placeImageXY(new TextImage("banana", 20, Color.BLUE), 160, 0)
            .placeImageXY(new TextImage("car", 20, Color.BLUE), 30, 0)
            .placeImageXY(new TextImage("cat", 20, Color.RED), 79, 0)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30))
        //test for a word that has reached the bottom
        && t.checkExpect(this.testBotWorld.makeScene(), 
            new WorldScene(400, 700)
            .placeImageXY(new TextImage("test", 20, Color.red), 30, 700)
            .placeImageXY(new RectangleImage(
                UI_WIDTH, UI_HEIGHT, OutlineMode.SOLID, Color.BLACK),
                0, 0)
            .placeImageXY(new RectangleImage(280, 40, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("Restart: PRESS(TAB)", Color.WHITE), 70, 10)
            .placeImageXY(new TextImage("Score: " + 0, Color.WHITE), 200, 55)
            .placeImageXY(new TextImage("Level:" + 0 + " Speed:" 
                + 20 + " ticks per word", 10, Color.WHITE), 200, 30)
            .placeImageXY(new TextImage("Game Over", 50, Color.BLACK), 
                WIDTH / 2, HEIGHT / 2));
  }

  //test for the method onTickForTesting
  boolean testOnTickForTesting(Tester t) {
    return 
        //test for an Mt world
        t.checkExpect(this.testMtWorld.onTickForTesting(), 
            new ZTypeWorld(
                new ConsLoWord(new InactiveWord("iwp", 267, 0), this.mt), 20, 0, 0, new Random()))
        //test on a world containing elements
        && t.checkExpect(this.testWorld.onTickForTesting(), 
            new ZTypeWorld(
                new ConsLoWord(new InactiveWord("apple", 280, 5),
                    new ConsLoWord(new InactiveWord("banana", 160, 5), 
                        new ConsLoWord(new InactiveWord("car", 30, 5), 
                            new ConsLoWord(new InactiveWord("iwp", 267, 0), this.mt)))), 
                20, 0, 0, new Random()))
        //test on a world with an element at the bottom 
        && t.checkExpect(this.testBotWorld.onTickForTesting(),
            new ZTypeWorld(
                new ConsLoWord(new ActiveWord("test", 30, 703), 
                    new ConsLoWord(new InactiveWord("iwp", 267, 0), this.mt)), 
                20, 0, 0, new Random()))
        //test on a word with the counter at 0
        && t.checkExpect(new ZTypeWorld(this.mt, 0, 0, 0, new Random()), 
            new ZTypeWorld(this.mt, 0, 0, 0, new Random()));
  }

  //Test for levelUp method
  boolean testLevelUp(Tester t) {
    ZTypeWorld worldAtLevel1 = new ZTypeWorld(mt, 0, 0, 0, new Random()); 
    ZTypeWorld worldAtLevel2 = new ZTypeWorld(list1, 0, 0, 50, new Random()); 
    ZTypeWorld worldAtLevel4 = new ZTypeWorld(list2, 0, 0, 150, new Random()); 
    //case for level 1
    return t.checkExpect(worldAtLevel1.levelUp(), 1)
        //case for level 2
        && t.checkExpect(worldAtLevel2.levelUp(), 2)
        //case for level 4
        && t.checkExpect(worldAtLevel4.levelUp(), 4);
  }

  //Test for adjustCounterBasedOnLevel method
  boolean testAdjustCounterBasedOnLevel(Tester t) {
    ZTypeWorld worldAtLevel1 = new ZTypeWorld(mt, 0, 0, 0, new Random());
    ZTypeWorld worldAtLevel3 = new ZTypeWorld(list3, 0, 5, 100, new Random());
    ZTypeWorld worldAtHighLevel = new ZTypeWorld(mt, 0, 16, 800, new Random()); 
    //case for no change in counter
    return t.checkExpect(worldAtLevel1.adjustCounterBasedOnLevel(), 20)
        //case for level 5
        && t.checkExpect(worldAtLevel3.adjustCounterBasedOnLevel(), 15)
        //case for hardest level
        && t.checkExpect(worldAtHighLevel.adjustCounterBasedOnLevel(), MIN_COUNTER);
  }

  //test for onKeyEvent
  boolean testonKeyEvent(Tester t) {
    ZTypeWorld worldBeforeRestart = new ZTypeWorld(list3, 5, 1, 10, new Random(10));
    ZTypeWorld worldAfterRestart = worldBeforeRestart.onKeyEvent("tab");
    ZTypeWorld worldBeforeIncorrectKey = new ZTypeWorld(list4, 5, 1, 10, new Random(10));
    ZTypeWorld worldAfterIncorrectKey = worldBeforeIncorrectKey.onKeyEvent("z");
    //Simulate game over state
    ZTypeWorld worldBeforeGameOver = new ZTypeWorld(list4, 5, 1, 10, new Random(10)); 
    ZTypeWorld worldAfterGameOverKey = worldBeforeGameOver.onKeyEvent("a");
    //Restarting the game with the "tab" key
    return t.checkExpect(worldAfterRestart, new ZTypeWorld(new MtLoWord(), 0, 0, 0, new Random(10)))
        &&
        //Typing an incorrect letter
        t.checkExpect(worldAfterIncorrectKey, worldBeforeIncorrectKey)
        &&
        //Trying to type after a game over scenario
        t.checkExpect(worldAfterGameOverKey, new ZTypeWorld(list4, 5, 1, 10, new Random(10)));
  }

  //test for the method wordWrap
  boolean testWordWrap(Tester t) {
    return t.checkExpect(this.u.wordWrap(3), 
        new InactiveWord("iwp", 267, 0));
  }

  //test for the method generateWord in the Utils class
  boolean testGenerateWord(Tester t) {
    return t.checkExpect(this.u1.generateWord(3), "iwp"); 
  }

  //test for the method generateWordAcc in the Utils class
  boolean testGenerateWordAcc(Tester t) {
    return t.checkExpect(this.u2.generateWordAcc("", 3), "iwp");
  }
}