import java.util.Stack;

/**
 * @author gztravelsky
 * @author MaiShuRen
 * @date 2020-10-29
 */
public class Test42 {
    public static void main(String[] args) {

    }

    public int trap(int[] height) {
        Stack<Integer> stack = new Stack<>();
        int result = 0;
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int bottomIndex = stack.pop();
                // 如果栈顶元素一直相等，那么全都pop出去，只留第一个
                while (!stack.isEmpty() && height[stack.peek()] == height[bottomIndex]) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    // stack.peek()是此次接住的雨水的左边界的位置，右边界是当前的柱体，即i。
                    // Math.min(height[stack.peek()], height[i]) 是左右柱子高度的min，减去height[bottomIdx]就是雨水的高度。
                    // i - stack.peek() - 1 是雨水的宽度。
                    result += (Math.min(height[stack.peek()], height[i]) - height[bottomIndex]) * (i - stack.peek() - 1);
                }
            }
            stack.push(i);
        }
        return result;
    }
}
