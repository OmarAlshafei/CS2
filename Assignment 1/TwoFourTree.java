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
            TwoFourTreeItem node = this;

            if (value == node.value1 || value == node.value2 || value == node.value3)
                return true;
            else if (node.isTwoNode()){
                if (value < node.value1 && node.leftChild != null) 
                    return node.leftChild.searchValue(value);
                        
                else if (node.rightChild != null) 
                    return node.rightChild.searchValue(value);

            }
            else if (node.isThreeNode()) {
                if (value < node.value1 && node.leftChild != null) 
                    return node.leftChild.searchValue(value);
                        
                else if (value > node.value2 && node.rightChild != null) 
                    return node.rightChild.searchValue(value);
                    
                else if (node.centerChild != null)
                    return node.centerChild.searchValue(value);
            } 
            else if (node.isFourNode()) {
                if (value < node.value1 && node.leftChild != null) 
                    return node.leftChild.searchValue(value);
                            
                else if (value > node.value3 && node.rightChild != null)
                    return node.rightChild.searchValue(value);
                            
                else if (value > node.value2 && node.centerRightChild != null)
                    return node.centerRightChild.searchValue(value);
                            
                else if (node.centerLeftChild != null)
                    return node.centerLeftChild.searchValue(value);
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

            if (node.parent.isFourNode() && node.isTwoNode()){
                //rotate anticlockwise
                if(node == node.parent.leftChild && !node.parent.centerLeftChild.isTwoNode()){
                    int pVal = node.parent.value1;
                    node.value2 = pVal;
                    node.parent.value1 = node.parent.centerLeftChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerLeftChild.leftChild;
                    node.values = 2;

                    // fix R pointers
                    if(node.parent.centerLeftChild.isThreeNode()){
                        node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
                        node.parent.centerLeftChild.value2 = 0;
                        node.parent.centerLeftChild.values = 1;
                        node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerChild;
                        node.parent.centerLeftChild.centerChild = null;
                    }
                    if(node.parent.centerLeftChild.isFourNode()){
                        node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
                        node.parent.centerLeftChild.value2 = node.parent.centerLeftChild.value3;
                        node.parent.centerLeftChild.value3 = 0;
                        node.parent.centerLeftChild.values = 2;                
                        node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerLeftChild;
                        node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerRightChild;
                        node.parent.centerLeftChild.centerLeftChild = null;
                        node.parent.centerLeftChild.centerRightChild = null;
                    }
                }
                //rotate anticlockwise
                else if(node == node.parent.centerLeftChild && !node.parent.centerRightChild.isTwoNode()){
                    int pVal = node.parent.value2;
                    node.value2 = pVal;
                    node.parent.value2 = node.parent.centerRightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerRightChild.leftChild;
                    node.values = 2;

                    // fix R pointers
                    if(node.parent.centerRightChild.isThreeNode()){
                        node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
                        node.parent.centerRightChild.value2 = 0;
                        node.parent.centerRightChild.values = 1;   
                        node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerChild;
                        node.parent.centerRightChild.centerChild = null;
                    }
                    if(node.parent.centerRightChild.isFourNode()){
                        node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
                        node.parent.centerRightChild.value2 = node.parent.centerRightChild.value3;
                        node.parent.centerRightChild.value3 = 0;
                        node.parent.centerRightChild.values = 2;   
                        node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerLeftChild;
                        node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerRightChild;
                        node.parent.centerRightChild.centerLeftChild = null;
                        node.parent.centerRightChild.centerRightChild = null;
                    }
                }
                //rotate anticlockwise
                else if(node == node.parent.centerRightChild && !node.parent.rightChild.isTwoNode()){
                    int pVal = node.parent.value3;
                    node.value2 = pVal;
                    node.parent.value3 = node.parent.rightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.rightChild.leftChild;
                    node.values = 2;

                    // fix R values
                    node.parent.rightChild.value1 = node.parent.rightChild.value2;
                    node.parent.rightChild.value2 = node.parent.rightChild.value3;
                    node.parent.rightChild.value3 = 0;

                    // fix R pointers
                    if(node.parent.rightChild.isThreeNode()){
                        node.parent.rightChild.value1 = node.parent.rightChild.value2;
                        node.parent.rightChild.value2 = 0;
                        node.parent.rightChild.values = 1;
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                        node.parent.rightChild.centerChild = null;
                    }
                    if(node.parent.rightChild.isFourNode()){
                        node.parent.rightChild.value1 = node.parent.rightChild.value2;
                        node.parent.rightChild.value2 = node.parent.rightChild.value3;
                        node.parent.rightChild.value3 = 0;
                        node.parent.rightChild.values = 2;
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                        node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                        node.parent.rightChild.centerLeftChild = null;
                        node.parent.rightChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.centerLeftChild && !node.parent.leftChild.isTwoNode()){
                    int pVal = node.parent.value1;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.leftChild.rightChild;
                    node.values = 2;

                    // fix L pointers
                    if(node.parent.leftChild.isThreeNode()){
                        node.parent.value1 = node.parent.leftChild.value2;
                        node.parent.leftChild.value2 = 0;
                        node.parent.leftChild.values = 1;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                        node.parent.leftChild.centerChild = null;
                    }
                    if(node.parent.leftChild.isFourNode()){
                        node.parent.value1 = node.parent.leftChild.value3;
                        node.parent.leftChild.value3 = 0;
                        node.parent.leftChild.values = 1;
                        node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                        node.parent.leftChild.centerLeftChild = null;
                        node.parent.leftChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.centerRightChild && !node.parent.centerLeftChild.isTwoNode()){
                    int pVal = node.parent.value2;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerLeftChild.rightChild;
                    node.values = 2;

                    // fix L pointers
                    if(node.parent.centerLeftChild.isThreeNode()){
                        node.parent.value2 = node.parent.centerLeftChild.value2;
                        node.parent.centerLeftChild.value2 = 0;
                        node.parent.centerLeftChild.values = 1;
                        node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerChild;
                        node.parent.centerLeftChild.centerChild = null;
                    }
                    if(node.parent.centerLeftChild.isFourNode()){
                        node.parent.value2 = node.parent.leftChild.value3;
                        node.parent.centerLeftChild.value3 = 0;
                        node.parent.centerLeftChild.values = 2;
                        node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerLeftChild;
                        node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerRightChild;
                        node.parent.centerLeftChild.centerLeftChild = null;
                        node.parent.centerLeftChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.rightChild && !node.parent.centerRightChild.isTwoNode()){
                    int pVal = node.parent.value3;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerRightChild.rightChild;
                    node.values = 2;

                    // fix L pointers
                    if(node.parent.centerRightChild.isThreeNode()){
                        node.parent.value3 = node.parent.centerRightChild.value2;
                        node.parent.centerRightChild.value2 = 0;
                        node.parent.centerRightChild.values = 1;
                        node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerChild;
                        node.parent.centerRightChild.centerChild = null;
                    }
                    if(node.parent.centerRightChild.isFourNode()){
                        node.parent.value3 = node.parent.centerRightChild.value3;
                        node.parent.centerRightChild.value3 = 0;
                        node.parent.centerRightChild.values = 2;
                        node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerLeftChild;
                        node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerRightChild;
                        node.parent.centerRightChild.centerLeftChild = null;
                        node.parent.centerRightChild.centerRightChild = null;
                    }
                }
                else{
                    if(node == node.parent.leftChild || node == node.parent.centerLeftChild){//-----------
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

                    else if(node == node.parent.centerRightChild || node == node.parent.rightChild){
                        node = node.parent.centerRightChild;
                        node.value2 = node.parent.value3;
                        node.value3 = node.parent.rightChild.value1;
                        node.values = 3;
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.rightChild.leftChild;
                        node.rightChild = node.parent.rightChild.rightChild;
                        node.parent.centerChild = node.parent.centerLeftChild;
                        node.parent.rightChild = node.parent.centerRightChild;
                        node.parent.centerLeftChild = null;
                        node.parent.centerRightChild = null;
                        node.parent.value2 = node.parent.value3;
                        node.parent.values--;
                    }

                }
            }
            else if (node.parent.isThreeNode() && node.isTwoNode()){
                //rotate anticlockwise
                if(node == node.parent.leftChild && !node.parent.centerChild.isTwoNode()){
                    int pVal = node.parent.value1;
                    node.value2 = pVal;
                    node.parent.value1 = node.parent.centerChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerChild.leftChild;
                    node.values = 2;


                    // fix R pointers
                    if(node.parent.centerChild.isThreeNode()){
                        node.parent.centerChild.value1 = node.parent.centerChild.value2;
                        node.parent.centerChild.value2 = 0;
                        node.parent.centerChild.values = 1; 
                        node.parent.centerChild.leftChild = node.parent.centerChild.centerChild;
                        node.parent.centerChild.centerChild = null;
                    }
                    else if(node.parent.centerChild.isFourNode()){
                        node.parent.centerChild.value1 = node.parent.centerChild.value2;
                        node.parent.centerChild.value2 = node.parent.centerChild.value3;
                        node.parent.centerChild.value3 = 0;
                        node.parent.centerChild.values = 2;   
                        node.parent.centerChild.leftChild = node.parent.centerChild.centerLeftChild;
                        node.parent.centerChild.centerChild = node.parent.centerChild.centerRightChild;
                        node.parent.centerChild.centerLeftChild = null;
                        node.parent.centerChild.centerRightChild = null;
                    }
                }
                //rotate anticlockwise
                else if(node == node.parent.centerChild && !node.parent.rightChild.isTwoNode()){
                    int pVal = node.parent.value2;
                    node.value2 = pVal;
                    node.parent.value2 = node.parent.rightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.rightChild.leftChild;
                    node.values = 2;


                    // fix R pointers
                    if(node.parent.rightChild.isThreeNode()){
                        node.parent.rightChild.value1 = node.parent.rightChild.value2;
                        node.parent.rightChild.value2 = 0;
                        node.parent.rightChild.values = 1;   
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                        node.parent.rightChild.centerChild = null;
                    }
                    else if(node.parent.rightChild.isFourNode()){
                        node.parent.rightChild.value1 = node.parent.rightChild.value2;
                        node.parent.rightChild.value2 = node.parent.rightChild.value3;
                        node.parent.rightChild.value3 = 0;
                        node.parent.rightChild.values = 2;   
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                        node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                        node.parent.rightChild.centerLeftChild = null;
                        node.parent.rightChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.centerChild && !node.parent.leftChild.isTwoNode()){
                    int pVal = node.parent.value1;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.leftChild.rightChild;
                    node.values = 2;

                    // fix R pointers
                    if(node.parent.leftChild.isThreeNode()){
                        node.parent.value1 = node.parent.leftChild.value2;
                        node.parent.leftChild.value2 = 0;
                        node.parent.leftChild.values = 1;  
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                        node.parent.leftChild.centerChild = null;
                    }
                    if(node.parent.leftChild.isFourNode()){
                        node.parent.value1 = node.parent.leftChild.value3;
                        node.parent.leftChild.value3 = 0;
                        node.parent.leftChild.values = 2;  
                        node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                        node.parent.leftChild.centerLeftChild = null;
                        node.parent.leftChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.rightChild && !node.parent.centerChild.isTwoNode()){
                    int pVal = node.parent.value2;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerChild.rightChild;
                    node.values = 2;

                    // fix R pointers
                    if(node.parent.centerChild.isThreeNode()){
                        node.parent.value2 = node.parent.leftChild.value2;
                        node.parent.centerChild.value2 = 0;
                        node.parent.centerChild.values = 1;  
                        node.parent.centerChild.rightChild = node.parent.centerChild.centerChild;
                        node.parent.centerChild.centerChild = null;
                    }
                    if(node.parent.centerChild.isFourNode()){
                        node.parent.value2 = node.parent.leftChild.value3;
                        node.parent.centerChild.value3 = 0;
                        node.parent.centerChild.values = 2; 
                        node.parent.centerChild.centerChild = node.parent.centerChild.centerLeftChild;
                        node.parent.centerChild.rightChild = node.parent.centerChild.centerRightChild;
                        node.parent.centerChild.centerLeftChild = null;
                        node.parent.centerChild.centerRightChild = null;
                    }
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
                        node.value2 = node.parent.value2;
                        node.value3 = node.parent.rightChild.value1;
                        node.values = 3;
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.rightChild.leftChild;
                        node.rightChild = node.parent.rightChild.rightChild;
                        node.parent.rightChild = node;
                        node.parent.centerChild = null;
                        node.parent.values = 1;
                    }
                    else if(node == node.parent.rightChild){
                        node.value3 = node.value1;
                        node.value2 = node.parent.value2;
                        node.value1 = node.parent.centerChild.value1;
                        node.values = 3;
                        node.centerRightChild = node.leftChild;
                        node.centerLeftChild = node.parent.centerChild.rightChild;
                        node.leftChild = node.parent.centerChild.leftChild;
                        node.parent.centerChild = null;
                        node.parent.value2 = 0;
                        node.parent.values = 1;
                    }
                }
            }
            else if (node.parent.isTwoNode() && !node.parent.isRoot())
                node.parent.omgTwoNode(value);

            return node;
        }
        private void deleteCases(int value){            
            if(isLeaf){
                if(isTwoNode())
                    omgTwoNode(value);
                if (value == value1){
                    value1 = value2;
                    value2 = value3;
                    values--;
                }
                else if (value == value2){
                    value2 = value3;
                    value3 = 0;
                    values--;
                }
                else if (value == value3){
                    value3 = 0;
                    values--;
                }
            }            
            // Value 1
            else if (!isLeaf && !isTwoNode() && !parent.isRoot()){
                TwoFourTreeItem temp = this;
                if (value == value1){
                    if (isFourNode())
                        temp = centerLeftChild;
                    else
                        temp = centerChild;
                        if (temp.isTwoNode() && !temp.parent.isRoot())
                            temp = temp.omgTwoNode(value);
                    while(!temp.isLeaf){
                        if (temp.isTwoNode() && !temp.parent.isRoot())
                            temp = temp.omgTwoNode(value);
                        temp = temp.leftChild;
                    }
                    value1 = temp.value1;
                }
                
                // Value 2
                else if (value == value2){
                    if (isFourNode())
                        temp = centerRightChild;
                    else
                        temp = rightChild;
                        if (temp.isTwoNode() && !temp.parent.isRoot())
                            temp = temp.omgTwoNode(value);
                    while(!temp.isLeaf){
                        if (temp.isTwoNode() && !temp.parent.isRoot())
                            temp = temp.omgTwoNode(value);
                        temp = temp.leftChild;
                    }
                    value2 = temp.value1;
                }
                
                // Value 3
                else if (value == value3){
                    temp = rightChild;
                    if (temp.isTwoNode() && !temp.parent.isRoot())
                        temp = temp.omgTwoNode(value);
                    while(!temp.isLeaf){
                        if (temp.isTwoNode() && !temp.parent.isRoot())
                            temp = temp.omgTwoNode(value);
                        temp = temp.leftChild;
                    }
                    value3 = temp.value1;
                }
                if (temp.isTwoNode() && !temp.parent.isRoot()){
                    temp = temp.omgTwoNode(value);
                }
                temp.value1 = value;
                temp.deleteCases(temp.value1);
            }
        }
        
        private void delete(int value){
            if(isTwoNode()){
                if(isRoot())
                    fixRoot(value);
                else if(!parent.isRoot())
                    omgTwoNode(value);
            }
            
            if ((value == value1 || value == value2 || value == value3)){
                deleteCases(value);
                return;
            }
            else{
                if (isThreeNode()) {
                    if (value < value1 && leftChild != null)
                        leftChild.delete(value);
                    else if (value > value2 && rightChild != null)
                        rightChild.delete(value);
                    else if(value < value2 && centerChild != null)
                        centerChild.delete(value);
                }
                else{
                    if (value < value1 && leftChild != null)
                        leftChild.delete(value);
                    else if (value > value3 && rightChild != null)
                        rightChild.delete(value);
                    else if (value > value2 && centerRightChild != null)
                        centerRightChild.delete(value);
                    else if (value < value2 && centerLeftChild != null)
                        centerLeftChild.delete(value);    
                }
            }
        }
        private void fixRoot(int value){
            if (root.isTwoNode() && root.leftChild.isTwoNode() && root.rightChild.isTwoNode()){
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
                }
                else if(root.leftChild.isTwoNode() && !root.rightChild.isTwoNode() && value < root.value1){
                    TwoFourTreeItem node = root.leftChild;
                    node.value2 = node.parent.value1;
                    node.values++;
                    node.parent.value1 = node.parent.rightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.rightChild.leftChild;

                    node.parent.rightChild.value1 = node.parent.rightChild.value2;
                    node.parent.rightChild.value2 = node.parent.rightChild.value3;
                    node.parent.rightChild.value3 = 0;
                    
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
                else if(root.rightChild.isTwoNode() && !root.leftChild.isTwoNode() && value > root.value1){
                    TwoFourTreeItem node = root.rightChild;
                    node.value2 = node.value1;
                    node.value1 = node.parent.value1;
                    node.values++;
                    node.parent.leftChild.value1 = node.parent.leftChild.value2;
                    node.parent.leftChild.value2 = node.parent.leftChild.value3;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.leftChild.rightChild;
                    
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
            }
        }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }
        if(hasValue(value))
            return false;
        
        root.insertValue(value);
        return true;
        
    }
        
    public boolean hasValue(int value) {
        return root.searchValue(value);
    }
    

    public boolean deleteValue(int value) {
        if(root == null)
            return false;
            
        if (!hasValue(value))
            return false;
        
        root.fixRoot(value);

        root.delete(value);
        return true;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {
        
    }
}