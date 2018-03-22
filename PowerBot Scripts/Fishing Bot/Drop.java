package scripts;

import org.powerbot.script.rt6.Action;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.CombatBar;
import org.powerbot.script.rt6.Item;

import java.util.ArrayList;

public class Drop extends Task<ClientContext>{
    static int stillCounter = 0;
    public Drop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if(ctx.players.local().animation() == -1){
           stillCounter++;
        }
        else{
            stillCounter = 0;
        }
        return ctx.backpack.select().count() >= 28
                && !ctx.chat.chatting()
                && stillCounter >= 3;
    }

    public static boolean contains(final int[] array, final int v){
        for (int i : array){
            if(i == v){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> actionbarIds(){
        CombatBar actionBar = new CombatBar(ctx);
        Action allActions[] = actionBar.actions();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for(Action a : allActions){
            if(a.id() != -1) {
               ids.add(a.id());
            }
        }
        return ids;
    }

    public ArrayList<Integer> activeActionBarIndices(){
        CombatBar actionBar = new CombatBar(ctx);
        Action allActions[] = actionBar.actions();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        int index = 0;
        for(Action a : allActions){
            if(a.id() != -1) {
                indices.add(index);
            }
            ++index;
        }
        return indices;
    }

    public void dropByClicking(final int[] ids){
        Item backpackItems[] = ctx.backpack.items();
        for(int i = 0; i < backpackItems.length; ++i){
           Item dropItem = backpackItems[i];
           if (contains(ids, dropItem.id())){
               dropItem.interact("Drop");
            }
        }
    }

    public void dropByActionBar(){
        ArrayList<Integer> dropIndices = activeActionBarIndices();
        CombatBar actionBar = new CombatBar(ctx);
        for(int i = 0; i < dropIndices.size(); ++i) {
            Action currentAction = actionBar.actionAt(dropIndices.get(i));
            while (currentAction.ready()){
                currentAction.select();
            }
        }
    }

    @Override
    public void execute() {
        dropByActionBar();
    }
}
