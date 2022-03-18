package healpoolremake.bwhealpool;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.server.ArenaDisableEvent;
import com.andrei1058.bedwars.api.events.upgrades.UpgradeBuyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
        if (e.getTeamUpgrade().getName().contains("heal")){
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
    public void teamDead(PlayerKillEvent event) {
        if (event.getArena().getTeam(event.getVictim()).isBedDestroyed()){
            for (Player mate: event.getArena().getTeam(event.getVictim()).getMembers()) {
                if (mate.isDead()){
                    HealPoolTask.removeForTeam(event.getArena().getTeam(event.getVictim()));
                }

            }
        }
    }
}
