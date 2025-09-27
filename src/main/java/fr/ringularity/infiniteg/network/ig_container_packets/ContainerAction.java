package fr.ringularity.infiniteg.network.ig_container_packets;

public enum ContainerAction {
    CLICK(0),
    SHIFT_CLICK(1),
    CTRL_CLICK(2);

    public final int actionType;

    ContainerAction(int actionType) {
        this.actionType = actionType;
    }
}
