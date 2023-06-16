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
            if(root == null || isFourNode())
                return; 
                           
            TwoFourTreeItem temp = root;
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

                    
        public void splitRoot() {
            TwoFourTreeItem lChild = new TwoFourTreeItem(value1);
            TwoFourTreeItem rChild = new TwoFourTreeItem(value3);
            lChild.parent = root;
            lChild.parent = root;
            
            if (leftChild != null)
                leftChild.parent = lChild;
                
            if (centerLeftChild != null)
                centerLeftChild.parent = lChild;
                
            if (centerRightChild != null)
                centerRightChild.parent = rChild;
                
            if (rightChild != null) 
                rightChild.parent = rChild;
                
            leftChild = lChild;
            rightChild = rChild;
            centerChild = null;
            centerLeftChild = null;
            centerRightChild = null;
            value1 = value2;
            value2 = 0;
            value3 = 0;
            values = 1;
            parent = null;
            isLeaf = false;
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
            if (isRoot()) {
                splitRoot();
            } else {
                TwoFourTreeItem newRightNode = new TwoFourTreeItem(value3);
                newRightNode.leftChild = centerRightChild;
                newRightNode.rightChild = rightChild;
                if (centerRightChild != null) {
                    centerRightChild.parent = newRightNode;
                }
                if (rightChild != null) {
                    rightChild.parent = newRightNode;
                }
                
                values = 1;
                centerRightChild = null;
                rightChild = null;
                parent.merge(this, newRightNode, value2);
                value3 = 0;
                value2 = 0;
            }
        }

        private void merge(TwoFourTreeItem oldChild, TwoFourTreeItem newChild, int newParentVal) {
            if (isTwoNode()) {
                if (oldChild == leftChild) {
                    centerChild = newChild;
                } else {
                    rightChild = newChild;
                    centerChild = oldChild;
                }
            } else if (isThreeNode()) {
                if (oldChild == leftChild) {
                    centerLeftChild = newChild;
                    centerRightChild = centerChild;
                } 
                else if (oldChild == centerChild) {
                    centerLeftChild = leftChild;
                    centerRightChild = newChild;
                } 
                else {
                    centerRightChild = leftChild;
                    rightChild = newChild;
                }
                
            }
            orderValues(newParentVal);
        }

        

        public void insertValue(int value) {
            if (isFourNode()) {
                    split();
            }

            if (isLeaf) {
                orderValues(value);
            } 
            else {
                if (isTwoNode()) {
                    if (value < value1) {
                        leftChild.insertValue(value);
                    } else {
                        rightChild.insertValue(value);
                    }
                }
                else if (isThreeNode()) {
                    if (value < value1) {
                        leftChild.insertValue(value);
                    } else if (value > value2) {
                        rightChild.insertValue(value);
                    } else {
                        centerChild.insertValue(value);
                    }
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