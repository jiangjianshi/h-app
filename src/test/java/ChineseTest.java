
public class ChineseTest {
	/**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
     
    public static void main(String args[]){
//        String str = "hello你好";
//        System.out.println(length(str));
//        for(int i=0;i<10;i++){
//            System.out.println(Math.round(Math.random()*(6-1)+1));
//
//        }
        String a ="12.8";
        int b = Integer.valueOf(a);
        System.out.println(b);
    }
}
