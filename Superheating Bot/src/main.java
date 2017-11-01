import org.sikuli.script.*;
import java.util.*;

public class main {
//global constants
public static final int LEFT_CLICK = 0;
public static final int RIGHT_CLICK = 1;
public static final String SUPERHEAT_KEYBIND = Key.NUM1;
public static final String PRESET_KEYBIND = Key.NUM1;
public static final String EMPTYBACKPACK_KEYBIND = Key.NUM3;
public static final int NUMBER_SPELLCASTS = 11;
public static final int NUMBER_ITERATIONS = 4;
//global elements
static Region backpackRegion;
static Region bankBackpackRegion;
static Region mainRegion;
static Location lastOreLocation;

public static Location chooseLast(List<Match> elementGroup) {
	Location lastElem = new Location(0,0);
	for(int i = 0; i < elementGroup.size(); ++i) {
		Match temp = elementGroup.get(i);
		Location tmp = temp.getTarget();
		//System.out.printf("%s \n", tmp);
		if(tmp.y > lastElem.y || (tmp.y == lastElem.y && tmp.x > lastElem.x)) {
			lastElem = tmp;
		}
	}
	return lastElem;
}
public static List<Match> searchRegionAll(Region curRegion, String item, double similarity) {
	String img_path = "images/" + item;
	try {
		Iterator<Match> t = curRegion.findAll(img_path);
		List<Match> values = new ArrayList<Match>();
		while(t.hasNext()) {
			Match x = t.next();
			if(x.getScore() >= similarity) {
				values.add(x);
			}
		}
		return values;
	} catch (FindFailed e) {
		e.printStackTrace();
	}
	return null;
}
public static Match searchRegionOne(Region curRegion, String item, double similarity) {
	String img_path = "images/" + item;
	try {
		Match t = curRegion.find(img_path);
		if(t.getScore() > similarity) {
			return t;
		}
	} catch (FindFailed e) {
		e.printStackTrace();
	}
	return null;
}
public static void keyPress(String key) {
	Screen s = new Screen();
	s.keyDown(key);
	delayTime(100);
	s.keyUp(key);
}
public static void delayTime(int ms) {
	try{
		int offset = new Random().nextInt(100) + 1;
		Thread.sleep(ms + offset);
	} catch(InterruptedException x){ 
	}
}
public static void clickThis(Location loc, int clickType) {
	if(clickType == LEFT_CLICK) {
		loc.click();
	}
	else if(clickType == RIGHT_CLICK) {
		loc.rightClick();
	}
}
public static Region findBackpack(String path) {
	Screen s = new Screen();
	try {
		Match t = s.find(path);
		return t;
	} catch (FindFailed e) {
		e.printStackTrace();
	}
	return null;
}
public static Region defineObservationRegion(int w, int h) {
	Screen s = new Screen();
	Region r = new Region(s.getBounds());
	Location l = r.getCenter();
	return new Region(l.x, l.y, w, h).offset(new Location(-w/2, -h/2));
}
public static void startSuperheating() {
	Location bank = searchRegionOne(mainRegion, "bank_chest.jpg", .90).getCenter();
	clickThis(bank, LEFT_CLICK);
	delayTime(400);
	bankBackpackRegion = findBackpack("images/bank_backpack.jpg");
	keyPress(PRESET_KEYBIND);
	delayTime(400);
	List<Match> allOres = searchRegionAll(backpackRegion, "ore.jpg", .90);
	lastOreLocation = chooseLast(allOres);
	clickThis(bank, LEFT_CLICK);
	delayTime(400);
	Location coalBag = searchRegionOne(bankBackpackRegion, "coal_bag.jpg", .90).getCenter();
	clickThis(coalBag, RIGHT_CLICK);
	delayTime(100);
	Location fillCoalBag = searchRegionOne(mainRegion, "fill_coal_bag.jpg", .85).getCenter();
	clickThis(fillCoalBag, LEFT_CLICK);
	delayTime(30);
	for(int i = 0; i < NUMBER_ITERATIONS; ++i){
		keyPress(PRESET_KEYBIND);
		delayTime(480);
		for(int j = 0; j < NUMBER_SPELLCASTS; ++j) {
			keyPress(SUPERHEAT_KEYBIND);
			delayTime(30);
			clickThis(lastOreLocation, LEFT_CLICK);
			delayTime(340);
		}
		if(bank == null)
			break;
		bank = searchRegionOne(mainRegion, "bank_chest.jpg", .80).getCenter();
		clickThis(bank, LEFT_CLICK);
		delayTime(300);
		coalBag = searchRegionOne(bankBackpackRegion, "coal_bag.jpg", .90).getCenter();
		if(coalBag == null)
			break;
		clickThis(coalBag, RIGHT_CLICK);
		delayTime(30);
		clickThis(fillCoalBag, LEFT_CLICK);
		delayTime(30);
	}
}
public static void main(String[] args) {
	delayTime(2000);
	//find / initialize backpack region
	backpackRegion = findBackpack("images/backpack.jpg");
	//define main region
	mainRegion = defineObservationRegion(1000, 600);
	startSuperheating();
}
}