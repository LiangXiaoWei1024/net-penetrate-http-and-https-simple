package com.kele.penetrate.page;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 托盘系统
 */
@Slf4j
@SuppressWarnings("unused")
@Recognizer
public class Tray
{
    public static final Image icon = new ImageIcon(Tray.class.getResource("/icon.png")).getImage();
    @Autowired
    private MainFrame mainFrame;

    public Tray()
    {
        if (SystemTray.isSupported())
        {
            SystemTray tray = SystemTray.getSystemTray();
            PopupMenu popupMenu = new PopupMenu();

            //<editor-fold desc="打开 ASCll编码">
            popupMenu.add(createMenuItem("\u6253\u5f00", e ->
            {
                if (!mainFrame.isShowing())
                {
                    mainFrame.setVisible(true);
                }
            }));
            //</editor-fold>

            //<editor-fold desc="退出 ASCll编码">
            popupMenu.add(createMenuItem("\u9000\u51fa", e -> System.exit(0)));
            //</editor-fold>

            TrayIcon trayIcon = new TrayIcon(icon, "penetrate", popupMenu);
            trayIcon.setImageAutoSize(true);
            try
            {
                tray.add(trayIcon);
            }
            catch (AWTException e)
            {
                log.error("图标添加失败");
            }
        }
        else
        {
            log.error("当前系统不支持托盘系统");
        }
    }

    public MenuItem createMenuItem(String label, ActionListener actionListener)
    {
        MenuItem menuItem = new MenuItem(label);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }
}
