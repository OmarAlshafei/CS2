
public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;          
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;
        
        public boolean isLeaf() {
            if (root.leftChild == null 
                && root.rightChild == null
                && root.centerChild == null
                && root.centerLeftChild == null
                && root.centerRightChild == null)
                return true;
                
            else
                return false;
                
        }
        public boolean isTwoNode() {
            return (root.values == 1);
        }

        public boolean isThreeNode() {
            return (root.values == 2);
        }

        public boolean isFourNode() {
            return (root.values == 3);
        }

        public boolean isRoot() {
            return (root.parent == null);
        }

        public TwoFourTreeItem(int val1) {
        values = 2;
        value1 = val1;
        isLeaf = true;
        parent = null;
        leftChild = null;
        rightChild = null;
        centerChild = null;
        centerLeftChild = null;
        centerRightChild = null;
        }

        public TwoFourTreeItem(int val1, int val2) {
        values = 2;
        root.value1 = value1;
        root.value2 = value2;
        isLeaf = true;
        parent = null;
        leftChild = null;
        rightChild = null;
        centerChild = null;
        centerLeftChild = null;
        centerRightChild = null;
        }

        public TwoFourTreeItem(int val1, int val2, int val3) {
        values = 3;
        value1 = val1;
        value2 = val2;
        value3 = val3;
        isLeaf = true;
        parent = null;
        leftChild = null;
        rightChild = null;
        centerChild = null;
        centerLeftChild = null;
        centerRightChild = null;
        }

        private void printIndents(int indent) {
            for(int i = 0; i < indent; i++) System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if(!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if(isThreeNode()) {
                if(!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if(isFourNode()) {
                if(!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if(!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if(!isLeaf) rightChild.printInOrder(indent + 1);
        }
        
    }
    
    TwoFourTreeItem root = null;
        
    
    public boolean addValue(int value) {
        
        if (root == null){
            root = new TwoFourTreeItem(value);
            return false;
        }
        return hasValue(value);
    }
    
    
    public boolean hasValue(int value) {

    
        if (root.isFourNode()){
            
            if (value == root.value1 || value == root.value2 || value == root.value3)
                return true;   
            
            else if (root.isRoot()){
                TwoFourTreeItem newRoot = new TwoFourTreeItem(root.value2);
                root.parent = newRoot;
                newRoot.leftChild = new TwoFourTreeItem(root.value1);
                newRoot.rightChild = new TwoFourTreeItem(root.value3);
                newRoot.rightChild.isLeaf = true;
                newRoot.leftChild.isLeaf = true;
                root = newRoot;
            }
            
            int a = root.value1;
            int b = root.value2;
            int c = root.value3;
            
            TwoFourTreeItem x = root.parent;
            TwoFourTreeItem splitNode1 = new TwoFourTreeItem(a);
            TwoFourTreeItem splitNode2 = new TwoFourTreeItem(c);
            
            splitNode1.leftChild = root.leftChild;
            splitNode1.rightChild = root.centerLeftChild;
            splitNode1.parent = x;
            splitNode2.leftChild = root.centerRightChild;
            splitNode2.rightChild = root.rightChild;
            splitNode2.parent = x;            
            if (root == x.rightChild){
                if (x.isTwoNode()){
                    x.value2 = b;
                    x.rightChild = splitNode2;
                    x.centerChild = splitNode1;
                }
                if (x.isThreeNode()){
                    x.value3 = b;
                    x.rightChild = splitNode2;
                    x.centerRightChild = splitNode1;
                }
                root = x;
                hasValue(value);
            }
            else if (root == x.leftChild){
                if (x.isTwoNode()){
                    x.value2 = b;
                    x.leftChild = splitNode1;
                    x.centerChild = splitNode1;
                }
                if (x.isThreeNode()){
                    x.value3 = x.value2;
                    x.value2 = x.value1;
                    x.value1 = b;
                    x.leftChild = splitNode1;
                    x.centerLeftChild = splitNode2;
                }
                root = x;
                hasValue(value);
            }                
            else if (root == x.centerChild){

                if (x.isThreeNode()){
                    x.value3 = x.value2;
                    x.value2 = b;                    
                    x.centerLeftChild = splitNode1;
                    x.centerRightChild = splitNode2;
                }

            }
            
            hasValue(value);
        }
        
        else if (root.isThreeNode()){
    
            if (value == root.value1 || value == root.value2)
                return true;   
            
            else if (value < root.value1 && value < root.value2){
                if (root.isLeaf){
                    TwoFourTreeItem node = new TwoFourTreeItem(value, root.value1, root.value2);
                    root.value1 = node.value1;
                    root.value2 = node.value2;
                    root.value3 = node.value3;
                }
                else {
                root = root.leftChild;
                hasValue(value);
                }
            }
            else if (value > root.value1 && value > root.value2){
                if (root.isLeaf){
                    TwoFourTreeItem node = new TwoFourTreeItem(root.value1, root.value2, value);
                    root.value1 = node.value1;
                    root.value2 = node.value2;
                    root.value3 = node.value3;
                }
                else {
                root = root.rightChild;
                hasValue(value);
                }
            }
            else if (value > root.value1 && value < root.value2){
                if (root.isLeaf){
                    TwoFourTreeItem node = new TwoFourTreeItem(root.value1, value, root.value2);
                    root.value1 = node.value1;
                    root.value2 = node.value2;
                    root.value3 = node.value3;
                }
                else {
                root = root.centerChild;
                hasValue(value);
                }
            }
        }
        
        else if (root.isTwoNode()){
        
            if (value == root.value1)
                return true;   
            
            else if (value < root.value1){
                if (root.isLeaf){
                    TwoFourTreeItem node = new TwoFourTreeItem(value, root.value1);
                    root.value1 = node.value1;
                    root.value2 = node.value2;
                }
                else {
                root = root.leftChild;
                hasValue(value);
                }
            }
            else if (value > root.value1 && root.rightChild != null){
                if (root.isLeaf){
                    TwoFourTreeItem node = new TwoFourTreeItem(root.value1, value);
                    root.value1 = node.value1;
                    root.value2 = node.value2;

                }               
                else {
                root = root.rightChild;
                hasValue(value);
                }
            }
                
        }
        
    return false;
    }
    

        public boolean deleteValue(int value) {
            return false;
        }

        public void printInOrder() {
            if(root != null) root.printInOrder(0);
        }

        public TwoFourTree() {
        
        }
}
