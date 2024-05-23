import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

class GraphDisplay extends JFrame {

    public GraphDisplay(String imagePath) {
        setTitle("Graph Display");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel(new ImageIcon(imagePath));
        JScrollPane scrollPane = new JScrollPane(label);

        add(scrollPane, BorderLayout.CENTER);
    }
}

class GraphToDot {
    public static void writeDotFile(DirectedGraph graph, String filename, List<String> shortestPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("digraph G {");
            for (String vertex : graph.getVertices()) {
                for (Map.Entry<String, Integer> entry : graph.getNeighbors(vertex).entrySet()) {
                    String source = vertex;
                    String destination = entry.getKey();
                    int weight = entry.getValue();
                    boolean isShortestEdge = shortestPath.contains(source) && shortestPath.contains(destination)
                            && shortestPath.indexOf(destination) == shortestPath.indexOf(source) + 1;

                    if (isShortestEdge) {
                        writer.println("    \"" + source + "\" -> \"" + destination + "\" [label=\"" + weight + "\", color=\"red\"];");
                    } else {
                        writer.println("    \"" + source + "\" -> \"" + destination + "\" [label=\"" + weight + "\"];");
                    }
                }
            }
            writer.println("}");
        } catch (IOException e) {
            System.err.println("Error writing DOT file: " + e.getMessage());
        }
    }
}

class GraphvizRunner {
    public static void generateGraphImage(String dotFilePath, String outputFilePath) {
        try {
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", dotFilePath, "-o", outputFilePath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error generating graph image: " + e.getMessage());
        }
    }
}

class DataDeal {
    public static StringBuilder datadeal() {
        // 指定要读取的文件路径
        String filePath = "D:\\Intellij IDEA\\Code\\exp\\test.txt";

        // 创建一个字符串来保存文件内容
        StringBuilder content = new StringBuilder();

        // 使用try-with-resources语句创建文件读取流和缓冲读取器
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            // 逐行读取文件内容并追加到字符串中
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("无法读取文件: " + e.getMessage());
        }

        // 输出字符串中的内容
        System.out.println("处理前："+content);

        // 将换行/回车符替换为空格
        content = new StringBuilder(content.toString().replaceAll("[\\n\\r]", " "));

        // 将标点符号替换为空格
        content = new StringBuilder(content.toString().replaceAll("[\\p{Punct}]", " "));

        // 将非字母字符忽略
        content = new StringBuilder(content.toString().replaceAll("[^a-zA-Z ]", ""));

        // 将连续的空格替换为一个空格
        content = new StringBuilder(content.toString().replaceAll("\\s+", " "));

        // 输出处理后的文本内容
        System.out.println("处理后：" + content);

        return content;
    }
}

class DirectedGraph {

    private final Map<String, Map<String, Integer>> adjacencyMap;

    public DirectedGraph() {
        this.adjacencyMap = new HashMap<>();
    }

    // 获取所有顶点
    public Set<String> getVertices() {
        return adjacencyMap.keySet();
    }

    // 获取某个顶点的邻居节点
    public Map<String, Integer> getNeighbors(String source) {
        return adjacencyMap.getOrDefault(source, new HashMap<>());
    }

    // 获取某条边的权重
    public int getWeight(String source, String destination) {
        if (adjacencyMap.containsKey(source)) {
            Map<String, Integer> neighbors = adjacencyMap.get(source);
            return neighbors.getOrDefault(destination, 0);
        }
        return 0;
    }
    // 添加有向边
    public void addEdge(String source, String destination) {
        Map<String, Integer> neighbors = adjacencyMap.getOrDefault(source, new HashMap<>());
        neighbors.put(destination, neighbors.getOrDefault(destination, 0) + 1);
        adjacencyMap.put(source, neighbors);
    }

    // 打印有向图
    public void showDirectedGrap() {
        for (String source : adjacencyMap.keySet()) {
            Map<String, Integer> neighbors = adjacencyMap.get(source);
            for (String destination : neighbors.keySet()) {
                int weight = neighbors.get(destination);
                System.out.println(source + " -> " + destination + ": " + weight);
            }
        }
    }
    // 查找桥接词
    public List<String> queryBridgeWords(String word1, String word2) {
        List<String> bridgeWords = new ArrayList<>();
        String errorWord = "1";

        // 如果 word1 或 word2 不在图中出现，则返回空列表
        if (!adjacencyMap.containsKey(word1) || !adjacencyMap.containsKey(word2)) {
            if (!adjacencyMap.containsKey(word1) && !adjacencyMap.containsKey(word2))
            {
                System.out.println("No " + word1 + " and "+ word2+" in the graph!");
                bridgeWords.add(errorWord);
                return bridgeWords;
            }
            if(!adjacencyMap.containsKey(word1))
            {
                System.out.println("No "+ word1 +" in the graph!");
                bridgeWords.add(errorWord);
                return bridgeWords;
            }
            if(!adjacencyMap.containsKey(word2)) {
                System.out.println("No " + word2 + " in the graph!");
                bridgeWords.add(errorWord);
                return bridgeWords;
            }
        }

        // 遍历 word1 的邻居节点
        Map<String, Integer> neighbors1 = adjacencyMap.get(word1);

        // 寻找桥接词
        for (String bridgeWord : neighbors1.keySet()) {
            // 如果 word1 → 桥接词 和 桥接词 → word2 都存在，则添加桥接词到列表中
            if (adjacencyMap.containsKey(bridgeWord) && adjacencyMap.get(bridgeWord).containsKey(word2)) {
                bridgeWords.add(bridgeWord);
            }
        }
        return bridgeWords;
    }

    public List<String> InsertqueryBridgeWords(String word1, String word2) {
        List<String> bridgeWords = new ArrayList<>();

        // 如果 word1 或 word2 不在图中出现，则返回空列表
        String word1Lower = word1.toLowerCase();
        String word2Lower = word2.toLowerCase();
        if (!adjacencyMap.containsKey(word1Lower) || !adjacencyMap.containsKey(word2Lower)) {
            //System.out.println("***");
            return bridgeWords;
        }
        // 遍历 word1 的邻居节点
        Map<String, Integer> neighbors1 = adjacencyMap.get(word1Lower);
        // 寻找桥接词
        for (String bridgeWord : neighbors1.keySet()) {
            // 将桥接词转换为小写字母形式
            String bridgeWordLower = bridgeWord.toLowerCase();
            // 如果 word1 → 桥接词 和 桥接词 → word2 都存在，则添加桥接词到列表中
            if (adjacencyMap.containsKey(bridgeWordLower) && adjacencyMap.get(bridgeWordLower).containsKey(word2Lower)) {
                bridgeWords.add(bridgeWord);
            }
        }
        return bridgeWords;
    }

    // Dijkstra 算法实现，返回距离和前驱节点映射
    public Map<String, String> dijkstra(String start, Map<String, Integer> distances) {
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String vertex : adjacencyMap.keySet()) {
            if (vertex.equals(start)) {
                distances.put(vertex, 0);
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
            }
            priorityQueue.add(vertex);
        }

        while (!priorityQueue.isEmpty()) {
            String current = priorityQueue.poll();
            if (distances.get(current) == Integer.MAX_VALUE) break; // 剪枝，如果当前最短距离是无穷大，后面的节点也不用处理了

            for (Map.Entry<String, Integer> neighbor : getNeighbors(current).entrySet()) {
                String neighborVertex = neighbor.getKey();
                int weight = neighbor.getValue();
                int currentDist = distances.getOrDefault(current, Integer.MAX_VALUE);
                int newDist = currentDist + weight;
                if (newDist < distances.getOrDefault(neighborVertex, Integer.MAX_VALUE)) {
                    priorityQueue.remove(neighborVertex);
                    distances.put(neighborVertex, newDist);
                    previous.put(neighborVertex, current);
                    priorityQueue.add(neighborVertex);
                }
            }
        }
        return previous;
    }

    public List<String> calcShortestPath(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = dijkstra(start, distances);
        List<String> path = new ArrayList<>();
        if (distances.getOrDefault(end, Integer.MAX_VALUE) == Integer.MAX_VALUE) {
            return path; // 不可达
        }

        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public Map<String, List<String>> allShortestPaths(String start) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = dijkstra(start, distances);
        Map<String, List<String>> paths = new HashMap<>();

        for (String vertex : adjacencyMap.keySet()) {
            if (!vertex.equals(start)) {
                List<String> path = new ArrayList<>();
                if (distances.getOrDefault(vertex, Integer.MAX_VALUE) != Integer.MAX_VALUE) {
                    for (String at = vertex; at != null; at = previous.get(at)) {
                        path.add(at);
                    }
                    Collections.reverse(path);
                }
                paths.put(vertex, path);
            }
        }
        return paths;
    }

    // 随机游走
    public List<String> randomWalk() {
        List<String> visited = new ArrayList<>();
        Set<String> visitedEdges = new HashSet<>();
        Random random = new Random();
        List<String> vertices = new ArrayList<>(getVertices());

        if (vertices.isEmpty()) {
            return visited;
        }

        String current = vertices.get(random.nextInt(vertices.size()));
        visited.add(current);

        while (true) {
            Map<String, Integer> neighbors = getNeighbors(current);
            if (neighbors.isEmpty()) {
                break;
            }

            List<String> neighborList = new ArrayList<>(neighbors.keySet());
            String next = neighborList.get(random.nextInt(neighborList.size()));
            String edge = current + " -> " + next;

            if (visitedEdges.contains(edge)) {
                break;
            }

            visitedEdges.add(edge);
            visited.add(next);
            current = next;
        }

        return visited;
    }

    // 将路径写入文件
    public void writePathToFile(List<String> path, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String node : path) {
                writer.write(node + "\n");
            }
        } catch (IOException e) {
            System.err.println("写入文件失败: " + e.getMessage());
        }
    }

    public void showDirectedGraph() {
        String dotFilePath = "D:\\Intellij IDEA\\Code\\exp\\graph.dot";
        String outputFilePath = "D:\\Intellij IDEA\\Code\\exp\\graph.png";
        List<String> shortestPath = new ArrayList<>();

        //将当前有向图对象转换为 DOT 文件
        GraphToDot.writeDotFile(this, dotFilePath,shortestPath);
        //将 DOT 文件转换为图像文件
        GraphvizRunner.generateGraphImage(dotFilePath, outputFilePath);

        SwingUtilities.invokeLater(() -> {
            //创建了一个GraphDisplay 对象，该对象用于显示生成的图像文件，传入图像文件的路径作为参数
            GraphDisplay display = new GraphDisplay(outputFilePath);
            display.setVisible(true);
        });
    }

    public void show_shortDirectedGraph(List<String> Path) {
        String dotFilePath = "D:\\Intellij IDEA\\Code\\exp\\graph_short.dot";
        String outputFilePath = "D:\\Intellij IDEA\\Code\\exp\\graph_short.png";

        GraphToDot.writeDotFile(this, dotFilePath,Path);
        GraphvizRunner.generateGraphImage(dotFilePath, outputFilePath);

        SwingUtilities.invokeLater(() -> {
            GraphDisplay display = new GraphDisplay(outputFilePath);
            display.setVisible(true);
        });
    }
}

class BridgeWordInsertion {

    private final DirectedGraph graph;
    private final Random random;

    public BridgeWordInsertion(DirectedGraph graph) {
        this.graph = graph;
        this.random = new Random();
    }

    // 计算桥接词
    public String calculateBridgeWord(String word1, String word2) {
        List<String> bridgeWords = graph.InsertqueryBridgeWords(word1, word2);

        if (bridgeWords.isEmpty()) {
            return "";
        } else {
            return bridgeWords.get(random.nextInt(bridgeWords.size()));
        }
    }

    // 插入桥接词并输出结果
    public void generateNewText(String text) {
        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            result.append(word1).append(" ");

            String bridgeWord = calculateBridgeWord(word1, word2);
            //System.out.println(bridgeWord);
            if (!bridgeWord.isEmpty()) {
                result.append(bridgeWord).append(" ");
            }
        }

        // 将最后一个单词添加到结果中
        result.append(words[words.length - 1]);

        // 输出结果
        System.out.println("Output: " + result.toString());
    }
}


public class Main {
    public static void main(String[] args) {
        //数据处理
        DataDeal dataDeal = new DataDeal();
        StringBuilder content = dataDeal.datadeal();
        content = new StringBuilder(content.toString().toLowerCase());

        // 将文本数据转化为有向图
        DirectedGraph graph = new DirectedGraph();
        String[] words = content.toString().split(" ");
        for (int i = 0; i < words.length - 1; i++) {
            String source = words[i];
            String destination = words[i + 1];
            graph.addEdge(source, destination);
        }
        Scanner scan = new Scanner(System.in);


        int flag = 0;
        while(flag != -1)
        {
            System.out.println("所能执行的操作：");
            System.out.println("1：打印有向图");
            System.out.println("2：查询桥接点");
            System.out.println("3：输入文本并根据桥接词更新文本");
            System.out.println("4：计算两个单词的最短路径");
            System.out.println("5：随机游走");
            System.out.println("6：程序结束");
            System.out.println("输入操作前的序号：");
            flag = scan.nextInt();
            scan.nextLine(); // 消耗换行符
            switch (flag){
                case 1:
                    // 打印有向图
                    graph.showDirectedGrap();
                    graph.showDirectedGraph();
                    break;
                case 2:
                    //查找桥接词
                    System.out.println("输入桥接词 word1：");
                    String word1 = scan.nextLine();
                    System.out.println("输入桥接词 word2：");
                    String word2= scan.nextLine();
                    // 查询桥接词
                    List<String> bridgeWords = graph.queryBridgeWords(word1, word2);
                    // 输出结果

                    if (bridgeWords.isEmpty()) {
                        System.out.println("No bridge words from " + word1 + " to " + word2 + "!");
                    }
                    else if("1".equals(bridgeWords.getFirst()))
                    {
                         System.out.println();
                    }
                    else {
                        System.out.print("The bridge words from " + word1 + " to " + word2 + " are: ");
                        for (String word : bridgeWords) {
                            System.out.print(word + ", ");
                        }
                        System.out.println();
                    }
                    break;
                case 3:
                    // 用户输入的新文本
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("请输入新文本: ");
                    String newText = scanner.nextLine();

                    // 进行桥接词插入并输出结果
                    BridgeWordInsertion insertion = new BridgeWordInsertion(graph);
                    insertion.generateNewText(newText);
                    break;
                case 4:
                    // 计算最短路径
                    System.out.println("输入单词（一个或两个）：");
                    String input = scan.nextLine();
                    String[] inputWords = input.split(" ");
                    if (inputWords.length == 1) {
                        String startWord = inputWords[0];
                        Map<String, List<String>> allPaths = graph.allShortestPaths(startWord);
                        for (Map.Entry<String, List<String>> entry : allPaths.entrySet()) {
                            String endWord = entry.getKey();
                            List<String> path = entry.getValue();
                            if (path.isEmpty()) {
                                System.out.println("No path from " + startWord + " to " + endWord);
                            } else {
                                System.out.println("Shortest path from " + startWord + " to " + endWord + ": " + String.join(" -> ", path));
                                System.out.println("Path length: " + (path.size() - 1));
                            }
                        }
                    } else if (inputWords.length == 2) {
                        String startWord = inputWords[0];
                        String endWord = inputWords[1];
                        List<String> calcShortestPath = graph.calcShortestPath(startWord, endWord);
                        if (calcShortestPath.isEmpty()) {
                            System.out.println("No path from " + startWord + " to " + endWord);
                        } else {
                            System.out.println("Shortest path from " + startWord + " to " + endWord + ": " + String.join(" -> ", calcShortestPath));
                            System.out.println("Path length: " + (calcShortestPath.size() - 1));
                        }
                        graph.show_shortDirectedGraph(calcShortestPath);
                    } else {
                        System.out.println("输入无效，请输入一个或两个单词。");
                    }
                    break;
                case 5:
                    // 随机游走
                    Thread randomWalkThread = new Thread(() -> {
                        List<String> path = graph.randomWalk();
                        System.out.println("Random walk path: " + String.join(" -> ", path));
                        graph.writePathToFile(path, "D:\\Intellij IDEA\\Code\\exp\\random_walk.txt");
                    });
                    randomWalkThread.start();
                    System.out.println("按Enter键停止遍历...");
                    scan.nextLine(); // 等待用户输入
                    randomWalkThread.interrupt(); // 停止遍历
                    break;
                case 6:
                    // 终止程序
                    System.out.println("程序结束。");
                    System.exit(0); // 正常终止程序
                    break;
                default:
                    System.out.println("无效的操作序号，请输入 1 到 5 或 6 终止程序。");
            }

        }


    }
}