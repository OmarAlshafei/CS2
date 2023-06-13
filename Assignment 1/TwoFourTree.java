
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
            return (values == 1);
        }

        public boolean isThreeNode() {
            return (values == 2);
        }

        public boolean isFourNode() {
            return (values == 3);
        }

        public boolean isRoot() {
            return (parent == null);
        }

        public TwoFourTreeItem(int value1) {
            this.value1 = value1;
        }

        public TwoFourTreeItem(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
            values = 2;
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            values = 3;
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
        
    public void split(TwoFourTreeItem node) {
        
        if (node.isRoot()) {
            TwoFourTreeItem newRoot = new TwoFourTreeItem(node.value2);
            newRoot.leftChild = new TwoFourTreeItem(node.value1);
            newRoot.rightChild = new TwoFourTreeItem(node.value3);
            newRoot.rightChild.isLeaf = true;
            newRoot.leftChild.isLeaf = true;
            root = newRoot;
        }
        
        else {
            int a = node.value1;
            int b = node.value2;
            int c = node.value3;
    
            TwoFourTreeItem parent = node.parent;
            TwoFourTreeItem splitNode1 = new TwoFourTreeItem(a);
            TwoFourTreeItem splitNode2 = new TwoFourTreeItem(c);
    
            splitNode1.leftChild = node.leftChild;
            splitNode1.rightChild = node.centerLeftChild;
    
            splitNode2.leftChild = node.centerRightChild;
            splitNode2.rightChild = node.rightChild;
    
            if (node == parent.rightChild) {
                if (parent.values == 1) {
                    parent.values = 2;
                    parent.value2 = b;
                    parent.rightChild = splitNode2;
                    parent.centerChild = splitNode1;
                } 
                else if (parent.values == 2) {
                    parent.values = 3;
                    parent.value3 = b;
                    parent.rightChild = splitNode2;
                    parent.centerRightChild = splitNode1;
                }
                node.parent = parent;
            } else if (node == parent.leftChild) {
                if (parent.values == 1) {
                    parent.values = 2;
                    parent.value2 = b;
                    parent.leftChild = splitNode1;
                    parent.centerChild = parent.rightChild;
                    parent.rightChild = splitNode2;
                } 
                else if (parent.values == 2) {
                    parent.values = 3;
                    parent.value3 = parent.value2;
                    parent.value2 = b;
                    parent.leftChild = splitNode1;
                    parent.centerLeftChild = parent.centerRightChild;
                    parent.centerRightChild = splitNode2;
                }
                node.parent = parent;
            } 
            else if (node == parent.centerChild) {
                if (parent.values == 2) {
                    parent.values = 3;
                    parent.value3 = parent.value2;
                    parent.value2 = b;
                    parent.centerLeftChild = splitNode1;
                    parent.centerRightChild = splitNode2;
                }
                node.parent = parent;
            }
        }
    }

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }

        if (!hasValue(value)) {
            if (root.isFourNode())
                split(root);

            if (root.isTwoNode()) {
                if (value < root.value1)
                    root = new TwoFourTreeItem(value, root.value1);
                else 
                    root = new TwoFourTreeItem(root.value1, value);
            }
            
            else if (root.isThreeNode()) {
                if (value < root.value1)
                    root = new TwoFourTreeItem(value, root.value1, root.value2);
                    
                else if (value > root.value2) 
                    root = new TwoFourTreeItem(root.value1, root.value2, value);
                    
                else 
                    root = new TwoFourTreeItem(root.value1, value, root.value2);
            }
            
            return true;
        }
        
        return false;
    }
    
    public boolean hasValue(int value) {
        
        if (root == null)
            return false;
        

        TwoFourTreeItem node = root;
        while (node != null) {
            if (node.isThreeNode()) {
                if (value == node.value1 || value == node.value2 || value == node.value3)
                    return true;

                if (value < node.value1) 
                    node = node.leftChild;
                    
                else if (value > node.value2)
                    node = node.rightChild;
                    
                else
                    node = node.centerChild;
            } 
            
            else if (node.isTwoNode()) {
                if (value == node.value1 || value == node.value2) 
                    return true;
                
                if (value < node.value1) 
                    node = node.leftChild;
                    
                else 
                    node = node.rightChild;
            } 
            
            else {
                if (value == node.value1) 
                    return true;
                
                if (value < node.value1) 
                    node = node.leftChild;
                    
                else 
                    node = node.rightChild;
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
