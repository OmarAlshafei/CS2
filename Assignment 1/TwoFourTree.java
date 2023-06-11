
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
            if (root.parent == null)
                return true;
            return false;
        }

        public TwoFourTreeItem(int value1) {
        root.values = 1;
        root.value1 = value1;
        isLeaf = true;
        parent = root.parent;
        leftChild = null;
        rightChild = null;
        centerChild = null;
        centerLeftChild = null;
        centerRightChild = null;
        }

        public TwoFourTreeItem(int value1, int value2) {
        root.values = 2;
        root.value1 = value1;
        root.value2 = value2;
        isLeaf = true;
        parent = root.parent;
        leftChild = null;
        rightChild = null;
        centerChild = null;
        centerLeftChild = null;
        centerRightChild = null;
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
        root.values = 3;
        root.value1 = value1;
        root.value2 = value2;
        root.value3 = value3;        
        isLeaf = true;
        parent = root.parent;
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
        
        if(root == null && root.isRoot()) {
            root = new TwoFourTreeItem(value);
            return false;
        }
        
        if(root.isRoot()) {
            root = new TwoFourTreeItem(value);
            return false;
        }
        
        else if (root.isFourNode()){
        
            if (value == root.value1 || value == root.value2 || value == root.value3)
                return true;   
            
            else if (value < root.value1 && value < root.value2 && value < root.value3){
                if (root.leftChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(value);
                    root.leftChild = newRoot;
                    return false;
                }
                
                TwoFourTreeItem newRoot = new TwoFourTreeItem(root.value2);
                root.parent.value1 = root.value2;
                root.parent.values = 3;
                root.parent.centerLeftChild = newRoot.leftChild;                
                root.parent.leftChild = 
                newRoot.leftChild = root.leftChild;
                root.parent.leftChild = root.value1
                root.leftChild.parent = root;                
                root = root.leftChild;
                addValue(value);
            }    
            else if (value > root.value1 && value > root.value2 && value > root.value3){
                if (root.rightChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(value);
                    int x = root.value2;
                    root.parent
                    return false;
                }
                root.rightChild.parent = root;                
                root = root.rightChild;
                addValue(value);
            }
            else if (value > root.value1 && value < root.value2){
                if (root.centerLeftChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(value);
                    root = newRoot;
                    return false;
                }
                root.centerLeftChild.parent = root;                                
                root = root.centerLeftChild;
                addValue(value);
            }
            else if (value > root.value1 && value > root.value2 && value < root.value3){
                if (root.centerRightChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(value);
                    root = newRoot;                    
                    return false;
                }
                root.centerRightChild.parent = root;                                                
                root = root.centerRightChild;
                addValue(value);
            }
        }
        
        else if (root.isThreeNode()){

            if (value == root.value1 || value == root.value2)
                return true;   
            
            else if (value < root.value1 && value < root.value2){
                if (root.leftChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(value, root.value1, root.value2);
                    root = newRoot;
                    return false;
                }
                root.isLeaf = false;
                root.leftChild.parent = root;
                root = root.leftChild;
                addValue(value);
            }
            else if (value > root.value1 && value > root.value2){
                if (root.rightChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(root.value1, root.value2, value);
                    root = newRoot;
                    return false;
                }
                root.isLeaf = false;
                root.rightChild.parent = root;
                root = root.rightChild;
                addValue(value);
            }
            else if (value > root.value1 && value < root.value2){
                if (root.centerChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(root.value1, value, root.value2);
                    root = newRoot;
                    return false;
                }
                root.isLeaf = false;
                root.centerChild.parent = root;
                root = root.centerChild;
                addValue(value);
            }
        }
        
        else if (root.isTwoNode()){
        
            if (value == root.value1)
                return true;   
            
            else if (value < root.value1){
                if (root.leftChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(value, root.value1);
                    root = newRoot;
                    return false;
                }
                root.isLeaf = false;
                root.leftChild.parent = root;
                root = root.leftChild;
                addValue(value);
            }
            else if (value > root.value1){
                if (root.rightChild == null){
                    TwoFourTreeItem newRoot = new TwoFourTreeItem(root.value1, value);
                    root = newRoot;
                    return false;
                }
                root.isLeaf = false;
                root.rightChild.parent = root;
                root = root.rightChild;
                addValue(value);
            }      
        }
        else
            return false;
    }


        public boolean hasValue(int value) {
            return false;
        }

        public boolean deleteValue(int value) {
            return false;
        }

        public void printInOrder() {
            if(root != null) root.printInOrder(0);
        }

        public TwoFourTree() {
            root = null;
            
        }
}
