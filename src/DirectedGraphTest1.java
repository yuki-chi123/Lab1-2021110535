import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class DirectedGraphTest1 {

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

    // 测试用例1：两个词都不在图中
    @Test
    void testBothWordsNotInGraph() {
        List<String> expected = Arrays.asList("1");
        List<String> result = graph.queryBridgeWords("unicorn", "dragon");
        assertEquals(expected, result);
    }

    // 测试用例2：第一个词不在图中
    @Test
    void testFirstWordNotInGraph() {
        List<String> expected = Arrays.asList("1");
        List<String> result = graph.queryBridgeWords("unicorn", "dog");
        assertEquals(expected, result);
    }

    // 测试用例3：第二个词不在图中
    @Test
    void testSecondWordNotInGraph() {
        List<String> expected = Arrays.asList("1");
        List<String> result = graph.queryBridgeWords("cat", "dragon");
        assertEquals(expected, result);
    }

    // 测试用例4：两个词都在图中，存在桥接词
    @Test
    void testBridgeWordExists() {
        List<String> expected = Arrays.asList("bridge1");
        List<String> result = graph.queryBridgeWords("cat", "dog");
        assertEquals(expected, result);
    }

    // 测试用例5：两个词都在图中，不存在桥接词
    @Test
    void testNoBridgeWordExists() {
        List<String> expected = Collections.emptyList();
        List<String> result = graph.queryBridgeWords("cat", "bridge2");
        assertEquals(expected, result);
    }
}