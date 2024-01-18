public class ColorWalk {
    //
    public static class WalkPair {
        char startColor;
        int walkWeight;

        public WalkPair(char startColor, int walkWeight) {
            this.startColor = startColor;
            this.walkWeight = walkWeight;
        }
    }

    public static WalkPair[] colorWalk(Graph G, int start) {

        WalkPair[] redPath = new WalkPair[G.numVertices];
        WalkPair[] greenPath = new WalkPair[G.numVertices];
        WalkPair[] bluePath = new WalkPair[G.numVertices];

        modifiedDijkstra(G, start, 'R', redPath);
        modifiedDijkstra(G, start, 'G', greenPath);
        modifiedDijkstra(G, start, 'B', bluePath);




        WalkPair[] res = new WalkPair[G.numVertices];

        int maxVal = Integer.MAX_VALUE;

        for (int i = 0; i < G.numVertices; i++) {
            WalkPair bestPath = redPath[i].walkWeight <= greenPath[i].walkWeight ?
                    (redPath[i].walkWeight <= bluePath[i].walkWeight ? redPath[i]
                            : bluePath[i]) : (greenPath[i].walkWeight <=
                    bluePath[i].walkWeight ? greenPath[i] : bluePath[i]);
            res[i] = bestPath;
        }
        for (WalkPair w : res) {
            if (w.walkWeight == maxVal) {
                w.walkWeight = -1;
                w.startColor = '-';
            }
        }

        return res;
    }

    private static void modifiedDijkstra(Graph G, int srcVert, char color, WalkPair[] colorPath) {


        int inf = Integer.MAX_VALUE;

        for (int i = 0; i < G.numVertices; i++) {
            colorPath[i] = new WalkPair(color, inf);
        }

        colorPath[srcVert].walkWeight = 0;
        colorPath[srcVert].startColor = '-';



        int start = color == 'R' ? 0 : (color == 'G' ? 1 : 2);
        boolean [][] visited = new boolean[G.numVertices][3];
        visited[srcVert][start] = true;
        Graph.Edge curr = G.edgeList[srcVert].adjacentEdges.head;
        int counter = 0;
        while (curr != null) {
            if (curr.color == start) {
                counter++;
                G.addToMinHeap(new Graph.Edge(curr.srcVertex, curr.destVertex, curr.weight, curr.color));
            }
            curr = curr.next;
        }
        if (counter == 0) {
            return;
        }
        while (!G.isHeapEmpty()) {
            Graph.Edge minCostEdge = G.extractMin();
            int destVertex = minCostEdge.destVertex;
            int nextColorInPattern = (minCostEdge.color + 1) % 3;
            int weight = minCostEdge.weight;



            if (!visited[destVertex][nextColorInPattern]) {

                visited[destVertex][nextColorInPattern] = true;

                int curWeight = colorPath[destVertex].walkWeight;

                if (weight < curWeight) {
                    colorPath[destVertex].walkWeight = weight;
                }
                Graph.Edge temp = G.edgeList[destVertex].adjacentEdges.head;
                while (temp != null) {
                    if (temp.color == nextColorInPattern) {
                        G.addToMinHeap(new Graph.Edge(temp.srcVertex, temp.destVertex, temp.weight + weight, nextColorInPattern));
                    }
                    temp = temp.next;
                }
            }
        }
    }
}
