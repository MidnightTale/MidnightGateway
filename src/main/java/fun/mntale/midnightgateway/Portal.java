package fun.mntale.midnightgateway;

public record Portal(
        String world,
        int x1, int y1, int z1,
        int x2, int y2, int z2,
        String destination
) {
}