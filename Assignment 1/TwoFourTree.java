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
            TwoFourTreeItem newRoot = new TwoFourTreeItem(value2);
            TwoFourTreeItem leftChild = new TwoFourTreeItem(value1);
            TwoFourTreeItem rightChild = new TwoFourTreeItem(value3);
            leftChild.parent = newRoot;
            rightChild.parent = newRoot;
            newRoot.leftChild = leftChild;
            newRoot.rightChild = rightChild;
            newRoot.centerChild = null;
            newRoot.centerRightChild = null;
            newRoot.centerLeftChild = null;
            newRoot.isLeaf = false;
            root = newRoot;
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
            TwoFourTreeItem leftChild = new TwoFourTreeItem(value1);
            TwoFourTreeItem rightChild = new TwoFourTreeItem(value3);
            
            int b = value2;
            parent.orderValues(b);
            leftChild.parent = parent;
            rightChild.parent = parent;

            leftChild.leftChild = this.leftChild;
            leftChild.rightChild = this.centerLeftChild;
            rightChild.leftChild = this.centerRightChild;
            rightChild.rightChild = this.rightChild;

            if (leftChild.leftChild != null) 
                leftChild.leftChild.parent = leftChild;
            
            if (leftChild.rightChild != null) 
                leftChild.rightChild.parent = leftChild;
            
            if (rightChild.leftChild != null) 
                rightChild.leftChild.parent = rightChild;
            
            if (rightChild.rightChild != null) 
                rightChild.rightChild.parent = rightChild;
            
            
            if (parent.centerChild == this) {
                if (parent.isFourNode()) {
                    parent.centerLeftChild = leftChild;
                    parent.centerRightChild = rightChild;
                    parent.leftChild = parent.leftChild;
                    parent.rightChild = parent.rightChild;

                }
            } else if (parent.leftChild == this) {
                if (parent.isThreeNode()) {
                    parent.centerChild = rightChild;
                    parent.rightChild = parent.rightChild;
                    parent.leftChild = leftChild;
                } else if (parent.isFourNode()) {
                    parent.centerLeftChild = rightChild;
                    parent.centerRightChild = parent.centerChild;
                    parent.rightChild = parent.rightChild;
                    parent.leftChild = leftChild;
                }
            }
            else if (parent.rightChild == this) {
                if (parent.isThreeNode()) {
                    parent.centerChild = leftChild;
                    parent.rightChild = rightChild;
                    parent.leftChild = parent.leftChild;
                } else if (parent.isFourNode()) {
                    parent.centerLeftChild = parent.centerChild;
                    parent.centerRightChild = leftChild;
                    parent.rightChild = rightChild;
                    parent.leftChild = parent.leftChild;
                }
            }

            

            if (parent.leftChild != null) 
                parent.leftChild.isLeaf = true;
            if (leftChild.centerLeftChild != null) 
                parent.centerLeftChild.isLeaf = true;
            if (leftChild.centerRightChild != null) 
                parent.centerRightChild.isLeaf = true;
            if (leftChild.rightChild != null) 
                parent.rightChild.isLeaf = true;

                root = parent;
            
        }



        

        public void insertValue(int value) {
            if (isFourNode()) {
                this.split();
                if (this.isLeaf)
                    orderValues(value);
                else    
                    this.insertValue(value);
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

        else if (root.isRoot() && root.isFourNode())
            root.splitRoot();
        
            
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
