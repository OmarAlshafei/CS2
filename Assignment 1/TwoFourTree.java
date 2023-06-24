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
            if (value == value1 || value == value2 || value == value3)
                return true;
            else if (isFourNode()) {
                if (value < value1 && leftChild != null) 
                    leftChild.searchValue(value);
                            
                else if (value > value3 && rightChild != null)
                    rightChild.searchValue(value);
                            
                else if (value > value2 && centerRightChild != null)
                    centerRightChild.searchValue(value);
                            
                else if (centerLeftChild != null)
                    centerLeftChild.searchValue(value);
                else
                    return false;
            }
            else if (isThreeNode()) {
                if (value < value1 && leftChild != null) 
                    leftChild.searchValue(value);
                        
                else if (value > value2 && rightChild != null) 
                    rightChild.searchValue(value);
                    
                else if (centerChild != null)
                    centerChild.searchValue(value);
                else
                    return false;
            } 
            else if (isTwoNode()) {
                if (value < value1 && leftChild != null) 
                    leftChild.searchValue(value);
                        
                else if (rightChild != null) 
                    rightChild.searchValue(value);
                else
                    return false;
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
            if (node.isFourNode()) {
                node = split();
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
                if (value < node.value1)
                    return node.leftChild.insertValue(value);
                else if (value > node.value3)
                    return node.rightChild.insertValue(value);
                else if (value > node.value2)
                    return node.centerRightChild.insertValue(value);
                else
                    return node.centerLeftChild.insertValue(value);    
            }
            
        }
        

        private TwoFourTreeItem omgTwoNode(int value){
            TwoFourTreeItem node = this;

            if (node.parent.isFourNode()){
                //rotate anticlockwise
                if(node == node.parent.leftChild && !node.parent.centerLeftChild.isTwoNode()){
                    int pVal = parent.value1;
                    node.value2 = pVal;
                    node.parent.value1 = node.parent.centerLeftChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerLeftChild.leftChild;
                    node.values++;

                    // fix R values
                    node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
                    node.parent.centerLeftChild.value2 = node.parent.centerLeftChild.value3;
                    node.parent.centerLeftChild.value3 = 0;

                    // fix R pointers
                    if(node.parent.centerLeftChild.isThreeNode()){
                        node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerChild;
                        node.parent.centerLeftChild.centerChild = null;
                    }
                    if(node.parent.centerLeftChild.isFourNode()){
                        node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerLeftChild;
                        node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerRightChild;
                        node.parent.centerLeftChild.centerLeftChild = null;
                        node.parent.centerLeftChild.centerRightChild = null;
                    }
                    node.parent.centerLeftChild.values--;                
                }
                //rotate anticlockwise
                else if(node == node.parent.centerLeftChild && !node.parent.centerRightChild.isTwoNode()){
                    int pVal = parent.value2;
                    node.value2 = pVal;
                    node.parent.value2 = node.parent.centerRightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerRightChild.leftChild;
                    node.values++;

                    // fix R values
                    node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
                    node.parent.centerRightChild.value2 = node.parent.centerRightChild.value3;
                    node.parent.centerRightChild.value3 = 0;

                    // fix R pointers
                    if(node.parent.centerRightChild.isThreeNode()){
                        node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerChild;
                        node.parent.centerRightChild.centerChild = null;
                    }
                    if(node.parent.centerRightChild.isFourNode()){
                        node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerLeftChild;
                        node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerRightChild;
                        node.parent.centerRightChild.centerLeftChild = null;
                        node.parent.centerRightChild.centerRightChild = null;
                    }
                    node.parent.centerRightChild.values--;   
                }
                //rotate clockwise
                else if(node == node.parent.centerLeftChild && !node.parent.leftChild.isTwoNode()){
                    int pVal = parent.value1;
                    node.value2 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.leftChild.rightChild;
                    node.values++;

                    // fix L pointers
                    if(node.parent.leftChild.isThreeNode()){
                        node.parent.value1 = node.parent.leftChild.value2;
                        node.parent.leftChild.value2 = 0;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                        node.parent.leftChild.centerChild = null;
                    }
                    if(node.parent.leftChild.isFourNode()){
                        node.parent.value1 = node.parent.leftChild.value3;
                        node.parent.leftChild.value3 = 0;
                        node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                        node.parent.leftChild.centerLeftChild = null;
                        node.parent.leftChild.centerRightChild = null;
                    }
                    node.parent.leftChild.values--;   
                }
                //rotate anticlockwise
                else if(node == node.parent.centerRightChild && !node.parent.rightChild.isTwoNode()){
                    int pVal = parent.value3;
                    node.value2 = pVal;
                    node.parent.value3 = node.parent.centerRightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.rightChild.leftChild;
                    node.values++;

                    // fix R values
                    node.parent.rightChild.value1 = node.parent.rightChild.value2;
                    node.parent.rightChild.value2 = node.parent.rightChild.value3;
                    node.parent.rightChild.value3 = 0;

                    // fix R pointers
                    if(node.parent.rightChild.isThreeNode()){
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                        node.parent.rightChild.centerChild = null;
                    }
                    if(node.parent.rightChild.isFourNode()){
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                        node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                        node.parent.rightChild.centerLeftChild = null;
                        node.parent.rightChild.centerRightChild = null;
                    }
                    node.parent.rightChild.values--;                   
                }
                //rotate clockwise
                else if(node == node.parent.centerRightChild && !node.parent.centerLeftChild.isTwoNode()){
                    int pVal = parent.value2;
                    node.value2 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerLeftChild.rightChild;
                    node.values++;

                    // fix L pointers
                    if(node.parent.centerLeftChild.isThreeNode()){
                        node.parent.value2 = node.parent.centerLeftChild.value2;
                        node.parent.centerLeftChild.value2 = 0;
                        node.parent.centerLeftChild.rightChild = node.parent.leftChild.centerChild;
                        node.parent.centerLeftChild.centerChild = null;
                    }
                    if(node.parent.centerLeftChild.isFourNode()){
                        node.parent.value1 = node.parent.leftChild.value3;
                        node.parent.centerLeftChild.value3 = 0;
                        node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerLeftChild;
                        node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerRightChild;
                        node.parent.centerLeftChild.centerLeftChild = null;
                        node.parent.centerLeftChild.centerRightChild = null;
                    }
                    node.parent.centerLeftChild.values--;     
                }
                //rotate clockwise
                else if(node == node.parent.rightChild && !node.parent.centerRightChild.isTwoNode()){
                    int pVal = parent.value3;
                    node.value2 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerRightChild.rightChild;
                    node.values++;

                    // fix L pointers
                    if(node.parent.centerRightChild.isThreeNode()){
                        node.parent.value3 = node.parent.centerRightChild.value2;
                        node.parent.centerRightChild.value2 = 0;
                        node.parent.centerRightChild.rightChild = node.parent.leftChild.centerChild;
                        node.parent.centerRightChild.centerChild = null;
                    }
                    if(node.parent.centerRightChild.isFourNode()){
                        node.parent.value3 = node.parent.leftChild.value3;
                        node.parent.centerRightChild.value3 = 0;
                        node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerLeftChild;
                        node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerRightChild;
                        node.parent.centerRightChild.centerLeftChild = null;
                        node.parent.centerRightChild.centerRightChild = null;
                    }
                    node.parent.centerRightChild.values--;  
                }
                else{
                    if(node == node.parent.leftChild){
                        node.value2 = node.parent.value1;
                        node.value3 = node.parent.centerLeftChild.value1;
                        node.values = 3;
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.centerLeftChild.leftChild;
                        node.rightChild = node.parent.centerLeftChild.rightChild;
                        node.parent.centerChild = node.parent.centerRightChild;
                        node.parent.centerLeftChild = null;
                        node.parent.centerRightChild = null;
                        node.parent.value1 = node.parent.value2;
                        node.parent.value2 = node.parent.value3;
                        node.parent.value3 = 0;
                        node.parent.values--;
                    }
                    else if(node == node.parent.centerLeftChild){
                        node = node.parent.leftChild;
                        node.value2 = node.parent.value1;
                        node.value3 = node.parent.centerLeftChild.value1;
                        node.values = 3;
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.centerLeftChild.leftChild;
                        node.rightChild = node.parent.centerLeftChild.rightChild;
                        node.parent.centerChild = node.parent.centerRightChild;
                        node.parent.centerLeftChild = null;
                        node.parent.centerRightChild = null;
                        node.parent.value1 = node.parent.value2;
                        node.parent.value2 = node.parent.value3;
                        node.parent.value3 = 0;
                        node.parent.values--;
                    }
                    else if(node == node.parent.centerRightChild){
                        node = node.parent.rightChild;
                        node.value3 = node.value1;
                        node.value2 = node.parent.value3;
                        node.value1 = node.parent.centerRightChild.value1;
                        node.values = 3;
                        node.leftChild = node.parent.centerChild.leftChild;
                        node.centerLeftChild = node.parent.centerChild.rightChild;
                        node.centerRightChild = node.leftChild;
                        node.parent.centerChild = node.parent.centerLeftChild;
                        node.parent.centerLeftChild = null;
                        node.parent.centerRightChild = null;
                        node.parent.value3 = 0;
                        node.parent.values--;
                    }
                    else if(node == node.parent.rightChild){
                        node.value3 = node.value1;
                        node.value2 = node.parent.value3;
                        node.value1 = node.parent.centerRightChild.value1;
                        node.values = 3;
                        node.leftChild = node.parent.centerChild.leftChild;
                        node.centerLeftChild = node.parent.centerChild.rightChild;
                        node.centerRightChild = node.leftChild;
                        node.parent.centerChild = node.parent.centerLeftChild;
                        node.parent.centerLeftChild = null;
                        node.parent.centerRightChild = null;
                        node.parent.value3 = 0;
                        node.parent.values--;
                    }
                }
            }
            if (node.parent.isThreeNode()){
                //rotate anticlockwise
                if(node == node.parent.leftChild && !node.parent.centerChild.isTwoNode()){
                    int pVal = parent.value1;
                    node.value2 = pVal;
                    node.parent.value1 = node.parent.centerChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerChild.leftChild;
                    node.values++;

                    // fix R values
                    node.parent.centerChild.value1 = node.parent.centerChild.value2;
                    node.parent.centerChild.value2 = node.parent.centerChild.value3;
                    node.parent.centerChild.value3 = 0;

                    // fix R pointers
                    if(node.parent.centerChild.isThreeNode()){
                        node.parent.centerChild.leftChild = node.parent.centerChild.centerChild;
                        node.parent.centerChild.centerChild = null;
                    }
                    if(node.parent.centerChild.isFourNode()){
                        node.parent.centerChild.leftChild = node.parent.centerChild.centerLeftChild;
                        node.parent.centerChild.centerChild = node.parent.centerChild.centerRightChild;
                        node.parent.centerChild.centerLeftChild = null;
                        node.parent.centerChild.centerRightChild = null;
                    }
                    node.parent.centerChild.values--;   
                }
                //rotate anticlockwise
                else if(node == node.parent.centerChild && !node.parent.rightChild.isTwoNode()){
                    int pVal = parent.value2;
                    node.value2 = pVal;
                    node.parent.value2 = node.parent.rightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.rightChild.leftChild;
                    node.values++;

                    // fix R values
                    node.parent.rightChild.value1 = node.parent.rightChild.value2;
                    node.parent.rightChild.value2 = node.parent.rightChild.value3;
                    node.parent.rightChild.value3 = 0;

                    // fix R pointers
                    if(node.parent.rightChild.isThreeNode()){
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                        node.parent.rightChild.centerChild = null;
                    }
                    if(node.parent.rightChild.isFourNode()){
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                        node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                        node.parent.rightChild.centerLeftChild = null;
                        node.parent.rightChild.centerRightChild = null;
                    }
                    node.parent.centerChild.values--;   
                }
                //rotate clockwise
                else if(node == node.parent.centerChild && !node.parent.leftChild.isTwoNode()){
                    int pVal = parent.value1;
                    node.value2 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.leftChild.rightChild;
                    node.values++;

                    // fix R pointers
                    if(node.parent.leftChild.isThreeNode()){
                        node.parent.value1 = node.parent.leftChild.value2;
                        node.parent.leftChild.value2 = 0;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                        node.parent.leftChild.centerChild = null;
                    }
                    if(node.parent.leftChild.isFourNode()){
                        node.parent.value1 = node.parent.leftChild.value3;
                        node.parent.leftChild.value3 = 0;
                        node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                        node.parent.leftChild.centerLeftChild = null;
                        node.parent.leftChild.centerRightChild = null;
                    }
                    node.parent.leftChild.values--;  
                }
                //rotate clockwise
                else if(node == node.parent.rightChild && !node.parent.centerChild.isTwoNode()){
                    int pVal = parent.value2;
                    node.value2 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerChild.rightChild;
                    node.values++;

                    // fix R pointers
                    if(node.parent.centerChild.isThreeNode()){
                        node.parent.value2 = node.parent.leftChild.value2;
                        node.parent.centerChild.value2 = 0;
                        node.parent.centerChild.rightChild = node.parent.centerChild.centerChild;
                        node.parent.centerChild.centerChild = null;
                    }
                    if(node.parent.centerChild.isFourNode()){
                        node.parent.value2 = node.parent.leftChild.value3;
                        node.parent.centerChild.value3 = 0;
                        node.parent.centerChild.centerChild = node.parent.centerChild.centerLeftChild;
                        node.parent.centerChild.rightChild = node.parent.centerChild.centerRightChild;
                        node.parent.centerChild.centerLeftChild = null;
                        node.parent.centerChild.centerRightChild = null;
                    }
                    node.parent.centerChild.values--;  
                }
                else{
                    if(node == node.parent.leftChild){
                        node.value2 = node.parent.value1;
                        node.value3 = node.parent.centerChild.value1;
                        node.values = 3;
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.centerChild.leftChild;
                        node.rightChild = node.parent.centerChild.rightChild;
                        node.parent.centerChild = null;
                        node.parent.value1 = node.parent.value2;
                        node.parent.value2 = 0;
                        node.parent.values--;
                    }
                    else if(node == node.parent.centerChild){
                        node = node.parent.leftChild;
                        node.value2 = node.parent.value1;
                        node.value3 = node.parent.centerChild.value1;
                        node.values = 3;
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.centerChild.leftChild;
                        node.rightChild = node.parent.centerChild.rightChild;
                        node.parent.centerChild = null;
                        node.parent.value1 = node.parent.value2;
                        node.parent.value2 = 0;
                        node.parent.values--;
                    }
                    else if(node == node.parent.rightChild){
                        node.value3 = node.value1;
                        node.value2 = node.parent.value2;
                        node.value1 = node.parent.centerChild.value1;
                        node.values = 3;
                        node.leftChild = node.parent.centerChild.leftChild;
                        node.centerLeftChild = node.parent.centerChild.rightChild;
                        node.centerRightChild = node.leftChild;
                        node.parent.centerChild = null;
                        node.parent.value2 = 0;
                        node.parent.values--;
                    }
                }
            }
            return node;
        }
        private TwoFourTreeItem deleteCases(int value){
            TwoFourTreeItem node = this;
            if(node.isLeaf){
                if (value == node.value1){
                    node.value1 = node.value2;
                    node.value2 = node.value3;
                    node.value3 = 0;
                    node.values--;
                }
                if (value == node.value2){
                    node.value2 = node.value3;
                    node.value3 = 0;
                    node.values--;
                }
                if (value == node.value3){
                    node.value3 = 0;
                    node.values--;
                }
            }
            else if (!node.isLeaf && !node.isTwoNode()){
                TwoFourTreeItem temp = node;
                if (value == node.value1){
                    if (node.isFourNode())
                        temp = node.centerLeftChild;
                    else
                        temp = node.centerChild;
                    if (!temp.isLeaf){
                        while(true){
                            temp = temp.leftChild;
                            if (temp.isTwoNode())
                                temp = temp.omgTwoNode(value);
                            if (temp.isLeaf)
                                break;
                        }
                    }
                    node.value1 = temp.value1;
                    temp.value1 = temp.value2;
                    temp.value2 = temp.value3;
                    temp.value3 = 0;
                    temp.values--;

                }
                else if (value == node.value2){
                    if (node.isFourNode())
                        temp = node.centerRightChild;
                    else
                        temp = node.rightChild;
                    if (!temp.isLeaf){
                        while(true){
                            temp = temp.leftChild;
                            if (temp.isTwoNode())
                                temp = temp.omgTwoNode(value);
                            if (temp.isLeaf)
                                break;
                        }
                    }
                    node.value2 = temp.value1;
                    temp.value1 = temp.value2;
                    temp.value2 = temp.value3;
                    temp.value3 = 0;
                    temp.values--;


                }
                else if (value == node.value3){
                    temp = node.rightChild;
                    if (!temp.isLeaf){
                        while(true){
                            temp = temp.leftChild;
                            if (temp.isTwoNode())
                                temp = temp.omgTwoNode(value);
                            if (temp.isLeaf)
                                break;
                        }
                    }
                    int n = node.value3;
                    node.value3 = temp.value1;
                    temp.value1 = n;
                    temp.value2 = temp.value3;
                    temp.value3 = 0;
                    temp.values--;
                }
            }
            
            return node;
        }
        private TwoFourTreeItem delete(int value){
            TwoFourTreeItem node = this;
            if (value == value1 || value == value2 || value == value3 && !node.isTwoNode()){
            System.out.println("VALUE1: " + node.value1 + " VALUE: " + value);
                node = node.deleteCases(value);
                return node;
            }
            if (root.isTwoNode() && root.leftChild.isTwoNode() && root.rightChild.isTwoNode() && root.isRoot()) {
                root.value2 = root.value1;
                root.value1 = root.leftChild.value1;
                root.value3 = root.rightChild.value1;
                root.leftChild = root.leftChild.leftChild;
                root.centerLeftChild = root.leftChild.rightChild;
                root.centerRightChild = root.rightChild.leftChild;
                root.rightChild = root.rightChild.rightChild;
                root.leftChild.leftChild.parent = root;
                root.leftChild.rightChild.parent = root;
                root.rightChild.leftChild.parent = root;
                root.rightChild.rightChild.parent = root;
                root.values = 3;
                return root.delete(value);
            }
            if (node.isTwoNode() && !node.isRoot()) {
                node = node.omgTwoNode(value);
                return node.delete(value);
            }
            if (node.isThreeNode()) {
                if (value < value1)
                    return node.leftChild.delete(value);
                else if (value > value2)
                    return node.rightChild.delete(value);
                else
                    return node.centerChild.delete(value);
            }
            else{
                if (value < node.value1)
                    return node.leftChild.delete(value);
                else if (value > node.value3)
                    return node.rightChild.delete(value);
                else if (value > node.value2)
                    return node.centerRightChild.delete(value);
                else
                    return node.centerLeftChild.delete(value);    
            }
        }
    }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }
        else if (hasValue(value))
            return false;
            
        root.insertValue(value);
        return true;
    }
        
    public boolean hasValue(int value) {
        if(root == null)
            return false;
        
        return root.searchValue(value);
    } 
    

    public boolean deleteValue(int value) {
        if(root == null)
            return false;


        root.delete(value);
        return true;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {
        
    }
}