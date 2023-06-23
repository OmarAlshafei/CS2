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

        private void printInOrder(int indent) {
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
        
        private void orderValues(int newValue) {
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

        private boolean searchValue(int value) {
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

       private TwoFourTreeItem split() {
            TwoFourTreeItem newLeftNode = new TwoFourTreeItem(value1);
            TwoFourTreeItem newRightNode = new TwoFourTreeItem(value3);
            
            newLeftNode.leftChild = leftChild;
            newLeftNode.rightChild = centerLeftChild;
            newRightNode.leftChild = centerRightChild;
            newRightNode.rightChild = rightChild;
            
            if (leftChild != null){
                leftChild.parent = newLeftNode;
                newLeftNode.isLeaf = false;
            }
            if (centerLeftChild != null){
                centerLeftChild.parent = newLeftNode;
                newLeftNode.isLeaf = false;
            }
            if (centerRightChild != null){
                centerRightChild.parent = newRightNode;
                newRightNode.isLeaf = false;
            }
            if (rightChild != null) {
                rightChild.parent = newRightNode;
                newRightNode.isLeaf = false;
            }
            if (isRoot()){
                root = new TwoFourTreeItem(value2);
                root.leftChild = newLeftNode;
                root.rightChild = newRightNode;
                newLeftNode.parent = root;
                newRightNode.parent = root;
                root.isLeaf = false;
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

        private TwoFourTreeItem insertValue(int value) {
            TwoFourTreeItem node = this;
                
            if (isFourNode()) {
                node = split();
                if(node.isFourNode()){
                    node.insertValue(value);
                }
            }
            if (node.isLeaf) {
                node.orderValues(value);
                return node;
            } 
            if (node.isTwoNode()) {
                    if (value < node.value1)
                        return node.leftChild.insertValue(value);
                    else
                        return node.rightChild.insertValue(value);
            } 
            if (node.isThreeNode()) {
                    if (value < value1)
                        return node.leftChild.insertValue(value);
                    else if (value > value2)
                        return node.rightChild.insertValue(value);
                    else
                        return node.centerChild.insertValue(value);
            } 
            else{
                if(value < node.value1) 
                    return node.leftChild; 

                else if(value > node.value3) 
                    return node.rightChild;

                else if(value < node.value2) 
                    return node.centerLeftChild;

                else 
                    return node.centerRightChild;
            }
        }
        
        private TwoFourTreeItem delete(int value){
            TwoFourTreeItem node = this;
            if (isRoot() && isTwoNode() && leftChild.isTwoNode() && rightChild.isTwoNode()) {
                node = new TwoFourTreeItem(leftChild.value1, value1, rightChild.value1);
                node.leftChild = leftChild.leftChild;
                node.centerLeftChild = leftChild.rightChild;
                node.centerRightChild = rightChild.leftChild;
                node.rightChild = rightChild.rightChild;
                leftChild.leftChild.parent = node;
                leftChild.rightChild.parent = node;
                rightChild.leftChild.parent = node;
                rightChild.rightChild.parent = node;
                return node.delete(value);
            }

            else if (isThreeNode()) {
            } 
            else if (isFourNode()) {

            }
            return this;
        }
    
    }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return false;
        }
        if (!hasValue(value))
            return true;
        else 
            root.insertValue(value);
        return false;
    }
        
    public boolean hasValue(int value) {
        return (!root.searchValue(value));
    } 
    

    public boolean deleteValue(int value) {
        if (!hasValue(value))
            return true;
        
        root.delete(value);
            
        return false;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {
        
    }
}