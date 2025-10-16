import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

interface Upgrade extends Cloneable {

    long getDuration();

    /**
     * Upgrade behaviour upon pickup upgrades player and schedules removal after
     * time.
     *
     * @param p player picking up the upgrade
     */
    default void onPickup(Player p) {
        upgradePlayer(p);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeUpgrade(p);
            }
        }, getDuration());
    }

    void upgradePlayer(Player p);

    void removeUpgrade(Player p);

    Object clone();

}
