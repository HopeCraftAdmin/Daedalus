package funkemunky.Daedalus.check.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;

import funkemunky.Daedalus.Daedalus;
import funkemunky.Daedalus.check.Check;
import funkemunky.Daedalus.check.other.Latency;
import funkemunky.Daedalus.utils.Chance;
import funkemunky.Daedalus.utils.UtilCheat;

public class HitBoxes extends Check {
	
	public HitBoxes(Daedalus Daedalus) {
		super("HitBoxes", "Hitboxes", Daedalus);
	}
	
	public static Map<UUID, Integer> count = new HashMap();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e) {
		if(count.containsKey(e.getPlayer().getUniqueId())) {
			count.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onUse(EntityDamageByEntityEvent e) {
		if(e.getCause() != DamageCause.ENTITY_ATTACK) {
			return;
		}
		if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
			return;
		}
	    Player player = (Player) e.getDamager();
	    Player attacked = (Player) e.getEntity();
	    if(player.hasPermission("daedalus.bypass")) {
	    	return;
	    }
	    
	    int Count = 0;
	    if(count.containsKey(player.getUniqueId())) {
	    	Count = count.get(player.getUniqueId());
	    }
	    
	    double offset = UtilCheat.getOffsetOffCursor(player, attacked);
	    double Limit = 41D;
	    double distance = UtilCheat.getHorizontalDistance(player.getLocation(), attacked.getLocation());
	    Limit+= distance * 12;
	    Limit+= (attacked.getVelocity().length() + player.getVelocity().length()) * 42;
	    
	    if(Latency.getLag(player) > 80 || Latency.getLag(attacked) > 80) {
	    	return;
	    }
	    
	    if(offset > Limit) {
	    	Count++;
	    } else {
	    	Count = 0;
	    }
	    
	    if(Count > 2) {
	    	getDaedalus().logCheat(this, player, offset + " > " + Limit, Chance.LIKELY, new String[] {"Experimental"});
	    	Count = 0;
	    }
	    
	    this.count.put(player.getUniqueId(), Count);
	}

}
