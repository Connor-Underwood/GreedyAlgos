import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Graph {
    Vertex[] adjacencyList;

    Edge[] minHeap;

    Edge[] edgeList;

    int heapSize;
    int numVertices;

    int numEdges;

    int[][] edgeWeights;

    int[][] sortedEdgeWeights;


    boolean[] marked;

    public Graph(int numVertices, boolean flag) {
        this.numVertices = numVertices;
        this.marked = new boolean[numVertices];
        this.adjacencyList = new Vertex[numVertices];

        if (flag) {
            this.edgeList = new Edge[numVertices];
            for (int i = 0; i < numVertices; i++) {
                this.edgeList[i] = new Edge(0,0,0,0);
            }
        }

        if (flag) {
            this.edgeWeights = new int[numVertices][numVertices];
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    this.edgeWeights[i][j] = -1;
                }
            }
        }

        for (int i = 0; i < numVertices; i++) {
            adjacencyList[i] = null;
        }


    }

    public Graph(int numVertices, int numEdges) {
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        this.adjacencyList = new Vertex[numVertices];
        this.minHeap = new Edge[numEdges];
        this.heapSize = 0;



        this.edgeWeights = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                this.edgeWeights[i][j] = -1;
            }
        }
        for (int i = 0; i < numVertices; i++) {
            adjacencyList[i] = null;
        }

        this.edgeList = new Edge[numEdges];
        for (int i = 0; i < numEdges; i++) {
            this.edgeList[i] = new Edge(0,0,0,0);
        }

    }

    public static class LinkedList {
        Vertex head;
        int size;


        public LinkedList() {
            this.head = null;
            this.size = 0;
        }

        public void addVertex(Vertex newVertex) {
            if (this.head == null) {
                this.head = newVertex;
                this.size++;
                return;
            }
            Vertex curr = this.head;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = newVertex;
            this.size++;
        }


    }

    public static class Edge {
        int srcVertex;
        int destVertex;
        int weight;
        int color;

        EdgeLinkedList adjacentEdges;

        Edge next;

        public Edge(int src, int dest, int weight, int color) {
            this.srcVertex = src;
            this.destVertex = dest;
            this.weight = weight;
            this.color = color;
            this.adjacentEdges = new EdgeLinkedList();
        }

    }

    public static class EdgeLinkedList {
        Edge head;
        int size;


        public EdgeLinkedList() {
            this.head = null;
            this.size = 0;
        }

        public void addEdge(Edge newEdge) {
            if (this.head == null) {
                this.head = newEdge;
                this.size++;
                return;
            }
            Edge curr = this.head;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = newEdge;
            this.size++;
        }

    }

    public static class Vertex {
        int weight;

        int index;

        Vertex next;

        LinkedList adjacentVertices;


        public Vertex(int index, int weight) {
            this.index = index;
            this.weight = weight;
            this.adjacentVertices = new LinkedList();
        }
    }

    public static Graph readVertexWeights(String filename) throws IOException {
        Scanner input = new Scanner(new File(filename));
        int numVertices = Integer.parseInt(input.nextLine());
        Graph g = new Graph(numVertices, false); // Change this constructor, get rid of adjacency matrix

        String[] stringWeights = input.nextLine().split(" ");
        int length = stringWeights.length;
        for (int i = 0; i < length; i++) {
            g.adjacencyList[i] = new Vertex(i, Integer.parseInt(stringWeights[i]));
        }
        String line;
        int lineNbr = 0;
        while (lineNbr < numVertices) {
            line = input.nextLine();
            String[] split = line.split(" ");
            if (split.length > 0) {
                for (int i = 0; i < split.length; i++) {
                    if (split[i].length() > 0) {
                        int index = g.adjacencyList[Integer.parseInt(split[i])].index;
                        int weight = g.adjacencyList[Integer.parseInt(split[i])].weight;
                        Vertex v = new Vertex(index, weight);
                        g.adjacencyList[lineNbr].adjacentVertices.addVertex(v);
                    }
                }
            }
            lineNbr++;
        }
        input.close();
        return g;
    }

    public static Graph readEdgeWeights(String filename) throws IOException {
        Scanner input = new Scanner(new File(filename));

        String first = input.nextLine();
        String[] vertAndEdges = first.split(" ");
        int vert = Integer.parseInt(vertAndEdges[0]);
        int edges = Integer.parseInt(vertAndEdges[1]);

        int lineNbr = 0;
        Graph g = new Graph(vert, true);

        g.sortedEdgeWeights = new int[edges][3];

        while (lineNbr < edges) {
            String line = input.nextLine();
            String[] split = line.split(" ");
            int sourceVertex = Integer.parseInt(split[0]);
            int destVertex = Integer.parseInt(split[1]);
            g.edgeWeights[sourceVertex][destVertex] = Integer.parseInt(split[2]);
            g.sortedEdgeWeights[lineNbr] =  new int[]{sourceVertex, destVertex, Integer.parseInt(split[2])};
            lineNbr++;
        }
        g.numEdges = edges;
        g.sortedEdgeWeights = g.heapSort(g.sortedEdgeWeights);
        input.close();
        return g;
    }

    public static Graph readEdgeColors(String filename) throws IOException {
        Scanner input = new Scanner(new File(filename));

        String first = input.nextLine();
        String[] vertAndEdges = first.split(" ");
        int vert = Integer.parseInt(vertAndEdges[0]);
        int edges = Integer.parseInt(vertAndEdges[1]);


        int lineNbr = 0;
        Graph g = new Graph(vert, edges);
        while (lineNbr < edges) {
            String line = input.nextLine();
            String[] split = line.split(" ");
            int sourceVertex = Integer.parseInt(split[0]);
            int destVertex = Integer.parseInt(split[1]);
            g.edgeWeights[sourceVertex][destVertex] = Integer.parseInt(split[2]);
            char clr = split[3].charAt(0);
            int color = clr == 'R' ? 0 : (clr == 'G' ? 1 : 2);
            Edge e = new Edge(sourceVertex, destVertex, Integer.parseInt(split[2]), color);
            g.edgeList[sourceVertex].adjacentEdges.addEdge(e);
            lineNbr++;
        }
        input.close();

        return g;
    }

    public void addToMinHeap(Edge edge) {

        minHeap[heapSize] = edge;
        int current = heapSize;
        heapSize++;

        while (current > 0 && minHeap[current].weight < minHeap[parent(current)].weight) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public Edge extractMin() {
        Edge minItem = minHeap[0];
        minHeap[0] = minHeap[heapSize - 1];
        heapSize--;
        minHeapify(0); // Reorder the heap
        return minItem;
    }

    private boolean isLeaf(int pos) {
        return pos >= heapSize / 2 && pos < heapSize;
    }

    private void minHeapify(int pos) {
        if (!isLeaf(pos)) {
            int left = leftChild(pos);
            int right = rightChild(pos);

            boolean hasLeftChild = left < heapSize && minHeap[left] != null;
            boolean hasRightChild = right < heapSize && minHeap[right] != null;

            if (minHeap[left] == null && minHeap[right] == null) {
                return;
            }

            if ((hasLeftChild && minHeap[pos].weight > minHeap[left].weight) ||
                    (hasRightChild && minHeap[pos].weight > minHeap[right].weight)) {

                if (!hasRightChild || (hasLeftChild && minHeap[left].weight < minHeap[right].weight)) {
                    swap(pos, left);
                    minHeapify(left);
                } else {
                    swap(pos, right);
                    minHeapify(right);
                }
            }
        }
    }

    public boolean isHeapEmpty() {
        return heapSize == 0;
    }

    private int leftChild(int pos) {
        return 2 * pos + 1;
    }

    private int rightChild(int pos) {
        return 2 * pos + 2;
    }


    private int parent(int pos) {
        return (pos - 1) / 2;
    }

    private void swap(int fpos, int spos) {
        Edge tmp;
        tmp = minHeap[fpos];
        minHeap[fpos] = minHeap[spos];
        minHeap[spos] = tmp;
    }

    public Vertex findLeaf() {
        for (Vertex v : adjacencyList) {
            if (v.adjacentVertices.size == 1) {
                return v;
            }
        }
        return null;
    }
    public int[] dfsChain(Vertex v, int[] childInOuts) {

        int inWeight = v.weight + Math.max(childInOuts[1],childInOuts[2]);

        int maxWeight = Math.max(Math.max(childInOuts[0], childInOuts[1]), childInOuts[2]);

        maxWeight = Math.max(inWeight, maxWeight);

        int[] currentInOuts = {inWeight, childInOuts[0], childInOuts[1], maxWeight + childInOuts[3]};


        for (int i = 0; i < numVertices; i++) {
            Vertex curr = adjacencyList[v.index].adjacentVertices.head;
            while (curr != null) {
                if (curr.index == i) {
                    curr.index = -1;
                    Vertex temp = adjacencyList[i].adjacentVertices.head;
                    while (temp != null) {
                        if (temp.index == v.index) {
                            temp.index = -1;
                        }
                        temp = temp.next;
                    }
                    int[] pathWeight = dfsChain(adjacencyList[i], currentInOuts);
                    maxWeight = Math.max(pathWeight[3], maxWeight);
                }
                curr = curr.next;
            }

        }

        currentInOuts[3] = maxWeight;
        return currentInOuts;

    }

    public int findMaxChain() {
        Vertex leaf = findLeaf();
        return dfsChain(leaf, new int[]{0,0,0,0})[3];
    }

    public int[] dfsTree(Vertex current, boolean[] marked) {

        marked[current.index] = true;
        int[] currentValues = new int[] {0, current.weight, 0, 0};

        for (int i = 0; i < numVertices; i++) {
            // Find Edge
            Vertex curr = adjacencyList[current.index].adjacentVertices.head;
            while (curr != null) {
                if ((curr.index == i) && !marked[i]) {
                    int[] pathVals = dfsTree(adjacencyList[i], marked);

                    int currentL2 = Math.max(currentValues[0], pathVals[0]);
                    currentL2 = Math.max(currentL2, currentValues[1] + Math.max(pathVals[2], pathVals[3]));
                    currentL2 = Math.max(currentL2, currentValues[2] + Math.max(pathVals[1], pathVals[2]));
                    currentL2 = Math.max(currentL2, currentValues[3] + pathVals[1]);
                    currentValues[0] = currentL2;

                    currentValues[1] = Math.max(currentValues[1], current.weight + Math.max(pathVals[2], pathVals[3]));
                    currentValues[2] = Math.max(currentValues[2], pathVals[1]);
                    currentValues[3] = Math.max(currentValues[3], pathVals[2]);
                }
                curr = curr.next;
            }
        }

        return currentValues;

    }

    public int findMaxTree() {
        Vertex v = findLeaf();
        boolean[] marked = new boolean[numVertices];
        return dfsTree(v, marked)[0];
    }






    public int maxHeight() {
        int[] parent = new int[numVertices];

        for (int i =0; i < numVertices; i++) {
            parent[i] = i;
        }

        int minHeight = Integer.MAX_VALUE;

        for (int i = 0; i < sortedEdgeWeights.length; i++) {
            int set1 = find(parent,sortedEdgeWeights[i][0]);
            int set2 = find(parent, sortedEdgeWeights[i][1]);
            if (set1 != set2) {
                union(parent, set1, set2);
                minHeight = Math.min(minHeight, sortedEdgeWeights[i][2]);
            }
        }


        return minHeight;
    }

    public void heapify(int[][] arr, int N, int a) {
        int largest = a;
        int left = 2 * a + 1;
        int right = 2 * a + 2;

        if (left < N && arr[largest][2] < arr[left][2]) {
            largest = left;
        }
        if (right < N && arr[largest][2] < arr[right][2]) {
            largest = right;
        }

        if (largest != a) {
            int[] swap = arr[largest];
            arr[largest] = arr[a];
            arr[a] = swap;
            heapify(arr, N, largest);
        }
    }

    public int[][] heapSort(int [][] arr) {
        int length = arr.length;
        for (int i = length / 2 - 1; i >= 0; i--) {
            heapify(arr, length, i);
        }


        for (int i = length - 1; i >= 0; i--) {
            int[] temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }



        for (int i = 0; i < length / 2; i++) {
            int[] temp = arr[i];
            arr[i] = arr[length - 1 - i];
            arr[length - 1 - i] = temp;
        }
        return arr;
    }




    public int find(int[] parent, int i) {
        if (parent[i] == i) {
            return i;
        } else {
            return find(parent, parent[i]);
        }
    }

    public void union(int[] parent, int i, int j) {
        int iRoot = find(parent, i);
        int jRoot = find(parent, j);
        parent[iRoot] = jRoot;
    }
}