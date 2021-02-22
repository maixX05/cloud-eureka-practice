import java.util.Stack;

/**
 * @author gztravelsky
 * @author MaiShuRen
 * @date 2020-10-29
 */
public class Test150 {

    public static void main(String[] args) {
        String[] tokens = {"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"};

        Test150 test150 = new Test150();
        System.out.println(test150.evalRPN(tokens));
    }

    public int evalRPN(String[] tokens) {
        int result = 0;
        if (tokens.length == 0) {
            return result;
        }
        Stack<Integer> stack = new Stack<>();
        // 用于暂存除数和减数。减法和除法讲究两个数的位置
        int tmp;
        for (String token : tokens) {
            switch (token) {
                case "+":
                    result = stack.pop() + stack.pop();
                    stack.push(result);
                    break;
                case "-":
                    tmp = stack.pop();
                    result = stack.pop() - tmp;
                    stack.push(result);
                    break;
                case "*":
                    result = stack.pop() * stack.pop();
                    stack.push(result);
                    break;
                case "/":
                    tmp = stack.pop();
                    result = stack.pop() / tmp;
                    stack.push(result);
                    break;
                default:
                    stack.push(Integer.valueOf(token));
                    break;
            }
        }
        return stack.pop();
    }
}
