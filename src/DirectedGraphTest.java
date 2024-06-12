import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class DirectedGraphTest {

    private DirectedGraph graph;

    @BeforeEach
    void setUp() {
        graph = new DirectedGraph();
        // 添加一些边以供测试
        graph.addEdge1("cat", "bridge1");
        graph.addEdge1("bridge1", "dog");
        graph.addEdge1("cat", "bridge2");
        graph.addEdge1("bridge2", "elephant");
        graph.addEdge1("bridge3", "elephant");
        graph.addEdge1("bridge1", "cat");
    }


    @Test
    void testGraphContainsEdges() {
        assertTrue(graph.hasEdge("cat", "bridge1"));
        assertTrue(graph.hasEdge("bridge1", "dog"));
        assertTrue(graph.hasEdge("cat", "bridge2"));
        assertTrue(graph.hasEdge("bridge2", "elephant"));
        // 这里假设 hasEdge 方法用于检查图中是否包含指定的边
        // 如果 hasEdge 方法不存在，你需要根据实际情况验证图的状态
    }

    @Test
    void testValidBridgeWordExists() {
        List<String> expected = Arrays.asList("bridge1");
        List<String> result = graph.queryBridgeWords("cat", "dog");
        assertEquals(expected, result);
    }

    @Test
    void testNoBridgeWordExists() {
        List<String> expected = Collections.emptyList();
        List<String> result = graph.queryBridgeWords("cat", "bridge3");
        assertEquals(expected, result);
    }

    @Test
    void testSameWordWithBridge() {
        List<String> expected = Arrays.asList("bridge1");
        List<String> result = graph.queryBridgeWords("cat", "cat");
        assertEquals(expected, result);
    }

    @Test
    void testSameWordWithoutBridge() {
        List<String> expected = Collections.emptyList();
        List<String> result = graph.queryBridgeWords("dog", "dog");
        assertEquals(expected, result);
    }

    @Test
    void testEmptyWord1() {
        List<String> expected = Arrays.asList("1");
        List<String> result = graph.queryBridgeWords("", "dog");
        assertEquals(expected, result);
    }

    @Test
    void testEmptyWord2() {
        List<String> expected = Arrays.asList("1");
        List<String> result = graph.queryBridgeWords("cat", "");
        assertEquals(expected, result);
    }

    @Test
    void testWordNotInGraph() {
        List<String> expected = Arrays.asList("1");
        List<String> result = graph.queryBridgeWords("unicorn", "dragon");
        assertEquals(expected, result);
    }

    @Test
    void testWord1NotInGraph() {
        List<String> expected = Arrays.asList("1");
        List<String> result = graph.queryBridgeWords("unicorn", "cat");
        assertEquals(expected, result);
    }

    @Test
    void testWord2NotInGraph() {
        List<String> expected = Arrays.asList("1");
        List<String> result = graph.queryBridgeWords("dog", "dragon");
        assertEquals(expected, result);
    }

    @Test
    void testEmptyGraph() {
        DirectedGraph emptyGraph = new DirectedGraph();
        List<String> expected = Arrays.asList("1");
        List<String> result = emptyGraph.queryBridgeWords("cat", "dog");
        assertEquals(expected, result);
    }
}

