package me.wickyplays.spigot.crowdfans.obj;

import org.bukkit.Location;

public class CrowdPoint {

    private Location point1;
    private Location point2;

    public CrowdPoint(Location l1, Location l2) {
        this.point1 = l1;
        this.point2 = l2;
    }

    public Location getPoint1() {
        return point1;
    }

    public void setPoint1(Location point1) {
        this.point1 = point1;
    }

    public Location getPoint2() {
        return point2;
    }

    public void setPoint2(Location point2) {
        this.point2 = point2;
    }
}
