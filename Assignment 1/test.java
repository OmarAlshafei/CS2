
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
        
    public void splitRoot(TwoFourTreeItem node) {
        
        if (node.isRoot()) {
            TwoFourTreeItem newRoot = new TwoFourTreeItem(node.value2);
            TwoFourTreeItem leftChild = new TwoFourTreeItem(node.value1);
            TwoFourTreeItem rightChild = new TwoFourTreeItem(node.value3);
            leftChild.parent = newRoot;
            rightChild.parent = newRoot;
            leftChild.isLeaf = true;
            rightChild.isLeaf = true;
            newRoot.leftChild = leftChild;
            newRoot.rightChild = rightChild;
        }
    }
    public void split(TwoFourTreeItem node) {

        int a = node.value1;
        int b = node.value2;
        int c = node.value3;
    
        TwoFourTreeItem parent = node.parent;
        TwoFourTreeItem leftChild = new TwoFourTreeItem(a);
        TwoFourTreeItem rightChild = new TwoFourTreeItem(c);
        leftChild.isLeaf = true;
        leftChild.isLeaf = true;    
        leftChild.leftChild = node.leftChild;
        leftChild.rightChild = node.rightChild;

        rightChild.leftChild = node.centerRightChild;
        rightChild.rightChild = node.rightChild;
    
        if (node == parent.rightChild) {
            if (parent.isTwoNode()) {
                parent = new TwoFourTreeItem(parent.value1, b);
                parent.rightChild = rightChild;
                parent.centerChild = leftChild;
            } 
            else if (parent.isThreeNode()) {
                parent = new TwoFourTreeItem(parent.value1, parent.value2, b);
                parent.rightChild = rightChild;
                parent.centerRightChild = leftChild;
            }
        } 
        else if (node == parent.leftChild) {
            if (parent.isTwoNode()) {
                parent = new TwoFourTreeItem(b, parent.value1);
                parent.leftChild = leftChild;
                parent.centerChild = parent.rightChild;
                parent.rightChild = rightChild;
            } 
            else if (parent.isThreeNode()) {
                parent = new TwoFourTreeItem(parent.value1, b, parent.value2);
                parent.leftChild = leftChild;
                parent.centerLeftChild = parent.centerRightChild;
                parent.centerRightChild = rightChild;
            }
        } 
        else if (node == parent.centerChild) {
            if (parent.isThreeNode()) {
                parent = new TwoFourTreeItem(parent.value1, b, parent.value2);
                parent.centerLeftChild = leftChild;
                parent.centerRightChild = rightChild;
            }
        }
    }

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }

        
        
        if (!hasValue(value)) {
            if (root.isFourNode()){
                if (root.isRoot()){
                    splitRoot(root);
            }
            else
                split(root);
            }
            if (root.isThreeNode()) {
                if (value < root.value1)
                root = new TwoFourTreeItem(value, root.value1, root.value2);
                
                else if (value > root.value2) 
                root = new TwoFourTreeItem(root.value1, root.value2, value);
                
                else 
                root = new TwoFourTreeItem(root.value1, value, root.value2);
            }
            if (root.isTwoNode()) {
                if (value < root.value1)
                    root = new TwoFourTreeItem(value, root.value1);
                else 
                    root = new TwoFourTreeItem(root.value1, value);
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
                if (node != null && node.isLeaf)
                    break;
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
