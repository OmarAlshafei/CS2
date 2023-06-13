public class App {
    public static void main(String[] args) throws Exception {
    TwoFourTree tft = new TwoFourTree();
    tft.addValue(23);
    tft.addValue(24);
    tft.printInOrder();
     tft.addValue(2);   
    tft.addValue(1);    
     tft.addValue(42);   
    tft.addValue(31);
    tft.addValue(2);    
    tft.printInOrder();
    }
}