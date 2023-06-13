public class App {
    public static void main(String[] args) throws Exception {
    TwoFourTree tft = new TwoFourTree();
    tft.addValue(23);

    tft.addValue(2); 
    tft.addValue(223);

    tft.addValue(20); 

    tft.addValue(213);

    tft.addValue(12); 
    tft.addValue(23);

    tft.addValue(21); 

    tft.printInOrder();
    }
}