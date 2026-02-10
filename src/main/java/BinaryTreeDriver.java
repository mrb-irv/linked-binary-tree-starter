public class BinaryTreeDriver {
    public static void main(String[] args) {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>();

        Position<String> root = tree.addRoot("A");
        Position<String> b = tree.addLeft(root, "B");
        Position<String> c = tree.addRight(root, "C");
        Position<String> d = tree.addLeft(b, "D");
        Position<String> e = tree.addRight(b, "E");
        Position<String> f = tree.addLeft(c, "F");
        Position<String> g = tree.addRight(c, "G");

        tree.print(); // expected: D B E A F C G
    }
}
