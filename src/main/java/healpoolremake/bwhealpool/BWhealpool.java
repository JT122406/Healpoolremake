package healpoolremake.bwhealpool;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.server.ArenaDisableEvent;
import com.andrei1058.bedwars.api.events.team.TeamEliminatedEvent;
import com.andrei1058.bedwars.api.events.upgrades.UpgradeBuyEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BWhealpool extends JavaPlugin implements Listener {

    public static BWhealpool plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        //Check for Bedwars
        if (!Bukkit.getPluginManager().isPluginEnabled("Bedwars1058")){
            this.getLogger().info("You must install Bedwars1058");
            this.setEnabled(false);
            return;
        }
        else
        {
            this.getLogger().info("Detected Bedwars1058!");
        }

        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onTeamUpgrade(UpgradeBuyEvent e){
        if (e.getTeamUpgrade().getName().equalsIgnoreCase("upgrade-heal-pool")){
            IArena a = e.getArena();
            if (a == null) return;
            ITeam bwt = a.getTeam(e.getPlayer());
            if (bwt == null) return;
            if (!HealPoolTask.exists(a, bwt)){
                new HealPoolTask(bwt);
            }
        }
    }

    @EventHandler
    public void onDisable(ArenaDisableEvent e){HealPoolTask.removeForArena(e.getArenaName());}

    @EventHandler
    public void onEnd(GameEndEvent e) {HealPoolTask.removeForArena(e.getArena());}

    @EventHandler
    public void teamDead(TeamEliminatedEvent e) {HealPoolTask.removeForTeam(e.getTeam());}
}
