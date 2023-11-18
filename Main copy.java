import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {//avl tree class

    public class Node {//avl node class
        double GMS;
        int height;
        String name;
        Node left;
        Node right;
        int in;// every node will store its indep element info constructed by looking down the tree from it, this is in
        int out;// every node will store its indep element info constructed by looking down the tree from it, this is out

        public Node(double GMS, Node l, Node r,String name){
            this.GMS = GMS;
            this.height=0;
            this.left=l;
            this.right=r;
            this.name=name;
            this.out=0;
            this.in=0;

        }

    }
    public static Node root;//declaring first node,the root
    public FileWriter file;
    private Node findMinNode(Node x){//used for finding the most left of right
        if(x == null)return x;
        Node current = x;
        while(current.left != null) current = current.left;//goes left until the left pointer is null
        return current;
    }
    public void remove(Node x ) throws IOException {
        root = remove(x, root, false);//after removing node x, root must be updated
    }
    public Node remove(Node x,Node n, boolean check) throws IOException {//check by default set to false
        if(n==null)
            return n;
        if(x.GMS< n.GMS)//nodes value is smaller than current node
            n.left = remove(x, n.left,check);//go to the left subtree
        else if(x.GMS>n.GMS)//nodes value is bigger than current node
            n.right = remove(x,n.right,check);//go to the right subtree
        else if( n.left !=null && n.right != null){//to be removed node has left and right child-replace with right left case
            Node minNode = findMinNode(n.right);//find right left
            if(!check){
                file.write(n.name + " left the family, replaced by "+minNode.name +"\n");
            }
            n.GMS = minNode.GMS;//update to be removed node's GMS with right left's GMS
            n.name= minNode.name;//update to be removed node's name with right left's name
            n.right = remove(minNode,n.right,true);//check set to true
        }
        else//node to be removed do not have two children
            if(n.left != null){//left children replaces
                if(!check){
                    file.write(n.name + " left the family, replaced by "+n.left.name +"\n");
                }
                n = n.left;
            }
            else if(n.right != null){//right children replaces
                if(!check){
                    file.write(n.name +" left the family, replaced by "+n.right.name +"\n");
                }
                n = n.right;
            }else{
                if(!check){//solely deleted nothing replaces it
                    file.write(n.name + " left the family, replaced by nobody"+"\n");
                }
                n = null;
            }
            updateHeight(n);//updating height of nodes in the given subtree
        return balance(n);//check is the subtree rooted n is balanced
    }
    public Node find(double GMS){
        return findNode(GMS, root);
    }//finding the node's address with the given GMS value-starting from the node
    public Node findNode(double GMS, Node current){
        if(GMS <current.GMS){//go left
            return findNode(GMS, current.left);
        }
        else if (GMS > current.GMS){//go right
            return findNode(GMS, current.right);
        }
        else{//found
            return current;
        }
    }
    public Node balance(Node n){//checking for balance
        if(n==null){
            return n;
        }
        if( Height( n.left ) - Height( n.right ) > 1 )//left subtree bigger than right--rotation applied to left
            if( Height( n.left.left ) >= Height( n.left.right ) )//checking which rotation to perform
                n = rightRotation( n );
            else
                n = leftRight( n );
        else
        if( Height( n.right ) - Height( n.left ) > 1 )//right subtree bigger than left--rotation applied to right
            if( Height( n.right.right ) >= Height( n.right.left ) )//checking which rotation to perform
                n = leftRotation( n );
            else
                n = rightLeft( n );

        n.height = Math.max( Height( n.left ), Height( n.right ) ) + 1;//calculating the height of the current node after
        return n;
    }


    public void insert( double x , String name) throws IOException {//called insertion method
        root = insert( x, root ,name);//equalizing the update the root
    }

    public Node insert(double x, Node n, String name) throws IOException {//called inside first insertion
        if (n == null){//when current n is null -- we are at a leaf's null child
            return new Node (x, null,null,name);//creating the new node only when place is found
        }

        else if( x<n.GMS ) {//proceed left
            file.write(n.name+" welcomed "+name+"\n");//every step going down a commander is reached and should be greeting
            n.left = insert(x, n.left, name);
        }
        else if( x>n.GMS ) {//proceed right
            file.write(n.name +" welcomed "+name+"\n");//every step going down a commander is reached and should be greeting
            n.right = insert(x, n.right, name);
        }
        else
            ;
        updateHeight(n);//update heights in every removal as in the function remove
        return balance( n );// run balance function

    }

    public Main(FileWriter file) throws IOException {//tree constructor
        root= null;
        this.file=file;

    }
    int Height(Node n){//getheight method

        if (n==null)
            return -1;
        else
            return n.height;

    }

    void updateHeight(Node n){//updating the height by simply getting heights of the nodes left and write
        // and setting the biggest among them
        if(n==null){
            return ;
        }
        int l = Height(n.left);
        int r = Height(n.right);

        n.height=Math.max(l, r) + 1;
    }

    public Node rightRotation(Node n){
        Node m = n.left;// m--reference to the left child of the current
        n.left = m.right;//make n's left pointer to show m's right, not m
        m.right = n;//make m new root of the subtree by making n its right child
        n.height=Math.max(Height(n.right), Height(n.left)) +1;//do calculations similar to updateHeight function
        m.height = Math.max(Height(m.left), n.height)+1;
        return m;
    }
    public Node leftRotation(Node n){
        Node m = n.right;//m--reference to the right child of the current
        n.right = m.left;//make n's right pointer to show m's left, not m
        m.left = n;//make m new root of the subtree by making m its left child
        n.height=Math.max(Height(n.left), Height(n.right)) +1;//do calc similar to updateHeight function
        m.height = Math.max(Height(m.right), n.height) +1;
        return m;
    }
    public Node leftRight(Node n){//do two singular rotation to achieve one double rotation
        n.left = leftRotation(n.left);
        return rightRotation(n);
    }

    public Node rightLeft(Node n){//do two singular rotation to achieve one double rotation
        n.right = rightRotation(n.right);
        return leftRotation(n);
    }

    public int findRank(Node root, double targetGMS) {//calculate rank of the given member by increasing rnak at each iteration
        if (root == null) {//if root not exist rank is -1 directly
            return -1;
        }
        int rank = 0;
        Node current = root;//start with the root as current

        while (current != null) {
            if (targetGMS == current.GMS) {
                return rank; // node found, return its depth.
            } else if (targetGMS < current.GMS) {
                current = current.left;//update current
            } else {
                current = current.right;//update current
            }
            rank++;//increase rank by 1
        }

        return -1; // Node not found; return -1 to indicate that.
    }

    public String rankAnalysis(int rank, String initial){
        return rankAnalysis(rank,root,initial);
    }

    public String rankAnalysis(int rank,Node n,String initial){//starting with an initial integer rank and decreasing it
        // one by one every time a parent is encountered while going down the tree
        if(n==null){
            return initial;
        }
        if(rank==0){//targeted level(other words rank) of tree is reached
            initial = initial + " " +n.name + " " + String.format("%.3f",n.GMS);
            return initial;
        }else{
            initial = rankAnalysis(rank-1,n.left,initial);//going left
            initial = rankAnalysis(rank-1,n.right,initial);//going right
        }
        return initial;
    }
    
    public String targetAnalysis(Node current, double gms1,double gms2){// idea is to print the currently worked on root when
        //two members go in different directions
        if(gms1<current.GMS && gms2<current.GMS){//both on the left --still haven't found breaking point
            return targetAnalysis(current.left,gms1,gms2);
        }
        else if(gms1>current.GMS && gms2>current.GMS){//both on the right-still haven't found breaking point
            return targetAnalysis(current.right,gms1,gms2);
        }
        else{
            return "Target Analysis Result: "+current.name+ " "+String.format("%.3f",current.GMS);
        }

    }

    public int intelDivide(Node root) {//first called method to divide
        int[] result = maxIndep(root);//using arrays of length two
        return Math.max(result[0], result[1]);//result containing largest indep values of included and excluded cases, take the largest of them
    }

    private int[] maxIndep(Node node) {//aim is to accumulate indep member info from bottom to top, the boss node
        if (node == null) {//when reached the leaf start with 0,0 and pass it
            return new int[]{0, 0};
        }

        int[] left = maxIndep(node.left);//max indep for the left subtree
        int[] right = maxIndep(node.right);//max indep for the right subtree

        int in = 1 + left[1] + right[1];//calc including current node
        int out = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);//calc excluding current node

        return new int[]{in, out};
    }


    public static void main(String[] args) throws IOException {

        FileWriter file = new FileWriter(args[1],true);
        Main tree = new Main(file);//constructing the tree

        try{
            File input = new File(args[0]);
            Scanner scanner = new Scanner(input);
            String boss = scanner.nextLine();//treating first line differently, taking it as the root and inserting first
            tree.insert(Double.parseDouble(boss.split(" ")[1]),boss.split(" ")[0]);

            while(scanner.hasNextLine()) {//while loop checking if file has a next line
                String line = scanner.nextLine();

                if(line.split(" ")[0].equals("MEMBER_IN")){//insertion case out of 5 input cases
                    tree.insert(Double.parseDouble(line.split(" ")[2]), line.split(" ")[1]);

                }
                if(line.split(" ")[0].equals("MEMBER_OUT")){//remove case out of 5 input cases
                    tree.remove(tree.find(Double.parseDouble(line.split(" ")[2])));
                }
                if(line.split(" ")[0].equals("INTEL_RANK")){//rank analyse case out of 5 input cases
                    String initial = "Rank Analysis Result:";
                    tree.file.write(tree.rankAnalysis(tree.findRank(root,Double.parseDouble(line.split(" ")[2])),initial)+"\n");

                }
                if(line.split(" ")[0].equals("INTEL_TARGET")){//ıntel target case
                    tree.file.write(tree.targetAnalysis(root,Double.parseDouble(line.split(" ")[2]),Double.parseDouble(line.split(" ")[4]))+"\n");

                }
                if(line.split(" ")[0].equals("INTEL_DIVIDE")){//ıntel divide case
                    tree.file.write("Division Analysis Result: "+tree.intelDivide(root) + "\n");

                }

            }
            tree.file.close();
            scanner.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

}