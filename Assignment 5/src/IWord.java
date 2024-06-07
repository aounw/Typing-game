import tester.Tester;
import java.awt.Color;
import javalib.worldimages.*;
import javalib.funworld.*;  

//interface for an IWord
interface IWord {

  //builds an IWord
  IWord iWordBuilder(String letter);

  //compares the first letter of a word to another and reduces this word by that letter 
  String compareLetter(String l);

  //helper for the method compareLetter & inactiveMatch
  //checks if a letter is the same as the other,
  //if this or that string is ""
  boolean iWordChecker(String letter);

  //determines if a string contains nothing
  boolean hasNothing();

  //builds a textImage in a placeImageXY
  WorldScene textImageBuilder(WorldScene currScene);

  //moves the word down via the Y axis
  IWord moveWord();

  //determines if a word has reached the bottom of the screen
  boolean reachedBottom();

  //determines if the first of an inactive word matches the given letter
  boolean inactiveMatch(String l);

  //converts an IWord to an active Word
  IWord makeActive();

  //determines if a word is active
  //ONLY USED FOR SEARCHING IN A LIST 
  //TA office hour advice to use inactive for only this case!!
  boolean isActive();

}

//represents an abstract class for words 
//in the ZType game
abstract class AWord implements IWord, IConstants {
  String word;
  int x;
  int y;

  //Constructor checking for correctness
  AWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }

  /* fields: 
   *  this.word ... String
   *  this.x ... int
   *  this.y ... int
   * methods: 
   *  this.iWordBuilder(String) ... IWord
   *  this.compareLetter(String) ... String
   *  this.iWordChecker(String) ... boolean
   *  this.hasNothing() ... boolean
   *  this.textImageBuilder(WorldScene) ... WorldScene
   *  this.moveWord() ... IWord
   *  this.reachedBottom() ... boolean
   *  this.inactiveMatch(String) ... boolean
   *  this.makeActive() ... IWord
   *  this.isActive() ... boolean
   */

  //builds an IWord
  public abstract IWord iWordBuilder(String letter);

  //compares the first letter of a word to another and reduces this word by that letter 
  public abstract String compareLetter(String l);

  //helper for the method compareLetter
  //checks if a letter is the same as the other,
  //if this or that string is ""
  public boolean iWordChecker(String l) {
    return this.word.isEmpty() 
        || l.isEmpty() 
        || (! (this.word.substring(0, 1).equals(l.substring(0, 1))));
  }

  //determines if a string is empty
  public boolean hasNothing() {
    return this.word.isEmpty();
  }

  //builds a text image
  public abstract WorldScene textImageBuilder(WorldScene currScene);

  //moves the word down via the y axis
  public abstract IWord moveWord();

  //determines if a word has reached the bottom of the screen
  public boolean reachedBottom() {
    return this.y >= HEIGHT;
  }

  //determines if the first of an inactive word matches the given letter
  public boolean inactiveMatch(String l) {
    return false;
  }

  //converts an IWord to an active Word
  public IWord makeActive() {
    return new ActiveWord(this.word, this.x, this.y);
  }

  //determines if a word is active
  //ONLY USED FOR SEARCHING IN A LIST 
  //TA office hour advice to use inactive for only this case!!
  public boolean isActive() {
    return false;
  }
}

//represents an active word in the ZType game
class ActiveWord extends AWord {

  ActiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  /* fields: 
   *  this.word ... String
   *  this.x ... int
   *  this.y ... int
   * methods: 
   *  this.iWordBuilder(String) ... IWord
   *  this.compareLetter(String) ... String
   *  this.textImageBuilder(WorldScene) ... WorldScene
   *  this.moveWord() ... IWord
   *  this.isActive() ... boolean
   */

  //builds an IWord from a String
  public IWord iWordBuilder(String letter) {
    return new ActiveWord(this.compareLetter(letter), this.x, this.y);
  }

  //compares the first letter of a word to another and reduces this word by that letter 
  public String compareLetter(String l) {
    if (this.iWordChecker(l)) {
      return this.word;
    } else {
      return this.word.substring(1);
    }
  }

  //builds a text image, red for an Active word
  public WorldScene textImageBuilder(WorldScene currScene) {
    return currScene.placeImageXY(new TextImage(this.word, 20, Color.RED), this.x, this.y);
  }

  //moves the word down via the y axis
  public IWord moveWord() {
    return new ActiveWord(this.word, x, y + 3);
  }

  //determines if a word is active
  //ONLY USED FOR SEARCHING IN A LIST 
  //TA office hour advice to use inactive for only this case!!
  public boolean isActive() {
    return true;
  }
}

//represents an inactive word in the ZType game
class InactiveWord extends AWord {

  InactiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  /* fields: 
   *  this.word ... String
   *  this.x ... int
   *  this.y ... int
   * methods: 
   *  this.iWordBuilder(String) ... IWord
   *  this.compareLetter(String) ... String
   *  this.textImageBuilder(WorldScene) ... WorldScene
   *  this.moveWord() ... IWord
   *  this.inactiveMatch(String) ... boolean
   */

  //builds an IWord 
  public IWord iWordBuilder(String letter) {
    return this;
  }

  //compares the first letter of a word to another and reduces this word by that letter 
  public String compareLetter(String l) {
    return this.word;
  }

  //builds a text image, blue for an Inactive word
  public WorldScene textImageBuilder(WorldScene currScene) {
    return currScene.placeImageXY(new TextImage(this.word, 20, Color.BLUE), this.x, this.y);
  }

  //moves the word down via the y axis
  public IWord moveWord() {
    return new InactiveWord(this.word, x, y + 5);
  }

  //Returns the current inactive word as it is already inactive
  public boolean inactiveMatch(String l) {
    return !(this.iWordChecker(l));
  }
}

//all examples and tests for ILoWord
class ExamplesWord implements IConstants {
  IWord apple = new ActiveWord("apple", 0, 0);
  IWord banana = new InactiveWord("banana", 0, 1);
  IWord car = new ActiveWord("cAr", 0, 1);
  IWord carrot = new ActiveWord("Carrot", 1, 0);
  IWord charm = new InactiveWord("charm", 1, 0);
  IWord dog = new ActiveWord("dOg", 1, 1);
  IWord dino = new InactiveWord("diNO", 1, 1);
  IWord ant = new ActiveWord("ant", 0 ,0);
  IWord bat = new ActiveWord("bat", 2, 2);
  IWord cat = new InactiveWord("cat", 2, 1);
  IWord empty = new InactiveWord("", 2, 2);

  WorldScene wsEmpty = new WorldScene(500, 500)
      .placeImageXY(
          new RectangleImage(500, 500, OutlineMode.SOLID, Color.GRAY), 0, 0);
  WorldScene wsWords = new WorldScene(500, 500)
      .placeImageXY(new RectangleImage(500, 500, OutlineMode.SOLID, Color.GRAY), 0, 0)
      .placeImageXY(new TextImage("apple", 20, Color.RED), 0, 0)
      .placeImageXY(new TextImage("banana", 20, Color.BLUE), 0, 1);

  //tests for the method iWordBuilder
  boolean testIWordBuilder(Tester t) {
    return
        //test for an active word
        t.checkExpect(this.ant.iWordBuilder("a"), 
            new ActiveWord("nt", 0, 0))
        //test for an active word with 1 element
        && t.checkExpect(new ActiveWord("a", 0, 0).iWordBuilder("a"),
            new ActiveWord("", 0, 0))
        //test for an active word with empty string 
        && t.checkExpect(new ActiveWord("", 0, 0).iWordBuilder("a"),
            new ActiveWord("", 0, 0))
        //test for an inactive word
        && t.checkExpect(this.banana.iWordBuilder("b"), 
            new InactiveWord("banana", 0, 1));
  }

  //tests for the method compareLetter
  boolean testCompareLetter(Tester t) {
    return
        //letter in first of the active word
        t.checkExpect(this.ant.compareLetter("a"), "nt") 
        //letter not in first of the active word
        && t.checkExpect(this.bat.compareLetter("a"), "bat")
        //letter the only one in the active word
        && t.checkExpect(this.empty.compareLetter(""), "")
        //letter in first of inactive word
        && t.checkExpect(this.banana.compareLetter("b"), "banana")
        //letter not in first of the inactive word
        && t.checkExpect(this.dino.compareLetter("a"), "diNO")
        //letters match but one is upper case
        && t.checkExpect(this.carrot.compareLetter("c"), "Carrot");
  }

  //tests for the method iWordChecker
  boolean testIWordChecker(Tester t) {
    return 
        //letter in first of the active word
        t.checkExpect(this.ant.iWordChecker("a"), false) 
        //letter not in first of the active word
        && t.checkExpect(this.bat.iWordChecker("a"), true)
        //letter the only one in the active word
        && t.checkExpect(this.empty.iWordChecker(""), true)
        //letter in first of inactive word
        && t.checkExpect(this.banana.iWordChecker("b"), false)
        //letter not in first of the inactive word
        && t.checkExpect(this.dino.iWordChecker("a"), true)
        //letters match but one is upper case
        && t.checkExpect(this.carrot.iWordChecker("c"), true);
  }

  //tests for the method hasNothing
  boolean testHasNothing(Tester t) {
    return
        //non-empty string
        t.checkExpect(this.ant.hasNothing(), false)
        //empty string
        && t.checkExpect(this.empty.hasNothing(), true);   
  }

  //tests for the method textImageBuilder
  boolean testTextImageBuilder(Tester t) {
    return 
        //test for an active word
        t.checkExpect(this.ant.textImageBuilder(wsEmpty),
            new WorldScene(500, 500)
            .placeImageXY(new RectangleImage(500, 500, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("ant", 20, Color.RED), 0, 0))
        //test for an inactive word
        && t.checkExpect(this.banana.textImageBuilder(wsEmpty),
            new WorldScene(500, 500)
            .placeImageXY(new RectangleImage(500, 500, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("banana", 20, Color.BLUE), 0, 1));
  }

  //tests for the moveWords function in IWord
  boolean testMoveWord(Tester t) {
    return
        // Test for an ActiveWord
        t.checkExpect(this.apple.moveWord(), new ActiveWord("apple", 0, 3)) 
        // Test for an InactiveWord
        && t.checkExpect(this.banana.moveWord(), new InactiveWord("banana", 0, 6))
        // Test an ActiveWord closer to the bottom of the screen
        && t.checkExpect(this.dog.moveWord(), new ActiveWord("dOg", 1, 4))
        // Test an InactiveWord with a different starting y-coordinate
        && t.checkExpect(this.charm.moveWord(), new InactiveWord("charm", 1, 5));
  }

  //tests for the reachedBottom function in IWord
  boolean testReachedBottom(Tester t) {
    return 
        //test for an active word at the bottom
        t.checkExpect(new ActiveWord("test", 30, HEIGHT).reachedBottom(), true)
        //test for an active word passed the bottom
        && t.checkExpect(new ActiveWord("test", 30, 750).reachedBottom(), true)
        //test for an active word not at the bottom
        && t.checkExpect(new ActiveWord("test", 30, 1).reachedBottom(), false)
        //test for an inactive word at the bottom
        && t.checkExpect(new InactiveWord("test", 30, HEIGHT).reachedBottom(), true)
        //test for an inactive word passed the bottom
        && t.checkExpect(new InactiveWord("test", 30, 750).reachedBottom(), true)
        //test for an inactive word not at the bottom
        && t.checkExpect(new InactiveWord("test", 30, 1).reachedBottom(), false);
  }

  //  *  this.inactiveMatch(String) ... boolean
  //  *  this.makeActive() ... IWord
  //  *  this.isActive() ... boolean

  //tests for the method inactiveMatch
  boolean testInactiveMatch(Tester t) {
    return 
        //letter in first of the active word
        t.checkExpect(this.ant.inactiveMatch("a"), false) 
        //letter not in first of the active word
        && t.checkExpect(this.bat.inactiveMatch("a"), false)
        //letter the only one in the active word
        && t.checkExpect(this.empty.inactiveMatch(""), false)
        //letter in first of inactive word
        && t.checkExpect(this.banana.inactiveMatch("b"), true)
        //letter not in first of the inactive word
        && t.checkExpect(this.dino.inactiveMatch("a"), false)
        //letters match but one is upper case
        && t.checkExpect(this.carrot.inactiveMatch("c"), false); 
  }

  //tests for the method makeActive
  boolean testMakeActive(Tester t) {
    return 
        //test for an active word
        t.checkExpect(this.apple.makeActive(), this.apple)
        //test for an inactive word
        && t.checkExpect(this.banana.makeActive(), 
            new ActiveWord("banana", 0, 1))
        //test for an inactive word with an empty string 
        && t.checkExpect(this.empty.makeActive(), 
            new ActiveWord("", 2, 2));
  }

  //tests for the method isActive
  boolean testIsActive(Tester t) {
    return 
        //test for an active word
        t.checkExpect(this.ant.isActive(), true)
        //test for an inactive word
        && t.checkExpect(this.banana.isActive(), false);
  }
}