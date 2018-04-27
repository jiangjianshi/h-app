
public class TestOilstore{
	int kucun = 0;//库存初始值
	//入库汽油
	public int ruku(int count){
		return kucun+=count;		
	}
	//获取汽油数量
	public int getkun(){
		return kucun;		
	}
	//加油
	public int jiayou(int count){
		if(count > this.getkun()){//如果加油量大于库存了
			System.out.println("库存不足");
			return -1 ;//throw EXCEPTION
		}
		 kucun-=count;//减少汽油
		 return kucun;//当前汽油量
	}
	public static void main(String[] args) {
		TestOilstore o = new TestOilstore();
		System.out.println(o.ruku(20));
		System.out.println(o.ruku(20));
		System.out.println(o.getkun());
		System.out.println(o.ruku(20));
		System.out.println(o.jiayou(110));
	}
	
}
