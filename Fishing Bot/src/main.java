import org.sikuli.script.*;
import java.util.*;

public class main {
//global constants
public static final int LEFT_CLICK = 0;
public static final int RIGHT_CLICK = 1;
//global arrays
static inventoryItem[] dropItemArray = {new inventoryItem("raw_shrimp.jpg",Key.NUM1,0.93),
	new inventoryItem("raw_anchovie.jpg",Key.NUM2,0.91), new inventoryItem("raw_tuna.jpg",Key.NUM3,0.75),
	new inventoryItem("raw_lobster.jpg",Key.NUM4,0.75)};
static String[] fishingMethodArray = {"net_fishing.jpg", "cage_fishing.jpg"};
//global elements
static Region backpackRegion;
static Region characterRegion;
static Region fishingRegion;
static int numFishingSpotImages = 7;
static double activityThreshold = 43;

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
public static void dropAll(Region curRegion, inventoryItem itemObj) {
	
	List<Match> item;
	if((item = searchRegionAll(curRegion, itemObj.imageName, itemObj.similarity)) != null) {
		keyPress(Key.SPACE);
		for(int i = 0; i < item.size(); ++i) {
			keyPress(itemObj.keyBind);
			delayTime(100);
		}
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
public static void fish() {
	delayTime(500);
	while(true) {
		for(int i = 0; i < numFishingSpotImages; ++i) {
			String tmpName = "fish_spot" + i + ".jpg";
			Match spot = searchRegionOne(fishingRegion, tmpName, .75);
			if(spot != null) {
				Location loc = spot.getTarget();
				clickThis(loc, RIGHT_CLICK);
				delayTime(500);
				for(int j = 0; j < fishingMethodArray.length; ++j) {
					Match fishingType = searchRegionOne(fishingRegion, fishingMethodArray[j], .85);
					if(fishingType != null) {
						Location type = fishingType.getTarget();
						clickThis(type, LEFT_CLICK);
						delayTime(1000);
						return;
					}
				}
			}
		}
	}
}
public static void dropInventory(inventoryItem[] allItems) {
	for(int i = 0; i < allItems.length; ++i) {
		dropAll(backpackRegion, allItems[i]);
	}
}
public static Region findBackpack() {
	Screen s = new Screen();
	try {
		Match t = s.find("images/backpack.jpg");
		return t;
	} catch (FindFailed e) {
		e.printStackTrace();
	}
	return null;
}
public static Region defineCharacterRegion(int w, int h) {
	Screen s = new Screen();
	Region r = new Region(s.getBounds());
	Location l = r.getCenter();
	return new Region(l.x, l.y-10, w, h).offset(new Location(-w/2, -h/2));
}
public static Region defineObservationRegion(int w, int h) {
	Screen s = new Screen();
	Region r = new Region(s.getBounds());
	Location l = r.getCenter();
	return new Region(l.x, l.y, w, h).offset(new Location(-w/2, -h/2));
}
static double avgCt = 0;
static int avgCtTotal = 0;
public static void startFishing() {
	dropInventory(dropItemArray);
	fish();
	long lastFishTime = System.currentTimeMillis();
	int i = 0;
	int stillCount = 3;
	while (characterRegion.isObserving()) { // do something while observe is running
		characterRegion.wait(0.2);
		long elapsedFishTime = (System.currentTimeMillis() - lastFishTime)/1000;
		//if 30 seconds since last fish time, call fish() as backup measure
		if(elapsedFishTime >= 30) {
			dropInventory(dropItemArray);
			fish();
			lastFishTime = System.currentTimeMillis();
		}
		if (i % 15 == 14) {
			double avg = avgCt/avgCtTotal;
			System.out.println(avg);
			if(avg < activityThreshold) {
				//if not fishing
				System.out.println("Probably Still");
				if(stillCount >= 3) {
					dropInventory(dropItemArray);
					fish();
					lastFishTime = System.currentTimeMillis();
				}
				++stillCount;
			}
			else {
				//if fishing
				System.out.println("Probably Fishing");
				stillCount = 0;
			}
			avgCt = 0;
			avgCtTotal = 0;
		}
		++i;
	}
}
public static void main(String[] args) {
	delayTime(2000);
	//find / initialize backpack region
	backpackRegion = findBackpack();
	//initialize character region
	characterRegion = defineCharacterRegion(30,60);
	fishingRegion = defineObservationRegion(700, 350);
	//fishingRegion.highlight(1);
	//characterRegion.getCenter().click();
	//characterRegion.highlight(1);
	characterRegion.onChange(new ObserverCallBack(){ // define the handler
		@Override
		public void changed(ObserveEvent evt) {
			//System.out.println(evt.getChanges());
			List<Match> characterChanges = evt.getChanges();
			for(int i = 0; i < characterChanges.size();++i) {
				avgCt += characterChanges.get(i).h;
				avgCtTotal++;
				//System.out.println(characterChanges.get(i));
			}
		}
	});
	characterRegion.observeInBackground(); // start observation in background forever
	startFishing();
	//Test Code front: fishing: 35-48, still: 25-32,  side: still 38-41, Fishing;  40-48 (46)
	/*int i = 0;
	while (characterRegion.isObserving()) {
		characterRegion.wait(0.2);
		if(i % 15 == 14) {
			double avg = avgCt/avgCtTotal;
			System.out.println(avg);
			avgCtTotal = 0;
			avgCt = 0;
			//System.out.println(avg);
		}
		++i;
	}*/
}
}