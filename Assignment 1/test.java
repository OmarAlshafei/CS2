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
        
        
        public void orderValues(int newValue) {
                           
            TwoFourTreeItem temp = this;
            if (isThreeNode()){
                if (newValue < value1 && newValue < value2)
                    temp = new TwoFourTreeItem(newValue, value1, value2);
                else if (newValue > value1 && newValue > value2)
                    temp = new TwoFourTreeItem(value1, value2, newValue);
                else
                    temp = new TwoFourTreeItem(value1, newValue, value2);
            } 
            else if (isTwoNode()){
                if (newValue < value1)
                    temp = new TwoFourTreeItem(newValue, value1);
                else
                    temp = new TwoFourTreeItem(value1, newValue);
            }
            values = temp.values;
            value1 = temp.value1;
            value2 = temp.value2;
            value3 = temp.value3;
        }

                    


        
        public boolean searchValue(int value) {
            
            if (isFourNode()) {
                if (value == value1 || value == value2 || value == value3)
                    return true;
                        
                else if (value < value1 && leftChild != null) 
                    leftChild.searchValue(value);
                            
                else if (value > value3 && rightChild != null)
                    rightChild.searchValue(value);
                            
                else if (value < value3 && value > value2 && centerRightChild != null)
                    centerRightChild.searchValue(value);
                            
                else if (value > value1 && value < value2 && centerLeftChild != null)
                    centerLeftChild.searchValue(value);
            }
                
            else if (isThreeNode()) {
                if (value == value1 || value == value2) 
                    return true;
                        
                else if (value < value1 && leftChild != null) 
                    leftChild.searchValue(value);
                        
                else if (value > value2 && rightChild != null) 
                    rightChild.searchValue(value);
                    
                else if (value > value1 && value < value2 && centerChild != null)
                    centerChild.searchValue(value);
            } 
                
            else if (isTwoNode()) {
                if (value == value1) 
                    return true;
                        
                else if (value < value1 && leftChild != null) 
                    leftChild.searchValue(value);
                        
                else if (value > value1 && rightChild != null) 
                    rightChild.searchValue(value);
            }
            return false;
        }

        
       public TwoFourTreeItem split() {
            TwoFourTreeItem newLeftNode = new TwoFourTreeItem(value1);
            TwoFourTreeItem newRightNode = new TwoFourTreeItem(value3);
            
            newLeftNode.leftChild = leftChild;
            newLeftNode.rightChild = centerLeftChild;
            newRightNode.leftChild = centerRightChild;
            newRightNode.rightChild = rightChild;
            
            if (leftChild != null)
            leftChild.parent = newLeftNode;
            
            if (centerLeftChild != null) 
            centerLeftChild.parent = newLeftNode;
            
            if (centerRightChild != null)
            centerRightChild.parent = newRightNode;
            
            if (rightChild != null) 
            rightChild.parent = newRightNode;
            
            if (isRoot()){
                TwoFourTreeItem newRoot = new TwoFourTreeItem(value2);
                newRoot.leftChild = newLeftNode;
                newRoot.rightChild = newRightNode;
                newLeftNode.parent = newRoot;
                newRightNode.parent = newRoot;
                newRoot.isLeaf = false;
                root = newRoot;
                return root;
            }
            else {
                newLeftNode.parent = parent;
                newRightNode.parent = parent;
                parent.orderValues(value2);
                parent.merge(this, newLeftNode, newRightNode);
                return parent;
            } 
        }

        private void merge(TwoFourTreeItem oldNode, TwoFourTreeItem lChild, TwoFourTreeItem rChild) {
            if (isThreeNode()) {
                if (oldNode == leftChild) {
                    leftChild = lChild;
                    centerChild = rChild;
                } 
                else {
                    rightChild = rChild;
                    centerChild = lChild;
                }
            } 
            else if (isFourNode()) {
                if (oldNode == leftChild) {
                    leftChild = lChild;
                    centerLeftChild = rChild;
                    centerRightChild = centerChild;
                    centerChild = null;
                }
                else if (oldNode == centerChild) {
                    centerChild = null;
                    centerLeftChild = lChild;
                    centerRightChild = rChild;
                }
                else if (oldNode == rightChild){
                    rightChild = rChild;
                    centerRightChild = lChild;
                    centerLeftChild = centerChild;
                }
            }
        }

        public TwoFourTreeItem insertValue(int value) {   
            TwoFourTreeItem node = this;    
              
            if (isFourNode()){
                node = split();
                if (value < node.value1)
                    node = node.leftChild;
                else if (value > node.value1)
                    node = node.rightChild;
            }
                            
            if (isLeaf){
                node.orderValues(value);
                return node;
            }
            
            else if (isTwoNode()) {
                    if (value < value1) 
                        return leftChild.insertValue(value);
                    else 
                        return rightChild.insertValue(value);
                }
                
            else if (isThreeNode()) {
                    if (value < value1)
                        return leftChild.insertValue(value);
                        
                    else if (value > value2)
                        return rightChild.insertValue(value);
                        
                    else
                        return centerChild.insertValue(value);
            }
            else
                return this;
        }
    }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return false;
        }
        
        else if (hasValue(value))
            return true;

        root.insertValue(value);
        
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
