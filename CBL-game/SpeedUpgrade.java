class SpeedUpgrade implements Upgrade {

    static final long DURATION = 5000;
    static final double SPEED_MODIFIER = 1.2;

    @Override
    public Object clone() {
        return new SpeedUpgrade();
    }

    @Override
    public long getDuration() {
        return DURATION;
    }

    @Override
    public void upgradePlayer(Player p) {
        p.bulletInterval /= SPEED_MODIFIER;
        p.SPEED *= SPEED_MODIFIER;
    }

    @Override
    public void removeUpgrade(Player p) {

        p.bulletInterval *= SPEED_MODIFIER;
        p.SPEED /= SPEED_MODIFIER;
    }
}
