package castlepanic.view;

import castlepanic.Model;
import castlepanic.game.Arc;
import castlepanic.game.CastleTower;
import castlepanic.game.CastleWall;
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
    private static final Map<Arc, Point> wallCoords = new HashMap<>();
    private static final Map<Arc, Point> fortCoords = new HashMap<>();

    static {
        Arrays.stream(Arc.values()).forEach(arc -> {
            wallCoords.put(arc, new Point());
            fortCoords.put(arc, new Point());
            boardCoords.put(arc, new HashMap<>());
            Arrays.stream(Ring.values()).forEach(ring -> {
                boardCoords.get(arc).put(ring, new Point());
            });
        });

        boardCoords.get(Arc.ARC_1).get(Ring.CASTLE).setLocation(420, 446);
        boardCoords.get(Arc.ARC_1).get(Ring.SWORDSMAN).setLocation(340, 405);
        boardCoords.get(Arc.ARC_1).get(Ring.KNIGHT).setLocation(264, 350);
        boardCoords.get(Arc.ARC_1).get(Ring.ARCHER).setLocation(175, 305);
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

        wallCoords.get(Arc.ARC_1).setLocation(345, 365);
        wallCoords.get(Arc.ARC_2).setLocation(415, 365);
        wallCoords.get(Arc.ARC_3).setLocation(550, 368);
        wallCoords.get(Arc.ARC_4).setLocation(545, 488);
        wallCoords.get(Arc.ARC_5).setLocation(412, 585);
        wallCoords.get(Arc.ARC_6).setLocation(347, 488);

        fortCoords.get(Arc.ARC_1).setLocation(365, 395);
        fortCoords.get(Arc.ARC_2).setLocation(465, 355);
        fortCoords.get(Arc.ARC_3).setLocation(570, 398);
        fortCoords.get(Arc.ARC_4).setLocation(565, 508);
        fortCoords.get(Arc.ARC_5).setLocation(462, 585);
        fortCoords.get(Arc.ARC_6).setLocation(360, 518);
    }

    private Model model;
    private View view;
    private BufferedImage boardImage;
    private BufferedImage fortificationImage;
    private BufferedImage tarTokenImage = ImageUtil.get("Tar Token.png", 50);
    private BufferedImage fireTokenImage = ImageUtil.get("Fire.png", 40);

    // Show details of this monster
    private Monster monsterDetails = null;
    private int monsterDetailsMx, monsterDetailsMy;

    private int mx, my;

    public BoardPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        boardImage = ImageUtil.get("Board 3.png", 1200);
        fortificationImage = ImageUtil.get("Fortification_new.png", 50);

        setPreferredSize(new Dimension(boardImage.getWidth(), boardImage.getHeight()));
        setSize(new Dimension(boardImage.getWidth(), boardImage.getHeight()));

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

    public void init(){
        for (CastleWall wall: model.getGame().getWalls()) {
            Point p = wallCoords.get(wall.getArc());
            BufferedImage image = wall.getImage();
            wall.getBounds().setBounds(p.x, p.y, image.getWidth(), image.getHeight());
        }
    }

    public void paintComponent(Graphics graphics){
        Graphics2D g = (Graphics2D) graphics;

        g.drawImage(boardImage, 0, 0, null);

        // Draw castle structures
        for (CastleWall wall: model.getGame().getWalls()){
            Point p = wallCoords.get(wall.getArc());
            BufferedImage image = wall.getImage();
            g.drawImage(image, p.x, p.y, null);
            if (wall.isFortified()){
                p = fortCoords.get(wall.getArc());
                BufferedImage rotated = ImageUtil.rotateImageByDegrees(fortificationImage, getMonsterRotationDegrees(wall.getArc()));
                g.drawImage(rotated, p.x, p.y, null);
            }
        }

        for (CastleTower tower: model.getGame().getTowers()){
            if (!tower.isDestroyed()) {
                Point p = boardCoords.get(tower.getArc()).get(Ring.CASTLE);
                BufferedImage image = tower.getImage();
                int x = p.x - (image.getWidth() / 2);
                int y = p.y - (image.getHeight() / 2);
                g.drawImage(image, x, y, null);
            }
        }

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
                    double rotation = monster.getDamageRotationDegrees(getMonsterRotationDegrees(monster.getArc()));
                    BufferedImage image = ImageUtil.rotateImageByDegrees(monster.getImage(), rotation);
                    monster.getBounds().setBounds(x, y, image.getWidth(), image.getHeight());
                    g.drawImage(image, x, y, null);

                    // Draw tar
                    if (monster.isTar()){
                        int tarX = x + ((image.getWidth() - tarTokenImage.getWidth()) / 2);
                        int tarY = y + ((image.getHeight() - tarTokenImage.getHeight()) / 2);
                        g.drawImage(tarTokenImage, tarX, tarY, null);
                    }

                    // Draw Fire Tokens
                    if (monster.getFireTokens() > 0){
                        int fireX = x + ((image.getWidth() - fireTokenImage.getWidth()) / 2);
                        int fireY = y + ((image.getHeight() - fireTokenImage.getHeight()) / 2);
                        g.drawImage(fireTokenImage, fireX, fireY, null);
                    }
                }
            }
        }

        drawMonsterDetails(g);

        g.setColor(Color.WHITE);
        for (Point p: getSpaceCoords(Arc.ARC_4, Ring.KNIGHT, 4)){
            g.drawLine(p.x - 5, p.y - 5, p.x + 5, p.y + 5);
            g.drawLine(p.x - 5, p.y + 5, p.x + 5, p.y - 5);
        }

        g.drawString(mx + ", " + my, 20, 20);
    }

    private void drawMonsterDetails(Graphics2D g){
        if (monsterDetails == null)
            return;
        if (monsterDetailsMy + monsterDetails.getOriImage().getHeight() > getHeight())
            monsterDetailsMy -= monsterDetails.getOriImage().getHeight();
        g.drawImage(monsterDetails.getOriImage(), monsterDetailsMx, monsterDetailsMy, null);
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

    public void setMonsterDetails(Monster monsterDetails) {
        this.monsterDetails = monsterDetails;
        this.monsterDetailsMx = mx;
        this.monsterDetailsMy = my;
    }
}
