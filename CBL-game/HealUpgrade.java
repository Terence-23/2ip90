class HealUpgrade implements Upgrade {

    @Override
    public Object clone() {
        return new HealUpgrade();

    }

    static final double HEAL_AMMOUNT = 10;

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public void upgradePlayer(Player p) {
        p.health += HEAL_AMMOUNT;
        p.health = Math.min(p.health, Player.MAX_HEALTH);
    }

    @Override
    public void removeUpgrade(Player p) {
    }

}
