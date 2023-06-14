
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
        
    public void orderVals(int newValue){
        TwoFourTreeItem temp = root;
            if (isThreeNode()) {
                if (newValue < value1)
                temp = new TwoFourTreeItem(newValue, value1, value2);
                
                else if (newValue > value2) 
                temp = new TwoFourTreeItem(value1, value2, newValue);
                
                else 
                temp = new TwoFourTreeItem(value1, newValue, value2);
            }
            if (isTwoNode()) {
                if (newValue < value1)
                    temp = new TwoFourTreeItem(newValue, value1);
                else 
                    temp = new TwoFourTreeItem(value1, newValue);
            }   
            value1 = temp.value1;
            value2 = temp.value2;
            value3 = temp.value3;
            values = temp.values;
    }
        
    public void splitRoot() {
        
        if (isRoot()) {
            TwoFourTreeItem newRoot = new TwoFourTreeItem(value2);
            TwoFourTreeItem leftChild = new TwoFourTreeItem(value1);
            TwoFourTreeItem rightChild = new TwoFourTreeItem(value3);
            leftChild.parent = newRoot;
            rightChild.parent = newRoot;
            newRoot.leftChild = leftChild;
            newRoot.rightChild = rightChild;
            
            root = newRoot;
        }
    }
    public void split() {
    
        int b = value2;
    
        parent.orderVals(b);
        TwoFourTreeItem newChildL = new TwoFourTreeItem(value1);
        TwoFourTreeItem newChildR = new TwoFourTreeItem(value3);
        
        newChildL.leftChild = leftChild;
        newChildL.rightChild = centerLeftChild;
        
        newChildR.leftChild = centerRightChild;
        newChildR.rightChild = rightChild;
        
        newChildL.parent = parent;
        newChildR.parent = parent;
        if (newChildL.leftChild != null)
            newChildL.leftChild.parent = newChildL;
        if (newChildL.rightChild != null)
            newChildL.rightChild.parent = newChildL;
        if (newChildR.leftChild != null)
            newChildR.leftChild.parent = newChildR;
        if (newChildR.rightChild != null)
            newChildR.rightChild.parent = newChildR;
        
        if (root == parent.rightChild) {
            if (parent.isTwoNode()) {
                parent.rightChild = newChildR;
                parent.centerChild = newChildL;
            } 
            else if (parent.isThreeNode()) {
                parent.rightChild = newChildR;
                parent.centerRightChild = newChildL;
            }
        } 
        else if (root == parent.leftChild) {
            if (parent.isTwoNode()) {
                parent.leftChild = newChildL;
                parent.centerChild = newChildR;
            } 
            else if (parent.isThreeNode()) {
                parent.leftChild = newChildL;
                parent.centerLeftChild = newChildR;
            }
        } 
        else if (root == parent.centerChild) {
            if (parent.isThreeNode()) {
                parent.centerLeftChild = newChildL;
                parent.centerRightChild = newChildR;
            }
        }
        root = root.parent;
    }
        public void merge(int value) {
                
            if (root == null)
                return;
                        
            if (isFourNode()) {

            }
                
            else if (isThreeNode()) {
                    
                if(isLeaf)
                    orderVals(value);
                
                if (value <value1) 
                    root = leftChild;
                    
                else if (value > value2) 
                    root = rightChild;
                else
                    root = centerChild;
                    
                merge(value);
            } 
                
            else if (isTwoNode()) {
                
                if(isLeaf)
                    orderVals(value);
                    
                if (value < value1) 
                    root = leftChild;
                    
                else 
                    root = rightChild;
                    
                    merge(value);
            }
            
        }
        
    }
    
    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }
        
        else if (!hasValue(value)) {
            
            if (!root.isFourNode())
                root.orderVals(value);
            
            else{
                if (root.isRoot()){
                    root.splitRoot();
                    root.isLeaf = false;
                }
                else{
                    root.merge(value);
                }
            }
            
            return true;

        }
        
        return false;
    }
    
    
        public boolean hasValue(int value) {
            
            if (root == null)
                return false;
                
            if (!searchValue(root, value))
                return false;

            else return true;
        }    
    
        public boolean searchValue(TwoFourTreeItem node, int value) {
            
        if (node == null)
            return false;
                    
        if (node.isFourNode()) {
            if (value == node.value1 || value == node.value2 || value == node.value3)
                return true;
                
            else if (value < node.value1) 
                node = node.leftChild;
                    
            else if (value > node.value3)
                node = node.rightChild;
                    
            else if (value < node.value3 && value > node.value2)
                node = node.centerRightChild;
                    
            else if (value > node.value1 && value < node.value2)
                node = node.centerLeftChild;
                
                return searchValue(node, value);
        }
            
        else if (node.isThreeNode()) {
            if (value == node.value1 || value == node.value2) 
                return true;
                
            else if (value < node.value1) 
                node = node.leftChild;
                
            else if (value > node.value2) 
                node = node.rightChild;
            else
                node = node.centerChild;
                
            return searchValue(node, value);
        } 
            
        else if (node.isTwoNode()) {
            if (value == node.value1) 
                return true;
                
            else if (value < node.value1) 
                node = node.leftChild;
                
            else 
                node = node.rightChild;
                
                return searchValue(node, value);
        }
        
        else{
            return false;
        }
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
