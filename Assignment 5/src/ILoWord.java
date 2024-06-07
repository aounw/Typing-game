import tester.Tester;
import java.awt.Color;
import javalib.worldimages.*;
import javalib.funworld.*;

//represents a list of words
interface ILoWord {

  //removes the first letter in a list with the letters inputed 
  ILoWord checkAndReduce(String l);

  //adds a word to the end of the list
  ILoWord addToEnd(IWord w);

  //filters out any empty strings
  ILoWord filterOutEmpties();

  //draws all the words in a list onto a world scene
  WorldScene draw(WorldScene scene);

  //moves all the words displayed in a list 
  ILoWord moveWords();

  //determines if a word has reached the bottom of a screen
  //in a list
  boolean anyReachedBottom();

  //determines if a word has been typed out
  boolean finishedWord();

  //helper for the method checkAndReduce
  //Converts a member of the list from inactive to an
  //active word
  ILoWord convertInactive(String l);

  //determines if a list has an active word
  boolean hasActiveWord();

  //helper for the method checkAndReduce
  //reduces the active word
  ILoWord reduceActive(String l);

}

//represents an empty list of IWords
class MtLoWord implements ILoWord {

  /* fields: 
   *  none
   * methods:
   *  this.checkAndReduce(String) ... ILoWord
   *  this.addToEnd(IWord) ... ILoWord
   *  this.filterOutEmpties() ... ILoWord
   *  this.draw(WorldScene) ... WorldScene
   *  this.moveWords() ... ILoWord
   *  this.anyReachedBottom() ... boolean
   *  this.finishedWord() ... boolean
   *  this.convertInactive(String) ... ILoWord
   *  this.hasActiveWord() ... boolean
   *  this.reduceActive(String) ... ILoWord 
   * methods for fields: 
   *  none
   */

  //removes the first letter in a list with the letters inputed 
  public ILoWord checkAndReduce(String l) {
    return this;
  }

  //adds a word to the end of the list
  public ILoWord addToEnd(IWord w) {
    /* fields: 
     *  w.word ... String
     *  w.x ... int
     *  w.y ... int
     * methods: 
     *  w.iWordBuilder(String) ... IWord
     *  w.compareLetter(String) ... String
     *  w.iWordChecker(String) ... boolean
     *  w.hasNothing() ... boolean
     *  w.textImageBuilder(WorldScene) ... WorldScene
     *  w.moveWord() ... IWord
     *  w.reachedBottom() ... boolean
     *  w.inactiveMatch(String) ... boolean
     *  w.makeActive() ... IWord
     *  w.isActive() ... boolean
     */
    return new ConsLoWord(w, this);
  }

  //filters out any empty strings
  public ILoWord filterOutEmpties() {
    return this;
  }

  //draws all the words in a list onto a world scene
  public WorldScene draw(WorldScene scene) {
    return scene;
  }

  //moves all the words displayed in an empty list 
  public ILoWord moveWords() {
    return this;
  }

  //determines if a word has reached the bottom of a screen
  //in an empty list
  public boolean anyReachedBottom() {
    return false;
  }

  //determines if a word has been typed out
  public boolean finishedWord() {
    return false;
  }

  //helper for the method checkAndReduce
  //Converts a member of the list from inactive to an
  //active word
  public ILoWord convertInactive(String l) {
    return this;
  }

  //determines if a list has an active word
  public boolean hasActiveWord() {
    return false;
  }

  //helper for the method checkAndReduce
  //reduces the active word
  public ILoWord reduceActive(String l) {
    return this;
  }

}

//represents a non-empty list of IWords
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }

  /* fields: 
   *  this.first ... IWord
   *  this.rest ... ILoWord
   * methods:
   *  this.checkAndReduce(String) ... ILoWord
   *  this.addToEnd(IWord) ... ILoWord
   *  this.filterOutEmpties() ... ILoWord
   *  this.draw(WorldScene) ... WorldScene
   *  this.moveWords() ... ILoWord
   *  this.anyReachedBottom() ... boolean
   *  this.finishedWord() ... boolean
   *  this.convertInactive(String) ... ILoWord
   *  this.hasActiveWord() ... boolean
   *  this.reduceActive(String) ... ILoWord 
   * methods for fields:
   *  this.first.iWordBuilder(String) ... IWord
   *  this.first.compareLetter(String) ... String
   *  this.first.iWordChecker(String) ... boolean
   *  this.first.hasNothing() ... boolean
   *  this.first.textImageBuilder(WorldScene) ... WorldScene
   *  this.first.moveWord() ... IWord
   *  this.first.reachedBottom() ... boolean
   *  this.first.inactiveMatch(String) ... boolean
   *  this.first.makeActive() ... IWord
   *  this.first.isActive() ... boolean
   */

  //determines if a list has an active word
  public boolean hasActiveWord() {
    return this.first.isActive() || this.rest.hasActiveWord(); 
    //TA office hour advice to use inactive for only this case!!
  }

  //helper for the method checkAndReduce
  //Converts a member of the list from inactive to an
  //active word
  public ILoWord convertInactive(String l) {
    if (this.first.inactiveMatch(l)) {
      return new ConsLoWord(this.first.makeActive().iWordBuilder(l), this.rest);
    } else {
      return new ConsLoWord(this.first, this.rest.convertInactive(l));
    }
  }

  //helper for the method checkAndReduce
  //reduces the active word
  public ILoWord reduceActive(String l) {
    return new ConsLoWord(this.first.iWordBuilder(l), this.rest.reduceActive(l));
  }

  //removes the first letter in a list with the letters inputed 
  public ILoWord checkAndReduce(String l) {
    if (this.hasActiveWord()) {
      return this.reduceActive(l);
      //      return this; // does key match activeword - then reduce 
    } else {
      return this.convertInactive(l);
    } 
  }

  //adds a word to the end of the list
  public ILoWord addToEnd(IWord w) {
    /* fields: 
     *  w.word ... String
     *  w.x ... int
     *  w.y ... int
     * methods: 
     *  w.iWordBuilder(String) ... IWord
     *  w.compareLetter(String) ... String
     *  w.iWordChecker(String) ... boolean
     *  w.hasNothing() ... boolean
     *  w.textImageBuilder(WorldScene) ... WorldScene
     *  w.moveWord() ... IWord
     *  w.reachedBottom() ... boolean
     *  w.inactiveMatch(String) ... boolean
     *  w.makeActive() ... IWord
     *  w.isActive() ... boolean
     */
    return new ConsLoWord(this.first, this.rest.addToEnd(w));
  }

  //filters out any empty strings
  public ILoWord filterOutEmpties() {
    if (this.first.hasNothing()) {
      return this.rest.filterOutEmpties();
    } else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  //draws all the words in a list onto a world scene
  public WorldScene draw(WorldScene scene) {
    return this.rest.draw(this.first.textImageBuilder(scene));
  }

  //moves all the words displayed in a non-empty list   
  public ILoWord moveWords() {
    return new ConsLoWord(this.first.moveWord(), this.rest.moveWords());
  }

  //determines if a word has reached the bottom of a screen
  //in an empty list
  public boolean anyReachedBottom() {
    return this.first.reachedBottom() || this.rest.anyReachedBottom();
  }

  //determines if a word has been typed out
  public boolean finishedWord() {
    if (this.first.hasNothing()) {
      return true; // The first word is finished
    } else {
      return this.rest.finishedWord(); // Recursively check the rest of the list
    }
  }
}

//all examples and tests for ILoWord
class ExamplesWordLists {
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

  ILoWord mt = new MtLoWord();

  ILoWord list1 = new ConsLoWord(this.dog, this.mt);
  ILoWord list2 = new ConsLoWord(this.dino, this.list1);
  ILoWord list3 = new ConsLoWord(this.charm, this.list2);
  ILoWord list4 = new ConsLoWord(this.carrot, this.list3);
  ILoWord list5 = new ConsLoWord(this.car, this.list4);
  ILoWord list6 = new ConsLoWord(this.banana, this.list5);
  ILoWord list7 = new ConsLoWord(this.apple, this.list6);

  ILoWord list8 = new ConsLoWord(this.cat, this.mt);
  ILoWord list9 = new ConsLoWord(this.bat, this.list8);
  ILoWord list10 = new ConsLoWord(this.ant, this.list9);
  ILoWord list11 = new ConsLoWord(this.empty, this.list10);
  ILoWord list12 = new ConsLoWord(this.empty, this.list11);

  ILoWord sorList1 = new ConsLoWord(this.cat, this.mt);
  ILoWord sorList2 = new ConsLoWord(this.bat, this.sorList1);
  ILoWord sorList3 = new ConsLoWord(this.ant, this.sorList2);
  ILoWord list13 = new ConsLoWord(this.bat, this.sorList3);

  ILoWord sorList4 = new ConsLoWord(this.charm, this.mt);
  ILoWord sorList5 = new ConsLoWord(this.banana, this.sorList4);
  ILoWord sorList6 = new ConsLoWord(this.apple, this.sorList5);
  ILoWord sorList7 = new ConsLoWord(this.ant, this.sorList6);

  ILoWord unList1 = new ConsLoWord(this.carrot, this.mt);
  ILoWord unList2 = new ConsLoWord(this.car, this.unList1);
  ILoWord unList3 = new ConsLoWord(this.apple, this.unList2);
  ILoWord unList4 = new ConsLoWord(this.banana, this.unList3);
  ILoWord unList5 = new ConsLoWord(new ActiveWord("c", 0, 0), this.unList1);
  ILoWord unList6 = new ConsLoWord(this.car, this.unList5);

  ILoWord listAfterMoving = new ConsLoWord(new ActiveWord("ant", 0, 3),
      new ConsLoWord(new ActiveWord("bat", 2, 5),
          new ConsLoWord(new InactiveWord("cat", 2, 6), this.mt)));
  ILoWord mixedListDifferentYMoved = new ConsLoWord(new ActiveWord("apple", 0, 3),
      new ConsLoWord(new InactiveWord("banana", 0, 6),
          new ConsLoWord(new ActiveWord("cAr", 0, 4),
              new ConsLoWord(new InactiveWord("charm", 1, 5), this.mt))));
  ILoWord listMultipleActiveMoved = new ConsLoWord(new ActiveWord("apple", 0, 3),
      new ConsLoWord(new ActiveWord("cAr", 0, 4),
          new ConsLoWord(new ActiveWord("Carrot", 1, 3), this.mt)));

  WorldScene wsEmpty = new WorldScene(500, 500)
      .placeImageXY(
          new RectangleImage(500, 500, OutlineMode.SOLID, Color.GRAY), 0, 0);
  WorldScene wsWords = new WorldScene(500, 500)
      .placeImageXY(new RectangleImage(500, 500, OutlineMode.SOLID, Color.GRAY), 0, 0)
      .placeImageXY(new TextImage("apple", 20, Color.RED), 0, 0)
      .placeImageXY(new TextImage("banana", 20, Color.BLUE), 0, 1);

  //tests for the method checkAndReduce
  boolean testCheckAndReduce(Tester t) {
    return 
        //empty case
        t.checkExpect(this.mt.checkAndReduce("a"), this.mt)
        //list with active words matching
        && t.checkExpect(this.list10.checkAndReduce("a"), 
            new ConsLoWord(new ActiveWord("nt", 0, 0), list9))
        //list without active words matching
        && t.checkExpect(this.list10.checkAndReduce("z"), list10)
        //list with active words matching and input greater then string length 1
        && t.checkExpect(this.list1.checkAndReduce("dO"), 
            new ConsLoWord(new ActiveWord("Og", 1, 1), this.mt))
        //list with more than one word matching the letter
        && t.checkExpect(this.list13.checkAndReduce("b"), 
            new ConsLoWord(new ActiveWord("at", 2, 2), 
                new ConsLoWord(this.ant, 
                    new ConsLoWord(new ActiveWord("at", 2, 2),
                        new ConsLoWord(this.cat, this.mt)))))
        //list with an element that is one letter
        && t.checkExpect(this.unList6.checkAndReduce("c"), 
            new ConsLoWord(new ActiveWord("Ar", 0, 1),
                new ConsLoWord(new ActiveWord("", 0, 0),
                    new ConsLoWord(new ActiveWord("Carrot", 1, 0), this.mt))));
  }

  //tests for the method addToEnd
  boolean testAddToEnd(Tester t) {
    return
        //empty case 
        t.checkExpect(this.mt.addToEnd(cat), this.list8)
        //case for filled list 
        && t.checkExpect(this.list10.addToEnd(car), 
            new ConsLoWord(this.ant, 
                new ConsLoWord(this.bat,
                    new ConsLoWord(this.cat,
                        new ConsLoWord(this.car, this.mt)))))
        //case for filled list with the same element
        && t.checkExpect(this.list10.addToEnd(ant),
            new ConsLoWord(this.ant, 
                new ConsLoWord(this.bat,
                    new ConsLoWord(this.cat,
                        new ConsLoWord(this.ant, this.mt)))));
  }

  //tests for the method filterOutEmpties
  boolean testFilterOutEmpties(Tester t) {
    return
        //empty case
        t.checkExpect(this.mt.filterOutEmpties(), this.mt)
        //case without any empty strings
        && t.checkExpect(this.list10.filterOutEmpties(), this.list10)
        //case with an empty string
        && t.checkExpect(this.list11.filterOutEmpties(), this.list10)
        //case with multiple empty strings
        && t.checkExpect(this.list12.filterOutEmpties(), this.list10);
  }

  //tests for the method draw
  boolean testDraw(Tester t) {
    return
        //empty case
        t.checkExpect(this.mt.draw(this.wsEmpty), this.wsEmpty)
        //tests for non-empty case and empty scene
        && t.checkExpect(this.list10.draw(this.wsEmpty),
            new WorldScene(500, 500)
            .placeImageXY(new RectangleImage(500, 500, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("ant", 20, Color.RED), 0, 0)
            .placeImageXY(new TextImage("bat", 20, Color.RED), 2, 2)
            .placeImageXY(new TextImage("cat", 20, Color.BLUE), 2, 1))
        //tests for non-empty case and non-empty scene
        && t.checkExpect(this.list10.draw(this.wsWords),
            new WorldScene(500, 500)
            .placeImageXY(new RectangleImage(500, 500, OutlineMode.SOLID, Color.GRAY), 0, 0)
            .placeImageXY(new TextImage("apple", 20, Color.RED), 0, 0)
            .placeImageXY(new TextImage("banana", 20, Color.BLUE), 0, 1)
            .placeImageXY(new TextImage("ant", 20, Color.RED), 0, 0)
            .placeImageXY(new TextImage("bat", 20, Color.RED), 2, 2)
            .placeImageXY(new TextImage("cat", 20, Color.BLUE), 2, 1));
  }

  //test moveWord function in IWord
  boolean testMoveWord(Tester t) {
    return
        // Test for an empty list
        t.checkExpect(this.mt.moveWords(), this.mt) 
        // Test for a list with both active and inactive words
        && t.checkExpect(this.list10.moveWords(), listAfterMoving)
        // Test a list with multiple ActiveWord instances
        && t.checkExpect(new ConsLoWord(this.apple,
            new ConsLoWord(this.car,
                new ConsLoWord(this.carrot, this.mt))).moveWords(), listMultipleActiveMoved)
        // Test a mixed list with different starting y-coordinates
        && t.checkExpect(new ConsLoWord(this.apple,
            new ConsLoWord(this.banana,
                new ConsLoWord(this.car,
                    new ConsLoWord(this.charm, this.mt)))).moveWords(), mixedListDifferentYMoved);
  }

  //Test for anyReachedBottom() method
  boolean testAnyReachedBottom(Tester t) {
    IWord word = new ActiveWord("bottom", 100, 290);
    ILoWord listWithWord = new ConsLoWord(word, this.mt);
    ILoWord listWithBottomWord = new ConsLoWord(word, this.mt);
    return
        // Empty list case
        t.checkExpect(this.mt.anyReachedBottom(), false) 
        // No words at bottom
        && t.checkExpect(this.list10.anyReachedBottom(), false)
        // One word at bottom
        && t.checkExpect(listWithBottomWord.anyReachedBottom(), false) 
        // Multiple words, one at bottom
        && t.checkExpect(new ConsLoWord(this.apple, listWithWord).anyReachedBottom(), false);
  }

  //Test for finishedWord() method
  boolean testFinishedWord(Tester t) {
    // A word that has been completely typed out
    IWord finishedWord = new InactiveWord("", 100, 100); 
    ILoWord listWithFinishedWord = new ConsLoWord(finishedWord, this.mt);
    ILoWord listWithNoFinishedWord = new ConsLoWord(finishedWord, this.mt);
    return
        // Empty list case
        t.checkExpect(this.mt.finishedWord(), false) 
        // No words finished
        && t.checkExpect(this.list10.finishedWord(), false) 
        // One word finished
        && t.checkExpect(listWithFinishedWord.finishedWord(), true) 
        // Multiple words, one finished
        && t.checkExpect(new ConsLoWord(this.apple, listWithNoFinishedWord).finishedWord(), true);
  }

  //Test for convertInactive() method
  boolean testConvertInactive(Tester t) {
    // Setup test data
    IWord inactiveWord1 = new InactiveWord("test", 100, 100);
    IWord inactiveWord2 = new InactiveWord("example", 200, 200);
    IWord activeWord = new ActiveWord("active", 300, 300);
    ILoWord mixedList = new ConsLoWord(inactiveWord1, 
        new ConsLoWord(inactiveWord2, 
            new ConsLoWord(activeWord, this.mt)));
    ILoWord expectedListAfterConversion = new ConsLoWord(new ActiveWord("test", 100, 100), 
        new ConsLoWord(inactiveWord2, 
            new ConsLoWord(activeWord, this.mt)));
    return
        // Empty list case
        t.checkExpect(this.mt.convertInactive("test"), this.mt) 
        // No matching inactive words
        && t.checkExpect(mixedList.convertInactive("nonexistent"), 
            new ConsLoWord(new InactiveWord("test", 100, 100), 
                new ConsLoWord(new InactiveWord("example", 200, 200), 
                    new ConsLoWord(activeWord, this.mt)))) 
        // One matching inactive word
        && t.checkExpect(mixedList.convertInactive("test"), new ConsLoWord(
            new ActiveWord("est", 100, 100), 
            new ConsLoWord(new InactiveWord("example", 200, 200), 
                new ConsLoWord(activeWord, this.mt)))) 
        // Multiple matching inactive words 
        //- assuming a setup where `inactiveWord2` also matches "test"
        // For this, you would need to adjust your setup to include another matching inactive word.
        // Combination of active and inactive words, with one match
        && t.checkExpect(expectedListAfterConversion.convertInactive("test"), new ConsLoWord(
            new ActiveWord("test", 100, 100), 
            new ConsLoWord(new InactiveWord("example", 200, 200), 
                new ConsLoWord(activeWord, this.mt))));
  }

  //test for hasActiveWord
  boolean testHasActiveWord(Tester t) {
    return t.checkExpect(this.mt.hasActiveWord(), false) 
        && t.checkExpect(this.list2.hasActiveWord(), true) 
        && t.checkExpect(this.list3.hasActiveWord(), true);
  }

  //tests for the method checkAndReduce
  boolean testReduceActive(Tester t) {
    return 
        //empty case
        t.checkExpect(this.mt.reduceActive("a"), this.mt)
        //list with active words matching
        && t.checkExpect(this.list10.reduceActive("a"), 
            new ConsLoWord(new ActiveWord("nt", 0, 0), list9))
        //list without active words matching
        && t.checkExpect(this.list10.reduceActive("z"), list10)
        //list with active words matching and input greater then string length 1
        && t.checkExpect(this.list1.reduceActive("dO"), 
            new ConsLoWord(new ActiveWord("Og", 1, 1), this.mt))
        //list with more than one word matching the letter
        && t.checkExpect(this.list13.reduceActive("b"), 
            new ConsLoWord(new ActiveWord("at", 2, 2), 
                new ConsLoWord(this.ant, 
                    new ConsLoWord(new ActiveWord("at", 2, 2),
                        new ConsLoWord(this.cat, this.mt)))))
        //list with an element that is one letter
        && t.checkExpect(this.unList6.reduceActive("c"), 
            new ConsLoWord(new ActiveWord("Ar", 0, 1),
                new ConsLoWord(new ActiveWord("", 0, 0),
                    new ConsLoWord(new ActiveWord("Carrot", 1, 0), this.mt))));
  }

}