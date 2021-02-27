package castlepanic.view;

import castlepanic.game.Ring;

import javax.swing.*;
import java.awt.*;

public class ViewUtil {
    public static void popupNotify(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public static boolean popupConfirm(String title, String message){
        int ret = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return ret == 0;
    }

    public static Color popupSelectColor(String title, String message){
        Color ret = (Color) JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, null, new Color[]{Color.BLUE, Color.GREEN, Color.RED}, Color.BLUE);
        return ret;
    }

    public static Ring popupSelectRing(String title, String message){
        Ring ret = (Ring) JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, null, new Ring[]{Ring.ARCHER, Ring.KNIGHT, Ring.SWORDSMAN}, Ring.ARCHER);
        return ret;
    }

    private ViewUtil(){}
}
