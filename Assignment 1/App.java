public class App {
    public static void main(String[] args) throws Exception {
    TwoFourTree tft = new TwoFourTree();
    tft.addValue(10);//

    tft.addValue(1); //
    
    tft.addValue(14); //

    tft.addValue(3); // Problem here
    
    tft.addValue(5); //

    tft.addValue(12); //
    
    tft.addValue(2); //

    tft.addValue(16); //

    
    tft.printInOrder();
    }
}
