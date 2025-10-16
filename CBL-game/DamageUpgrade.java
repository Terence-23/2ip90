
class DamageUpgrade implements Upgrade {

    @Override
    public Object clone() {
        return new DamageUpgrade();
    }

    // 5 seconds
    static final long DURATION = 5000;
    static final double DAMAGE_BUFF = 10;

    @Override
    public void upgradePlayer(Player p) {
        p.bulletDamage += DAMAGE_BUFF;
    }

    @Override
    public void removeUpgrade(Player p) {
        p.bulletDamage -= DAMAGE_BUFF;
    }

    @Override
    public long getDuration() {
        return DURATION;
    }

}
