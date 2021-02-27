package castlepanic.view;

import javax.swing.*;

public class ViewUtil {
    public static void popupNotify(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public static boolean popupConfirm(String title, String message){
        int ret = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return ret == 0;
    }

    private ViewUtil(){}
}
