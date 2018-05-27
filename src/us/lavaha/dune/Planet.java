package us.lavaha.dune;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleGraph;

public class Planet {

    private static Graph <Planet, GuildRoute> guildNetwork;
    private static Graph <Planet, SmugglerRoute> smugglerNetwork;

    private String name;

    public Planet(name) {
        this.name = name;
    }

    public static void init() {
        guildNetwork = new SimpleGraph(SmugglerRoute.class);
    }

    public static GraphPath<Planet, GuildRoute> guildPath(Planet from, Planet to) {
        DijkstraShortestPath<Planet, GuildRoute> dsp = new DijkstraShortestPath<>(guildNetwork);
        return dsp.getPath(from, to);
    }

    public static GraphPath<Planet, SmugglerRoute> smugglerPath(Planet from, Planet to) {
        DijkstraShortestPath<Planet, SmugglerRoute> dsp = new DijkstraShortestPath<>(smugglerNetwork);
        return dsp.getPath(from, to);
    }

}