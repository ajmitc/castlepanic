package castlepanic.view;

import castlepanic.Model;
import castlepanic.game.Arc;
import castlepanic.game.Ring;
import castlepanic.game.monster.Monster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class BoardPanel extends JPanel {
    private static final Point CENTER_POINT = new Point(490, 487);
    private static final double ARC_DEGREES = 60.0;
    private static final Map<Arc, Map<Ring, Point>> boardCoords = new HashMap<>();

    static {
        Arrays.stream(Arc.values()).forEach(arc -> {
            boardCoords.put(arc, new HashMap<>());
            Arrays.stream(Ring.values()).forEach(ring -> {
                boardCoords.get(arc).put(ring, new Point());
            });
        });

        boardCoords.get(Arc.ARC_1).get(Ring.CASTLE).setLocation(420, 446);
        boardCoords.get(Arc.ARC_1).get(Ring.SWORDSMAN).setLocation(340, 405);
        boardCoords.get(Arc.ARC_1).get(Ring.KNIGHT).setLocation(264, 350);
        boardCoords.get(Arc.ARC_1).get(Ring.ARCHER).setLocation(185, 315);
        boardCoords.get(Arc.ARC_1).get(Ring.FOREST).setLocation(110, 275);

        boardCoords.get(Arc.ARC_2).get(Ring.CASTLE).setLocation(490, 410);
        boardCoords.get(Arc.ARC_2).get(Ring.SWORDSMAN).setLocation(490, 321);
        boardCoords.get(Arc.ARC_2).get(Ring.KNIGHT).setLocation(490, 224);
        boardCoords.get(Arc.ARC_2).get(Ring.ARCHER).setLocation(490, 140);
        boardCoords.get(Arc.ARC_2).get(Ring.FOREST).setLocation(490, 50);

        boardCoords.get(Arc.ARC_3).get(Ring.CASTLE).setLocation(560, 445);
        boardCoords.get(Arc.ARC_3).get(Ring.SWORDSMAN).setLocation(638, 400);
        boardCoords.get(Arc.ARC_3).get(Ring.KNIGHT).setLocation(720, 360);
        boardCoords.get(Arc.ARC_3).get(Ring.ARCHER).setLocation(785, 310);
        boardCoords.get(Arc.ARC_3).get(Ring.FOREST).setLocation(865, 265);

        boardCoords.get(Arc.ARC_4).get(Ring.CASTLE).setLocation(555, 527);
        boardCoords.get(Arc.ARC_4).get(Ring.SWORDSMAN).setLocation(640, 560);
        boardCoords.get(Arc.ARC_4).get(Ring.KNIGHT).setLocation(725, 595);
        boardCoords.get(Arc.ARC_4).get(Ring.ARCHER).setLocation(780, 640);
        boardCoords.get(Arc.ARC_4).get(Ring.FOREST).setLocation(865, 700);

        boardCoords.get(Arc.ARC_5).get(Ring.CASTLE).setLocation(490, 565);
        boardCoords.get(Arc.ARC_5).get(Ring.SWORDSMAN).setLocation(490, 655);
        boardCoords.get(Arc.ARC_5).get(Ring.KNIGHT).setLocation(490, 750);
        boardCoords.get(Arc.ARC_5).get(Ring.ARCHER).setLocation(490, 835);
        boardCoords.get(Arc.ARC_5).get(Ring.FOREST).setLocation(490, 920);

        boardCoords.get(Arc.ARC_6).get(Ring.CASTLE).setLocation(420, 530);
        boardCoords.get(Arc.ARC_6).get(Ring.SWORDSMAN).setLocation(340, 570);
        boardCoords.get(Arc.ARC_6).get(Ring.KNIGHT).setLocation(267, 624);
        boardCoords.get(Arc.ARC_6).get(Ring.ARCHER).setLocation(190, 665);
        boardCoords.get(Arc.ARC_6).get(Ring.FOREST).setLocation(110, 705);
    }

    private Model model;
    private View view;
    private BufferedImage boardImage;
    private int mx, my;

    public BoardPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        boardImage = ImageUtil.get("Board 3.png", 1200);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mx = e.getX();
                my = e.getY();
                repaint();
            }
        });
    }

    public void paintComponent(Graphics graphics){
        Graphics2D g = (Graphics2D) graphics;

        g.drawImage(boardImage, 0, 0, null);

        // Draw monsters
        Map<Arc, Map<Ring, List<Monster>>> monstersByLocation = new HashMap<>();
        model.getGame().getMonstersOnBoard().stream()
                .forEach(monster ->
                        monstersByLocation.computeIfAbsent(monster.getArc(), k -> new HashMap<>()).computeIfAbsent(monster.getRing(), k -> new ArrayList<>()).add(monster));
        for (Arc arc: monstersByLocation.keySet()){
            for (Ring ring: monstersByLocation.get(arc).keySet()){
                List<Point> points = getSpaceCoords(arc, ring, monstersByLocation.get(arc).get(ring).size());
                if (points.isEmpty())
                    continue;
                for (int i = 0; i < points.size(); ++i){
                    Monster monster = monstersByLocation.get(arc).get(ring).get(i);
                    Point point = points.get(i);
                    int x = point.x - (monster.getImage().getWidth() / 2);
                    int y = point.y - (monster.getImage().getHeight() / 2);
                    BufferedImage image = ImageUtil.rotateImageByDegrees(monster.getImage(), getMonsterRotationDegrees(monster.getArc()));
                    g.drawImage(image, x, y, null);
                }
            }
        }

        g.setColor(Color.WHITE);
        for (Point p: getSpaceCoords(Arc.ARC_4, Ring.KNIGHT, 4)){
            g.drawLine(p.x - 5, p.y - 5, p.x + 5, p.y + 5);
            g.drawLine(p.x - 5, p.y + 5, p.x + 5, p.y - 5);
        }

        g.drawString(mx + ", " + my, 20, 20);
    }

    public void refresh(){
        repaint();
    }

    public List<Point> getSpaceCoords(Arc arc, Ring ring, int numMonsters){
        if (numMonsters == 1)
            return Collections.singletonList(boardCoords.get(arc).get(ring));
        //System.out.println("Getting space coords for " + arc + " " + ring + " " + numMonsters + " monsters");
        double spacing = 60.0 / numMonsters;
        double offset = 30.0 / numMonsters;
        List<Double> degreeOffsets = new ArrayList<>();
        //System.out.println("Degree Offsets:");
        for (int i = 0; i < numMonsters; ++i){
            degreeOffsets.add(getArcDegrees(arc) + offset + (spacing * i));
            //System.out.println("   " + degreeOffsets.get(degreeOffsets.size() - 1));
        }

        Point centerPoint = boardCoords.get(arc).get(ring);
        double centerBoardOffsetX = centerPoint.x - CENTER_POINT.x;
        double centerBoardOffsetY = centerPoint.y - CENTER_POINT.y;
        double distanceFromCenterOfBoard = Math.sqrt((centerBoardOffsetX * centerBoardOffsetX) + (centerBoardOffsetY * centerBoardOffsetY));
        //System.out.println("Distance from center of board: "+ distanceFromCenterOfBoard);

        List<Point> returnList = new ArrayList<>();
        //System.out.println("Return Coords:");
        for (Double degrees: degreeOffsets){
            double deltaY = Math.sin(Math.toRadians(degrees)) * distanceFromCenterOfBoard;
            double deltaX = Math.cos(Math.toRadians(degrees)) * distanceFromCenterOfBoard;
            Point p = new Point((int) (CENTER_POINT.x + deltaX), (int) (CENTER_POINT.y + deltaY));
            returnList.add(p);
            //System.out.println("   " + p.x + ", " + p.y);
        }
        return returnList;
    }

    public double getArcDegrees(Arc arc){
        switch (arc) {
            case ARC_1 -> {
                return 180.0;
            }
            case ARC_2 -> {
                return 240.0;
            }
            case ARC_3 -> {
                return 300.0;
            }
            case ARC_4 -> {
                return 0.0;
            }
            case ARC_5 -> {
                return 60.0;
            }
            case ARC_6 -> {
                return 120.0;
            }
        }
        return 0.0;
    }

    public double getMonsterRotationDegrees(Arc arc){
        switch (arc) {
            case ARC_1 -> {
                return 300.0;
            }
            case ARC_2 -> {
                return 0.0;
            }
            case ARC_3 -> {
                return 60.0;
            }
            case ARC_4 -> {
                return 120.0;
            }
            case ARC_5 -> {
                return 180.0;
            }
            case ARC_6 -> {
                return 240.0;
            }
        }
        return 0.0;
    }
}
