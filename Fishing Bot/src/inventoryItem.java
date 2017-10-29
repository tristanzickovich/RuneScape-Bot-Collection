public class inventoryItem{
	String imageName;
	String keyBind;
	double similarity;
	public inventoryItem(String name, String keyBind, double similarity) {
		this.imageName = name;
		this.keyBind = keyBind;
		this.similarity = similarity;
	}
}