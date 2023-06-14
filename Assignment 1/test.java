
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
        
    public void orderValues(int newValue){
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
        public boolean searchValue(int value) {
            
        if (root == null)
            return false;
                    
        if (root.isFourNode()) {
            if (value == root.value1 || value == root.value2 || value == root.value3)
                return true;
                
            else if (value < root.value1 && leftChild != null) 
                return leftChild.searchValue(value);
                    
            else if (value > root.value3 && rightChild != null)
                return rightChild.searchValue(value);
                    
            else if (value < value3 && value > value2 && centerRightChild != null)
                return centerRightChild.searchValue(value);
                    
            else if (value > value1 && value < value2 && centerLeftChild != null)
                return centerLeftChild.searchValue(value);
                
        }
            
        else if (isThreeNode()) {
            if (value == value1 || value == value2) 
                return true;
                
            else if (value < value1 && leftChild != null) 
                return leftChild.searchValue(value);
                
            else if (value > value2 && rightChild != null) 
                return rightChild.searchValue(value);
            
            else if (value > value1 && value < value2 && centerChild != null)
                return centerChild.searchValue(value);
                
        } 
            
        else if (isTwoNode()) {
            if (value == value1) 
                return true;
                
            else if (value < value1 && leftChild != null) 
                return leftChild.searchValue(value);
                
            else if (value > value1 && rightChild != null) 
                return rightChild.searchValue(value);
                
        }
        return false;
    }
    
    public void split() {
    
        int b = value2;
        TwoFourTreeItem newParent = root.parent;
        TwoFourTreeItem newChildL = new TwoFourTreeItem(value1);
        TwoFourTreeItem newChildR = new TwoFourTreeItem(value3);
        newParent.orderValues(b);
        
        newChildL.parent = newParent;
        newChildR.parent = newParent;
        if (newChildL.leftChild != null)
            newChildL.leftChild.parent = newChildL;
        if (newChildL.rightChild != null)
            newChildL.rightChild.parent = newChildL;
        if (newChildR.leftChild != null)
            newChildR.leftChild.parent = newChildR;
        if (newChildR.rightChild != null)
            newChildR.rightChild.parent = newChildR;
        
        if (root == parent.rightChild) {
            if (newParent.isTwoNode()) {
                newParent.rightChild = newChildR;
                newParent.centerChild = newChildL;
            } 
            else if (newParent.isThreeNode()) {
                newParent.rightChild = newChildR;
                newParent.centerRightChild = newChildL;
            }
        } 
        else if (root == newParent.leftChild) {
            if (newParent.isTwoNode()) {
                newParent.leftChild = newChildL;
                newParent.centerChild = newChildR;
            } 
            else if (newParent.isThreeNode()) {
                newParent.leftChild = newChildL;
                newParent.centerLeftChild = newChildR;
            }
        } 
        else if (root == newParent.centerChild) {
            if (newParent.isThreeNode()) {
                newParent.centerLeftChild = newChildL;
                newParent.centerRightChild = newChildR;
            }
        }
        root = newParent;
    }
    
        public void insertValue(int value) {
            
            TwoFourTreeItem newNode = root;
            
            if (isFourNode()) {
                
                split();
                
                orderValues(value);

            }
                
            else if (isThreeNode()) {
                if(!isLeaf){
                    if (value < value1) 
                        leftChild.insertValue(value);
                        
                    else if (value > value2) 
                        rightChild.insertValue(value);
                    
                    else if (value > value1 && value < value2)
                        centerChild.insertValue(value);
                }
                else{
                    orderValues(value);
                }
                    
            } 
                
            else if (isTwoNode()) {
                if(!isLeaf){
                    if (value < value1) 
                        leftChild.insertValue(value);
                        
                    else if (value > value1) 
                        rightChild.insertValue(value);
                }
                else{
                    orderValues(value);
                }
            }
        }
    }
    
    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return false;
        }
        
        if (hasValue(value))
            return true;
            
        else if (root.isRoot() && root.isFourNode()){
            root.splitRoot();
            root.isLeaf = false;
        }
        else{
            root.insertValue(value);
            
            }

        
        return false;
        
    }
        
    public boolean hasValue(int value) {
        if (root == null)
            return false;
            
        if (root.searchValue(value))
            return true;
                
        else 
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
