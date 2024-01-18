public class MaxWeight {
    public static int maxWeightChain(Graph G) {
        return G.findMaxChain();
    }

    public static int maxWeightTree(Graph G) {
        return G.findMaxTree();
    }
}
