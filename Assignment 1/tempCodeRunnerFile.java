public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the node is a non-leaf.
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
            if (!isLeaf) {
                leftChild.printInOrder(indent + 1);
            }
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if (isThreeNode()) {
                if (!isLeaf) {
                    centerChild.printInOrder(indent + 1);
                }
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if (isFourNode()) {
                if (!isLeaf) {
                    centerLeftChild.printInOrder(indent + 1);
                }
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if (!isLeaf) {
                    centerRightChild.printInOrder(indent + 1);
                }
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if (!isLeaf) {
                rightChild.printInOrder(indent + 1);
            }
        }
    }
    
    private TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }
        
        if (!hasValue(value)) {
            if (!root.isFourNode()) {
                root.orderVals(value);
            } else {
                if (root.isRoot()) {
                    root.splitRoot();
                    root.isLeaf = false;
                } else {
                    TwoFourTreeItem newNode = root;
                    while (!newNode.isLeaf) {
                        newNode.split();
                        newNode = searchValue(newNode, value);
                        newNode.orderVals(value);
                        root = newNode;
                    }
                }
            }
            return true;
        }
        
        return false;
    }
    
    public boolean hasValue(int value) {
        if (root == null) {
            return false;
        }
        
        return searchValue(root, value) != null;
    }    
    
    private TwoFourTreeItem searchValue(TwoFourTreeItem node, int value) {
        if (node == null) {
            return null;
        }
        
        if (node.isFourNode()) {
            if (value == node.value1 || value == node.value2 || value == node.value3) {
                return node;
            } else if (value < node.value1) {
                node = node.leftChild;
            } else if (value > node.value3) {
                node = node.rightChild;
            } else if (value < node.value3 && value > node.value2) {
                node = node.centerRightChild;
            } else if (value > node.value1 && value < node.value2) {
                node = node.centerLeftChild;
            }
            return searchValue(node, value);
        } else if (node.isThreeNode()) {
            if (value == node.value1 || value == node.value2) {
                return node;
            } else if (value < node.value1) {
                node = node.leftChild;
            } else if (value > node.value2) {
                node = node.rightChild;
            } else {
                node = node.centerChild;
            }
            return searchValue(node, value);
        } else if (node.isTwoNode()) {
            if (value == node.value1) {
                return node;
            } else if (value < node.value1) {
                node = node.leftChild;
            } else {
                node = node.rightChild;
            }
            return searchValue(node, value);
        }
        
        return null;
    }

    public boolean deleteValue(int value) {
        // Implement the deletion logic here
        return false;
    }

    public void printInOrder() {
        if (root != null) {
            root.printInOrder(0);
        }
    }

    public TwoFourTree() {
        
    }
}
